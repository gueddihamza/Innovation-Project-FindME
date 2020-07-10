package com.project.findme.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.findme.HelperClasses.HomeAdapter.FriendHelperClass;
import com.project.findme.HelperClasses.HomeAdapter.FriendMainAdapter;
import com.project.findme.HelperClasses.HomeAdapter.FriendRequestMainAdapter;
import com.project.findme.HelperClasses.HomeAdapter.FriendsAdapter;
import com.project.findme.HelperClasses.HomeAdapter.FriendsHelperClass;
import com.project.findme.HelperClasses.HomeAdapter.GroupHelperClass;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;

import java.util.ArrayList;

public class FriendsMain extends AppCompatActivity implements FriendRequestMainAdapter.FriendreqListener, FriendMainAdapter.FriendListener {
    private FirebaseDatabase database;
    private DatabaseReference friends;
    private DatabaseReference users;
    private DatabaseReference currentUser;
    private RecyclerView recyclerViewRequests;
    private RecyclerView recyclerViewFriends;
    private FirebaseUser user;
    private SearchView searchView;
    private FriendRequestMainAdapter requestAdapter;
    private FriendMainAdapter friendAdapter;
    private ArrayList<FriendHelperClass> requestsList = new ArrayList<>();
    private ArrayList<FriendHelperClass> requestsFilteredList = new ArrayList<>();
    private ArrayList<FriendHelperClass> friendsList = new ArrayList<>();
    private ArrayList<FriendHelperClass> friendsFilteredList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friends_main);
        database = FirebaseDatabase.getInstance();
        recyclerViewRequests = findViewById(R.id.recycler_view_requests);
        recyclerViewFriends = findViewById(R.id.recycler_view_friends);
        user = FirebaseAuth.getInstance().getCurrentUser();
        searchView = findViewById(R.id.search_view);
        currentUser = database.getReference("users").child(user.getUid());
        users = database.getReference("users");
        initRecyclers();

    }

    private void initRecyclers() {
        recyclerViewRequests.setHasFixedSize(true);
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewFriends.setHasFixedSize(true);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void onStart() {
        super.onStart();
        friends = database.getReference("users").child(user.getUid()).child("friends");
        friends.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestsList.clear();
                friendsList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    FriendHelperClass friend = ds.getValue(FriendHelperClass.class);
                    Log.i("MyLog", friend.toString());
                    if (friend.getStatus().equals("Received")) {
                        requestsList.add(friend);
                    } else if (friend.getStatus().equals("True")) {
                        friendsList.add(friend);
                    }
                }
                setAdapters();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (searchView != null) {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String newText) {
        requestsFilteredList = new ArrayList<>();
        friendsFilteredList = new ArrayList<>();

        for (FriendHelperClass request : requestsList) {
            if (request.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                requestsFilteredList.add(request);
            }
        }

        for (FriendHelperClass friend : friendsList) {
            if (friend.getUsername().toLowerCase().contains(newText.toLowerCase())) {
                friendsFilteredList.add(friend);
            }
        }

        requestAdapter = new FriendRequestMainAdapter(requestsFilteredList, this);
        recyclerViewRequests.setAdapter(requestAdapter);
        friendAdapter = new FriendMainAdapter(friendsFilteredList, this);
        recyclerViewFriends.setAdapter(friendAdapter);

    }

    private void setAdapters() {
        requestAdapter = new FriendRequestMainAdapter(requestsList, this);
        recyclerViewRequests.setAdapter(requestAdapter);
        friendAdapter = new FriendMainAdapter(friendsList, this);
        recyclerViewFriends.setAdapter(friendAdapter);
    }

    @Override
    public void onProfileClick(int position) {
        FriendHelperClass request = new FriendHelperClass();
        if (requestsFilteredList.size() == 0) {
            request = requestsList.get(position);
        } else {
            request = requestsFilteredList.get(position);
        }
        requestsFilteredList = requestsList;
        requestsList.clear();
        Intent intent = new Intent(getApplicationContext(), UserProfileOther.class);
        intent.putExtra("Username", request.getUsername());
        startActivity(intent);

    }

    @Override
    public void onAccept(int position) {
        FriendHelperClass request = new FriendHelperClass();
        if (requestsFilteredList.size() == 0) {
            request = requestsList.get(position);
        } else {
            request = requestsFilteredList.get(position);
        }
        requestsFilteredList = requestsList;
        requestsList.clear();
        acceptInDatabase(request.getUsername());
    }

    private void acceptInDatabase(final String username) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue(UserInfo.class).getUsername().equals(username)) {
                        ds.child("friends").child(user.getUid()).child("status").getRef().setValue("True");
                        currentUser.child("friends").child(ds.getKey()).child("status").setValue("True");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void rejectInDatabase(final String username) {

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue(UserInfo.class).getUsername().equals(username)) {
                        ds.child("friends").child(user.getUid()).getRef().removeValue();
                        currentUser.child("friends").child(ds.getKey()).removeValue();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRefuse(int position) {

        FriendHelperClass request = new FriendHelperClass();
        if (requestsFilteredList.size() == 0) {
            request = requestsList.get(position);
        } else {
            request = requestsFilteredList.get(position);
        }
        requestsFilteredList = requestsList;
        requestsList.clear();
        rejectInDatabase(request.getUsername());
    }

    @Override
    public void onOpenChat(int position) {
        FriendHelperClass friend = new FriendHelperClass();
        if (friendsFilteredList.size() == 0) {
            friend = friendsList.get(position);
        } else {
            friend = friendsFilteredList.get(position);
        }
        friendsFilteredList = friendsList;
        friendsList.clear();
        Intent intent = new Intent(getApplicationContext(), FriendChat.class);
        intent.putExtra("Username", friend.getUsername());
        startActivity(intent);
    }

    @Override
    public void onProfileOpen(int position) {
        FriendHelperClass friend = new FriendHelperClass();
        if (friendsFilteredList.size() == 0) {
            friend = friendsList.get(position);
        } else {
            friend = friendsFilteredList.get(position);
        }
        friendsFilteredList = friendsList;
        friendsList.clear();
        Intent intent = new Intent(getApplicationContext(), UserProfileOther.class);
        intent.putExtra("Username", friend.getUsername());
        startActivity(intent);

    }
}

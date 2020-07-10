package com.project.findme.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.findme.HelperClasses.HomeAdapter.FeaturedAdapter;
import com.project.findme.HelperClasses.HomeAdapter.FriendsAdapter;
import com.project.findme.HelperClasses.HomeAdapter.FriendsHelperClass;
import com.project.findme.HelperClasses.HomeAdapter.ProfileAdapter;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersSearch extends AppCompatActivity implements ProfileAdapter.UserListener {
    FirebaseDatabase database;
    DatabaseReference users;
    ArrayList<UserInfo> userInfos = new ArrayList<UserInfo>();
    ArrayList<UserInfo> usersList = new ArrayList<UserInfo>();
    private SearchView searchView;
    private RecyclerView recyclerView;
    FirebaseUser user;
    DatabaseReference groups;
    ArrayList<String> listUIDs = new ArrayList<String >();
    String username = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_users_search);
        user = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.result_list);
        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
        searchView = findViewById(R.id.search_view);
        usersRecycler();

    }

    private void setAdapter() {
        ProfileAdapter profileAdapter = new ProfileAdapter(userInfos, this);
        recyclerView.setAdapter(profileAdapter);
    }

    private boolean usernameExistsInList(String username, ArrayList<UserInfo> list) {
        if (list == null || list.size() == 0) {
            return false;
        } else {
            for (UserInfo userInfo : list) {
                if (userInfo.getUsername().equals(username))
                    return true;
            }
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (users != null) {
            if (getIntent().getStringExtra("groupUID") != null && getIntent().getStringExtra("userUID") != null) {
                String groupUID = getIntent().getStringExtra("groupUID");
                String userUID = getIntent().getStringExtra("userUID");
                groups = database.getReference("groups").child(groupUID).child("usersUID");
                groups.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listUIDs.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String uid = ds.getValue().toString();
                            listUIDs.add(uid);
                        }
                        addToUserInfos();
                        Log.i("MyLog", "userInfos = " + userInfos.toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("MyLog",databaseError.getMessage());
                    }
                });


            } else {

                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.i("MyLog", "Data Changed");
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (!ds.getKey().equals(user.getUid())) {
                                if (!usernameExistsInList(ds.getValue(UserInfo.class).getUsername(), userInfos)) {
                                    userInfos.add(ds.getValue(UserInfo.class));
                                }
                            }
                        }
                        Log.i("MyLog", "userInfos = " + userInfos.toString());
                        setAdapter();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UsersSearch.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }


        }

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

    private void addToUserInfos() {
        Log.i("MyLog","listuids = "+listUIDs.toString());
        for(String uid : listUIDs){
            users.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getKey().equals(user.getUid())){
                        username=dataSnapshot.getValue(UserInfo.class).getUsername();
                    }
                    if(dataSnapshot.getValue(UserInfo.class)!=null) {
                        if (!usernameExistsInList(dataSnapshot.getValue(UserInfo.class).getUsername(), userInfos)) {
                            userInfos.add(dataSnapshot.getValue(UserInfo.class));
                        }
                    }
                    setAdapter();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("MyLog",databaseError.getMessage());
                }
            });
        }

    }

    private void usersRecycler() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void search(String newText) {
        usersList = new ArrayList<>();

        for (UserInfo userInfo : userInfos) {
            if (userInfo.getFullName().toLowerCase().contains(newText.toLowerCase())) {
                usersList.add(userInfo);
            }
        }
        Log.i("MyLog", "usersListArray =" + usersList.toString());

        ProfileAdapter profileAdapter = new ProfileAdapter(usersList, this);
        recyclerView.setAdapter(profileAdapter);
    }

    @Override
    public void onProfileClick(int position) {
        Log.i("MyLog","position = "+position);
        UserInfo userInfo = new UserInfo();
        if (usersList.size() == 0) {
            userInfo = userInfos.get(position);
        } else {
            userInfo = usersList.get(position);
        }
        usersList = userInfos;
        userInfos.clear();
        if (username != null) {
            Log.i("MyLog",username);
            if (userInfo.getUsername().equals(username)) {
                startActivity(new Intent(this,UserProfile.class));
            } else {
                callProfileScreen(new View(this), userInfo.getUsername());
            }
        }
        else {
            callProfileScreen(new View(this), userInfo.getUsername());
        }
    }

    private void callProfileScreen(View view, String username) {
        Intent intent = new Intent(getApplicationContext(), UserProfileOther.class);
        intent.putExtra("Username", username);
        startActivity(intent);
    }
}

package com.project.findme.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.findme.HelperClasses.GroupInfo;
import com.project.findme.HelperClasses.HomeAdapter.GroupAdapter;
import com.project.findme.HelperClasses.HomeAdapter.GroupHelperClass;
import com.project.findme.HelperClasses.HomeAdapter.GroupMessage;
import com.project.findme.HelperClasses.HomeAdapter.ProfileAdapter;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;
import com.project.findme.common.Authentication.StartScreen;

import java.util.ArrayList;
import java.util.List;

public class SearchGroup extends AppCompatActivity implements GroupAdapter.GroupListener {
    private FirebaseDatabase database;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private DatabaseReference groups , owner;
    private ArrayList<GroupHelperClass> groupList = new ArrayList<>();
    private ArrayList<GroupHelperClass> groupFilteredList = new ArrayList<>();
    private SearchView searchView;
    private GroupAdapter groupAdapter;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search_group);
        user = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.result_list);
        database = FirebaseDatabase.getInstance();
        groups=database.getReference("groups");
        searchView = findViewById(R.id.search_view);
        groupsRecycler();

    }

    private void groupsRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    @Override
    protected void onStart() {
        super.onStart();
        type=getIntent().getStringExtra("Type");
        groups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupList.clear();
                setAdapter();
                Log.i("MyLog","Changed");
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getValue(GroupInfo.class).getGroupType().equals(type)){
                            GroupInfo groupInfo = ds.getValue(GroupInfo.class);
                            GroupHelperClass groupHelperClass = new GroupHelperClass(groupInfo.getGroupName(), groupInfo.getGroupDescription(), groupInfo.getGroupType(), groupInfo.getOwnerUID(), false, groupInfo.getUsersUID(), ds.getKey());
                            GetOwnerName(groupHelperClass);
                        }


                }
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


    private GroupHelperClass getObject(String key){
        if(groupList==null || groupList.size()==0){
            return null;
        }
        else{
            for(GroupHelperClass group : groupList){
                if(group.getKey().equals(key))
                    return group;
            }
        }
        return null;
    }

    private void search(String newText) {
        groupFilteredList = new ArrayList<>();

        for (GroupHelperClass groupHelperClass : groupList) {
            if (groupHelperClass.getName().toLowerCase().contains(newText.toLowerCase())) {
                groupFilteredList.add(groupHelperClass);
            }
        }

        groupAdapter = new GroupAdapter(groupFilteredList, this);
        recyclerView.setAdapter(groupAdapter);
    }

    private void GetOwnerName(final GroupHelperClass groupHelperClass) {
        owner = database.getReference("users").child(groupHelperClass.getOwner());
        owner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupHelperClass.setOwner(dataSnapshot.getValue(UserInfo.class).getFullName());
                VerifyMembership(groupHelperClass);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void VerifyMembership(GroupHelperClass groupHelperClass) {
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), StartScreen.class));
            finish();
        } else {
            for (String uid : groupHelperClass.getListUID()) {
                if (user.getUid().equals(uid)){
                    groupHelperClass.setMember(true);
                    break;
                }
            }
        }
        if(getObject(groupHelperClass.getKey())==null){
        groupList.add(groupHelperClass); }
        else{
            GroupHelperClass group = getObject(groupHelperClass.getKey());
            group.setOwner(groupHelperClass.getOwner());
            group.setName(groupHelperClass.getName());
            group.setListUID(groupHelperClass.getListUID());
            group.setDescription(groupHelperClass.getDescription());
            group.setMember(groupHelperClass.isMember());
            group.setType(groupHelperClass.getType());

        }
        setAdapter();
    }

    private void setAdapter(){
        groupAdapter = new GroupAdapter(groupList,this);
        recyclerView.setAdapter(groupAdapter);

    }

    @Override
    public void onGroupClick(int position) {
        GroupHelperClass groupInfo = new GroupHelperClass();
        if (groupFilteredList.size() == 0) {
            groupInfo = groupList.get(position);
        } else {
            groupInfo = groupFilteredList.get(position);
        }
        groupFilteredList = groupList;
        groupList.clear();
        if(groupInfo.isMember()){
            Intent intent=new Intent(getApplicationContext(), MessageActivity.class);
            intent.putExtra("groupUID",groupInfo.getKey());
            startActivity(intent);
        }
        else{
            startActivity(new Intent(getApplicationContext(),JoinGroup.class));
        }

    }
}

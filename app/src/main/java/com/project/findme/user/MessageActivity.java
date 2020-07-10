package com.project.findme.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.findme.HelperClasses.GroupInfo;
import com.project.findme.HelperClasses.HomeAdapter.GroupMessage;
import com.project.findme.HelperClasses.HomeAdapter.MessageAdapter;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;
import com.project.findme.common.Authentication.StartScreen;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    FirebaseDatabase database;
    ImageView group_image;
    TextView group_name;
    FirebaseUser user;
    DatabaseReference users;
    DatabaseReference groups ;
    DatabaseReference groupMessage;
    String currentUID;
    String groupUID;
    String username;
    ImageView btn_send;
    EditText editTextMessage;
    MaterialToolbar toolbar;
    ArrayList<GroupMessage> messageList;
    MessageAdapter messageAdapter;
    RecyclerView recyclerView;
    boolean isOwner = false;
    MenuItem add_members , kick_members , leave_group;


 /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       Log.i("MyLog","test");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_menu,menu);
        return true;
    }*/




    public void sendMSG(View view){
        String message = editTextMessage.getText().toString();
        if(!message.equals("")){
            sendMessage(groupUID,currentUID,message);

        }else {
            editTextMessage.setError("Please Type A Message!");
        }
        editTextMessage.setText("");

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_message);
        initHooks();
        initFirebase();
        initMenu();

    }

    private void initItems() {
        if(isOwner==true){
            add_members.setVisible(true);
            kick_members.setVisible(true);
            leave_group.setVisible(false);
        }
        else{
            add_members.setVisible(false);
            kick_members.setVisible(false);
            leave_group.setVisible(true);
        }
    }


    private void initHooks() {
        toolbar=findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.group_menu);
        Menu menu = toolbar.getMenu();
        add_members=menu.findItem(R.id.nav_add_members);
        kick_members=menu.findItem(R.id.kick_members);
        leave_group=menu.findItem(R.id.leave_group);
        group_image=findViewById(R.id.group_type_image);
        group_name=findViewById(R.id.groupName);
        btn_send=findViewById(R.id.btn_send);
        editTextMessage=findViewById(R.id.editTextMessage);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void initMenu(){
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_search_users:
                        Intent intent = new Intent(getApplicationContext(),UsersSearch.class);
                        intent.putExtra("groupUID",groupUID);
                        intent.putExtra("userUID",currentUID);
                        startActivity(intent);
                        break;
                    case R.id.nav_add_members:
                        break;

                    case R.id.geolocate_members:
                        Intent geoIntent = new Intent(getApplicationContext(),GroupGeolocation.class);
                        geoIntent.putExtra("groupUID",groupUID);
                        startActivity(geoIntent);
                        break;

                    case R.id.kick_members:
                        break;

                    case R.id.leave_group:
                        break;

                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),UserDashboard.class));
                        break;

                }
                return true;
            }
        });
    }

    private void initFirebase() {
        user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            startActivity(new Intent(getApplicationContext(), StartScreen.class));
            finish();
        }
        else{
        currentUID=user.getUid();
        groupUID = getIntent().getStringExtra("groupUID");
        database=FirebaseDatabase.getInstance();
        groups = database.getReference("groups").child(groupUID);
        groups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GroupInfo groupInfo=dataSnapshot.getValue(GroupInfo.class);
                group_name.setText(groupInfo.getGroupName());
                if(user.getUid().equals(groupInfo.getOwnerUID())){
                    isOwner=true;
                }
                initItems();
                if(groupInfo.getGroupType().equals("class")){
                    group_image.setImageResource(R.drawable.classgroup);
                }
               else if(groupInfo.getGroupType().equals("club")){
                    group_image.setImageResource(R.drawable.club_group_icon);
                }
                else if(groupInfo.getGroupType().equals("custom")){
                    group_image.setImageResource(R.drawable.custom_group_icon);
                }
                setUsername();
                initMessages();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        }
    }

    private void setUsername() {

        users=database.getReference("users").child(currentUID);
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInfo userInfo=dataSnapshot.getValue(UserInfo.class);
                username=userInfo.getUsername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("MyLog",databaseError.getMessage());
            }
        });
    }

    private void initMessages() {
        groupMessage=database.getReference("groupMessages").child(groupUID);
        groupMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    readMessages(groupUID,currentUID);
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("MyLog",databaseError.getMessage());
            }
        });
    }

    private void sendMessage(String groupUID,String currentUID,String message){
        groupMessage.push().setValue(new GroupMessage(groupUID,currentUID,message,username));

    }

    private void readMessages(String groupUID,String currentUID){
        messageList=new ArrayList<>();
        database.getReference("groupMessages").child(groupUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    GroupMessage groupMessage=ds.getValue(GroupMessage.class);
                    messageList.add(groupMessage);

                }
                messageAdapter=new MessageAdapter(messageList);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("MyLog",databaseError.getMessage());
            }
        });




    }
}

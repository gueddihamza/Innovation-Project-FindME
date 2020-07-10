package com.project.findme.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.findme.HelperClasses.HomeAdapter.FriendMessage;
import com.project.findme.HelperClasses.HomeAdapter.FriendMessageAdapter;
import com.project.findme.HelperClasses.HomeAdapter.GroupMessage;
import com.project.findme.HelperClasses.HomeAdapter.MessageAdapter;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;
import com.project.findme.common.Authentication.StartScreen;

import java.util.ArrayList;

public class FriendChat extends AppCompatActivity {

    FirebaseDatabase database;
    MaterialLetterIcon friend_image;
    TextView friend_name;
    String friendName;
    FirebaseUser user;
    DatabaseReference users;
    DatabaseReference friendMessage;
    String currentUID;
    String friendUID;
    String username;
    ImageView btn_send;
    EditText editTextMessage;
    MaterialToolbar toolbar;
    ArrayList<FriendMessage> messageList;
    FriendMessageAdapter messageAdapter;
    RecyclerView recyclerView;
    private int[] mMaterialColors;
    ImageView back_btn;



    public void goBack(View view){


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friend_chat);
        initHooks();
        initFirebase();
    }


    private void initFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), StartScreen.class));
            finish();
        } else {
            currentUID = user.getUid();
            friendName = getIntent().getStringExtra("Username");
            database = FirebaseDatabase.getInstance();
            users = database.getReference().child("users");
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(ds.getKey().equals(currentUID)){
                            username=ds.getValue(UserInfo.class).getUsername();
                        }
                        else if (ds.getValue(UserInfo.class).getUsername().equals(friendName)) {
                            friendUID=ds.getKey();
                            friend_name.setText(friendName);
                        }
                    }
                    initMaterialIcon();
                    initMessages();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("MyLog",databaseError.getMessage());
                }
            });
        }
    }

    private void initMessages() {
        friendMessage=database.getReference("friendMessages");
        friendMessage.child(currentUID).child(friendUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readMessages(currentUID,friendUID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages(String currentUID, String friendUID) {
        messageList=new ArrayList<>();
        database.getReference("friendMessages").child(currentUID).child(friendUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.hasChildren()) {
                        FriendMessage friendMessage = ds.getValue(FriendMessage.class);
                        messageList.add(friendMessage);
                    }
                }
                messageAdapter=new FriendMessageAdapter(messageList);
                recyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("MyLog",databaseError.getMessage());
            }
        });
    }


    public void sendMSG(View view){
        String message = editTextMessage.getText().toString();
        if(!message.equals("")){
            sendMessage(currentUID,friendUID,message);

        }else {
            editTextMessage.setError("Please Type A Message!");
        }
        editTextMessage.setText("");

    }

    private void sendMessage(String currentUID, String friendUID, String message) {
        friendMessage.child(currentUID).child(friendUID).push().setValue(new FriendMessage(currentUID,friendUID,message,username));
        friendMessage.child(friendUID).child(currentUID).push().setValue(new FriendMessage(friendUID,currentUID,message,username));
    }

    private void initHooks(){
        back_btn=findViewById(R.id.back_btn);
        toolbar=findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.friend_menu);
        Menu menu = toolbar.getMenu();
        friend_image=findViewById(R.id.group_type_image);
        friend_name=findViewById(R.id.groupName);
        btn_send=findViewById(R.id.btn_send);
        editTextMessage=findViewById(R.id.editTextMessage);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void initMaterialIcon() {
        friend_image.setInitials(true);
        friend_image.setInitialsNumber(2);
        friend_image.setLetterSize(18);
        friend_image.setLetter(friend_name.getText().toString());
        mMaterialColors = this.getResources().getIntArray(R.array.colors);

        char firstChar = friend_image.getLetter().charAt(0);
        if (firstChar == 'A' || firstChar == 'B' || firstChar == 'C') {
            friend_image.setShapeColor(mMaterialColors[0]);
        } else if (firstChar == 'D' || firstChar == 'E' || firstChar == 'F') {
            friend_image.setShapeColor(mMaterialColors[1]);
        } else if (firstChar == 'G' || firstChar == 'H' || firstChar == 'I') {
            friend_image.setShapeColor(mMaterialColors[2]);
        } else if (firstChar == 'J' || firstChar == 'K' || firstChar == 'L') {
            friend_image.setShapeColor(mMaterialColors[3]);
        } else if (firstChar == 'M' || firstChar == 'N') {
            friend_image.setShapeColor(mMaterialColors[4]);
        } else if (firstChar == 'O' || firstChar == 'P') {
            friend_image.setShapeColor(mMaterialColors[5]);
        } else if (firstChar == 'Q' || firstChar == 'R') {
            friend_image.setShapeColor(mMaterialColors[6]);
        } else if (firstChar == 'S' || firstChar == 'T') {
            friend_image.setShapeColor(mMaterialColors[7]);
        } else if (firstChar == 'U' || firstChar == 'V') {
            friend_image.setShapeColor(mMaterialColors[8]);
        } else if (firstChar == 'W' || firstChar == 'X') {
            friend_image.setShapeColor(mMaterialColors[9]);
        } else if (firstChar == 'Y' || firstChar == 'Z') {
            friend_image.setShapeColor(mMaterialColors[10]);
        }
    }

}

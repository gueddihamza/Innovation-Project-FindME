package com.project.findme.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;
import com.project.findme.common.Authentication.StartScreen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileOther extends AppCompatActivity {
    TextView fullname, username, status, phonenumber, friends_total,total_groups;;
    FirebaseDatabase database;
    DatabaseReference reference;
    String userName, profileUID, currentUID;
    UserInfo userInfo;
    MaterialLetterIcon profile_image;
    FirebaseUser user;
    Button mainclickButton, rejectButton;
    int countFriends = 0;
    int countGroups = 0;
    String currentUsername;
    private Map<String, String> friendsList = new HashMap<>();

    private int[] mMaterialColors;

    private void getUsername() {
        Intent intent = getIntent();
        userName = intent.getStringExtra("Username");
    }

    private void searchByUsername() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().equals(currentUID)){
                        currentUsername=ds.getValue(UserInfo.class).getUsername();
                    }
                    else if (ds.getValue(UserInfo.class).getUsername().equals(userName)) {
                        userInfo = ds.getValue(UserInfo.class);
                        profileUID = ds.getKey();
                        countGroups();
                        initProfileScreen();
                        initMaterialIcon();
                        initFriendStatus();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void countGroups() {
        DatabaseReference groups = database.getReference("groups");
        groups.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("usersUID").getValue() != null) {
                        List<String> groups = (List<String>) ds.child("usersUID").getValue();
                        Log.i("MyLog", "" + groups.toString());
                        if (keyExistsInList(groups))
                            countGroups++;
                    }
                }
                total_groups.setText(String.valueOf(countGroups));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean keyExistsInList(List<String> groups) {
        if (groups == null) {
            return false;
        }
        for (String uid : groups) {
            if (uid.equals(profileUID))
                return true;
        }
        return false;
    }

    private void initFriendStatus() {
        if (currentUID != null && profileUID != null) {
            DatabaseReference friends = reference.child(profileUID).child("friends");
            friends.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child("status").getValue().toString().equals("True"))
                            countFriends++;
                        friendsList.put(ds.getKey(), ds.child("status").getValue().toString());
                    }
                    Log.i("MyLog", "Friends List Value = " + friendsList.toString());
                    UpdateButton();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


    }

    private void UpdateButton() {
        friends_total.setText(String.valueOf(countFriends));
        Log.i("MyLog", "ProfileUID = " + profileUID);
        Log.i("MyLog", "usersList = " + friendsList.toString());
        if (friendsList.get(currentUID) == null) {
            mainclickButton.setText("Send Friend Request");
        } else {
            if (friendsList.get(currentUID).equals("True"))
                mainclickButton.setText("Remove From Friends List");
            else if (friendsList.get(currentUID).equals("Received"))
                mainclickButton.setText("Cancel Request");

            else if (friendsList.get(currentUID).equals("Sent")) {
                mainclickButton.setText("Accept Request");
                rejectButton.setText("Reject Request");
                rejectButton.setVisibility(View.VISIBLE);
            }

        }

        friendsList.clear();
        countFriends=0;
    }

    public void rejectRequest(View view){
        Button button= (Button) view;
        reference.child(currentUID).child("friends").child(profileUID).removeValue();
        reference.child(profileUID).child("friends").child(currentUID).removeValue();
        button.setVisibility(View.GONE);
        initFriendStatus();
    }

    public void mainClickButton(View view) {
        Button button = (Button) view;
        String action = button.getText().toString();
        switch (action) {
            case "Send Friend Request":
                sendRequest(currentUID,profileUID);
                initFriendStatus();
                break;
            case "Remove From Friends List":
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to remove "+username.getText().toString()+" from your friends list? ")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeFromList(currentUID,profileUID);
                                initFriendStatus();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.cancel();
                            }
                        });

                AlertDialog alertDialog=builder.create();
                alertDialog.show();
                break;

            case "Cancel Request":
                cancelRequest(currentUID,profileUID);
                initFriendStatus();
                break;

            case "Accept Request":
                acceptRequest(currentUID,profileUID);
                rejectButton.setVisibility(View.GONE);
                initFriendStatus();
                break;


            default:break;



        }

    }

    private void acceptRequest(String currentUID, String profileUID) {
        reference.child(currentUID).child("friends").child(profileUID).child("status").setValue("True");
        reference.child(profileUID).child("friends").child(currentUID).child("status").setValue("True");

    }

    private void removeFromList(String currentUID, String profileUID) {
        reference.child(currentUID).child("friends").child(profileUID).removeValue();
        reference.child(profileUID).child("friends").child(currentUID).removeValue();
    }

    private void cancelRequest(String currentUID, String profileUID) {
        reference.child(currentUID).child("friends").child(profileUID).removeValue();
        reference.child(profileUID).child("friends").child(currentUID).removeValue();
    }

    private void sendRequest(String currentUID, String profileUID) {
        reference.child(currentUID).child("friends").child(profileUID).child("status").setValue("Sent");
        reference.child(profileUID).child("friends").child(currentUID).child("status").setValue("Received");
        reference.child(currentUID).child("friends").child(profileUID).child("username").setValue(username.getText().toString());
        reference.child(profileUID).child("friends").child(currentUID).child("username").setValue(currentUsername);

    }

    private void initMaterialIcon() {
        profile_image.setInitials(true);
        profile_image.setInitialsNumber(2);
        profile_image.setLetterSize(18);
        profile_image.setLetter(userInfo.getFullName());
        mMaterialColors = this.getResources().getIntArray(R.array.colors);

        char firstChar = profile_image.getLetter().charAt(0);
        if (firstChar == 'A' || firstChar == 'B' || firstChar == 'C') {
            profile_image.setShapeColor(mMaterialColors[0]);
        } else if (firstChar == 'D' || firstChar == 'E' || firstChar == 'F') {
            profile_image.setShapeColor(mMaterialColors[1]);
        } else if (firstChar == 'G' || firstChar == 'H' || firstChar == 'I') {
            profile_image.setShapeColor(mMaterialColors[2]);
        } else if (firstChar == 'J' || firstChar == 'K' || firstChar == 'L') {
            profile_image.setShapeColor(mMaterialColors[3]);
        } else if (firstChar == 'M' || firstChar == 'N') {
            profile_image.setShapeColor(mMaterialColors[4]);
        } else if (firstChar == 'O' || firstChar == 'P') {
            profile_image.setShapeColor(mMaterialColors[5]);
        } else if (firstChar == 'Q' || firstChar == 'R') {
            profile_image.setShapeColor(mMaterialColors[6]);
        } else if (firstChar == 'S' || firstChar == 'T') {
            profile_image.setShapeColor(mMaterialColors[7]);
        } else if (firstChar == 'U' || firstChar == 'V') {
            profile_image.setShapeColor(mMaterialColors[8]);
        } else if (firstChar == 'W' || firstChar == 'X') {
            profile_image.setShapeColor(mMaterialColors[9]);
        } else if (firstChar == 'Y' || firstChar == 'Z') {
            profile_image.setShapeColor(mMaterialColors[10]);
        }
    }

    private void initProfileScreen() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            goToMainScreen();
        } else {
            fullname.setText(userInfo.getFullName());
            username.setText(userInfo.getUsername());
            status.setText(userInfo.getFunction());
            phonenumber.setText(userInfo.getPhoneNumber());
            currentUID = user.getUid();
        }
    }

    private void goToMainScreen() {
        startActivity(new Intent(getApplicationContext(), StartScreen.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile_other);
        fullname = findViewById(R.id.fullname_other);
        username = findViewById(R.id.username_other);
        status = findViewById(R.id.status_other);
        phonenumber = findViewById(R.id.phonenumber_other);
        profile_image = findViewById(R.id.profile_image_other);
        mainclickButton = findViewById(R.id.mainClickButton);
        rejectButton = findViewById(R.id.rejectRequestButton);
        friends_total = findViewById(R.id.friends_total_2);
        total_groups=findViewById(R.id.groups_total);
        getUsername();
        searchByUsername();
    }
}

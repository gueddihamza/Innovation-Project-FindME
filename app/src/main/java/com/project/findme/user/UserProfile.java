package com.project.findme.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.project.findme.HelperClasses.HomeAdapter.FriendHelperClass;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;
import com.project.findme.common.Authentication.StartScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfile extends AppCompatActivity {
    TextView fullname, username, total_friends, total_groups;
    MaterialLetterIcon profile_image;
    TextInputLayout fullnameLayout, usernameLayout, statusLayout, phoneLayout;
    String fullName, userName, status, phone;
    Button updateButton;
    FirebaseDatabase database;
    DatabaseReference users, listFriends;
    FirebaseUser user;
    String userUID;
    Map<String, FriendHelperClass> friends;
    int countFriends = 0;
    int countGroups = 0;
    private int[] mMaterialColors;
    LoadingDialog loadingDialog;

    public void updateProfile(View view) {
        fullnameLayout.setError(null);
        usernameLayout.setError(null);
        statusLayout.setError(null);
        phoneLayout.setError(null);
        if(changedData()){
            if(!validateFullName() | !validateUserName() | !validateStatus() | !validatePhoneNumber()){
                return;
            }
            else{
                updateInformations();
            }
        }


    }

    private void updateInformations() {
        users.child(userUID).child("fullName").setValue(fullnameLayout.getEditText().getText().toString());
        users.child(userUID).child("function").setValue(statusLayout.getEditText().getText().toString());
        users.child(userUID).child("username").setValue(usernameLayout.getEditText().getText().toString());
        users.child(userUID).child("phoneNumber").setValue(phoneLayout.getEditText().getText().toString());
        fullName = fullnameLayout.getEditText().getText().toString();
        userName = usernameLayout.getEditText().getText().toString();
        status = statusLayout.getEditText().getText().toString();
        phone = phoneLayout.getEditText().getText().toString();
        fullname.setText(fullnameLayout.getEditText().getText().toString());
        username.setText(usernameLayout.getEditText().getText().toString());
        Toast.makeText(this, "Data Updated!", Toast.LENGTH_SHORT).show();
    }


    private boolean validatePhoneNumber(){
        String val = phoneLayout.getEditText().getText().toString().trim();
        String checkspaces = ".*\\s.*";

        if(val.charAt(0)!='+'){
            phoneLayout.setError("Number is not valid!");
            return false;
        }

        if(val.isEmpty()){
            phoneLayout.setError("Field can not be empty");
            return false;
        }else if(val.length()>16){
            phoneLayout.setError("Phone Number Format can not exceed 16 characters");
            return false;
        }else if(val.length()<8){
            phoneLayout.setError("Phone Number Format can not be less that 8 characters");
            return false;

        }else if(val.matches(checkspaces)){
            phoneLayout.setError("No white spaces are allowed!");
            return false;


        }
        else {
            phoneLayout.setError(null);
            phoneLayout.setErrorEnabled(false);
            return true;
        }
    }


    private boolean validateUserName(){
        String val = usernameLayout.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if(val.isEmpty()){
            usernameLayout.setError("Field can not be empty");
            return false;
        }else if(val.length()>20){
            usernameLayout.setError("Username can not exceed 20 characters");
            return false;

        }else if(!val.matches(checkspaces)){
            usernameLayout.setError("No white spaces are allowed!");
            return false;


        }
        else {
            usernameLayout.setError(null);
            usernameLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateStatus(){
        String val = statusLayout.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if(val.isEmpty()){
            statusLayout.setError("Field can not be empty");
            return false;
        }else if(val.length()>20){
            statusLayout.setError("Status can not exceed 20 characters");
            return false;

        }else if(!val.matches(checkspaces)){
            statusLayout.setError("No white spaces are allowed!");
            return false;


        }
        else {
            statusLayout.setError(null);
            statusLayout.setErrorEnabled(false);
            return true;
        }
    }



    private boolean validateFullName(){
        String val = fullnameLayout.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            fullnameLayout.setError("Field can not be empty");
            return false;
        }
        else {
            fullnameLayout.setError(null);
            fullnameLayout.setErrorEnabled(false);
            return true;
        }
    }



    private boolean changedData() {
        if (!userName.equals(usernameLayout.getEditText().getText().toString()) || !fullName.equals(fullnameLayout.getEditText().getText().toString())
                || !status.equals(statusLayout.getEditText().getText().toString())
                || !phone.equals(phoneLayout.getEditText().getText().toString())){

            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        total_friends = findViewById(R.id.friends_total);
        total_groups = findViewById(R.id.groups_total);
        profile_image = findViewById(R.id.profile_image);
        fullnameLayout = findViewById(R.id.fullnameET);
        usernameLayout = findViewById(R.id.usernameET);
        statusLayout = findViewById(R.id.statusET);
        phoneLayout = findViewById(R.id.phonenumberET);
        updateButton = findViewById(R.id.updateButton);
        friends = new HashMap<>();
        initFirebase();
    }

    private void initFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), StartScreen.class));
        } else {
            database = FirebaseDatabase.getInstance();
            users = database.getReference("users");
            userUID = user.getUid();
            users.child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    countGroups();
                    countFriends();
                    fullname.setText(userInfo.getFullName());
                    username.setText(userInfo.getUsername());
                    fullnameLayout.getEditText().setText(userInfo.getFullName());
                    usernameLayout.getEditText().setText(userInfo.getUsername());
                    statusLayout.getEditText().setText(userInfo.getFunction());
                    phoneLayout.getEditText().setText(userInfo.getPhoneNumber());
                    fullName = userInfo.getFullName();
                    userName = userInfo.getUsername();
                    status = userInfo.getFunction();
                    phone = userInfo.getPhoneNumber();
                    initMaterialIcon();
                    loadingDialog.dismissDialog();

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("MyLog", databaseError.getMessage());
                }
            });

        }
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

    private void countFriends() {
        DatabaseReference friends = database.getReference("users").child(userUID).child("friends");
        friends.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue(FriendHelperClass.class).getStatus().equals("True"))
                        countFriends++;
                }
                total_friends.setText(String.valueOf(countFriends));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("MyLog", databaseError.getMessage());
            }
        });

    }

    private boolean keyExistsInList(List<String> groups) {
        if (groups == null) {
            return false;
        }
        for (String uid : groups) {
            if (uid.equals(userUID))
                return true;
        }
        return false;
    }


    private void initMaterialIcon() {
        profile_image.setInitials(true);
        profile_image.setInitialsNumber(2);
        profile_image.setLetterSize(18);
        profile_image.setLetter(fullname.getText().toString());
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
}


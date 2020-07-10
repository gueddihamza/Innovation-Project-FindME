package com.project.findme.common.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;

public class SignUp extends AppCompatActivity {

    //Firebase
    FirebaseDatabase database;
    DatabaseReference users;
    //Variables
    ImageView backBtn;
    Button next;
    Button login;
    TextView titleText;

    //Data
    TextInputLayout fullname;
    TextInputLayout username;
    TextInputLayout email;
    TextInputLayout password;
    boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
        backBtn=findViewById(R.id.signup_back_button);
        next=findViewById(R.id.signup_next_button);
        login=findViewById(R.id.signup_login_button);
        titleText=findViewById(R.id.signup_title_text);

        fullname = findViewById(R.id.signup_fullname);
        username = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);




    }

    public void Verify(String username2 , String email2 , DataSnapshot ds){

        if (username2.matches(ds.getValue(UserInfo.class).getUsername())) {
            Log.i("MyLog","Username = "+String.valueOf(ds.getValue(UserInfo.class).getUsername()));
            status = false;
            username.setError("Username already exists!");
            return;
        }
        if(email2.matches(ds.getValue(UserInfo.class).getEmail())) {
            Log.i("MyLog","Email = "+String.valueOf(ds.getValue(UserInfo.class).getEmail()));
            status = false;
            email.setError("Email already has an account associated with it!!");
            return;
        }

    }

    public void checkIfUsernameExists(){
        status=true;
        users.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username2 = username.getEditText().getText().toString().trim();
                String email2 = email.getEditText().getText().toString().trim();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Verify(username2,email2,ds);
                   Log.i("MyLog", ds.getValue(UserInfo.class).getUsername());
                   Log.i("MyLog", ds.getValue(UserInfo.class).getEmail());



                    }

                if(status==true){

                    Intent intent = new Intent(getApplicationContext(),SignUpSecondActivity.class);
                    intent.putExtra("FullName",fullname.getEditText().getText().toString().trim());
                    intent.putExtra("Username",username.getEditText().getText().toString().trim());
                    intent.putExtra("Email",email.getEditText().getText().toString().trim());
                    intent.putExtra("Password",password.getEditText().getText().toString().trim());
                    Log.i("MyLog",fullname.getEditText().getText().toString().trim());


                    //Add Transition
                    Pair[] pairs = new Pair[4];
                    pairs[0] = new Pair<View,String>(backBtn,"transition_back_arrow_btn");
                    pairs[1] = new Pair<View,String>(next,"transition_next_btn");
                    pairs[2] = new Pair<View,String>(login,"transition_login_btn");
                    pairs[3] = new Pair<View,String>(titleText,"transition_title_text");

                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pairs);
                    startActivity(intent,options.toBundle());
                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("MyLog",databaseError.getMessage());
            }
        });
    }

    public void callNextSignUpScreen(View view){

        if(!validateFullName() | !validateUserName() | !validateEmail() | !validatePassword()){
            return;
        }
        checkIfUsernameExists();


    }

    private boolean validateFullName(){
        String val = fullname.getEditText().getText().toString().trim();

        if(val.isEmpty()){
            fullname.setError("Field can not be empty");
            return false;
        }
        else {
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName(){
        String val = username.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if(val.isEmpty()){
            username.setError("Field can not be empty");
            return false;
        }else if(val.length()>20){
            username.setError("Username can not exceed 20 characters");
            return false;

        }else if(!val.matches(checkspaces)){
            username.setError("No white spaces are allowed!");
            return false;


        }
        else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail(){
        String val = email.getEditText().getText().toString().trim();
        String patternEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            email.setError("Field can not be empty");
            return false;
        }else if(!val.matches(patternEmail)){
            email.setError("Invalid Email!");
            return false;


        }
        else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(){
        String val = password.getEditText().getText().toString().trim();
        String patternsPassword = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
               // "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$";

        if(val.isEmpty()){
            password.setError("Field can not be empty");
            return false;
        }else if(!val.matches(patternsPassword)){
            password.setError("Invalid Password!");
            return false;


        }
        else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}

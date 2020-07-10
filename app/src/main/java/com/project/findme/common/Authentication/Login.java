package com.project.findme.common.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.findme.R;
import com.project.findme.user.UserDashboard;

public class Login extends AppCompatActivity {


    TextInputLayout email;
    TextInputLayout password;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Button login_btn;

    public void callForgotPasswordScreen(View view){
        startActivity(new Intent(getApplicationContext(),ForgotPassword.class));

    }



    public void performLogin(String email , String password){
        if(!validateEmail() | !validatePassword()){
        return;
        }
        Log.i("MyLog","email = "+email);
        Log.i("MyLog","password = "+password);

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Log.i("MyLog","Login was successful");
                    Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                    startActivity(intent);
                    finish();

                }else {
                    String errorMsg = task.getException().getMessage();
                    Toast.makeText(Login.this, errorMsg, Toast.LENGTH_SHORT).show();


                }
            }
        });






    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        login_btn=findViewById(R.id.login_btn);
        if(user!=null) {
            Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
            startActivity(intent);
            finish();

        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin(email.getEditText().getText().toString().trim(),password.getEditText().getText().toString().trim());
            }
        });

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

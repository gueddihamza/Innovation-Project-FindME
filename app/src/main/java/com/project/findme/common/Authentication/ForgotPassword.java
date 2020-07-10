package com.project.findme.common.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.project.findme.R;

public class ForgotPassword extends AppCompatActivity {
    TextInputLayout emailField;
    Button button_make_selection;


    public void callMailSelectionScreen(View view){
        final String email = emailField.getEditText().getText().toString();
        if(!validateEmail()){
            return;
        }
        FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(getApplicationContext(),ForgotPasswordSuccess.class);
                        intent.putExtra("Email",email);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(ForgotPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    }
                });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);
        emailField=findViewById(R.id.email_field);
        button_make_selection=findViewById(R.id.next_btn_for_selection);
    }

    private boolean validateEmail(){
        String val = emailField.getEditText().getText().toString().trim();
        String patternEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            emailField.setError("Field can not be empty");
            return false;
        }else if(!val.matches(patternEmail)){
            emailField.setError("Invalid Email!");
            return false;


        }
        else {
            emailField.setError(null);
            emailField.setErrorEnabled(false);
            return true;
        }
    }
}

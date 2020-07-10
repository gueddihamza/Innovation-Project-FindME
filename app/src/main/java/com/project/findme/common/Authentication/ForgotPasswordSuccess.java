package com.project.findme.common.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.project.findme.R;

public class ForgotPasswordSuccess extends AppCompatActivity {
    TextView textViewEmail;
    Button login_btn;

    public void change_text(){
    Intent intent=getIntent();
    String email=intent.getStringExtra("Email");
    textViewEmail.setText(textViewEmail.getText().toString().concat(" "+email));
    }

    public void callLoginScreen(View view){

        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password_success);
        textViewEmail=findViewById(R.id.email_info);
        login_btn=findViewById(R.id.login_btn);
        change_text();
    }
}

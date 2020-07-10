package com.project.findme.common.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.findme.R;

public class SignUpSecondActivity extends AppCompatActivity {

    //Variables
    ImageView backBtn;
    Button next;
    Button login;
    TextView titleText;
    RadioGroup radioGroup;
    RadioButton selectedFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_second);

        backBtn = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);
        radioGroup = findViewById(R.id.radioGroup);

    }

    public void callThirdSignUpScreen(View view) {
        if(!validateFunction()){
            return;
        }


        selectedFunction = findViewById(radioGroup.getCheckedRadioButtonId());
        String function = selectedFunction.getText().toString();
        Intent previousIntent = getIntent();
        String fullname = previousIntent.getStringExtra("FullName");
        String username = previousIntent.getStringExtra("Username");
        String email = previousIntent.getStringExtra("Email");
        String password = previousIntent.getStringExtra("Password");

        Intent intent = new Intent(getApplicationContext(), SignUpThirdActivity.class);
        intent.putExtra("FullName",fullname);
        intent.putExtra("Username",username);
        intent.putExtra("Email",email);
        intent.putExtra("Password",password);
        intent.putExtra("Function",function);

        //Add Transition
        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(backBtn, "transition_back_arrow_btn");
        pairs[1] = new Pair<View, String>(next, "transition_next_btn");
        pairs[2] = new Pair<View, String>(login, "transition_login_btn");
        pairs[3] = new Pair<View, String>(titleText, "transition_title_text");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpSecondActivity.this, pairs);
        startActivity(intent, options.toBundle());
    }


    private boolean validateFunction() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please Select Your Status", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            return true;
        }


    }
}

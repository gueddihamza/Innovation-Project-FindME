package com.project.findme.common.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;

public class SignUpThirdActivity extends AppCompatActivity {
    CountryCodePicker ccp;
    TextInputLayout layoutCarrierNumber;
    //Firebase
    FirebaseDatabase database;
    DatabaseReference users;
    boolean status = true;


    public void Verify(String phoneNumber ,DataSnapshot ds){

        Log.i("MyLog","Number = "+phoneNumber);
        Log.i("MyLog","Firebase Number = "+ds.getValue(UserInfo.class).getPhoneNumber());
        if(ds.getValue(UserInfo.class).getPhoneNumber() == null){
            return;
        }
        else if (phoneNumber.equals(ds.getValue(UserInfo.class).getPhoneNumber())) {
            status = false;
            layoutCarrierNumber.setError("Phone Number already has an account associated with it!");
            return;
        }


    }

    public void callPhoneVerification(View view){
        status=true;
        if(!ccp.isValidFullNumber()){
            String number = ccp.getFullNumberWithPlus();
            layoutCarrierNumber.setError("Number is not valid");
            return;

        }
        else {
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String number = ccp.getFullNumberWithPlus();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Verify(number,ds);


                    }
                        if(status==true) {
                            Intent previousIntent = getIntent();
                            String fullname = previousIntent.getStringExtra("FullName");
                            String username = previousIntent.getStringExtra("Username");
                            String email = previousIntent.getStringExtra("Email");
                            String password = previousIntent.getStringExtra("Password");
                            String function = previousIntent.getStringExtra("Function");

                            Intent intent = new Intent(getApplicationContext(), OTPAuthentication.class);
                            intent.putExtra("FullName", fullname);
                            intent.putExtra("Username", username);
                            intent.putExtra("Email", email);
                            intent.putExtra("Password", password);
                            intent.putExtra("Function", function);
                            intent.putExtra("Number", number);
                            startActivity(intent);

                        }
                    }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("MyLog",databaseError.getMessage());
                }
            });



        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_third);
        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");

        ccp = findViewById(R.id.ccp);
        layoutCarrierNumber=findViewById(R.id.layout_carrierNumber);


        //Attach

        ccp.registerCarrierNumberEditText(layoutCarrierNumber.getEditText());
        ccp.setNumberAutoFormattingEnabled(true);


        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if(isValidNumber){
                    Log.i("MyLog","the Number has changed and it's valid");
                }else {
                Log.i("MyLog","the Number has changed but it's invalid");
            }}
        });
    }





    /*private boolean validatePhoneNumber(){
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
    }*/
}

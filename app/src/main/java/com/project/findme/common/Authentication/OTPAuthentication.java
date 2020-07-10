package com.project.findme.common.Authentication;

import androidx.annotation.MainThread;
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

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.project.findme.HelperClasses.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.findme.R;

import java.util.concurrent.TimeUnit;

public class OTPAuthentication extends AppCompatActivity {
    String verificationCode;
    TextView numberMessage;
    Button verify_button;
    PinView pinView;


    public void verifyPhone(){
        if(getIntent().getStringExtra("Password")!=null) {
            Intent previousIntent = getIntent();
            String number = previousIntent.getStringExtra("Number");
            numberMessage = findViewById(R.id.numberMessage);
            numberMessage.setText(numberMessage.getText().toString().concat(number));

            sendVerificationCode(number);


        }
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_o_t_p_authentication);

        verify_button=findViewById(R.id.verify_btn);
        pinView=findViewById(R.id.PinView);


        verifyPhone();
        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = pinView.getText().toString();

                if(code.isEmpty() || code.length()<6){
                    pinView.setText(R.string.wrong_otp);
                    //pinView.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Log.i("MyLog","code = "+s);
            verificationCode = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){

                    verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OTPAuthentication.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };


    private void verifyCode(String codeByUser){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,codeByUser);
        signInUserByCredential(credential);

    }

    private void signInUserByCredential(PhoneAuthCredential credential) {
        final FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){


                            Intent previousIntent = getIntent();
                            final String fullname = previousIntent.getStringExtra("FullName");
                            final String username = previousIntent.getStringExtra("Username");
                            final String email = previousIntent.getStringExtra("Email");
                            String password = previousIntent.getStringExtra("Password");
                            final String function = previousIntent.getStringExtra("Function");
                            final String number = previousIntent.getStringExtra("Number");


                            //Creating Email Credentials
                            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                            Log.i("MyLog","Before Linking");
                            firebaseAuth.getCurrentUser().linkWithCredential(credential)
                                    .addOnCompleteListener(TaskExecutors.MAIN_THREAD, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()) {
                                                Log.i("MyLog", "linkWithCredential:success");
                                                FirebaseUser user = task.getResult().getUser();


                                                //Add user to Firebase Database

                                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                                DatabaseReference users = database.getReference("users");
                                                UserInfo userInfo = new UserInfo(fullname,username,email,number,function);
                                                users.child(user.getUid()).setValue(userInfo);
                                                Log.i("MyLog",userInfo.toString());


                                                //updateUI(user);

                                            }
                                            else {

                                                Log.w("MyLog", "linkWithCredential:failure", task.getException());
                                                Toast.makeText(OTPAuthentication.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                                //updateUI(null);

                                            }
                                        }
                                    });

                            Intent intent = new Intent(getApplicationContext() , Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                        
                        else{

                            Toast.makeText(OTPAuthentication.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


}

package com.project.findme.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.findme.HelperClasses.GroupInfo;
import com.project.findme.R;
import com.project.findme.common.Authentication.StartScreen;

import java.util.List;

public class JoinGroup extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference groups;
    PinView pinView;
    Button joinButton;
    boolean status=true;
    
    public void callJoinGroupSuccess(View view){
        status=true;
        if(!validateInviteCode()){
            return;

        }
        else{
            final String inviteCode = pinView.getText().toString();
            groups.child(inviteCode).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()==null){
                        Log.i("MyLog","Child does not exist");
                    }


                    else{
                        List<String> users = dataSnapshot.getValue(GroupInfo.class).getUsersUID();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user==null){
                            startActivity(new Intent(getApplicationContext(), StartScreen.class));
                            finish();
                        }
                        if(users.contains(user.getUid())){
                            pinView.setError("You are already in this group!");
                        }else{
                            groups.child(inviteCode).child("usersUID").child(String.valueOf(users.size())).setValue(user.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent=new Intent(getApplicationContext(),JoinGroupSuccess.class);
                                        intent.putExtra("inviteCode",inviteCode);
                                        startActivity(intent);

                                    }else{
                                        Log.i("MyLog",task.getException().getMessage());
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





        }
        
        
        
        
        
        
    }

    private boolean validateInviteCode() {
        String code = pinView.getText().toString();
        if(code.length()<8){
            pinView.setError("A 8 digit invite code is required!");
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join_group);
        pinView=findViewById(R.id.PinView);
        joinButton=findViewById(R.id.join_button);
        database = FirebaseDatabase.getInstance();
        groups = database.getReference("groups");
    }
}

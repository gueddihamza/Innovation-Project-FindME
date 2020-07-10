package com.project.findme.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.findme.HelperClasses.GroupInfo;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupInviteCode extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference groups;
    Button next_btn;
    PinView pinView;
    boolean status=true;


    public void Verify(String inviteCode ,DataSnapshot ds){
        Log.i("MyLog",ds.getKey());

        if(ds.getKey() == null){
            return;
        }
        else if (inviteCode.equals(ds.getKey())) {
            status = false;
            pinView.setError("This invite code already belongs to a group! Please choose another code");
            return;
        }
    }
    public void saveNewGroup(View view){
        status=true;
        if(!validateInviteCode()){
            return;

        }else{
            groups.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String inviteCode = pinView.getText().toString();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Verify(inviteCode,ds);
                    }

                    if(status){

                        Intent previousIntent = getIntent();
                        String group_name = previousIntent.getStringExtra("GroupName");
                        String group_desc = previousIntent.getStringExtra("GroupDescription");
                        String group_type = previousIntent.getStringExtra("Type");
                        String owner_uid = previousIntent.getStringExtra("OwnerUID");
                        List<String> group_users = new ArrayList<String>();
                        group_users.add(owner_uid);
                        GroupInfo group = new GroupInfo(group_name,group_desc,group_type,owner_uid,group_users);
                        groups.child(inviteCode).setValue(group).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent intent=new Intent(getApplicationContext(),CreateGroupSuccess.class);
                                    intent.putExtra("groupUID",inviteCode);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(CreateGroupInviteCode.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });







                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("MyLog",databaseError.getMessage());
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
        setContentView(R.layout.activity_create_group_change_invite_code);
        next_btn=findViewById(R.id.next_btn);
        pinView=findViewById(R.id.PinView);
        database = FirebaseDatabase.getInstance();
        groups = database.getReference("groups");
    }
}

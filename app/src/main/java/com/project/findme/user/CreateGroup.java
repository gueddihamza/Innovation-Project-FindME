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

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.findme.HelperClasses.GroupInfo;
import com.project.findme.HelperClasses.UserInfo;
import com.project.findme.R;
import com.project.findme.common.Authentication.OTPAuthentication;
import com.project.findme.common.Authentication.StartScreen;

public class CreateGroup extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference groups;
    Button callNext;
    TextInputLayout group_name;
    TextInputLayout group_desc;
    TextView group_type;
    boolean status = true;


    public void Verify(String groupName ,DataSnapshot ds){

        if(ds.getValue(GroupInfo.class).getGroupName() == null){
            return;
        }
        else if (groupName.equals(ds.getValue(GroupInfo.class).getGroupName())) {
            status = false;
            group_name.setError("This Group Name already exists! Please choose another name.");
            return;
        }


    }

    public boolean verifyGroupName(){
        String groupName = group_name.getEditText().getText().toString();
        if(groupName.length()<3){
            return false;
        }

    return true;
    }

    public void callNextScreen(View view){
        status=true;
        group_name.setError(null);
        if(!verifyGroupName()){
            group_name.setError("Group Name is too short");
            return;
        }
        else{
            groups.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String groupName = group_name.getEditText().getText().toString();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Verify(groupName,ds);
                    }

                        if(status) {
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            if(uid==null){
                                startActivity(new Intent(getApplicationContext(), StartScreen.class));
                                finish();
                            }
                            else {
                                Intent previousIntent = getIntent();
                                String type = previousIntent.getStringExtra("Type");
                                Intent intent = new Intent(getApplicationContext(), CreateGroupInviteCode.class);
                                intent.putExtra("GroupName", group_name.getEditText().getText().toString());
                                intent.putExtra("GroupDescription", group_desc.getEditText().getText().toString());
                                intent.putExtra("Type", type);
                                intent.putExtra("OwnerUID", uid);
                                startActivity(intent);
                            }
                        }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("MyLog",databaseError.getMessage());
                }
            });






        }




    }
    public void changeStatus(){
        Intent intent=getIntent();
        String status = intent.getStringExtra("Type");
        group_type.setText(group_type.getText().toString().concat(" "+status));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_group);
        group_type=findViewById(R.id.group_type);
        callNext=findViewById(R.id.next_btn);
        database = FirebaseDatabase.getInstance();
        groups = database.getReference("groups");
        group_name=findViewById(R.id.group_name);
        group_desc=findViewById(R.id.group_description);
        changeStatus();
    }
}

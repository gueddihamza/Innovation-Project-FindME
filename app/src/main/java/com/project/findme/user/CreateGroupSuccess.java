package com.project.findme.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.project.findme.R;

public class CreateGroupSuccess extends AppCompatActivity {
    Button go_to_Group;

    public void callGroupScreen(View view){
        String groupUID = getIntent().getStringExtra("groupUID");
        Intent intent=new Intent(getApplicationContext(),MessageActivity.class);
        intent.putExtra("groupUID",groupUID);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_group_success);
        go_to_Group=findViewById(R.id.button_gotoGroup);
    }
}

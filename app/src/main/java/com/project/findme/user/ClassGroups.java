package com.project.findme.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.project.findme.R;

public class ClassGroups extends AppCompatActivity {
    Button btn_create;
    Button btn_join;
    Button btn_search;
    public void callCreateGroupScreen(View view){
        Intent intent=new Intent(getApplicationContext(),CreateGroup.class);
        intent.putExtra("Type","class");
        startActivity(intent);
    }

    public void callJoinGroupScreen(View view){
        startActivity(new Intent(getApplicationContext(),JoinGroup.class));
    }

    public void callSearchGroupScreen(View view){
        Intent intent=new Intent(getApplicationContext(),SearchGroup.class);
        intent.putExtra("Type","class");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_class_groups);
        btn_create=findViewById(R.id.button_create);
        btn_join=findViewById(R.id.ButtonJoin);
        btn_search=findViewById(R.id.ButtonSearch);
    }
}

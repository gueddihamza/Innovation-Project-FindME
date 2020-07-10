package com.project.findme.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.project.findme.R;

public class GroupsMain extends AppCompatActivity {

    ImageView back_btn;
    Button expand_class_groups;
    Button expand_club_groups;
    Button expand_custom_groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_groups_main);
        back_btn = findViewById(R.id.back_pressed);
        expand_class_groups=findViewById(R.id.expand_all_class_groups);
        expand_club_groups=findViewById(R.id.expand_all_club_groups);
        expand_custom_groups=findViewById(R.id.expand_all_custom_groups);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupsMain.super.onBackPressed();
            }
        });

        expandClassGroups();
        expandClubGroups();
        expandCustomGroups();
    }

    private void expandClassGroups() {
        expand_class_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ClassGroups.class));
            }
        });
    }

    private void expandClubGroups() {
        expand_club_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ClubGroups.class));
            }
        });
    }

    private void expandCustomGroups() {
        expand_custom_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CustomGroups.class));
            }
        });
    }
}

package com.project.findme.common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.findme.R;
import com.project.findme.common.Authentication.StartScreen;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIMER = 5000;
    /*Button button;
    TextView latitude;
    TextView longitude;*/
    //Variables
    ImageView backgroundImage;
    TextView created_by;

    //Animations
    Animation sideAnim;
    Animation bottomAnim;


    SharedPreferences onBoardingScreen;


   /* public void showMap(View view){
        Intent intent = new Intent(this.getApplicationContext(), MapsActivity.class);
        startActivity(intent);


    }*/




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);


        //Design elements
        backgroundImage=findViewById(R.id.backgroundImage);
        created_by=findViewById(R.id.created_by);

        //Animations
        sideAnim= AnimationUtils.loadAnimation(this,R.anim.side_anim);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        //Set animations on elements
        backgroundImage.setAnimation(sideAnim);
        created_by.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBoardingScreen = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime",true);

                if(isFirstTime){
                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime",false);
                    editor.commit();
                Intent intent = new Intent(SplashScreen.this , OnBoarding.class);
                startActivity(intent);
                finish();
                }


                else{

                    Intent intent = new Intent(SplashScreen.this , StartScreen.class);
                    startActivity(intent);
                    finish();

                }
            }
        } , SPLASH_TIMER);

    }


}

package com.example.dell.jaapactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SplashActivity extends AppCompatActivity {
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    TextView appname;
    ImageView logoapp;

    private int RECORD_AUDIO_PERMISSION = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appname = findViewById(R.id.appnamesp);
        logoapp = findViewById(R.id.logoapp);
        YoYo.with(Techniques.Bounce).duration(1000).playOn(appname);
        YoYo.with(Techniques.FlipInX).duration(1300).playOn(logoapp);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, startActivity.class);
              ActivityOptionsCompat options =   ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this,appname,
                        ViewCompat.getTransitionName(appname));


                SplashActivity.this.startActivity(mainIntent,options.toBundle());
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);




    }}



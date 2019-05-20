package com.example.dell.jaapactivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.dell.jaapactivity.Tabbed.MainActivity;


public class ScrollingActivity extends AppCompatActivity  {

    private int RECORD_AUDIO_PERMISSION = 200;

    private FragmentManager fragmentManager;
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        fragmentManager = getSupportFragmentManager();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
       navigation.getMenu().getItem(0).setCheckable(false);  // to uncheck first item for preselecting


        final ImageView japActivity = findViewById(R.id.japButton22);
        final ImageView  medActivity = findViewById(R.id.medButton22);
        final ImageView  swaActivity = findViewById(R.id.swaButton22);
        final ImageView   yagyaActivity = findViewById(R.id.yagButton22);


        requestAudioPermission();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_scrolling);




        YoYo.with(Techniques.FadeInUp).duration(2000).playOn(japActivity);
        YoYo.with(Techniques.FadeInUp).duration(1800).playOn(medActivity);
        YoYo.with(Techniques.FadeInUp).duration(1800).playOn(swaActivity);
        YoYo.with(Techniques.FadeInUp).duration(1800).playOn(yagyaActivity);


        japActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScrollingActivity.this,JapActivity.class);
                ActivityOptionsCompat options =

                        ActivityOptionsCompat.makeSceneTransitionAnimation(ScrollingActivity.this,
                                japActivity,   // Starting view
                                ViewCompat.getTransitionName(japActivity)     // The String
                        );
                startActivity(i,options.toBundle());
            }
        });

        medActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScrollingActivity.this, MeditationActivity.class);
                ActivityOptionsCompat options =

                        ActivityOptionsCompat.makeSceneTransitionAnimation(ScrollingActivity.this,
                                medActivity,   // Starting view
                                ViewCompat.getTransitionName(medActivity)     // The String
                        );
                startActivity(i,options.toBundle());
            }
        });
        swaActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScrollingActivity.this,Swadhyay.class);
                ActivityOptionsCompat options =

                        ActivityOptionsCompat.makeSceneTransitionAnimation(ScrollingActivity.this,
                                swaActivity,   // Starting view
                                ViewCompat.getTransitionName(swaActivity)    // The String
                        );
                startActivity(i,options.toBundle());
            }
        });
        yagyaActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScrollingActivity.this,YagyaActivity.class);
                ActivityOptionsCompat options =

                        ActivityOptionsCompat.makeSceneTransitionAnimation(ScrollingActivity.this,
                                yagyaActivity,   // Starting view
                                ViewCompat.getTransitionName(yagyaActivity)    // The String
                        );
                startActivity(i,options.toBundle());
            }
        });

    }
    private void requestAudioPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RECORD_AUDIO_PERMISSION);
    }
    private boolean isReadAudioAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == RECORD_AUDIO_PERMISSION){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        int id = item.getItemId();
        item.setCheckable(true);  // to select the tapped item in bottom nav

        if (id == R.id.all_reports) {
            // Handle the camera action
            Intent reportsIntet = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(reportsIntet);
        }

        if(id == R.id.chat){
            Intent intent = new Intent(this,ChatActivity.class);
            startActivity(intent);
        }

        if(id == R.id.chat){
            Intent intent = new Intent(this,ChatActivity.class);
            startActivity(intent);
        }
        if(id == R.id.nav_share){

        }

        if(id == R.id.nav_send){


        }


        return true;
    };




}

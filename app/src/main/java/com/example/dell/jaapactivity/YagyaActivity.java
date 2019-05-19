package com.example.dell.jaapactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ScrollView;

public class YagyaActivity extends AppCompatActivity {


    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yagya);


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setCheckable(false);  // to uncheck first item for preselecting



    }
    // Bottom nav
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        int id = item.getItemId();
        item.setCheckable(true);  // to select the tapped item in bottom nav

        if (id == R.id.all_reports) {
            // Handle the camera action
            Intent reportsIntet = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(reportsIntet);
        }

        if(id == R.id.chat){
            Intent intent = new Intent(this,ChatActivity.class);
            startActivity(intent);
        }


        if(id == R.id.nav_share){

        }

        if(id == R.id.nav_send){

            Intent intent = new Intent(getApplicationContext(), ScrollingActivity.class);
            startActivity(intent);

        }


        return true;
    };



}

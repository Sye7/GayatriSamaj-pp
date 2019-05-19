package com.example.dell.jaapactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    ImageView dp;
    TextView name;
    TextView mob;
    TextView country;
    Button logOut;
    LinearLayout report;

    String nameT;
    String mobT;
    String countryT;
    String imageT;



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




    BottomNavigationView navigation;


    public void getData() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");


        Query query = reference.orderByChild("id").equalTo(firebaseUser.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);


                    if (user.id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {


                        // If already present
                        nameT = user.getUsername();
                        mobT = user.getMob();
                        countryT = user.getCountry();
                        imageT = user.getImageURL();

                        name.setText(nameT);
                        mob.setText(mobT);
                        country.setText(countryT);
                        Glide.with(ProfileActivity.this).load(imageT).into(dp);

                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void logOut(View view)
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), startActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);


        dp = findViewById(R.id.profileV);
        name = findViewById(R.id.nameV);
        mob = findViewById(R.id.mobV);
        country = findViewById(R.id.countryV);
        logOut = findViewById(R.id.signOutV);
        report = findViewById(R.id.sendReportV);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // Bottom nav
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setCheckable(false);  // to uncheck first item for preselecting




        // getting data
        getData();

    }
}

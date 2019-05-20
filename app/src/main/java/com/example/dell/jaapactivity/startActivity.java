package com.example.dell.jaapactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import static java.security.AccessController.getContext;

public class startActivity extends AppCompatActivity {
    Button singup, login;
    FirebaseUser firebaseUser;
    DatabaseReference reference;



    private int RC_SIGN_IN = 1;
    private int RC_LOGIN_IN = 2;




    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        singup = findViewById(R.id.signUp);
        login = findViewById(R.id.login);


        //check if user is null

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null ) {

            Intent in = new Intent(startActivity.this, ScrollingActivity.class);
            startActivity(in);

        }
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent  i = new Intent(startActivity.this,RegisterActivity.class);
                startActivity(i);


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(startActivity.this, LoginActivity.class);
               startActivity(i);



            }
        });

    }


    Boolean flag = false;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {


            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users");

            Query query = reference.orderByChild("id").equalTo(firebaseUser.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    Toast.makeText(startActivity.this, "Please wait", Toast.LENGTH_SHORT).show();
                    Log.i("yas", " 2");

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Users user = snapshot.getValue(Users.class);

                        if (FirebaseAuth.getInstance().getUid().equals(user.id)) {


                            // If already present
                            flag = true;

                        }

                    }


                    if (flag)                // If already present
                    {

                        Toast.makeText(startActivity.this, " Please Login", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), startActivity.class);
                        startActivity(intent);
                    } else {

                        Intent intent = new Intent(getApplicationContext(), ProfileFill.class);
                        startActivity(intent);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });

        }

        else if ( requestCode == RC_LOGIN_IN && resultCode == RESULT_OK)
        {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users");
            Query query = reference.orderByChild("id").equalTo(FirebaseAuth.getInstance().getUid());

            Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Users user = snapshot.getValue(Users.class);


                        if (FirebaseAuth.getInstance().getUid().equals(user.id)) {


                            // If already present
                            flag = true;
                        }

                    }

                    if(flag)  // If already present
                    {
                        Intent intent = new Intent(getApplicationContext(), ScrollingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {

                        Toast.makeText(startActivity.this, "Not a valid user Register First", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), startActivity.class);
                        startActivity(intent);
                        finish();
                    }


                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }


}

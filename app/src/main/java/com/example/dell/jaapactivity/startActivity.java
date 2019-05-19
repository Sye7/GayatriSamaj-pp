package com.example.dell.jaapactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class startActivity extends AppCompatActivity {
    Button singup, login;
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //check if user is null
        if(firebaseUser != null){
            Intent intent= new Intent(startActivity.this,ScrollingActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        singup =findViewById(R.id.signUp);
        login = findViewById(R.id.login);

        // phone register


        /*   FirebaseAuth.getInstance().signOut();

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.FirebaseLoginTheme)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build()
                          ))
                        .build(),
                RC_SIGN_IN);

            */





        //check if user is null

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!= null){
            Intent in = new Intent(startActivity.this,ScrollingActivity.class);
            startActivity(in);
            finish();
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
}

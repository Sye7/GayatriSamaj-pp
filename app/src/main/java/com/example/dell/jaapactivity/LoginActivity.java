package com.example.dell.jaapactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.jaapactivity.Model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText email,password;
    Button signIn;
    FirebaseAuth auth;
    ImageView back;
    ProgressBar loginProgress;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private final int RC_SIGN_IN = 1;
    boolean flag = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == RC_SIGN_IN && resultCode == RESULT_OK)
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


                        if (user.id.equals(FirebaseAuth.getInstance().getUid())) {


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

                        Toast.makeText(LoginActivity.this, "Not a valid user Register First", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.FirebaseLoginTheme)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.PhoneBuilder().build()
                        ))
                        .build(),
                RC_SIGN_IN);


    }
}

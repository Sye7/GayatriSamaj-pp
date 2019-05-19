package com.example.dell.jaapactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
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
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username , email, password;
    Button register;
    MaterialEditText phoneNumber;
    FirebaseAuth  auth;
    DatabaseReference reference;
    boolean verified;
    TextView verify,verify_email;
    ProgressBar registerProgress;
    ImageView back;
    private String mVerificationId;
    private static final String TAG = "RegisterActivity";

    private final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


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


    Boolean flag = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == RC_SIGN_IN && resultCode == RESULT_OK)
        {

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference("Users");



            Query query = reference.orderByChild("id").equalTo(firebaseUser.getUid());

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    Toast.makeText(RegisterActivity.this, "Please wait", Toast.LENGTH_SHORT).show();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Users user = snapshot.getValue(Users.class);

                        Log.i("yas","yas2" + user.id + user.country + firebaseUser.getUid());

                        if (user.id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                            Log.i("yas","new" + user.id + user.country + firebaseUser.getUid());

                            // If already present
                            flag = true;
                        }

                    }


                    if(flag)                // If already present
                    {

                        Log.i("yas","reg 1");
                        Toast.makeText(RegisterActivity.this, " Please Login", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), startActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Log.i("yas","reg 2");

                        Intent intent = new Intent(getApplicationContext(),ProfileFill.class);
                        startActivity(intent);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }





    }

    private boolean checkIfEmailIsVerified() {
        if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {

            verified = true;
        }
        return verified;

    }




    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void register(final String username, final String email, final String password, final String phoneNumber){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            final String userID   = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userID);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("phoneNumber", phoneNumber);
                            hashMap.put("emailID",email);
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        registerProgress.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });

                            //Toast.makeText(RegisterActivity.this,"Check your email and click on the link",Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(RegisterActivity.this,"You can't register with this email or password ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });




    }


}

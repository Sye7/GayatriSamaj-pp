package com.example.dell.jaapactivity;


import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;


public class ProfileFill extends AppCompatActivity {

    final int RC_PHOTO_PICKER = 1;
    private DatabaseReference mMessageDatabaseReferenceUser;

    private StorageReference mChatPhotoStorageRef;
    private FirebaseStorage mFirebaseStorage;

    private String mUsername;
    private FirebaseAuth authFirebase;

    String name = "mhsadgh";
    String lastName = "mhsadgh";
    String email = "mhsadgh";
    String country ="sahdg";
    String mobileNo ="sahdg";

    EditText editTextName;
    EditText editTextLastName;
    EditText editEmail;

    EditText editMobileNo;
    EditText editTextCountry;



    Boolean flag;
    DatabaseReference reference;
    TextInputLayout fn;
    TextInputLayout ln;
    TextInputLayout pn;
    TextInputLayout ct;
    TextInputLayout em;

    public void submit(final View view){



        // check if already exists

        reference = FirebaseDatabase.getInstance().getReference("Users");


        Snackbar.make(view,"Please wait while data is uploading",Snackbar.LENGTH_LONG).show();


        name = editTextName.getText().toString();
        lastName = editTextLastName.getText().toString();
        country = editTextCountry.getText().toString();
        mobileNo = editMobileNo.getText().toString();
        email = editEmail.getText().toString();


        if(name.isEmpty())
            fn.setError("Invalid First Name");
        if(lastName.isEmpty())
            fn.setError("Invalid Last Name");
        if(email.isEmpty())
            fn.setError("Invalid Email");
        if(country.isEmpty())
            fn.setError("Invalid Country Name");

        name = name + " "+lastName;


        String uid = FirebaseAuth.getInstance().getUid();


        Users profileInfo =
                new Users(uid, "urlImage", name, country, mobileNo,email );
        mMessageDatabaseReferenceUser.push().setValue(profileInfo);
        Snackbar.make(view,"Done :)",Snackbar.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),ScrollingActivity.class);
        startActivity(intent);
        finish();


    }

    private int RC_SIGN_IN = 1;


    public void logout()
    {
        FirebaseAuth.getInstance().signOut();

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.FirebaseLoginTheme)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build(),
                                new AuthUI.IdpConfig.AnonymousBuilder().build()))
                        .build(),
                RC_SIGN_IN);

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.profile_reg);



        // Init
        authFirebase = FirebaseAuth.getInstance();
        mUsername = "ANOMINOUS";

        mMessageDatabaseReferenceUser =FirebaseDatabase.getInstance().getReference().child("Users");
        mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotoStorageRef = mFirebaseStorage.getReference().child("image_photos");

        editTextName = (EditText) findViewById(R.id.nameP);
        editTextCountry = (EditText) findViewById(R.id.countryP);
        editMobileNo = findViewById(R.id.mobP);
        editTextLastName = findViewById(R.id.lastNameP);
        editEmail = findViewById(R.id.emailP);

        if(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null)
        editMobileNo.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

        fn = findViewById(R.id.fn);
        ln = findViewById(R.id.ln);
        pn = findViewById(R.id.pn);
        ct = findViewById(R.id.ct);
        em = findViewById(R.id.ea);





    }

}
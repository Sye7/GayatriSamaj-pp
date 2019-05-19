package com.example.dell.jaapactivity;


import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
    static   Uri downloadUri;
    private DatabaseReference mMessageDatabaseReferenceUser;

    private StorageReference mChatPhotoStorageRef;
    private FirebaseStorage mFirebaseStorage;

    private String mUsername;
    private FirebaseAuth authFirebase;

    Uri selectedImageUri;
    String name = "mhsadgh";
    String country ="sahdg";
    String mobileNo ="sahdg";

    EditText editTextName;
    EditText editMobileNo;
    EditText editTextCountry;
    ImageView dp;
    ProgressBar progressBar;



    Boolean flag;
    DatabaseReference reference;

    public void submit(final View view){



        // check if already exists

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");


        if(mobileNo == null)
        {
            Snackbar.make(view,"Fill Mobile Number first",Snackbar.LENGTH_SHORT).show();
            return;
        }


        if(selectedImageUri == null){

            Snackbar.make(view,"Please select DP first",Snackbar.LENGTH_LONG).show();
            return;
        }

        Snackbar.make(view,"Please wait while data is uploading",Snackbar.LENGTH_LONG).show();

        name = editTextName.getText().toString();
        country = editTextCountry.getText().toString();
        mobileNo = editMobileNo.getText().toString();

        String uid = FirebaseAuth.getInstance().getUid();


        // upload


        //Get a reference to store file at image_photos/<FileName>
        final StorageReference photoRef = mChatPhotoStorageRef.child(selectedImageUri.getLastPathSegment());


        //Upload file to firebase storage
        photoRef.putFile(selectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // got image url
                        downloadUri = uri;

                        Users profileInfo =
                                new Users(uid, downloadUri.toString(), name, country, mobileNo );
                        mMessageDatabaseReferenceUser.push().setValue(profileInfo);
                        Snackbar.make(view,"Done :)",Snackbar.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(),ScrollingActivity.class);
                        startActivity(intent);
                        finish();

                    }


                });


            }
        });

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
        setContentView(R.layout.activity_profile);



        // Init
        authFirebase = FirebaseAuth.getInstance();
        mUsername = "ANOMINOUS";

        mMessageDatabaseReferenceUser =FirebaseDatabase.getInstance().getReference().child("Users");
        mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotoStorageRef = mFirebaseStorage.getReference().child("image_photos");

        dp =  findViewById(R.id.dpP);
        editTextName = (EditText) findViewById(R.id.nameP);
        editTextCountry = (EditText) findViewById(R.id.countryP);
        editMobileNo = findViewById(R.id.mobP);





        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // TODO: Fire an intent to show an image picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"Complete Action Using"),RC_PHOTO_PICKER);


            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){

            selectedImageUri = data.getData();

        }


    }
}
package com.example.dell.jaapactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends AppCompatActivity {

    MaterialEditText verificationCode;
    String verificationId;
    Button done;
    FirebaseAuth mAuth;
    private static final String TAG = "PhoneVerificationActivi";
    ProgressBar progressBar;
    String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        progressBar = findViewById(R.id.progress);
        mAuth = FirebaseAuth.getInstance();
        verificationCode = findViewById(R.id.verification_two);
         phone_number = getIntent().getStringExtra("phoneNumber");
        Log.d(TAG, "onCreate: Phone Number "+ phone_number);
        sendVerificationCode(phone_number);
        done = findViewById(R.id.done);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = verificationCode.getText().toString().trim();
                if(code.isEmpty()||code.length()<6){
                    verificationCode.setError("Enter Code");
                    verificationCode.requestFocus();
                    return;
                }

              //  verifyCode(code);

            }
        });

    }

    private void verifyCode(String code){
        PhoneAuthCredential  credential = PhoneAuthProvider.getCredential(verificationId,code);
       signInWithCredential(credential);
       // Intent in = new Intent(PhoneVerificationActivity.this,RegisterActivity.class);
       // in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
       // startActivity(in);
        finish();
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
          mAuth.signInWithCredential(credential)
                  .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent in = new Intent(PhoneVerificationActivity.this,RegisterActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(in);
                            }
                            else {
                                Toast.makeText(PhoneVerificationActivity.this,"Error",Toast.LENGTH_SHORT).show();
                            }
                      }
                  });
    }

    public void sendVerificationCode(String number){
        Log.d(TAG, "sendVerificationCode: "+ number);
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
               "+91" +number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback
            =  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
           if(code != null){
               verificationCode.setText(code);
               verifyCode(code);
           }
        }


        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneVerificationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    };
}

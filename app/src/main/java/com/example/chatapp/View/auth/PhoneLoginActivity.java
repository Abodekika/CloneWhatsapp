package com.example.chatapp.View.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.ModelClass.User;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;


    private static final String TAG = "PhoneLoginActivity";

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private ProgressDialog progressDialog;
    Button btn_next;
    EditText et_phone;
    EditText et_code_country;
    EditText et_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_login);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(this, UserInfoActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        btn_next = findViewById(R.id.btn_next);
        et_phone = findViewById(R.id.et_phone);
        et_code = findViewById(R.id.et_code);
        et_code_country = findViewById(R.id.et_code_country);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: Completed");
                signInWithPhoneAuthCredential(phoneAuthCredential);
                progressDialog.dismiss();
                //   signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "onVerificationFailed: " + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;

                // verifyPhoneNumberWithCode(mVerificationId, et_code.getText().toString());

                btn_next.setText("Confirm");
                progressDialog.dismiss();
            }
        };


    }




    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        progressDialog.setMessage("send code to" + phoneNumber);
        progressDialog.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInWithPhoneAuthCredential(credential);
    }


    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            if (user != null) {
                                String userID = user.getUid();
                                User users = new User(
                                        userID,
                                        "",
                                        "A",
                                        user.getPhoneNumber(),
                                        "",
                                        "",
                                        "",
                                        "",
                                        "");

                                firestore.collection("User").document(userID).getParent().
                                        add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        startActivity(new Intent(PhoneLoginActivity.this, UserInfoActivity.class));
                                    }
                                });
                            } else {
                                Toast.makeText(PhoneLoginActivity.this, "Error in creating ", Toast.LENGTH_SHORT).show();
                            }


                            //  startActivity(new Intent(PhoneLoginActivity.this, HomeActivity.class));
                            // Update UI
                        } else {
                            progressDialog.dismiss();
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Log.d(TAG, "Error Code");
                            }
                        }
                    }
                });
    }


    public void btn_next(View view) {
        String phone = "+" + et_code_country.getText().toString() + et_phone.getText().toString();
        if (btn_next.getText().toString().equals("Next")) {
            progressDialog.setMessage("please wait");
            progressDialog.show();

            startPhoneNumberVerification(phone);
        } else {
            progressDialog.setMessage("verifying...");
            progressDialog.show();
            verifyPhoneNumberWithCode(mVerificationId, et_code.getText().toString());
        }


    }

}
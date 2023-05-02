package com.example.chatapp.View.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.ModelClass.User;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView txt_signUp, btn_signUp;
    EditText reg_name, reg_email, reg_password, reg_confirm_password;
    private FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Uri imageUri;
    FirebaseDatabase database;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    String imageURI;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //getSupportActionBar().hide();

        //ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        //
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //find
        btn_signUp = findViewById(R.id.reg_signup);
        txt_signUp = findViewById(R.id.txt_signIn);
        profile_image = findViewById(R.id.profile_image);
        reg_email = findViewById(R.id.reg_email);
        reg_name = findViewById(R.id.reg_name);
        reg_password = findViewById(R.id.reg_password);
        reg_confirm_password = findViewById(R.id.reg_confirm_password);
        //
        Date date = new Date();
        //button sing up
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                String name = reg_name.getText().toString();
                String email = reg_email.getText().toString();
                String password = reg_password.getText().toString();
                String C_password = reg_confirm_password.getText().toString();
                String status = " Hey There I'm using App";


                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(C_password)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Please Enter Valid Data ", Toast.LENGTH_SHORT).show();

                }
                /*
                else if (email.matches(emailPattern)) {
                    reg_email.setError("Please Enter Valid Email");
                    Toast.makeText(RegistrationActivity.this, "Please Enter Valid Email ", Toast.LENGTH_SHORT).show();
                }
                */
                else if (!password.equals(C_password)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Password does not match ", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Password 6 character password ", Toast.LENGTH_SHORT).show();

                }


                /////////////
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();

                            FirebaseUser user = task.getResult().getUser();
                            if (user != null) {
                                String userID = user.getUid();
                                User users = new User(
                                        userID,
                                        "",
                                        user.getEmail(),
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "");
                                firestore.collection("User").document(userID).getParent().
                                        add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        startActivity(new Intent(RegistrationActivity.this, UserInfoActivity.class));
                                    }
                                });
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Error in creating ", Toast.LENGTH_SHORT).show();
                            }


                            //  startActivity(new Intent(PhoneLoginActivity.this, HomeActivity.class));
                            // Update UI
                        } else {
                            progressDialog.dismiss();
                            // Sign in failed, display a message and update the UI


                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                            }
                        }

                    }
                });
                ////////////////



/*
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();

                            DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                            StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                            if (imageUri != null) {

                                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    imageURI = uri.toString();

                                                    User user = new User(auth.getUid(), name, email, imageURI, status, date.toString(), "", "", "");
                                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                progressDialog.dismiss();
                                                                startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                                            } else {
                                                                Toast.makeText(RegistrationActivity.this, "Error in creating ", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    }
                                });

                            } else {
                                String status = " Hey There I'm using App";
                                imageURI = "https://firebasestorage.googleapis.com/v0/b/chatapp-914c6.appspot.com/o/profile_image.png?alt=media&token=87c96b7e-c1ca-45ea-ad0c-825449a6ecf9";
                                User user = new User(auth.getUid(), name, email, imageURI, status, date.toString(), "", "", "");
                                reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                        } else {
                                            Toast.makeText(RegistrationActivity.this, "Error in creating ِاةثي", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Something  went wrong ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }


        });
*/
                // image profile
                profile_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
                    }
                });

                //

                // singup
                txt_signUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(RegistrationActivity.this, PhoneLoginActivity.class));
                    }
                });
                //

            }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (data != null) {
                imageUri = data.getData();
                profile_image.setImageURI(imageUri);
            }
        }


    }
    */
        });
    }
}
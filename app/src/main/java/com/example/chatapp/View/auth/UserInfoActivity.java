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
import com.example.chatapp.View.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoActivity extends AppCompatActivity {

    EditText editText;
    Button confirm;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        editText = findViewById(R.id.et_name);
        confirm = findViewById(R.id.btn_confirm);
        progressDialog = new ProgressDialog(this);

        initButtonClick();

    }

    private void initButtonClick() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(UserInfoActivity.this, "Please Enter input username  ", Toast.LENGTH_SHORT).show();
                } else {
                    doUpdate();
                }
            }
        });

    }

    public void doUpdate() {
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {

            String userID = firebaseUser.getUid();
            User users = new User(
                    userID,
                    editText.getText().toString(),
                    "A",
                    firebaseUser.getPhoneNumber(),
                    "",
                    "",
                    "",
                    "",
                    "");

            firestore.collection("User").document(firebaseUser.getUid()).set(users)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(UserInfoActivity.this, "Updating Successful ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Log.d("updating", "onFailure: " + e.getMessage());
                    Toast.makeText(UserInfoActivity.this, "updating failed  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(UserInfoActivity.this, "Error in creating  ", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

}


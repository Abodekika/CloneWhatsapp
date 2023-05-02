package com.example.chatapp.View.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.example.chatapp.View.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextView txt_signUp;
    EditText login_email, login_password;
    TextView signin_btn;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        txt_signUp = findViewById(R.id.txt_signUp);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        signin_btn = findViewById(R.id.signup_btn);

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email.getText().toString().trim();
                String password = login_password.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Enter Valid Data", Toast.LENGTH_SHORT).show();

                }
                /*
                else if (!email.matches(emailPattern)) {
                    login_email.setError("Envalid Email");
                    Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }

                 */
                else if (password.length() < 6) {
                    login_email.setError("Envalid Email");
                    Toast.makeText(LoginActivity.this, "please enter valid password", Toast.LENGTH_SHORT).show();
                } else {

                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                            } else {
                                Toast.makeText(LoginActivity.this, "Error in login", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });
        txt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }
}
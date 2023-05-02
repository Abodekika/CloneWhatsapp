package com.example.chatapp.View.Setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.example.chatapp.View.profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {


    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    TextView et_User_name;
    TextView et_User_status;
    LinearLayout linearLayout;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        et_User_name = findViewById(R.id.user_name);
        et_User_status = findViewById(R.id.User_status);
        linearLayout =findViewById(R.id.user_Profile);
        circleImageView = findViewById(R.id.sett_user_image);



        if (firebaseUser != null) {

            getInfo();

        }
        initClickAction();

    }

    public void initClickAction (){
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });
    }

    private void getInfo() {

        firestore.collection("User").document(firebaseUser.getUid()).get().addOnSuccessListener(documentSnapshot -> {


            String U_name = documentSnapshot.getString("u_name");
            String U_Status = documentSnapshot.getString("u_status");
            String U_Image = documentSnapshot.getString("u_imageUri");

            et_User_name.setText(U_name);
            et_User_status.setText(U_Status);
            Uri Image = Uri.parse(U_Image);

            Picasso.get().load(Image).into(circleImageView);

        }).addOnFailureListener(e -> {
            Log.d("updating", "onFailure: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Failure  " + e.getMessage(), Toast.LENGTH_SHORT).show();

        });
    }
}
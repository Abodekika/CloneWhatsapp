package com.example.chatapp.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.ModelClass.User;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {


    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    ImageView save;
    CircleImageView setting_image;
    EditText setting_name, setting_status;
    Uri selectedImageURI;
    String email;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        Date date = new Date();
        setting_image = findViewById(R.id.setting_image);
        setting_name = findViewById(R.id.setting_name);
        setting_status = findViewById(R.id.setting_Status);

        save = findViewById(R.id.save);


        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("email").getValue().toString();
                String name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                String status = snapshot.child("status").getValue().toString();
                String image = snapshot.child("imageUri").getValue().toString();


                setting_name.setText(name);
                setting_status.setText(status);
                Picasso.get().load(image).into(setting_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setting_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

        save.setOnClickListener(v -> {
            progressDialog.show();
            String name = setting_name.getText().toString();
            String status = setting_status.getText().toString();

            if (selectedImageURI != null) {
                storageReference.putFile(selectedImageURI).addOnCompleteListener(task ->
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String finalImageURi = uri.toString();
                                User user = new User(auth.getUid(), name, email, finalImageURi, status, date.toString(), "", "", "");
                                reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SettingActivity.this, "data successful updated", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(SettingActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                        }));

            }
            else {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String finalImageURi = uri.toString();
                        User user = new User(auth.getUid(), name, email, finalImageURi, status, date.toString(), "", "", "");
                        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(SettingActivity.this, "data successful updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SettingActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (data != null) {
                selectedImageURI = data.getData();
                setting_image.setImageURI(selectedImageURI);
            }
        }


    }
}
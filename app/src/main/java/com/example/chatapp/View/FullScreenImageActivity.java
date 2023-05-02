package com.example.chatapp.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.ModelClass.Message;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Random;

public class FullScreenImageActivity extends AppCompatActivity {
    ImageView imageView;
    String ImageUri;

    ChatActivity chatActivity =new ChatActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        imageView = findViewById(R.id.imageView1);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            ImageUri = bundle.getString("image","Moha");
            Toast.makeText(FullScreenImageActivity.this, "تمام ", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(FullScreenImageActivity.this, "احا ", Toast.LENGTH_SHORT).show();
        }



        Picasso.get().load(ImageUri).into(imageView);

        //imageView.setImageURI(ImageUri);
      //  fullScreenInd = getIntent().getStringExtra("fullScreenIndicator");
    }


}
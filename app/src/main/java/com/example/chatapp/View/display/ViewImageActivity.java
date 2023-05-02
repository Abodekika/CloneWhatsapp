package com.example.chatapp.View.display;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends AppCompatActivity {

    ImageView zoomageView;
    String d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        zoomageView=findViewById(R.id.zoom_image_view);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            d = bundle.getString("s", "Moha");
            Toast.makeText(getApplicationContext(), "تمام ", Toast.LENGTH_SHORT).show();

            Uri uri =Uri.parse(d);

            Picasso.get().load(uri).into(zoomageView);
        }
        else {
            Toast.makeText(getApplicationContext(), "احا ", Toast.LENGTH_SHORT).show();
        }

      //

      //  zoomageView.setImageURI(uri);




    }
}
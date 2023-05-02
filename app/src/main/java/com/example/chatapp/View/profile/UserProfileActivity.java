package com.example.chatapp.View.profile;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {
    String ReceiverImage, ReceiverUID, ReceiverName;
    CollapsingToolbarLayout toolbar_profile;
    ImageView user_img_profile;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar_profile = findViewById(R.id.toolbar_profile);
        //view_profile =findViewById(R.id.view_profile);


        ReceiverImage = getIntent().getStringExtra("receiver_image_profile");
        ReceiverName = getIntent().getStringExtra("receiver_name");
        ReceiverUID = getIntent().getStringExtra("receiver_id");

        user_img_profile = findViewById(R.id.user_img_profile);

        if (ReceiverUID != null) {
            toolbar_profile.setTitle(ReceiverName);
            if (ReceiverImage.equals("")) {
                user_img_profile.setImageResource(R.drawable.placeholder);
            } else {
                Picasso.get().load(Uri.parse(ReceiverImage)).into(user_img_profile);
            }

        }

        initToolBar();
    }


    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.U_toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
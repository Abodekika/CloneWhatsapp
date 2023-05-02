package com.example.chatapp.View.display;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class review_send_image extends AppCompatActivity {

    ImageView imageView;
    String ImageUri;

    String ReceiverImage, ReceiverUID, ReceiverName, SenderId;

    FirebaseDatabase database;

    FirebaseAuth auth;


    EditText editMessage;

    String senderRoom, receiverRoom;


    ProgressDialog dialog;

    FloatingActionButton  actionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_send_image);

        auth = FirebaseAuth.getInstance();
        SenderId = auth.getUid();


        dialog = new ProgressDialog(this);
        actionButton = findViewById(R.id.review_floatingButton);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(review_send_image.this, "تمام ", Toast.LENGTH_SHORT).show();
            }
        });

        //

        imageView = findViewById(R.id.zoomView);
        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            ImageUri = bundle.getString("image", "Moha");
            imageView.setImageURI(Uri.parse(ImageUri));
            Picasso.get().load(ImageUri).into(imageView);
            Toast.makeText(review_send_image.this, "تمام ", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(review_send_image.this, "احا ", Toast.LENGTH_SHORT).show();
        }




        //  uploadImageToFireBaseStorage(view, Uri.parse(ImageUri));
        //  dialog.setMessage("بيحمل ي نجم ");
//
    }

/*
    public void uploadImageToFireBaseStorage(View view) {
        Uri uri = Uri.parse(ImageUri);

        Random random = new Random();
        int randomname = random.nextInt() * 10000;

        StorageReference storageReference2 = FirebaseStorage.getInstance().getReference().child("chatImage/" + randomname);

        //  StorageReference storageReference = storage.getReference().child("chats");
        dialog.show();
        dialog.setMessage("بيحمل ي نجم ");

        storageReference2.putFile(uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                dialog.dismiss();


                storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String message = uri.toString();


                        if (message.isEmpty()) {
                            Toast.makeText(review_send_image.this, "pleas enter Valid Message", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editMessage.setText("");
                        Date date = new Date();
                        Message messages = new Message(message, SenderId, date.getTime());

                        messages.setImageUri(message);

                        messages.setMessage("photo");

                        database = FirebaseDatabase.getInstance();

                        database.getReference().child("chats")
                                .child(senderRoom)
                                .child("messages")
                                .push()
                                .setValue(messages)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        database.getReference().child("chats")
                                                .child(receiverRoom)
                                                .child("messages")
                                                .push()
                                                .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                               Toast.makeText(review_send_image.this, "تمام ", Toast.LENGTH_LONG).show();

                                            }
                                        });
                                    }
                                });

                    }
                });


            }


        });

    }

 */
}
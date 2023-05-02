package com.example.chatapp.View.profile;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.example.chatapp.ModelClass.User;
import com.example.chatapp.R;
import com.example.chatapp.View.display.ViewImageActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;


    TextView et_u_profile_name;
    TextView et_u_profile_Statue;
    TextView et_u_profile_phone;
    FloatingActionButton floatingActionButton;
    private BottomSheetDialog bottomSheetDialog;
    private ProgressDialog progressDialog;
    CircleImageView circleImageProfile;
    ImageView edit_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        et_u_profile_name = findViewById(R.id.u_profile_name);
        et_u_profile_Statue = findViewById(R.id.u_profile_Statue);
        et_u_profile_phone = findViewById(R.id.u_profile_phone);
        floatingActionButton = findViewById(R.id.change_profilePhoto);
        circleImageProfile = findViewById(R.id.im_profile);
        edit_name = findViewById(R.id.edit_name);


        if (firebaseUser != null) {

            getInfo();

        }
        initAction();


    }

    private void initAction() {

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetPickPhoto();
            }
        });

        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetEditName();
            }
        });

        circleImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleImageProfile.invalidate();
                Drawable dr = circleImageProfile.getDrawable();
                User user = new User();


                String s = user.getU_imageUri();

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(s));
                } catch (IOException e) {
                    e.printStackTrace();
                }


                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(ProfileActivity.this,
                        circleImageProfile, "image");

                //  String bitmap = String.valueOf(dr.getCurrent());

                Intent intent = new Intent(ProfileActivity.this, ViewImageActivity.class);

                intent.putExtra("s", bitmap);

                startActivity(intent, compat.toBundle());
            }
        });


    }

    private void bottomSheetPickPhoto() {
        View view = getLayoutInflater().inflate(R.layout.bottom_profilphoto, null);


        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog = null;
            }
        });
        bottomSheetDialog.show();
    }

    private void bottomSheetEditName() {

        View view = getLayoutInflater().inflate(R.layout.bottom_edit_name, null);
        EditText et_EditName = view.findViewById(R.id.et_EditName);

        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_Name(et_EditName.getText().toString());
                bottomSheetDialog.dismiss();
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog = null;
            }
        });
        bottomSheetDialog.show();
    }

    private void getInfo() {

        firestore.collection("User").document(firebaseUser.getUid()).get().addOnSuccessListener(documentSnapshot -> {


            String U_name = documentSnapshot.getString("u_name");
            String U_Status = documentSnapshot.getString("u_status");
            String U_Phone = documentSnapshot.getString("u_phone");
            String U_Image = documentSnapshot.getString("u_imageUri");

            Uri Image = Uri.parse(U_Image);

            et_u_profile_name.setText(U_name);
            et_u_profile_Statue.setText(U_Status);
            et_u_profile_phone.setText(U_Phone);


            Picasso.get().load(Image).into(circleImageProfile);


        }).addOnFailureListener(e -> {
            Log.d("updating", "onFailure: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Failure  " + e.getMessage(), Toast.LENGTH_SHORT).show();

        });
    }

    public void openGallery(View view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, 25);
        ImagePicker.with(this)
                .galleryOnly()//User can only capture image using Camera
                .start(30);
        bottomSheetDialog.cancel();
    }

    public void openCamera(View view) {
        ImagePicker.with(this)
                .cameraOnly()    //User can only capture image using Camera
                .start(20);
        bottomSheetDialog.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        choosePhoto(requestCode, data);


    }

    private void choosePhoto(int requestCode, Intent data) {

        if (requestCode == 20) {

            camera(data);
        }
        if (requestCode == 30) {
            gallery(data);
        }


/*


Intent intent = new Intent(this, review_send_image.class);
            intent.putExtra("image", send);
            this.startActivity(intent);


                Random random = new Random();
                int randomname = random.nextInt() * 10000;

                StorageReference storageReference2 = FirebaseStorage.getInstance().getReference().child("chatImage/" + randomname);

                //  StorageReference storageReference = storage.getReference().child("chats");
                dialog.show();


                storageReference2.putFile(selected_image).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        dialog.dismiss();


                        storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String message = uri.toString();


                                if (message.isEmpty()) {
                                    Toast.makeText(ChatActivity.this, "pleas enter Valid Message", Toast.LENGTH_SHORT).show();
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

                                                        Toast.makeText(ChatActivity.this, "تمام ", Toast.LENGTH_LONG).show();

                                                    }
                                                });
                                            }
                                        });

                            }
                        });


                    }


                });


*/


    }

    private void gallery(Intent data) {

        if (data != null) {
            Uri selected_image = data.getData();
            // String send = String.valueOf(selected_image);
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                selected_image);
                circleImageProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
            uploadToFirebase(selected_image);
            circleImageProfile.setImageURI(selected_image);

        }
    }

    public void camera(Intent data) {

        if (data != null) {
            Uri selected_image = data.getData();
            // String send = String.valueOf(selected_image);
            uploadToFirebase(selected_image);
            circleImageProfile.setImageURI(selected_image);


        }


    }

    private void uploadToFirebase(Uri selected_image) {


        if (selected_image != null) {
            progressDialog.setMessage("uploading.....");
            progressDialog.show();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("ImagesProfile/" + System.currentTimeMillis() + ".jpg");


            UploadTask uploadTask = storageReference.putFile(selected_image);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        final String sDownload = String.valueOf(downloadUri);

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("u_imageUri", sDownload);
                        progressDialog.dismiss();

                        firestore.collection("User").document(firebaseUser.getUid()).update(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(ProfileActivity.this, "تمام ", Toast.LENGTH_LONG).show();
                                        getInfo();
                                    }
                                });

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });




            /*
            storageReference.putFile(selected_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) {
                        Uri DownloadUrl = uriTask.getResult();
                        final String sDownload = String.valueOf(DownloadUrl);

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("u_imageUri", sDownload);
                        progressDialog.dismiss();

                        firestore.collection("User").document(firebaseUser.getUid()).update(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(ProfileActivity.this, "تمام ", Toast.LENGTH_LONG).show();
                                        getInfo();
                                    }
                                });

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "لا مش تمام  ", Toast.LENGTH_LONG).show();

                }
            });

            */
        }
    }

    private String getFileExtension(Uri uri) {

        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void edit_Name(String name) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("u_name", name);
        firestore.collection("User").document(firebaseUser.getUid()).update(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ProfileActivity.this, "الاسم اتغير ", Toast.LENGTH_LONG).show();
                        getInfo();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "الاسم متغيرش فيه مشكلة ", Toast.LENGTH_LONG).show();
            }
        });

    }
}
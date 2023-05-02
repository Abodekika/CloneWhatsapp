package com.example.chatapp.Service;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseService {
    private Context context;

    public FirebaseService(Context context) {
        this.context = context;
    }

    public void uploadImageToFirebase(Uri selected_image, OnCallBack callBack) {

        if (selected_image != null) {
//            progressDialog.setMessage("uploading.....");
//            progressDialog.show();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("ImagesChats/" + System.currentTimeMillis() + ".jpg");


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

                        callBack.onUploadSuccess(downloadUri);

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callBack.onUploadFailed(e);
                }
            });


        }
    }

    public interface OnCallBack {
        void onUploadSuccess(Uri imageUri);
        void onUploadFailed(Exception e);

    }

}

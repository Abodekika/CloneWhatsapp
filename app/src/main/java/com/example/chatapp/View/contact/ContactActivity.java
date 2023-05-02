package com.example.chatapp.View.contact;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.ContactAdapter;
import com.example.chatapp.ModelClass.User;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    ContactAdapter adapter;
    private List<User> list = new ArrayList<>();
    RecyclerView contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        contactList = findViewById(R.id.contact_recView);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();


        if (firebaseUser != null) {
            getContactList();
            // Toast.makeText(getApplicationContext(), "تمام ", Toast.LENGTH_SHORT).show();
        }
      //  getChatList();

    }
/*
    private void getChatList() {

        User user1 = new User("1", "MuradAlmdar", "A.Abodekika", "hi", "", "https://pbs.twimg.com/profile_images/1298746422687342593/sz1szuw5_400x400.jpg", "12/5/2012", "", "");
        User user2 = new User("1", "Memitypash", "A.Abodekika", "hi", "https://i1.sndcdn.com/artworks-000027481286-yh1ozq-t500x500.jpg", "12/5/2012", "", "", "");
        User user3 = new User("1", "AbdelhayShoban", "A.Abodekika", "hi", "https://media.elcinema.com/uploads/_315x420_cf115b092f9c390e9e7e6e2946c6bd81ba062f4132d57516e2fb105c696eae69.jpg", "12/5/2012", "", "", "");

        list.add(user1);
        list.add(user2);
        list.add(user3);
    }
*/
    private void getContactList() {

        firestore.collection("User").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot dataSnapshot : queryDocumentSnapshots) {

                    String userID = dataSnapshot.getString("u_id");
                    String userName = dataSnapshot.getString("u_name");
                    String ImageUrl = dataSnapshot.getString("u_imageUri");
                    String desc = dataSnapshot.getString("u_status");

                    User user = new User();

                    user.setU_id(userID);
                    user.setU_name(userName);
                    user.setU_imageUri(ImageUrl);
                    user.setU_status(desc);

                    if (userID != null && !userID.equals(firebaseUser.getUid())) {
                        list.add(user);
                    }


                }
                contactList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new ContactAdapter(list, ContactActivity.this);
                contactList.setAdapter(adapter);

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
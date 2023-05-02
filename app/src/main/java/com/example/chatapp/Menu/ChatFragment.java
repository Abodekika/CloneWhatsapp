package com.example.chatapp.Menu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.ChatListAdapter;
import com.example.chatapp.ModelClass.ChatList;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    RecyclerView mainUserRecyclerView;
    ChatListAdapter adapter;
    ArrayList<ChatList> U_chat_list_array;
    ProgressBar progressBar;
    Handler handler = new Handler();


    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    DatabaseReference reference;

    private ArrayList<String> allUserID;

    public ChatFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        U_chat_list_array = new ArrayList<>();
        allUserID = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mainUserRecyclerView = view.findViewById(R.id.mainUserRecyclerView);
        progressBar = view.findViewById(R.id.progress_cir);


        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatListAdapter(getContext(), U_chat_list_array);
        mainUserRecyclerView.setAdapter(adapter);
        // getChatList();

        readChatList();

        return view;
    }

    /*
        private void getChatList() {

            ChatList user1 = new ChatList("1", "MuradAlmdar", "A.Abodekika", "12/5/2012", "https://pbs.twimg.com/profile_images/1298746422687342593/sz1szuw5_400x400.jpg");
            ChatList user2 = new ChatList("2", "Memitypash", "A.Abodekika", "12/5/2012", "https://i1.sndcdn.com/artworks-000027481286-yh1ozq-t500x500.jpg");
            ChatList user3 = new ChatList("3", "AbdelhayShoban", "A.Abodekika", "12/5/2012", "https://media.elcinema.com/uploads/_315x420_cf115b092f9c390e9e7e6e2946c6bd81ba062f4132d57516e2fb105c696eae69.jpg");

            U_chat_list_array.add(user1);
            U_chat_list_array.add(user2);
            U_chat_list_array.add(user3);
        }
    */

    private void readChatList() {
        progressBar.setVisibility(View.VISIBLE);

        U_chat_list_array.clear();
          allUserID.clear();
/*
        reference.child("chatList").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String userID = Objects.requireNonNull(dataSnapshot.child("chatid").getValue()).toString();
                    progressBar.setVisibility(View.GONE);
                    Log.d("ChatsFragment", "onFailure : error" + dataSnapshot);
                    allUserID.add(userID);


                }
                getUserdata();
                Log.d("ChatsFragment", "onFailure : error" + allUserID.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
   */
    }

    private void getUserdata() {

        handler.post(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                //  progressBar.setVisibility(View.GONE);

                for (String userId : allUserID) {
                    firestore.collection("User").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d("ChatsFragment", "onFailure : error" + documentSnapshot);
                            ChatList U_chatList = new ChatList(
                                    documentSnapshot.getString("u_id"),
                                    documentSnapshot.getString("u_name"),
                                    "this is description",
                                    "",
                                    documentSnapshot.getString("u_imageUri")
                            );

                            U_chat_list_array.add(U_chatList);
                            if (adapter != null) {
                                adapter.notifyItemInserted(0);
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ChatsFragment", "onFailure : error" + e.getMessage());
                        }
                    });
                }


            }
        });

    }


}
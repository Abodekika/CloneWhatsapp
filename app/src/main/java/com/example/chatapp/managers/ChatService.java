package com.example.chatapp.managers;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.chatapp.Interfaces.OnReadChatCallBack;
import com.example.chatapp.ModelClass.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatService {
    private List<Message> list;
    private Context context;

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final String ReceiverUID;

    public ChatService(Context context, String ReceiverUID) {
        this.context = context;
        this.ReceiverUID = ReceiverUID;
    }


    public void readChatData(OnReadChatCallBack callBack) {

        reference.child("Chats").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);

                    if (message != null && message.getSenderId().equals(firebaseUser.getUid()) && message.getReceiver().equals(ReceiverUID)
                            || message.getReceiver().equals(firebaseUser.getUid()) && message.getSenderId().equals(ReceiverUID)) {
                        list.add(message);
                    }

                }
                callBack.onReadChatSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onReadChatFailure();
            }
        });

    }

    public void sendTextMessage(String text) {


        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh-mm-a");
        String currentTime = df.format(currentDateTime.getTime());

        Message messages = new Message(
                today + "," + currentTime,
                text,
                firebaseUser.getUid(),
                ReceiverUID,
                "",
                "TEXT"
        );

        reference.child("Chats").push().setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("chatList").child(firebaseUser.getUid()).child(ReceiverUID);
        chatRef1.child("chatid").setValue(ReceiverUID);

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("chatList").child(ReceiverUID).child(firebaseUser.getUid());
        chatRef2.child("chatid").setValue(firebaseUser.getUid());
    }

    public void sendImageMessage(String image) {

        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh-mm-a");
        String currentTime = df.format(currentDateTime.getTime());

        Message messages = new Message(
                today + "," + currentTime,
                "",
                firebaseUser.getUid(),
                ReceiverUID,
                image,
                "IMAGE"
        );

        reference.child("Chats").push().setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("chatList").child(firebaseUser.getUid()).child(ReceiverUID);
        chatRef1.child("chatid").setValue(ReceiverUID);

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("chatList").child(ReceiverUID).child(firebaseUser.getUid());
        chatRef2.child("chatid").setValue(firebaseUser.getUid());


    }
}

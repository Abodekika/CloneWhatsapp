package com.example.chatapp.View;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Adapter.MessageAdapter;
import com.example.chatapp.Interfaces.OnReadChatCallBack;
import com.example.chatapp.ModelClass.Message;
import com.example.chatapp.R;
import com.example.chatapp.Service.FirebaseService;
import com.example.chatapp.View.dialog.DialogReviewSendImage;
import com.example.chatapp.View.profile.UserProfileActivity;
import com.example.chatapp.managers.ChatService;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String ReceiverImage, ReceiverUID, ReceiverName, SenderId;
    CircleImageView chat_profileImage;
    TextView chat_receiver_name;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ImageButton chat_back_btn;
    public static String sImage;
    public static String rImage;
    FloatingActionButton sendBtn;
    CardView card_att;
    private boolean isActionShow = false;
    EditText editMessage;
    RecyclerView message_rec;
    String senderRoom, receiverRoom;
    List<Message> messageArrayList = new ArrayList<>();
    List<Message> list;
    ImageView attachment;
    ProgressDialog dialog;
    MessageAdapter adapter;
    private ChatService chatService;
    Uri selected_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        chat_receiver_name = findViewById(R.id.chat_receiver_name);
        chat_profileImage = findViewById(R.id.chat_profile_image);
        sendBtn = findViewById(R.id.sendBtn);
        card_att = findViewById(R.id.card_att);
        attachment = findViewById(R.id.attachment_btn);
        chat_back_btn = findViewById(R.id.chat_back_btn);
        editMessage = findViewById(R.id.editMessage);
        message_rec = findViewById(R.id.messageDate);


        // SenderId = auth.getUid();
//
//        senderRoom = SenderId + ReceiverUID;
//        receiverRoom = ReceiverUID + SenderId;


        initBtnClick();

        initialize();


    }

    private void initialize() {

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        ReceiverImage = getIntent().getStringExtra("u_imageUri");
        ReceiverName = getIntent().getStringExtra("u_name");
        ReceiverUID = getIntent().getStringExtra("u_id");

        chatService = new ChatService(this, ReceiverUID);

        adapter = new MessageAdapter(this, messageArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        message_rec.setLayoutManager(linearLayoutManager);
        message_rec.setAdapter(adapter);


        dialog = new ProgressDialog(this);

        dialog.setMessage("بيحمل ي نجم ");
        dialog.setCancelable(false);

        initBtnClick();

        if (ReceiverImage.equals("")) {
            chat_profileImage.setImageResource(R.drawable.placeholder);
        } else {

            Picasso.get().load(ReceiverImage).into(chat_profileImage);
        }

        chat_receiver_name.setText(ReceiverName);


        editMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(editMessage.getText().toString())) {
                    sendBtn.setImageDrawable(getDrawable(R.drawable.ic_voice));
                } else {
                    sendBtn.setImageDrawable(getDrawable(R.drawable.send_btn));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        readChat();
    }

    public void attachment(View view) {

        if (isActionShow) {
            card_att.setVisibility(View.GONE);
            isActionShow = false;

        } else {
            card_att.setVisibility(View.VISIBLE);
            isActionShow = true;
        }


        /*
        LinearLayout linearLayout;

        linearLayout = findViewById(R.id.ll3);

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.attachment_layout, null);
        int width = (LinearLayout.LayoutParams.WRAP_CONTENT);
        int height = (LinearLayout.LayoutParams.WRAP_CONTENT);

        PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        popupWindow.showAtLocation(linearLayout, Gravity.NO_GRAVITY, 50, 2100);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

*/
    }

    public void openGallery(View view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, 25);
        ImagePicker.with(this)
                .galleryOnly()//User can only capture image using Camera
                .start(25);
    }

    public void openCamera(View view) {
        ImagePicker.with(this)
                .cameraOnly()    //User can only capture image using Camera
                .start(25);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        choosePhoto(requestCode, data);


    }

    private void choosePhoto(int requestCode, Intent data) {


        if (requestCode == 25) {
            if (data != null) {
                selected_image = data.getData();
                String send = String.valueOf(selected_image);

                reviewImage(selected_image);


                /*
                Intent intent = new Intent(this, review_send_image.class);
                intent.putExtra("image", send);
                this.startActivity(intent);
*/

/*
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

        }
    }

    private void reviewImage(Uri imageUri) {
        new DialogReviewSendImage(ChatActivity.this, imageUri).show(new DialogReviewSendImage.OnCallBack() {
            @Override
            public void onButtonSendClick() {
                if (imageUri != null) {
                    ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);
                    progressDialog.setMessage("Sending......");
                    new FirebaseService(ChatActivity.this).uploadImageToFirebase(imageUri, new FirebaseService.OnCallBack() {
                        @Override
                        public void onUploadSuccess(Uri imageUri) {
                            progressDialog.show();
                            chatService.sendImageMessage(String.valueOf(imageUri));
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onUploadFailed(Exception e) {

                        }
                    });
                }
            }
        });
    }


    private String getFileExtension(Uri uri) {

        ContentResolver contentResolver = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void initBtnClick() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(editMessage.getText().toString())) {
                    chatService.sendTextMessage(editMessage.getText().toString());
                    editMessage.setText("");
                }
            }
        });

        chat_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        chat_profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, UserProfileActivity.class).
                        putExtra("receiver_id", ReceiverUID)
                        .putExtra("receiver_image_profile", ReceiverImage).putExtra("receiver_name", ReceiverName)
                );


            }
        });


    }

/*
    private void sendTextMessage(String text) {

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
                ""
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
*/

    private void readChat() {

        chatService.readChatData(new OnReadChatCallBack() {
            @Override
            public void onReadChatSuccess(List<Message> messageList) {

                adapter.setList(messageList);
            }

            @Override
            public void onReadChatFailure() {

            }
        });
    }

     /*
    private void sendTextMessage(String text) {

        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh-mm-a");
        String currentTime = df.format(currentDateTime.getTime());

        String message = editMessage.getText().toString();
        if (message.isEmpty()) {
            Toast.makeText(ChatActivity.this, "pleas enter Valid Message", Toast.LENGTH_SHORT).show();
            return;
        }



        Message messages = new Message(
                today + "," + currentTime, text, firebaseUser.getUid(), ReceiverUID
        );
        database = FirebaseDatabase.getInstance();

        database.getReference().child("chat")
                .child(senderRoom)
                .child("messages")
                .push()
                .setValue(messages)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chat")
                                .child(receiverRoom)
                                .child("messages")
                                .push()
                                .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });


    }
    */
     /*
    private void readChat() {

        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        DatabaseReference chatReference = database.getReference().child("chat").child(senderRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messageArrayList.add(message);

                }

                messageAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             //   sImage = snapshot.child("imageUri").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
*/
}





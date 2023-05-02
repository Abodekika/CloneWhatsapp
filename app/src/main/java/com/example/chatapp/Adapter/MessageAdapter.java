package com.example.chatapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.ModelClass.Message;
import com.example.chatapp.R;
import com.example.chatapp.View.FullScreenImageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    List<Message> message_list;
    public static final int Item_Send = 1;
    public static final int Item_receive = 2;


    public MessageAdapter(Context context, List<Message> message_list) {
        this.context = context;
        this.message_list = message_list;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Message> list) {
        this.message_list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == Item_Send) {
            View view =
                    LayoutInflater.from(context).inflate(R.layout.sender_layout_item, parent, false);
            return new SenderViewHolder(view, context);

        } else {
            View view =
                    LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return new RecieverViewHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = message_list.get(position);


        if (holder.getClass() == SenderViewHolder.class) {

            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;


            if (message.getType().equals("IMAGE")) {
                senderViewHolder.image.setVisibility(View.VISIBLE);
                senderViewHolder.textMessage.setVisibility(View.GONE);
                Picasso.get().load(message.getImageUri()).into(senderViewHolder.image);


                senderViewHolder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FullScreenImageActivity.class);
                        intent.putExtra("image", message.getImageUri());
                        context.startActivity(intent);
                    }
                });


            } else {

                senderViewHolder.textMessage.setText(message.getMessage());
                senderViewHolder.image.setVisibility(View.GONE);


            }
        } else {
            RecieverViewHolder recieverViewHolder = (RecieverViewHolder) holder;
            if (message.getMessage().equals("IMAGE")) {
                recieverViewHolder.image.setVisibility(View.VISIBLE);
                recieverViewHolder.textMessage.setVisibility(View.GONE);
                Picasso.get().load(message.getImageUri()).into(recieverViewHolder.image);


                recieverViewHolder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FullScreenImageActivity.class);
                        intent.putExtra("image", message.getImageUri());
                        context.startActivity(intent);
                    }
                });
            } else {

                recieverViewHolder.textMessage.setText(message.getMessage());
                recieverViewHolder.image.setVisibility(View.GONE);
                Picasso.get().load(message.getImageUri()).into(recieverViewHolder.image);


            }

        }


    }

    @Override
    public int getItemCount() {
        return message_list.size();
    }

    @Override
    public int getItemViewType(int position) {

        Message message = message_list.get(position);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (message.getSenderId().equals(firebaseUser.getUid())) {
            return Item_Send;

        } else {
            return Item_receive;
        }
    }

    public static class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView textMessage;
        ImageView image;


        public SenderViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.text_message);
            image = itemView.findViewById(R.id.image_attachment);


        }

        void bind(Message message) {

        }

    }

    static class RecieverViewHolder extends RecyclerView.ViewHolder {

        TextView textMessage;
        ImageView image;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.text_message);
            image = itemView.findViewById(R.id.image_attachment);
        }
    }

    public static void ShowPhoto(Context context, @NonNull Message message) {
        Intent intent = new Intent(context, FullScreenImageActivity.class);
        intent.putExtra("image", message.getImageUri());
        context.startActivity(intent);
    }

}

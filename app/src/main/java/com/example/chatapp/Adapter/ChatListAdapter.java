package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.ModelClass.ChatList;
import com.example.chatapp.R;
import com.example.chatapp.View.ChatActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Viewholder> {
    Context context;
    List<ChatList> userArrayList;

    public ChatListAdapter(Context context, ArrayList<ChatList> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_list, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ChatList U_chat_list = userArrayList.get(position);

        holder.user_name.setText(U_chat_list.getUserName());
        holder.user_status.setText(U_chat_list.getDescription());
        holder.tv_date.setText(U_chat_list.getDate());

        if (U_chat_list.getUriProfile().equals("")) {
            holder.user_profile.setImageResource(R.drawable.placeholder);
        } else {
            Picasso.get().load(U_chat_list.getUriProfile()).into(holder.user_profile);

        }
      //  Picasso.get().load(U_chat_list.getUriProfile()).into(holder.user_profile);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("u_name", U_chat_list.getUserName());
            intent.putExtra("u_imageUri", U_chat_list.getUriProfile());
            intent.putExtra("u_id", U_chat_list.getUserID());
            context.startActivity(intent);


        });
    }


    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    static class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView user_profile;
        TextView user_name;
        TextView tv_date;
        TextView user_status;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            user_profile = itemView.findViewById(R.id.user_image);
            user_name = itemView.findViewById(R.id.user_name);
            user_status = itemView.findViewById(R.id.user_status);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }


}

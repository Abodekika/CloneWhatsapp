package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.ModelClass.User;
import com.example.chatapp.R;
import com.example.chatapp.View.ChatActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<User> list;
    private Context context;

    public ContactAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_contact_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = list.get(position);


        holder.user_name.setText(user.getU_name());
        holder.user_status.setText(user.getU_status());
        Uri uri = Uri.parse(user.getU_imageUri());

        if (user.getU_imageUri().equals("")) {
            holder.user_profile.setImageResource(R.drawable.placeholder);
        } else {
            Picasso.get().load(uri).into(holder.user_profile);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("u_name", user.getU_name());
                intent.putExtra("u_imageUri", user.getU_imageUri());
                intent.putExtra("u_id", user.getU_id());
                context.startActivity(intent);
                //   context.startActivity(new Intent(context, ChatActivity.class).putExtra(""));
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends MessageAdapter.RecieverViewHolder {

        CircleImageView user_profile;
        TextView user_name;
        TextView user_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user_profile = itemView.findViewById(R.id.user_contact_image);
            user_name = itemView.findViewById(R.id.user_name);
            user_status = itemView.findViewById(R.id.user_status);
        }
    }
}

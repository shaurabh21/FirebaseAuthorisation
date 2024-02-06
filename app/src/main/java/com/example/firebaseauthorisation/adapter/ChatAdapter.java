package com.example.firebaseauthorisation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseauthorisation.Model.ChatModel;
import com.example.firebaseauthorisation.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    Context context;
    ArrayList<ChatModel> chatList = new ArrayList<>();
    String userId;

    public ChatAdapter(Context context, ArrayList<ChatModel> chatList, String userId) {
        this.context = context;
        this.chatList = chatList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_chat_interface, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {

        if (Objects.equals(userId, chatList.get(position).getSender_id())) {
            holder.senderLayout.setVisibility(View.VISIBLE);
            holder.receiverLayout.setVisibility(View.GONE);
            holder.sender.setText(chatList.get(position).getMsg());
            holder.time1.setText(convert(chatList.get(position).getTimeStamp()));
        } else {
            holder.senderLayout.setVisibility(View.GONE);
            holder.receiverLayout.setVisibility(View.VISIBLE);
            holder.receiver.setText(chatList.get(position).getMsg());
            holder.time2.setText(convert(chatList.get(position).getTimeStamp()));

        }


    }

    private String convert(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(Long.parseLong(timestamp));
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView receiver, sender, time1, time2;
        CircleImageView image;
        LinearLayout receiverLayout, senderLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            receiver = itemView.findViewById(R.id.receiver);
            sender = itemView.findViewById(R.id.sender);
            image = itemView.findViewById(R.id.receiver_image);
            time1 = itemView.findViewById(R.id.SenderTime);
            time2 = itemView.findViewById(R.id.ReceiverTime);
            receiverLayout = itemView.findViewById(R.id.receiverLayout);
            senderLayout = itemView.findViewById(R.id.senderLayout);
        }
    }
}

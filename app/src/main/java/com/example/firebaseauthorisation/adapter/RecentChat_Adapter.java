package com.example.firebaseauthorisation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseauthorisation.Model.NewChatModel;
import com.example.firebaseauthorisation.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentChat_Adapter extends RecyclerView.Adapter<RecentChat_Adapter.ViewHolder> {
    Context context;
    ArrayList<NewChatModel> arrlist = new ArrayList<>();
    String userId;

    public RecentChat_Adapter(Context context, ArrayList<NewChatModel> arrlist, String userId) {
        this.context = context;
        this.arrlist = arrlist;
        this.userId = userId;
    }

    @NonNull
    @Override
    public RecentChat_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.single_view_chat,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentChat_Adapter.ViewHolder holder, int position) {
        holder.name.setText(arrlist.get(position).getReceiver_name());
        holder.lastChat.setText(arrlist.get(position).getMsg());
        Picasso.get().load(arrlist.get(position).getReceiverImage()).into(holder.image);
        holder.time.setText(convert(arrlist.get(position).getTimeStamp()));




    }
    private String convert(String timeStamp){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
       return sdf.format(Long.parseLong(timeStamp));
    }

    @Override
    public int getItemCount() {
        return arrlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,lastChat,time;
        CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameChat);
            lastChat = itemView.findViewById(R.id.last_chat);
            image = itemView.findViewById(R.id.image);
            time = itemView.findViewById(R.id.time);
        }
    }
}

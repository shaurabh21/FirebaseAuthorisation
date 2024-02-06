package com.example.firebaseauthorisation.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseauthorisation.Model.UserModel;
import com.example.firebaseauthorisation.R;
import com.example.firebaseauthorisation.ui.Chat;
import com.example.firebaseauthorisation.ui.ChatFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    ArrayList<UserModel> arrList;

    public UserAdapter(Context context, ArrayList<UserModel> arrList) {
        this.context = context;
        this.arrList = arrList;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_view_user,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(arrList.get(position).getName());
        Picasso.get().load(arrList.get(position).getImage()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Chat.class);
                i.putExtra("name",arrList.get(position).getName());
                i.putExtra("image",arrList.get(position).getImage());
                i.putExtra("receiver_id",arrList.get(position).getId());
                context.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CircleImageView image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);

        }
    }
}

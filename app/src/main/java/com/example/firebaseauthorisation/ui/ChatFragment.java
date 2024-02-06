package com.example.firebaseauthorisation.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseauthorisation.Model.NewChatModel;
import com.example.firebaseauthorisation.R;
import com.example.firebaseauthorisation.adapter.RecentChat_Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference recentChatRef;
    FirebaseAuth mAuth;

    CircleImageView image;
    RecentChat_Adapter adapter;
    TextView name, lastChat;

    String userId;

    ArrayList<NewChatModel> arrayList = new ArrayList<NewChatModel>();
    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = v.findViewById(R.id.rec_view_chat);
        name = v.findViewById(R.id.name);
        lastChat = v.findViewById(R.id.last_chat);
        context = inflater.getContext();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        adapter = new RecentChat_Adapter(context, arrayList, userId);
        recyclerView.setAdapter(adapter);
        recentChatRef = FirebaseDatabase.getInstance().getReference().child("recentChat");


        recentChatRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    arrayList.add(new NewChatModel(snap.child("senderName").getValue().toString(),
                            snap.child("senderImage").getValue().toString(),
                            snap.child("receiverImage").getValue().toString(),
                            snap.child("sender_id").getValue().toString(),
                            snap.child("receiver_id").getValue().toString(),
                            snap.child("msg").getValue().toString(),
                            snap.child("timeStamp").getValue().toString(),
                            snap.child("receiver_name").getValue().toString()));

                    Log.d(getClass().getSimpleName(), "onDataChange: " + snap.child("receiver_name").toString());
                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //     Log.d(getClass().getSimpleName(), "onCreateView: " +convert(String.valueOf(System.currentTimeMillis())));
        return v;

    }

    private String convert(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(Long.parseLong(timestamp));
    }
}
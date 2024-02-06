package com.example.firebaseauthorisation.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseauthorisation.Model.ChatModel;
import com.example.firebaseauthorisation.Model.NewChatModel;
import com.example.firebaseauthorisation.R;
import com.example.firebaseauthorisation.adapter.ChatAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    MaterialToolbar toolbar;
    TextView name;
    FirebaseAuth mAuth;
    DatabaseReference chatRef, recentChatRef, ref;

    String receiver_id;
    String sender_id;
    String chatKey;
    String senderName;
    String senderImage;
    String userId;
    ChatAdapter adapter;
    RecyclerView recyclerView;
    CircleImageView image;
    FloatingActionButton fab;
    ArrayList<String> List = new ArrayList<>();
    ArrayList<ChatModel> chatList = new ArrayList<>();

    TextInputEditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = findViewById(R.id.toolbar);
        name = findViewById(R.id.reciever_name);
        fab = findViewById(R.id.send);
        image = findViewById(R.id.receiver_image);
        message = findViewById(R.id.message);
        recyclerView = findViewById(R.id.rec_view);


        mAuth = FirebaseAuth.getInstance();
        sender_id = mAuth.getCurrentUser().getUid();

        userId = mAuth.getCurrentUser().getUid();

        adapter = new ChatAdapter(this, chatList, sender_id);
        recyclerView.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance().getReference().child("users");
        chatRef = FirebaseDatabase.getInstance().getReference().child("chats");
        recentChatRef = FirebaseDatabase.getInstance().getReference().child("recentChat");


        name.setText(getIntent().getStringExtra("name"));
        Picasso.get().load(getIntent().getStringExtra("image")).into(image);
        receiver_id = getIntent().getStringExtra("receiver_id");


        List.add(sender_id);
        List.add(receiver_id);
        Collections.sort(List);

        chatKey = List.get(0) + "_chat_" + List.get(1);

        Log.d(getClass().getSimpleName(), "onCreate: " + chatKey);

        chatRef.child(chatKey).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                chatList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    chatList.add(new ChatModel(snap.child("receiver_id").getValue().toString(),
                            snap.child("sender_id").getValue().toString(),
                            snap.child("msg").getValue().toString(),
                            snap.child("timeStamp").getValue().toString(),
                            snap.child("receiver_name").getValue().toString()));
                }


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (message.getText().toString().isEmpty()) {
                    Toast.makeText(Chat.this, "At least Write something", Toast.LENGTH_SHORT).show();
                } else {
                    sendMsg();
                }

            }
        });

        ref = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(getClass().getSimpleName(), "onDataChange: " + snapshot);
                senderName = snapshot.child("name").getValue().toString();
                senderImage = snapshot.child("image").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void sendMsg() {
        Log.d(getClass().getSimpleName(), "sendMsg: " + chatRef.push().getKey());
        String pushKey = chatRef.push().getKey();

        ChatModel chatModel = new ChatModel(
                receiver_id,
                sender_id,
                message.getText().toString(),
                String.valueOf(System.currentTimeMillis()),
                getIntent().getStringExtra("name")
        );

        NewChatModel chat = new NewChatModel(
                senderName, senderImage,
                getIntent().getStringExtra("image"), sender_id,
                receiver_id, message.getText().toString(),
                String.valueOf(System.currentTimeMillis()),
                getIntent().getStringExtra("name"));


        chatRef.child(chatKey).child(pushKey).setValue(chatModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                message.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });


        recentChatRef.child(sender_id).child(receiver_id).setValue(chat);
        recentChatRef.child(receiver_id).child(sender_id).setValue(chat);
    }

}
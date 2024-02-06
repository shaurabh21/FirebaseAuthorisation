package com.example.firebaseauthorisation.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseauthorisation.Model.UserModel;
import com.example.firebaseauthorisation.R;
import com.example.firebaseauthorisation.adapter.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserFragment extends Fragment {

ProgressBar progressBar;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    ArrayList<UserModel> list = new ArrayList<>();
    UserAdapter adapter;


    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user, container, false);



        mAuth = FirebaseAuth.getInstance();
        context = inflater.getContext();
        adapter = new UserAdapter(context, list);
        progressBar = v.findViewById(R.id.progress);
        progressBar.setVisibility(v.getVisibility());

        RecyclerView userRec = v.findViewById(R.id.rec_view_user);
        userRec.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance().getReference().child("users");


        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(getClass().getSimpleName(), "onDataChange: " + snapshot);
                list.clear();

                Log.d(getClass().getSimpleName(), "onDataChange: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                for (DataSnapshot snap : snapshot.getChildren()) {

                    if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(snap.getKey())) {
                        list.add(new UserModel(snap.child("name").getValue().toString(), snap.child("email").getValue().toString(),
                                snap.getKey().toString(), snap.child("image").getValue().toString()));

                        progressBar.setVisibility(View.GONE);
                    }


                    Log.d(getClass().getSimpleName(), "onDataChange: " + snap.child("email").getValue().toString());


                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }
}
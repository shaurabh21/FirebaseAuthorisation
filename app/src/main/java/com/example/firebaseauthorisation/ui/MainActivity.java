package com.example.firebaseauthorisation.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.firebaseauthorisation.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
   ActivityMainBinding bind;
   FirebaseAuth mAuth;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       bind = ActivityMainBinding.inflate(getLayoutInflater());
       setContentView(bind.getRoot());

       mAuth = FirebaseAuth.getInstance();


        bind.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();


            }
        });


        bind.account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });



    }
    public void singInAccount(){

        String email = bind.email.getText().toString().trim();
        String password = bind.password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){


                            Log.d(getClass().getSimpleName(), "onComplete: ");
                            Toast.makeText(MainActivity.this, "signIn successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,Home.class));
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void validation(){
        if(bind.email.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
            bind.email.requestFocus();
        }else if(bind.password.getText().toString().isEmpty()){
            bind.password.requestFocus();
            Toast.makeText(MainActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
        }else {
            singInAccount();


        }
    }

}
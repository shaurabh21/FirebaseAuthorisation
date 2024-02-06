package com.example.firebaseauthorisation.ui;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.firebaseauthorisation.R;
import com.example.firebaseauthorisation.utils.Utils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    CircleImageView image1, select;
    MaterialButton button;
    MaterialToolbar toolbar;

    TextInputEditText name, phone, email;
    DatabaseReference ref;
    FirebaseAuth mAuth;

    Uri imageuri;
    Utils util;
    Context context;
    private final ActivityResultLauncher<Intent> startCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        image1.setImageURI(imageuri);
                    } else {
                        Log.d(getClass().getSimpleName(), "onActivityResult: not picked");
                    }

                }
            });
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getData() != null && result.getResultCode() == RESULT_OK) {
                        Uri uri = result.getData().getData();
                        Log.d(getClass().getSimpleName(), "onActivityResult: " + imageuri);
                        image1.setImageURI(uri);
                        imageuri = uri;
                    } else {
                        Log.d(getClass().getSimpleName(), "onActivityResult: not picked");
                    }
                }
            });


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        select = v.findViewById(R.id.photo);
        image1 = v.findViewById(R.id.image);
        name = v.findViewById(R.id.name);
        phone = v.findViewById(R.id.phone);
        button = v.findViewById(R.id.save);
        email = v.findViewById(R.id.email);


        toolbar = v.findViewById(R.id.toolbar);

        util = new Utils();
        mAuth = FirebaseAuth.getInstance();

        ref = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(getClass().getSimpleName(), "onDataChange: " + snapshot);
                name.setText(snapshot.child("name").getValue().toString());
                Picasso.get().load(snapshot.child("image").getValue().toString()).into(image1);
                phone.setText(snapshot.child("phone").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        context = inflater.getContext();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        return v;


    }

    public void checkValidation() {
        if (imageuri == null) {

            util.showToast(context, "Please Pick Image");

        } else if (name.getText().toString().isEmpty()) {
            util.showToast(context, "please enter name");
        } else {
            if (phone.getText().toString().isEmpty()) {
                util.showToast(context, "enter phone no.");
            }
        }
    }

    public void openDialog() {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Select From")
                .setItems(new String[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (checkCamPermission()) {
                                    openCamera();
                                } else {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                                }
                                break;
                            case 1:
                                if (checkPermission()) {
                                    openGallery();

                                } else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 0);
                                    } else {
                                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                                    }
                                }

                        }

                    }
                }).show();
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkCamPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 0);
            } else {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                }
            }
        } else if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }

        }
    }

    public void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImage.launch(i);

    }

    public void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageuri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent i = new Intent(ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startCamera.launch(i);

    }

    public void dialog() {
        new MaterialAlertDialogBuilder(context)
                .setTitle("Logout")
                .setMessage("Are you sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        startActivity(new Intent(context, MainActivity.class));
                        requireActivity().finish();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();

    }

}
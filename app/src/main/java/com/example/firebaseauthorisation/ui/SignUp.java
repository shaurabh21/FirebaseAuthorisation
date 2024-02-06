package com.example.firebaseauthorisation.ui;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.firebaseauthorisation.databinding.ActivitySignUpBinding;
import com.example.firebaseauthorisation.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding bind;
    Utils utils;
    String userId;
    String imageUrl;
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference userRef;

    Uri imageUri = null;

    private final ActivityResultLauncher<Intent> startCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        bind.image.setImageURI(imageUri);
                    } else {
                        Log.d(getClass().getSimpleName(), "onActivityResult: Not picked");
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getData() != null && result.getResultCode() == RESULT_OK) {
                        Uri uri = result.getData().getData();
                        Log.d(getClass().getSimpleName(), "onActivityResult: " + imageUri);
                        bind.image.setImageURI(uri);
                        imageUri = uri;

                    } else {
                        Log.d(getClass().getSimpleName(), "onActivityResult: not pick");
                    }

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        utils = new Utils();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");


        bind.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        bind.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()) {
                    createAccount();
                }


            }
        });
    }

    private Boolean checkValidation() {
        if (imageUri == null) {
            utils.showToast(this, "Please pick image!");
            return false;
        } else if (Objects.requireNonNull(bind.name.getText()).toString().isEmpty()) {
            utils.showToast(this, "Enter name");
            bind.name.requestFocus();
            return false;
        } else if (bind.email.getText().toString().isEmpty()) {
            utils.showToast(this, "Email can't be empty");
            bind.email.requestFocus();
            return false;
        } else if (bind.phone.getText().toString().isEmpty()) {
            utils.showToast(this, "Enter phone no.");
            bind.phone.requestFocus();
            return false;
        } else if (bind.phone.length() < 10) {
            utils.showToast(this, "Enter phone no.");
            return false;
        } else if (bind.password.getText().toString().isEmpty()) {
            utils.showToast(this, "Enter Password");
            bind.password.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    private void openDialog() {
        new MaterialAlertDialogBuilder(this)
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
                                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                                    }
                                }


                        }
                    }
                }).show();
    }

    public void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageUri = SignUp.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent i = new Intent(ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startCamera.launch(i);
    }

    public void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImage.launch(i);

    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
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

    private boolean checkCamPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void upload() {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("uploading");
        dialog.show();


        StorageReference ref = storage.getReference().child("image").child(String.valueOf(System.currentTimeMillis()));
        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(getClass().getSimpleName(), "onSuccess: Url" + uri);
                                imageUrl = uri.toString();

                                storeUserValue();
                            }
                        });


                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (float) (100 * snapshot.getBytesTransferred()) /
                                snapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded:" + percent + "%");

                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Log.d(getClass().getSimpleName(), "onFailure: " + e);
                    }
                });
    }

    public void createAccount() {

        String email = bind.email.getText().toString().trim();
        String password = bind.password.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            userId = task.getResult().getUser().getUid();
                            upload();
                            Log.d(getClass().getSimpleName(), "onComplete: " + userId);


                        } else {
                            Toast.makeText(SignUp.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(getClass().getSimpleName(), "onFailure: " + e);

                    }
                });


    }

    public void storeUserValue() {
        if (userId != null && imageUrl != null) {
            HashMap<String, Object> userData = new HashMap<>();
            userData.put("image", imageUrl);
            userData.put("name", bind.name.getText().toString().trim());
            userData.put("email", bind.email.getText().toString().trim());
            userData.put("phone", bind.phone.getText().toString().trim());
            userData.put("password", bind.password.getText().toString().trim());
            userRef.child(userId).setValue(userData).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(SignUp.this, "Data successfully stored", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUp.this, "Failed to store", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }



}
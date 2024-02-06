package com.example.firebaseauthorisation.Model;

import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserModel {
    String name,email;
    String id;
    String image;


    public UserModel(String name, String email, String id, String image) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

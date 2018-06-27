package com.example.user.getthroughapp;

import android.app.Application;

import com.firebase.client.Firebase;

public class FirebaseDatabase extends Application {

    public void onCreate() {

        super.onCreate();

        Firebase.setAndroidContext(this);
    }


}

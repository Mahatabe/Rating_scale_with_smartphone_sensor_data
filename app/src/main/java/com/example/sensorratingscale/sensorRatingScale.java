package com.example.sensorratingscale;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class sensorRatingScale extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

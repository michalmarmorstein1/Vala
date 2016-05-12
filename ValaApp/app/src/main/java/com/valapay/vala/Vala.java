package com.valapay.vala;

import android.app.Application;
import android.util.Log;

import com.valapay.vala.model.User;

public class Vala extends Application{

    private User user;
    private static Vala instance;

    public static Vala get(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("VALA", "Vala:onCreate()");
        instance = this;
        user = new User(this);
    }

    public static User getUser() {
        return get().user;
    }
}

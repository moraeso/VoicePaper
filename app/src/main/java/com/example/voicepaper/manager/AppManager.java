package com.example.voicepaper.manager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.voicepaper.data.User;

public class AppManager {

    private static AppManager instance;

    private Context context;
    private Resources resources;

    private User user;

    public static AppManager getInstance() {
        if (instance == null) return instance = new AppManager();
        return instance;
    }

    public Context getContext() { return context;}
    public void setContext(Context context) { this.context = context; }

    public Resources getResources() { return resources; }
    public void setResources(Resources resources) { this.resources = resources; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Bitmap getBitmap(int r) {
        return BitmapFactory.decodeResource(resources, r);
    }

}

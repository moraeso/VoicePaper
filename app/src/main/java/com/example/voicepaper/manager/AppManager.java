package com.example.voicepaper.manager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.voicepaper.data.Room;
import com.example.voicepaper.data.User;
import com.example.voicepaper.util.ConfirmDialog;

import java.util.ArrayList;

public class AppManager {

    private static AppManager instance;

    private AppManager() {
        user = new User();
        roomList = new ArrayList<>();
    }

    private Context context;
    private Resources resources;

    private User user;
    private ArrayList<Room> roomList;

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

    public ArrayList<Room> getRoomList() { return roomList; }
    public void setRoomList(ArrayList<Room> roomList) { this.roomList = roomList; }

    public Bitmap getBitmap(int r) {
        return BitmapFactory.decodeResource(resources, r);
    }

}

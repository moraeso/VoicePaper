package com.example.voicepaper.manager;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.voicepaper.activity.MainActivity;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.data.User;
import com.example.voicepaper.util.ConfirmDialog;

import java.util.ArrayList;

public class AppManager {

    private static AppManager instance;
    private MainActivity MA;

    private long backKeyPressedTime = 0;

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

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            ((Activity)AppManager.getInstance().getContext()).finish();
        }
    }

    public void showGuide() {
        Toast.makeText(context, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
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

    public void setMainActivity(MainActivity MA){this.MA = MA;}
    public MainActivity getMainActivity(){ return MA; }
}

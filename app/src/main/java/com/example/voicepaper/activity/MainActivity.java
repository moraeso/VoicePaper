package com.example.voicepaper.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.voicepaper.R;
import com.example.voicepaper.adapter.RoomSlidePagerAdapter;
import com.example.voicepaper.fragment.main.CreateRoomFragment;
import com.example.voicepaper.manager.AppManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager roomPager;
    private PagerAdapter roomPagerAdapter;
    private Button createRoomBtn, enterRoomBtn;

    private final static int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());

        setContentView(R.layout.activity_main);

        roomPager = (ViewPager) findViewById(R.id.roomViewPager);
        roomPagerAdapter = new RoomSlidePagerAdapter(getSupportFragmentManager());
        roomPager.setAdapter(roomPagerAdapter);

        createRoomBtn = (Button) findViewById(R.id.btn_createRoom);
        createRoomBtn.setOnClickListener(this);

        checkPermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_createRoom :
                CreateRoomFragment createRoomFragment = CreateRoomFragment.newInstance();
                createRoomFragment.show(getSupportFragmentManager(), null);
                break;
            case R.id.btn_enterRoom :

                break;
            case R.id.btn_setting :

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    //갤러리 접근 권한 설정
    private void checkPermission(){

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_READ_EXTERNAL_STORAGE);

            Log.v("갤러리 권한", "갤러리 사용을 위해 권한이 필요합니다.");
        }

    }
}

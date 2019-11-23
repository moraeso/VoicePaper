package com.example.voicepaper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voicepaper.R;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.manager.AppManager;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity {

    private ImageView roomProfileIv;
    private TextView roomTitleTv, roomCommentTv;

    private Room room;

    private String code;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());

        setContentView(R.layout.activity_room);

        initView();
        getRoomInfo();
    }

    private void getRoomInfo() {
        code = getIntent().getExtras().getString("code");
        Toast.makeText(this, "Room Code : " + code, Toast.LENGTH_SHORT).show();

        ArrayList<Room> roomItems = AppManager.getInstance().getRoomList();
        for (Room item : roomItems) {
            if (code.equals(item.getCode())) {
                room = item;
                break;
            }
         }

        roomProfileIv.setImageBitmap(room.getProfileImage());
        roomTitleTv.setText(room.getTitle());
        roomCommentTv.setText(room.getComment());
    }

    private void initView() {
        roomProfileIv = (ImageView)findViewById(R.id.iv_roomProfile);
        roomTitleTv = (TextView)findViewById(R.id.tv_roomTitle);
        roomCommentTv = (TextView)findViewById(R.id.tv_roomComment);
    }


}
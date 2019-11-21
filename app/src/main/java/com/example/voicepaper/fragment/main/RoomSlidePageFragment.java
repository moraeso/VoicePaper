package com.example.voicepaper.fragment.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.voicepaper.R;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.util.Constants;

import java.util.ArrayList;

public class RoomSlidePageFragment extends Fragment {

    private ImageView[] roomProfile = new ImageView[Constants.ROOMS];
    private TextView[] roomNameTv = new TextView[Constants.ROOMS];
    private ArrayList<Room> roomList;
    private int page;

    public RoomSlidePageFragment(ArrayList<Room> roomList, int page) {
        this.roomList = roomList;
        this.page = page;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.fragment_room_slide_page, container, false);


        initView(view, roomList);

        return view;
    }

    public void initView(ViewGroup view, ArrayList<Room> roomList) {

        roomProfile[0] = (ImageView) view.findViewById(R.id.iv_roomProfile1);
        roomProfile[1] = (ImageView) view.findViewById(R.id.iv_roomProfile2);
        roomProfile[2] = (ImageView) view.findViewById(R.id.iv_roomProfile3);
        roomProfile[3] = (ImageView) view.findViewById(R.id.iv_roomProfile4);

        roomNameTv[0] = (TextView) view.findViewById(R.id.tv_roomName1);
        roomNameTv[1] = (TextView) view.findViewById(R.id.tv_roomName2);
        roomNameTv[2] = (TextView) view.findViewById(R.id.tv_roomName3);
        roomNameTv[3] = (TextView) view.findViewById(R.id.tv_roomName4);

        for (int i = 0; i < roomList.size(); i++) {
            roomProfile[i].setImageBitmap(roomList.get(i).getProfileImage());
            roomNameTv[i].setText(roomList.get(i).getName());
        }
    }
}

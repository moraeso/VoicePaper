package com.example.voicepaper.fragment.main;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.voicepaper.R;
import com.example.voicepaper.activity.MainActivity;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.manager.ImageManager;
import com.example.voicepaper.util.Constants;

import java.util.ArrayList;

public class RoomSlidePageFragment extends Fragment {

    private ArrayList<Room> allRooms;

    private ImageView[] roomProfile = new ImageView[Constants.ROOMS];
    private TextView[] roomNameTv = new TextView[Constants.ROOMS];
    //private ArrayList<Room> roomList;
    private int page;
    private int roomNum;

    public RoomSlidePageFragment(int page) {
        //this.roomList = roomList;
        this.page = page;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.fragment_room_slide_page, container, false);

        initView(view);

        if (!isRoomListEmpty()) {
            initViewContents();
            initListener();
        }

        return view;
    }

    private boolean isRoomListEmpty() {
        return AppManager.getInstance().getRoomList().size() == 0;
    }

    private void initView(View view) {
        roomProfile[0] = (ImageView) view.findViewById(R.id.iv_roomProfile1);
        roomProfile[1] = (ImageView) view.findViewById(R.id.iv_roomProfile2);
        roomProfile[2] = (ImageView) view.findViewById(R.id.iv_roomProfile3);
        roomProfile[3] = (ImageView) view.findViewById(R.id.iv_roomProfile4);

        roomNameTv[0] = (TextView) view.findViewById(R.id.tv_roomName1);
        roomNameTv[1] = (TextView) view.findViewById(R.id.tv_roomName2);
        roomNameTv[2] = (TextView) view.findViewById(R.id.tv_roomName3);
        roomNameTv[3] = (TextView) view.findViewById(R.id.tv_roomName4);
    }


    private void initViewContents() {
        allRooms = AppManager.getInstance().getRoomList();

        roomNum = Constants.ROOMS;

        if (isLastPage() && isNotFullPage()) {
            roomNum = allRooms.size() % Constants.ROOMS;
        }

        initCurPage();
    }

    private void initListener() {

        for (int i = 0; i < roomNum; i++) {
            roomProfile[i].setOnClickListener((MainActivity) getActivity());
            roomProfile[i].setOnLongClickListener((MainActivity) getActivity());
        }
    }

    private boolean isLastPage() {
        return ((allRooms.size() - 1) / Constants.ROOMS) == page;
    }

    private boolean isNotFullPage() {
        return allRooms.size() % Constants.ROOMS != 0;
    }


    private void initCurPage() {
        ArrayList<Room> curPageRooms = new ArrayList<Room>();

        for (int i = 0; i < roomNum; i++) {
            curPageRooms.add(allRooms.get(page * Constants.ROOMS + i));
        }

        for (int i = 0; i < roomNum; i++) {

            if (curPageRooms.get(i).getProfileString().equals("undefined") ||
                    curPageRooms.get(i).getProfileString().equals("")) {
                Drawable drawable = getResources().getDrawable(R.drawable.ic_user_main);
                roomProfile[i].setImageBitmap(((BitmapDrawable) drawable).getBitmap());
            } else {
                String url = ImageManager.getInstance().getFullImageString(curPageRooms.get(i).getProfileString(), "groupImage");
                ImageManager.getInstance().GlideInto(AppManager.getInstance().getContext(), roomProfile[i], url);
            }
            roomNameTv[i].setText(curPageRooms.get(i).getTitle());
        }
    }

}

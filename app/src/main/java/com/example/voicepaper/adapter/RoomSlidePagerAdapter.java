package com.example.voicepaper.adapter;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.voicepaper.data.Room;
import com.example.voicepaper.fragment.main.RoomSlidePageFragment;
import com.example.voicepaper.util.Constants;

import java.util.ArrayList;

public class RoomSlidePagerAdapter extends FragmentPagerAdapter {

    private int numPages;
    private ArrayList<Room> roomList;

    public RoomSlidePagerAdapter(FragmentManager fm, ArrayList<Room> roomList) {
        super(fm);

        this.roomList = roomList;
    }

    @Override
    public Fragment getItem(int position) {
        ArrayList<Room> rooms = new ArrayList<>();

        int roomNum = Constants.ROOMS;
        if (((roomList.size() - 1) / Constants.ROOMS) == position)
            roomNum = roomList.size() % Constants.ROOMS;

        for (int i = 0; i < roomNum; i++) {
            rooms.add(roomList.get(position * Constants.ROOMS + i));
        }
        /*
        Log.d("sssong:RoomPagerAdapter", "room size : " + roomList.size());
        Log.d("sssong:RoomPagerAdapter", "roomNum : " + roomNum);
        Log.d("sssong:RoomPagerAdapter",
                "room profile : " + roomList.get((roomList.size() - 1) / 4 * 4 + 0).getProfileImage().toString());
*/
        return new RoomSlidePageFragment(rooms, position);
    }

    @Override
    public int getCount() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }
}
package com.example.voicepaper.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.voicepaper.data.Room;
import com.example.voicepaper.fragment.main.RoomSlidePageFragment;

import java.util.ArrayList;

public class RoomSlidePagerAdapter extends FragmentPagerAdapter {

    private int maxPages;
    private ArrayList<Room> roomList;
    // ViewPager에 들어갈 Fragment들을 담을 리스트
    private ArrayList<RoomSlidePageFragment> fragments = new ArrayList<>();

    public RoomSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        Log.d("sssong:RoomPagerAdapter", "getItemPosition");

        return POSITION_NONE;
    }
    @Override
    public int getCount() {
        return maxPages;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    public void initFragments() {
        fragments = new ArrayList<>();
    }

    public void setRoomList(ArrayList<Room> roomList) {
        this.roomList = roomList;
    }

    public void addItem(int position) {
        fragments.add(new RoomSlidePageFragment(position));
    }
}
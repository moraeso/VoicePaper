package com.example.voicepaper.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.voicepaper.fragment.main.RoomSlidePageFragment;

public class RoomSlidePagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 4;

    public RoomSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new RoomSlidePageFragment();
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
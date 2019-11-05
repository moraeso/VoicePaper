package com.example.voicepaper.activity;

import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.voicepaper.R;
import com.example.voicepaper.adapter.RoomSlidePagerAdapter;
import com.example.voicepaper.fragment.main.RoomSlidePageFragment;
import com.example.voicepaper.manager.AppManager;
import com.itsronald.widget.ViewPagerIndicator;

public class MainActivity extends AppCompatActivity {

    private ViewPager roomPager;
    private PagerAdapter roomPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());

        setContentView(R.layout.activity_main);

        roomPager = (ViewPager) findViewById(R.id.roomViewPager);
        roomPagerAdapter = new RoomSlidePagerAdapter(getSupportFragmentManager());
        roomPager.setAdapter(roomPagerAdapter);
    }
}

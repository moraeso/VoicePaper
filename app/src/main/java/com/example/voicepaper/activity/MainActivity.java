package com.example.voicepaper.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.voicepaper.R;
import com.example.voicepaper.fragment.main.RoomGridFragment;
import com.example.voicepaper.manager.AppManager;

public class MainActivity extends AppCompatActivity {

    Fragment roomGridFragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());

        setContentView(R.layout.activity_main);

        roomGridFragment = new RoomGridFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.fragment_roomGrid, roomGridFragment );
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

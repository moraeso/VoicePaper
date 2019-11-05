package com.example.voicepaper.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.voicepaper.R;

public class RoomSlidePageFragment extends Fragment {

    ImageView roomProfile1;
    ImageView roomProfile2;
    ImageView roomProfile3;
    ImageView roomProfile4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.fragment_room_slide_page, container, false);

        roomProfile1 = (ImageView) view.findViewById(R.id.iv_roomProfile1);
        roomProfile1.setImageResource(R.drawable.img_empty_room);

        roomProfile2 = (ImageView) view.findViewById(R.id.iv_roomProfile2);
        roomProfile2.setImageResource(R.drawable.img_empty_room);

        roomProfile3 = (ImageView) view.findViewById(R.id.iv_roomProfile3);
        roomProfile3.setImageResource(R.drawable.img_empty_room);

        roomProfile4 = (ImageView) view.findViewById(R.id.iv_roomProfile4);
        roomProfile4.setImageResource(R.drawable.img_empty_room);


        return view;
    }
}

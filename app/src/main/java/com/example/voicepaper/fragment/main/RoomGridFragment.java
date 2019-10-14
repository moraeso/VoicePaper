package com.example.voicepaper.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.voicepaper.R;
import com.example.voicepaper.activity.ImageActivity;
import com.example.voicepaper.adapter.RoomGridViewAdapter;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.manager.AppManager;

import java.util.ArrayList;

public class RoomGridFragment extends Fragment {

    private GridView gridView;
    private RoomGridViewAdapter gridViewAdapter;
    private ArrayList<Room> roomItems = new ArrayList();;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_gridview, container, false);

        roomItems.add(new Room(1, "Room1", AppManager.getInstance().getBitmap(R.drawable.img_android1), ".jpg","hi",1));
        roomItems.add(new Room(2, "Room2", AppManager.getInstance().getBitmap(R.drawable.img_android2),".jpg","hi",1));
        roomItems.add(new Room(3, "Room3", AppManager.getInstance().getBitmap(R.drawable.img_android3),".jpg","hi",1));
        roomItems.add(new Room(4, "Room4", AppManager.getInstance().getBitmap(R.drawable.img_android1),".jpg","hi",1));
        roomItems.add(new Room(5, "Room5", AppManager.getInstance().getBitmap(R.drawable.img_android2),".jpg","hi",1));
        roomItems.add(new Room(6, "Room6", AppManager.getInstance().getBitmap(R.drawable.img_android3),".jpg","hi",1));
        roomItems.add(new Room(7, "Room7", AppManager.getInstance().getBitmap(R.drawable.img_android1),".jpg","hi",1));
        roomItems.add(new Room(8, "Room8", AppManager.getInstance().getBitmap(R.drawable.img_android2),".jpg","hi",1));
        roomItems.add(new Room(9, "Room9", AppManager.getInstance().getBitmap(R.drawable.img_android3),".jpg","hi",1));
        roomItems.add(new Room(10, "Room10", AppManager.getInstance().getBitmap(R.drawable.img_android1),".jpg","hi",1));

        gridView = view.findViewById(R.id.gridView_room);
        gridViewAdapter = new RoomGridViewAdapter(roomItems);
        gridView.setAdapter(gridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                Intent intent = new Intent(AppManager.getInstance().getContext(),
                        ImageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }
}

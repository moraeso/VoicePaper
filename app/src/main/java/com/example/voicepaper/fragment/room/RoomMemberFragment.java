package com.example.voicepaper.fragment.room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voicepaper.R;
import com.example.voicepaper.adapter.Decoration.RoomMemberRecyclerViewDecoration;
import com.example.voicepaper.adapter.RoomMemberRecyclerViewerAdapter;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.data.RoomMember;
import com.example.voicepaper.manager.AppManager;

import java.util.ArrayList;

public class RoomMemberFragment extends DialogFragment {

    private ArrayList<RoomMember> roomMemberList;
    private Room room;

    private TextView roomCodeTv;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView memberRecyclerView;
    private RoomMemberRecyclerViewerAdapter roomMemberAdapter;

    private RoomMemberFragment(Room room, ArrayList<RoomMember> roomMemberList) {
        this.room = room;
        this.roomMemberList = roomMemberList;
    }

    public static RoomMemberFragment newInstance(Room room, ArrayList<RoomMember> roomMemberList) {
        RoomMemberFragment fragment = new RoomMemberFragment(room, roomMemberList);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roominfo, container, false);

        initView(view);
        initRoomMemberAdapter(view);

        return view;
    }

    public void initView(View view) {
        roomCodeTv = (TextView) view.findViewById(R.id.tv_roomCode);
        roomCodeTv.setText(room.getCode());
    }

    public void initRoomMemberAdapter(View view) {
        // RecycleView 생성 및 사이즈 고정
        memberRecyclerView = (RecyclerView) view.findViewById(R.id.rv_memberList);
        memberRecyclerView.setHasFixedSize(true);

        // Grid 레이아웃 적용
        layoutManager = new LinearLayoutManager(AppManager.getInstance().getContext());
        memberRecyclerView.setLayoutManager(layoutManager);
        memberRecyclerView.addItemDecoration(new RoomMemberRecyclerViewDecoration(this));

        // 어뎁터 연결
        roomMemberAdapter = new RoomMemberRecyclerViewerAdapter(roomMemberList);
        memberRecyclerView.setAdapter(roomMemberAdapter);

        roomMemberAdapter.setHostId(room.getHostID());
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}

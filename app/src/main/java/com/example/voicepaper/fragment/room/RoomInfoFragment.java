package com.example.voicepaper.fragment.room;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.voicepaper.manager.ImageManager;

import java.util.ArrayList;

public class RoomInfoFragment extends DialogFragment implements View.OnClickListener {

    private ArrayList<RoomMember> roomMemberList;
    private Room room;

    private TextView roomCodeTv;
    private ImageButton copyCodeBtn;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView memberRecyclerView;
    private RoomMemberRecyclerViewerAdapter roomMemberAdapter;

    private RoomInfoFragment(Room room, ArrayList<RoomMember> roomMemberList) {
        this.room = room;
        this.roomMemberList = roomMemberList;
    }

    public static RoomInfoFragment newInstance(Room room, ArrayList<RoomMember> roomMemberList) {
        RoomInfoFragment fragment = new RoomInfoFragment(room, roomMemberList);
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
        initListener();
        initRoomMemberAdapter(view);

        return view;
    }

    private void initListener() {
        copyCodeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_copyCode:
                ClipboardManager clipboard = (ClipboardManager)AppManager.getInstance().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Room Code Copy", room.getCode());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(AppManager.getInstance().getContext(), "방 코드 복사 완료!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void initView(View view) {
        roomCodeTv = (TextView) view.findViewById(R.id.tv_roomCode);
        copyCodeBtn = (ImageButton) view.findViewById(R.id.btn_copyCode);

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

package com.example.voicepaper.fragment.room;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voicepaper.adapter.VoiceRecycleViewerAdapter;
import com.example.voicepaper.fragment.main.CreateRoomFragment;

public class RoomMemberFragment extends DialogFragment {

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView memberRecyclerView;
    private VoiceRecycleViewerAdapter roomAdapter;

    private RoomMemberFragment() {
    }

    public static RoomMemberFragment newInstance() {
        RoomMemberFragment fragment = new RoomMemberFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

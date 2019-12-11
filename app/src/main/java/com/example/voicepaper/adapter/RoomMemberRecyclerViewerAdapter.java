package com.example.voicepaper.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voicepaper.R;
import com.example.voicepaper.adapter.viewholder.RoomMemberViewHolder;
import com.example.voicepaper.data.RoomMember;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.util.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RoomMemberRecyclerViewerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<RoomMember> roomMemberItems;
    private String hostId;

    public RoomMemberRecyclerViewerAdapter(ArrayList<RoomMember> roomMemberItems) {
        this.roomMemberItems = roomMemberItems;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomMemberViewHolder(LayoutInflater.from(AppManager.getInstance().getContext()).inflate(R.layout.item_roommember, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RoomMemberViewHolder) {
            RoomMember roomMember = roomMemberItems.get(position);

            ((RoomMemberViewHolder) holder).getMemberIdTv().setText(roomMember.getId());
            ((RoomMemberViewHolder) holder).getMemberNameTv().setText(roomMember.getName());

            if (roomMember.getId().equals(hostId)) {
                ((RoomMemberViewHolder) holder).getMemberIdTv().setTextColor(
                        ContextCompat.getColor(AppManager.getInstance().getContext(), R.color.colorMainBold));
                ((RoomMemberViewHolder) holder).getMemberNameTv().setTextColor(
                        ContextCompat.getColor(AppManager.getInstance().getContext(), R.color.colorMainBold));
            }
        }
    }

    @Override
    public int getItemCount() {
        return roomMemberItems.size();
    }

    /*
    public void addAll(ArrayList<RoomMember> items) {
        roomMemberItems.clear();
        roomMemberItems.addAll(items);
        notifyDataSetChanged();
    }*/
}

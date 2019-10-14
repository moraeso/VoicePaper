package com.example.voicepaper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.voicepaper.R;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.manager.AppManager;

import java.util.ArrayList;

public class RoomGridViewAdapter extends BaseAdapter {

    private ArrayList<Room> roomItems;

    public RoomGridViewAdapter(ArrayList<Room> roomItems){
        this.roomItems = roomItems;
    }

    @Override
    public int getCount() {
        return roomItems.size();
    }

    @Override
    public Object getItem(int position) {
        return roomItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RoomViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(AppManager.getInstance().getContext()).inflate(R.layout.item_room, null);
            viewHolder = new RoomViewHolder();
            viewHolder.iv_roomProfile = convertView.findViewById(R.id.iv_roomProfile);
            viewHolder.iv_roomProfile.setImageBitmap(roomItems.get(position).getProfileImage());
            viewHolder.tv_roomName = convertView.findViewById(R.id.tv_roomName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RoomViewHolder)convertView.getTag();
        }

        return convertView;
    }

    private class RoomViewHolder {
        ImageView iv_roomProfile;
        TextView tv_roomName;
    }
}

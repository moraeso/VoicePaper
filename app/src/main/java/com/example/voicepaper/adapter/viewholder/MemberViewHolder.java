package com.example.voicepaper.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.voicepaper.R;

public class MemberViewHolder extends RecyclerView.ViewHolder {
    private TextView memberIdTv;
    private TextView memberNameTv;

    public MemberViewHolder(View convertView) {
        super(convertView);

        memberIdTv = (TextView) convertView.findViewById(R.id.tv_memberId);
        memberNameTv = (TextView) convertView.findViewById(R.id.tv_memberName);
    }
}

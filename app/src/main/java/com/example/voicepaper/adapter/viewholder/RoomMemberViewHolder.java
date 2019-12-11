package com.example.voicepaper.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.voicepaper.R;

public class RoomMemberViewHolder extends RecyclerView.ViewHolder {
    private TextView memberIdTv;
    private TextView memberNameTv;

    public RoomMemberViewHolder(View convertView) {
        super(convertView);

        memberIdTv = (TextView) convertView.findViewById(R.id.tv_memberId);
        memberNameTv = (TextView) convertView.findViewById(R.id.tv_memberName);
    }



    public TextView getMemberIdTv() {
        return memberIdTv;
    }

    public void setMemberIdTv(TextView memberIdTv) {
        this.memberIdTv = memberIdTv;
    }

    public TextView getMemberNameTv() {
        return memberNameTv;
    }

    public void setMemberNameTv(TextView memberNameTv) {
        this.memberNameTv = memberNameTv;
    }
}

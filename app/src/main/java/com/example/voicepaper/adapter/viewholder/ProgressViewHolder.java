package com.example.voicepaper.adapter.viewholder;

import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.example.voicepaper.R;

public class ProgressViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar pBar;

    public ProgressViewHolder(View convertView) {
        super(convertView);

        pBar = (ProgressBar) convertView.findViewById(R.id.pBar);
    }
}

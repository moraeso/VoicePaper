package com.example.voicepaper.adapter.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.voicepaper.R;

public class VoiceViewHolder extends RecyclerView.ViewHolder {

    private TextView userNameTv;
    private ImageButton playerBtn;
    private ProgressBar progressBar;

    public VoiceViewHolder(View convertView) {
        super(convertView);

        userNameTv = (TextView) convertView.findViewById(R.id.tv_name);
        playerBtn = (ImageButton) convertView.findViewById(R.id.btn_player);
        progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
    }

    public TextView getUserNameTv() {
        return userNameTv;
    }

    public void setUserNameTv(TextView userNameTv) {
        this.userNameTv = userNameTv;
    }

    public ImageButton getPlayerBtn() {
        return playerBtn;
    }

    public void setPlayerBtn(ImageButton playerBtn) {
        this.playerBtn = playerBtn;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}

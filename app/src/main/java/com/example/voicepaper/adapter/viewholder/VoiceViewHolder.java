package com.example.voicepaper.adapter.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.voicepaper.R;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.util.ConfirmDialog;

public class VoiceViewHolder extends RecyclerView.ViewHolder {
    private TextView userNameTv;
    private ImageButton playerBtn;
    private ProgressBar progressBar;
    private View holder;

    public VoiceViewHolder(View convertView) {
        super(convertView);

        userNameTv = (TextView) convertView.findViewById(R.id.tv_name);
        playerBtn = (ImageButton) convertView.findViewById(R.id.btn_player);
        //progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        this.holder = convertView;
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

    public View getHolder(){ return holder; }
}

package com.example.voicepaper.adapter.viewholder;

import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.voicepaper.R;

public class VoiceViewHolder extends RecyclerView.ViewHolder {

    private Button voiceButton;

    public VoiceViewHolder(View convertView) {
        super(convertView);

        voiceButton = (Button) convertView.findViewById(R.id.voiceButton);
    }

    public Button getVoiceButton() {
        return voiceButton;
    }
}

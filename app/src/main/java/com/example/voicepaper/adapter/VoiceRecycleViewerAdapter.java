package com.example.voicepaper.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voicepaper.R;
import com.example.voicepaper.adapter.viewholder.VoiceViewHolder;
import com.example.voicepaper.data.Voice;
import com.example.voicepaper.manager.AppManager;

import java.util.ArrayList;

public class VoiceRecycleViewerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<com.example.voicepaper.data.Voice> voiceItems;

    public VoiceRecycleViewerAdapter() {
        voiceItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VoiceViewHolder(LayoutInflater.from(AppManager.getInstance().getContext()).inflate(R.layout.item_voice, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VoiceViewHolder) {
            final Voice voice = voiceItems.get(position);

            ((VoiceViewHolder) holder).getVoiceButton().setText("Voice " + position);
            ((VoiceViewHolder) holder).getVoiceButton().setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(AppManager.getInstance().getContext(), "Voice " + position, Toast.LENGTH_SHORT).show();
                        }
                    }
            );

        }
    }

    @Override
    public int getItemCount() {
        return voiceItems.size();
    }

    public ArrayList<Voice> getVoiceItems() {
        return voiceItems;
    }

    public void addAll(ArrayList<com.example.voicepaper.data.Voice> items) {
        voiceItems.clear();
        voiceItems.addAll(items);
        notifyDataSetChanged();
    }
}

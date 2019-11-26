package com.example.voicepaper.adapter;

import android.speech.tts.Voice;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voicepaper.R;
import com.example.voicepaper.adapter.viewholder.VoiceViewHolder;
import com.example.voicepaper.manager.AppManager;

import java.util.ArrayList;

public class VoiceRecycleViewerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Voice> voiceItems;

    public VoiceRecycleViewerAdapter() {
        voiceItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VoiceViewHolder(LayoutInflater.from(AppManager.getInstance().getContext()).inflate(R.layout.item_voice, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VoiceViewHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return voiceItems.size();
    }

    public ArrayList<Voice> getVoiceItems() {
        return voiceItems;
    }

    public void addAll(ArrayList<Voice> items) {
        voiceItems.clear();
        voiceItems.addAll(items);
        notifyDataSetChanged();
    }
}

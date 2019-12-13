package com.example.voicepaper.adapter;

import android.media.MediaPlayer;
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
import com.example.voicepaper.network.MusicPlayer;
import com.example.voicepaper.util.ConfirmDialog;
import com.example.voicepaper.util.Constants;

import java.util.ArrayList;

public class VoiceRecycleViewerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<com.example.voicepaper.data.Voice> voiceItems;
    int permission;
    boolean isHost;
    int roomID;

    public VoiceRecycleViewerAdapter(int roomID) {
        voiceItems = new ArrayList<>();
        this.roomID = roomID;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
    public void setIsUserHost(boolean isHost) {
        this.isHost = isHost;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VoiceViewHolder(LayoutInflater.from(AppManager.getInstance().getContext()).inflate(R.layout.item_voice, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final Voice voice = voiceItems.get(position);

        ((VoiceViewHolder) holder).getUserNameTv().setText("" + voice.getUserName());
        ((VoiceViewHolder) holder).getHolder().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (voice.getUserId().equals(AppManager.getInstance().getUser().getID())) {
                    ConfirmDialog confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());
                    confirmDialog.setMessage("기록을 삭제하시겠습니까?");
                    confirmDialog.getOkBtn().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (canListenVoice(voice)) {
                                Toast.makeText(AppManager.getInstance().getContext(), "Voice " + voice.getUserName(), Toast.LENGTH_SHORT).show();

                                final MusicPlayer musicPlayer = new MusicPlayer(voice.getVoiceFile(), ((VoiceViewHolder) holder).getPlayerBtn());
                                musicPlayer.execute();
                            } else {
                                Toast.makeText(AppManager.getInstance().getContext(), "권환이 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    confirmDialog.show();
                }
                return false;
            }
        });

        ((VoiceViewHolder) holder).getPlayerBtn().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (canListenVoice(voice)) {
                            Toast.makeText(AppManager.getInstance().getContext(), "Voice " + voice.getUserName(), Toast.LENGTH_SHORT).show();

                            final MusicPlayer musicPlayer = new MusicPlayer(voice.getVoiceFile(), ((VoiceViewHolder) holder).getPlayerBtn());
                            musicPlayer.execute();
                        } else {
                            Toast.makeText(AppManager.getInstance().getContext(), "권환이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }


    private boolean canListenVoice(Voice voice) {
        return isPermissionPublic() || isYourVoice(voice) || isHost;
    }

    private boolean isPermissionPublic() {
        return permission == Constants.VOICE_PUBLIC;
    }

    private boolean isYourVoice(Voice voice) {
        return voice.getUserId().equals(AppManager.getInstance().getUser().getID());
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

    public void clearItems() {
        voiceItems.clear();
    }
}

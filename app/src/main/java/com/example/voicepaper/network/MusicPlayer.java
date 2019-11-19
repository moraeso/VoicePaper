package com.example.voicepaper.network;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.io.IOException;

public class MusicPlayer extends AsyncTask<Void, Void, Void> {
    private MediaPlayer m_MediaPlayer;

    public MusicPlayer() {
        m_MediaPlayer = new MediaPlayer();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String url = "http://15011066.iptime.org:8123/static/testmusic4.mp3"; // your URL here

        m_MediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            m_MediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            m_MediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        m_MediaPlayer.start();
    }
}

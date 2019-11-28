package com.example.voicepaper.network;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.example.voicepaper.util.ConfirmDialog;
import com.example.voicepaper.util.Constants;

import java.io.IOException;

public class MusicPlayer extends AsyncTask<Void, Void, Void> {
    private MediaPlayer m_MediaPlayer;
    private String url;

    public MusicPlayer(String _filePath) {
        m_MediaPlayer = new MediaPlayer();

        String buf;
        buf = _filePath;
        //Log.d("smh:image address group", buf);
        String buf2[] = buf.split("/");
        //Log.d("smh:image address group", buf2[2]);

        url = Constants.URL + "/voicefile/" + buf2[2]; // your URL here
    }

    @Override
    protected Void doInBackground(Void... voids) {
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

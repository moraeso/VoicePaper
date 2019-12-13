package com.example.voicepaper.network;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.voicepaper.R;
import com.example.voicepaper.util.ConfirmDialog;
import com.example.voicepaper.util.Constants;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AsyncTask<Void, Void, Boolean>{
    private MediaPlayer m_MediaPlayer;
    private String url;
    private ImageButton imageButton;
    //private ProgressBar progressBar;
    int state;

    private static final int PLAY_START = 2;
    private static final int PLAY_STOP = 3;

    public MusicPlayer(String _filePath, ImageButton imageButton) {
        this.imageButton = imageButton;

        state = PLAY_STOP;

        String buf;
        buf = _filePath;
        String buf2[] = buf.split("/");
        url = Constants.URL + "/file/voice/" + buf2[2]; // your URL here
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressBar.setMax(100);
        //progressBar.setProgress(0);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        m_MediaPlayer = new MediaPlayer();
        m_MediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        m_MediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                onPlay(false);
            }
        });
        onPlay(true);

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        //progressBar.setProgress(0);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        float progress = ((float) m_MediaPlayer.getCurrentPosition() / m_MediaPlayer.getDuration()) * 100;
        Log.d("shin progress",""+progress);
        Log.d("shin current",""+m_MediaPlayer.getCurrentPosition());
        Log.d("shin dura",""+m_MediaPlayer.getDuration());
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }


    private void startPlaying() {
        try {
            m_MediaPlayer.setDataSource(url);
            m_MediaPlayer.prepare();
            m_MediaPlayer.start();
            m_MediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopPlaying();
                }
            });
            imageButton.setImageResource(R.drawable.ic_stop);
            imageButton.setEnabled(false);
            state = PLAY_STOP;

        } catch (IOException e) {
            Log.e("smh:record", "prepare() failed");
        }
    }

    private void stopPlaying() {
        m_MediaPlayer.release();
        m_MediaPlayer = null;
        imageButton.setImageResource(R.drawable.ic_playing);
        imageButton.setEnabled(true);
        state = PLAY_START;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //progressBar.setProgress(0);
    }
}

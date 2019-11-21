package com.example.voicepaper.fragment.room;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.voicepaper.R;
import com.example.voicepaper.manager.AppManager;

import java.io.File;
import java.io.IOException;

public class RecordFragment extends DialogFragment implements Button.OnClickListener{
    //view
    TextView tv_content;
    ImageButton btn_record;
    Button btn_cancel;
    Button btn_submit;
    private int state;

    //reocrd
    private MediaRecorder recorder; // 음성 기록
    private MediaPlayer player; // 음성 재생

    private String fileName = null;

    private static final int RECODE_START = 1;
    private static final int RECODE_STOP = 0;
    private static final int PLAY_START = 2;
    private static final int PLAY_STOP = 3;

    public RecordFragment(){ }

    public static RecordFragment newInstance(){
        RecordFragment fragment = new RecordFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissinCheck();

        state = RECODE_START;

        fileName = getActivity().getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";

        Log.d("smh:file path",fileName);
    }//프레그 먼트가 생성될때 호출됨. //그래픽이 아닌 초기화

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(AppManager.getInstance().getContext(), getTheme()){
            @Override
            public void onBackPressed() { dismiss(); }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        initView(view);

        return view;
    }//프레그먼트가 onCreate 후 화면을 구성할때 호출됨.

    public void initView(View view){
        tv_content = view.findViewById(R.id.tv_content);
        btn_record = view.findViewById(R.id.btn_record);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_submit = view.findViewById(R.id.btn_submit);

        btn_record.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    public void permissinCheck(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
        }
        // getActivity 바꿔야할수도 있음.
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_record:
                if(state == RECODE_START){
                    onRecord(true);
                    state = RECODE_STOP;
                }
                else if(state == RECODE_STOP){
                    onRecord(false);
                    state = PLAY_START;
                }
                else if(state == PLAY_START){
                    onPlay(true);
                    state = PLAY_STOP;
                }
                else if(state == PLAY_STOP){
                    onPlay(false);
                    state = PLAY_START;
                }

                break;
            case R.id.btn_cancel:
                break;
            case R.id.btn_submit:
                break;

        }
    }

    private void onRecord(boolean start){
        if(start){
            startRecording();
        }
        else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e("smh:record", "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        /*
        음성 녹음 시작, 압축해야한다.
         */
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.d("smh:record", "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    //프레그먼트 종료할때, 캐시저장해야하나? 모르겠다... 잘..
    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
}

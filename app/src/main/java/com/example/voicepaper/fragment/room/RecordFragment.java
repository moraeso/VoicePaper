package com.example.voicepaper.fragment.room;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
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

public class RecordFragment extends DialogFragment implements Button.OnClickListener{
    MediaRecorder recorder; // 음성 기록
    MediaPlayer player; // 음성 재생
    String filename;

    //view
    TextView tv_content;
    ImageButton btn_recorder;
    Button btn_cancel;
    Button btn_submit;

    public RecordFragment(){ }

    public static RecordFragment newInstance(){
        RecordFragment fragment = new RecordFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissinCheck();
    }

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

        return view;
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

    }
}

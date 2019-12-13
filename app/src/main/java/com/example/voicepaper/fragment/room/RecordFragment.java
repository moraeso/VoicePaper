package com.example.voicepaper.fragment.room;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.voicepaper.R;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.network.AsyncCallback;
import com.example.voicepaper.network.UploadFile;
import com.example.voicepaper.util.ConfirmDialog;

import java.io.IOException;

/*
음성파일 전송 작업
재생버튼 start and stop 할때 소리
 */

public class RecordFragment extends DialogFragment implements Button.OnClickListener{
    //view
    private TextView tv_content;
    private ImageButton btn_record;
    private Button btn_cancel;
    private Button btn_submit;
    private int state;

    //reocrd
    private MediaRecorder recorder; // 음성 기록
    private MediaPlayer player; // 음성 재생

    //private String fileName = null;
    private String filePath = null;

    private static final int RECODE_START = 1;
    private static final int RECODE_STOP = 0;
    private static final int PLAY_START = 2;
    private static final int PLAY_STOP = 3;

    private ConfirmDialog confirmDialog;

    //임시
    private String roomId;

    public RecordFragment(){}

    public static RecordFragment newInstance(){
        RecordFragment fragment = new RecordFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissinCheck();

        state = RECODE_START;

        setVoiceFilePath();


        confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());
    } //프레그 먼트가 생성될때 호출됨. //그래픽이 아닌 초기화

    private void setVoiceFilePath() {
        filePath = getActivity().getExternalCacheDir().getAbsolutePath();
        filePath += "/audiorecordtest.mp3";


        //fileName = "audiorecordtest.mp3";
        //Log.d("smh:file path",fileName);
    }

    public void setRoomId(int roomId){
        this.roomId = ""+roomId;
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

        tv_content.setText("목소리를 녹음할게요.\n버튼을 눌러 시작해 주세요.");
    }

    public void permissinCheck(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1);
        }
        // getActivity 바꿔야할수도 있음.
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_record:
                if(state == RECODE_START){
                    onRecord(true);
                }
                else if(state == RECODE_STOP){
                    onRecord(false);
                }
                else if(state == PLAY_START){
                    onPlay(true);
                }
                else if(state == PLAY_STOP){
                    onPlay(false);
                }

                break;
            case R.id.btn_cancel:
                dismiss();//취소시 프레그먼트 종료
                break;
            case R.id.btn_submit:
                /*
                여기부근에 통신 넣어야합니다.
                 */
                if(state==RECODE_STOP){
                    onRecord(false);
                }

                ContentValues values = new ContentValues();
                values.put("userId",AppManager.getInstance().getUser().getID());
                values.put("roomId",roomId);

                UploadFile uploadFile = new UploadFile(UploadFile.UPLOAD_AUDIO,values,filePath, new AsyncCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        confirmDialog.setMessage("녹음 완료!");
                        confirmDialog.show();
                        dismiss();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AppManager.getInstance().getContext(), "error : " + e, Toast.LENGTH_SHORT).show();
                        //confirmDialog.dismiss();
                    }
                });

                uploadFile.execute();
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
            player.setDataSource(filePath);
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    onPlay(false);
                }
            });

            btn_record.setImageResource(R.drawable.ic_stop);
            state = PLAY_STOP;

        } catch (IOException e) {
            Log.e("smh:record", "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
        btn_record.setImageResource(R.drawable.ic_playing);
        state = PLAY_START;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        /*
        음성 녹음 시작, 압축해야한다.
         */
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(filePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setMaxDuration(15000);//녹음 최대 시간 셋
        recorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {
                if(i==MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
                    Log.d("smh:Recoding time","reached");
                    onRecord(false);
                }
            }
        });
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.d("smh:record", "prepare() failed");
            }

        //이미지 변경
        btn_record.setImageResource(R.drawable.ic_stop2);
        state = RECODE_STOP;

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        btn_record.setImageResource(R.drawable.ic_playing);
        state = PLAY_START;
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

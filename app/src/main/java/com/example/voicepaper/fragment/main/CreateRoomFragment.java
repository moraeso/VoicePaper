package com.example.voicepaper.fragment.main;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.voicepaper.R;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.util.Constants;

import java.io.IOException;

public class CreateRoomFragment extends DialogFragment implements View.OnClickListener {

    private EditText roomNameEt, roomTextEt;
    private ImageButton roomProfileIb;
    private Button privateVoiceBtn, publicVoiceBtn, createRoomBtn;

    private int voicePermission;

    private Uri roomProfileUri;
    private int exifDegree;

    private static final int PICK_FROM_ALBUM = 1;

    private CreateRoomFragment() {    }

    public static CreateRoomFragment newInstance() {
        CreateRoomFragment fragment = new CreateRoomFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createroom, container, false);

        setCancelable(false);

        roomNameEt = (EditText) view.findViewById(R.id.et_roomName);
        roomProfileIb = (ImageButton) view.findViewById(R.id.ib_roomProfile);
        privateVoiceBtn = (Button) view.findViewById(R.id.btn_privateVoice);
        publicVoiceBtn = (Button) view.findViewById(R.id.btn_publicVoice);
        roomTextEt = (EditText) view.findViewById(R.id.et_roomText);
        createRoomBtn = (Button) view.findViewById(R.id.btn_createRoom);

        voicePermission = Constants.VOICE_PRIVATE;

        privateVoiceBtn.setOnClickListener(this);
        publicVoiceBtn.setOnClickListener(this);
        roomProfileIb.setOnClickListener(this);
        createRoomBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_privateVoice :
                voicePermission = Constants.VOICE_PRIVATE;
                privateVoiceBtn.setBackgroundColor(Color.CYAN);
                publicVoiceBtn.setBackgroundColor(Color.LTGRAY);
                break;
            case R.id.btn_publicVoice :
                voicePermission = Constants.VOICE_PUBLIC;
                privateVoiceBtn.setBackgroundColor(Color.LTGRAY);
                publicVoiceBtn.setBackgroundColor(Color.CYAN);
                break;
            case R.id.ib_roomProfile :
                pushImageButton();
                break;
            case R.id.btn_createRoom :
                dismiss(); //  임시 여기서 서버 호출해서 방 생성
                break;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(AppManager.getInstance().getContext(), getTheme()) {
            @Override
            public void onBackPressed() {
                dismiss();
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ((Activity)AppManager.getInstance().getContext()).RESULT_OK) {

            if (requestCode == PICK_FROM_ALBUM)  {
                //앨범 선택
                Uri uri = data.getData();
                roomProfileUri = uri;
                ExifInterface exif = null;
                String imagePath = getRealPathFromURI(uri);
                try {
                    exif = new ExifInterface(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
                roomProfileIb.setBackgroundColor(Color.WHITE);
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                roomProfileIb.setImageBitmap(rotate(bitmap, exifDegree));
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToFirst();
        column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        Log.d("smh:getRealPathFromURI", cursor.getString(column_index));
        return cursor.getString(column_index);
    }

    private Bitmap rotate(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree); // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) { return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) { return 270; }
        return 0;
    }

    // 앨범에서 이미지 가져오기
    public void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_FROM_ALBUM);
    }

    public void pushImageButton() {
        doTakeAlbumAction();

    }

}
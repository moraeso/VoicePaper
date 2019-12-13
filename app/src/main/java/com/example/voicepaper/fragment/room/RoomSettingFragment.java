package com.example.voicepaper.fragment.room;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.voicepaper.R;
import com.example.voicepaper.activity.RoomActivity;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.manager.ImageManager;
import com.example.voicepaper.network.AsyncCallback;
import com.example.voicepaper.network.SettingRoomTask;
import com.example.voicepaper.network.UploadFile;
import com.example.voicepaper.util.ConfirmDialog;
import com.example.voicepaper.util.Constants;

import java.io.IOException;
import java.util.ArrayList;

public class RoomSettingFragment extends DialogFragment implements View.OnClickListener {

    private ScrollView scrollView;

    /// 임시로 쓸 변경 사진
    private Bitmap bitmapTmp;

    private int id, permission;
    private String title, comment, imgStr;

    private EditText roomTitleEt, roomCommentEt;
    private ImageButton roomProfileIv;
    private Button privateVoiceBtn, publicVoiceBtn, settingRoomBtn;

    private int voicePermission;

    private ConfirmDialog confirmDialog;

    //수정필요
    private String albumImagePath;


    private RoomSettingFragment(int id, String title, String comment, int permission, String imgStr) {
        this.id = id;
        this.title = title;
        this.comment = comment;
        this.permission = permission;
        this.imgStr = imgStr;

        albumImagePath = null;
    }

    public static RoomSettingFragment newInstance(int id, String title, String comment, int permission, String imgStr) {

        RoomSettingFragment fragment = new RoomSettingFragment(id, title, comment, permission, imgStr);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_roomsetting, container, false);

        initView(view);
        initViewContents();
        initListener();

        roomCommentEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.scrollTo(0, scrollView.getBottom());
                        }
                    }, 500);
                }
            }
        });

        roomCommentEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, scrollView.getBottom());
                    }
                }, 500);
            }

        });

        return view;
    }

    private void initView(View view) {
        scrollView = (ScrollView) view.findViewById(R.id.sv_root);

        roomTitleEt = (EditText) view.findViewById(R.id.et_roomTitle);
        roomProfileIv = (ImageButton) view.findViewById(R.id.ib_roomProfile);
        privateVoiceBtn = (Button) view.findViewById(R.id.btn_privateVoice);
        publicVoiceBtn = (Button) view.findViewById(R.id.btn_publicVoice);
        roomCommentEt = (EditText) view.findViewById(R.id.et_roomComment);
        settingRoomBtn = (Button) view.findViewById(R.id.btn_settingRoom);

        confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());
    }

    private void initViewContents() {
        voicePermission = Constants.VOICE_PUBLIC;

        roomTitleEt.setText(title);
        roomCommentEt.setText(comment);

        publicVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                R.color.colorMainBold));
        privateVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                R.color.colorLightGray));

        if (permission == Constants.VOICE_PRIVATE) {
            voicePermission = Constants.VOICE_PUBLIC;
            publicVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                    R.color.colorLightGray));
            privateVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                    R.color.colorMainBold));
        }

        if (imgStr.equals("undefined") || imgStr.equals("")) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_user_main);
            roomProfileIv.setImageBitmap(((BitmapDrawable) drawable).getBitmap());
        } else {
            String url = ImageManager.getInstance().getFullImageString(imgStr, "groupImage");
            ImageManager.getInstance().GlideInto(AppManager.getInstance().getContext(), roomProfileIv, url);
        }
    }

    private void initListener() {
        confirmDialog.getOkBtn().setOnClickListener(this);
        privateVoiceBtn.setOnClickListener(this);
        publicVoiceBtn.setOnClickListener(this);
        roomProfileIv.setOnClickListener(this);
        settingRoomBtn.setOnClickListener(this);
    }

    private void changePermissionToPublic() {
        voicePermission = Constants.VOICE_PUBLIC;
        publicVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                R.color.colorMainBold));
        privateVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                R.color.colorLightGray));
    }
    private void changePermissionToPrivate() {
        voicePermission = Constants.VOICE_PRIVATE;
        publicVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                R.color.colorLightGray));
        privateVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                R.color.colorMainBold));
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_privateVoice:
                changePermissionToPrivate();
                break;
            case R.id.btn_publicVoice:
                changePermissionToPublic();
                break;
            case R.id.ib_roomProfile:
                doTakeAlbumAction();
                break;
            case R.id.btn_settingRoom:
                setDialogMessage();
                setDialogListener();
                confirmDialog.show();
                break;
            case R.id.btn_ok_dialog:
                if (isTitleSuitable() && isCommentSuitable()) {
                    confirmDialog.dismiss();
                    settingRoomInfo(); //  임시 여기서 서버 호출해서 방 생성
                }
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
        Log.d("sssong:CRFragment", "request Code : " + requestCode);
        Log.d("sssong:CRFragment", "RESULT CODE : " + ((Activity) AppManager.getInstance().getContext()).RESULT_OK);

        if (resultCode == ((Activity) AppManager.getInstance().getContext()).RESULT_OK) {

            if (requestCode == ImageManager.PICK_FROM_ALBUM) {
                //앨범 선택
                Uri uri = data.getData();
                ExifInterface exif = null;
                albumImagePath = ImageManager.getInstance().getRealPathFromURI(AppManager.getInstance().getContext(), uri);
                Log.d("sssong:RSFragment", albumImagePath);

                try {
                    exif = new ExifInterface(albumImagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = ImageManager.getInstance().exifOrientationToDegrees(exifOrientation);
                roomProfileIv.setBackgroundColor(Color.WHITE);
                Bitmap bitmap = BitmapFactory.decodeFile(albumImagePath);
                bitmapTmp = bitmap;
                roomProfileIv.setImageBitmap(ImageManager.getInstance().rotate(bitmap, exifDegree));
            }
        }
    }

    private void setDialogListener() {
        if (!isTitleSuitable() || !isCommentSuitable()) {
            confirmDialog.setOkBtnDismiss();
        } else {
            confirmDialog.getOkBtn().setOnClickListener(this);
        }
    }

    private void setDialogMessage() {
        if (!isTitleSuitable()) {
            confirmDialog.setMessage("방 이름을 입력해주세요.");
        } else if (!isCommentSuitable()) {
            confirmDialog.setMessage("방 소개글을 입력해주세요.");
        } else {
            confirmDialog.setMessage("방 정보를 수정하시겠습니까?");
        }
    }

    private boolean isTitleSuitable() {
        if (roomTitleEt.getText().toString().equals(""))
            return false;
        return true;
    }

    private boolean isCommentSuitable() {
        if (roomCommentEt.getText().toString().equals(""))
            return false;
        return true;
    }

    // 앨범에서 이미지 가져오기
    private void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, ImageManager.PICK_FROM_ALBUM);
    }

    private void settingRoomInfo() {
        progressON("방 수정 중...");

        ContentValues values = new ContentValues();
        values.put("roomID", id);
        values.put("roomName", roomTitleEt.getText().toString());
        values.put("roomText", roomCommentEt.getText().toString());
        values.put("roomPermission", voicePermission);
        values.put("userID", AppManager.getInstance().getUser().getID());

        SettingRoomTask settingRoomTask = new SettingRoomTask(values, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                Log.d("sssong:SRFragment", "onSuccess : setting room");

                if (albumImagePath != null)
                    // 이미지 통신
                    uploadRoomImage();
                else {
                    progressOFF();
                    confirmDialog.dismiss();
                    dismiss();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AppManager.getInstance().getContext(),
                        "error : " + e, Toast.LENGTH_SHORT).show();

                progressOFF();
                dismiss();
            }
        });
        settingRoomTask.execute();
    }

    private void uploadRoomImage() {
        ContentValues values = new ContentValues();
        values.put("roomId", id);
        UploadFile uploadFile = new UploadFile(UploadFile.UPLOAD_IMAGE_ROOM, values,
                albumImagePath, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                progressOFF();
                dismiss(); // dismiss and update
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AppManager.getInstance().getContext(),
                        "error : " + e, Toast.LENGTH_SHORT).show();
                progressOFF();
                dismiss();
            }
        });
        uploadFile.execute();
    }

    public void progressON(String message) {
        ImageManager.getInstance().progressON((Activity)AppManager.getInstance().getContext(), message);
    }
    public void progressOFF() {
        ImageManager.getInstance().progressOFF();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
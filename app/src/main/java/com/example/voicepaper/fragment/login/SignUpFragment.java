package com.example.voicepaper.fragment.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.voicepaper.R;
import com.example.voicepaper.data.User;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.manager.ImageManager;
import com.example.voicepaper.network.AsyncCallback;
import com.example.voicepaper.network.SignUpTask;
import com.example.voicepaper.network.UploadFile;
import com.example.voicepaper.util.ConfirmDialog;

import java.io.IOException;

public class SignUpFragment extends DialogFragment implements Button.OnClickListener, View.OnFocusChangeListener {

    private ScrollView scrollView;

    //image view
    private ImageView iv_userImage;
    //edit text
    private EditText et_name;
    private EditText et_id;
    private EditText et_pw;
    private EditText et_rePw;
    //button
    private Button btn_imageSet;
    private Button btn_signUp;

    private ConfirmDialog confirmDialog;

    private String albumImagePath;

    private Boolean imageSetting;

    private String errorMessage;


    public SignUpFragment() {
    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        imageSetting = false;

        initView(view);
        initListener();

        return view;
    }

    public void initView(View view) {
        scrollView = (ScrollView) view.findViewById(R.id.sv_root);
        //view
        iv_userImage = view.findViewById(R.id.iv_userImage);

        et_name = view.findViewById(R.id.et_name);
        et_id = view.findViewById(R.id.et_id);
        et_pw = view.findViewById(R.id.et_pw);
        et_rePw = view.findViewById(R.id.et_rePw);

        btn_imageSet = view.findViewById(R.id.btn_userIamgeSet);
        btn_signUp = view.findViewById(R.id.btn_signUp);
    }

    public void initListener() {
        //btn listener
        btn_imageSet.setOnClickListener(this);
        btn_signUp.setOnClickListener(this);

        et_name.setOnClickListener(this);
        et_id.setOnClickListener(this);
        et_pw.setOnClickListener(this);
        et_rePw.setOnClickListener(this);

        et_name.setOnFocusChangeListener(this);
        et_id.setOnFocusChangeListener(this);
        et_pw.setOnFocusChangeListener(this);
        et_rePw.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_userIamgeSet:
                doTakeAlbumAction();
                break;
            case R.id.btn_signUp:
                signup();
                break;

            case R.id.et_id:
            case R.id.et_name:
            case R.id.et_pw:
            case R.id.et_rePw:
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, scrollView.getBottom());
                    }
                }, 500);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.et_id:
            case R.id.et_name:
            case R.id.et_pw:
            case R.id.et_rePw:
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
    }

    // 앨범에서 이미지 가져오기
    private void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, ImageManager.PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ((Activity) AppManager.getInstance().getContext()).RESULT_OK) {

            if (requestCode == ImageManager.PICK_FROM_ALBUM) {
                //앨범 선택
                Uri uri = data.getData();
                ExifInterface exif = null;
                albumImagePath = ImageManager.getInstance().getRealPathFromURI(AppManager.getInstance().getContext(), uri);
                try {
                    exif = new ExifInterface(albumImagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = ImageManager.getInstance().exifOrientationToDegrees(exifOrientation);
                iv_userImage.setBackgroundColor(Color.WHITE);
                Bitmap bitmap = BitmapFactory.decodeFile(albumImagePath);
                iv_userImage.setImageBitmap(ImageManager.getInstance().rotate(bitmap, exifDegree));

                GradientDrawable drawable =
                        (GradientDrawable) AppManager.getInstance().getContext().getDrawable(R.drawable.custom_rounded);
                iv_userImage.setBackground(drawable);

                imageSetting = true;
            }
        }
    }

    private void doSignUpTask() {
        ContentValues values = new ContentValues();
        values.put("id", et_id.getText().toString());
        values.put("pw", et_pw.getText().toString());
        values.put("name", et_name.getText().toString());

        SignUpTask signUpTask = new SignUpTask(values, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                uploadUserImage((String)object);
            }

            @Override
            public void onFailure(Exception e) {
                //아이디가 이미 있음.
                confirmDialog.setMessage("동일한 아이디가 존재합니다.");
                confirmDialog.show();
                btn_signUp.setEnabled(true);
                progressOFF();
            }
        });
        signUpTask.execute();
    }

    private void uploadUserImage(String userId) {
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        UploadFile uploadFile = new UploadFile(UploadFile.UPLOAD_IMAGE_USER, values,
                albumImagePath, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                confirmDialog.setMessage("회원가입 완료!");
                confirmDialog.show();
                progressOFF();
                dismiss();
            }

            @Override
            public void onFailure(Exception e) {
                //이미지 업로드 실패시
                confirmDialog.setMessage("이미지 업로드 실패!");
                confirmDialog.show();
                progressOFF();
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

    private boolean checkTextLength(){
        if(et_id.getText().length() < 6){
            errorMessage = "아이디는 6글자 이상 10글자 이하여야합니다.";
            return false;
        }
        else if(et_name.getText().length() < 2){
            errorMessage = "이름은 2글자 이상 10글자 이하여야합니다.";
            return false;
        }
        else if(et_pw.getText().length() < 6){
            errorMessage = "비밀번호는 6글자 이상 12글자 이하여야합니다.";
            return false;
        }

        return true;
    }

    private boolean checkRepassward(){
        if (et_pw.getText().toString().equals(et_rePw.getText().toString()) == false) {
            errorMessage = "비밀번호 확인이 일치하지 않습니다.";
            return false;
        }
        return true;
    }

    private boolean checkExistImage(){
        if(imageSetting == false){
            errorMessage ="사진을 등록해주세요.";
            return false;
        }
        return true;
    }

    private void signup(){
        confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());

        if(checkTextLength() == false){
            confirmDialog.setMessage(errorMessage);
            confirmDialog.show();
            return;
        }

        if(checkRepassward() == false){
            confirmDialog.setMessage(errorMessage);
            confirmDialog.show();
            return;
        }

        if(checkExistImage() == false){
            confirmDialog.setMessage(errorMessage);
            confirmDialog.show();
            return;
        }

        btn_signUp.setEnabled(false);
        progressON("아이디 생성 중...");
        doSignUpTask();
    }
}


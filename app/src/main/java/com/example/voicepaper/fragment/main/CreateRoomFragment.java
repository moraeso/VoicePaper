package com.example.voicepaper.fragment.main;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
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
import android.view.WindowManager;
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
import com.example.voicepaper.activity.MainActivity;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.util.ConfirmDialog;
import com.example.voicepaper.util.Constants;

import java.io.IOException;

public class CreateRoomFragment extends DialogFragment implements View.OnClickListener {

    private ScrollView scrollView;

    private EditText roomTitleEt, roomCommentEt;
    private ImageButton roomProfileIb;
    private Button privateVoiceBtn, publicVoiceBtn, createRoomBtn;
    private Bitmap roomProfile;

    private int voicePermission;

    private Uri roomProfileUri;
    private int exifDegree;

    private ConfirmDialog confirmDialog;

    private static final int PICK_FROM_ALBUM = 1;
    private int inputSuitable;

    private CreateRoomFragment() {
    }

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

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        scrollView = (ScrollView) view.findViewById(R.id.sv_root);

        setCancelable(false);

        roomTitleEt = (EditText) view.findViewById(R.id.et_roomTitle);
        roomProfileIb = (ImageButton) view.findViewById(R.id.ib_roomProfile);
        privateVoiceBtn = (Button) view.findViewById(R.id.btn_privateVoice);
        publicVoiceBtn = (Button) view.findViewById(R.id.btn_publicVoice);
        roomCommentEt = (EditText) view.findViewById(R.id.et_roomComment);
        createRoomBtn = (Button) view.findViewById(R.id.btn_createRoom);

        publicVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                R.color.colorRedPink));
        privateVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                R.color.colorLightGray));

        voicePermission = Constants.VOICE_PUBLIC;

        confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());

        confirmDialog.getOkBtn().setOnClickListener(this);
        privateVoiceBtn.setOnClickListener(this);
        publicVoiceBtn.setOnClickListener(this);
        roomProfileIb.setOnClickListener(this);
        createRoomBtn.setOnClickListener(this);

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

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_privateVoice:
                voicePermission = Constants.VOICE_PRIVATE;
                publicVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                        R.color.colorLightGray));
                privateVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                        R.color.colorRedPink));
                break;
            case R.id.btn_publicVoice:
                voicePermission = Constants.VOICE_PUBLIC;
                publicVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                        R.color.colorRedPink));
                privateVoiceBtn.setBackgroundTintList(ContextCompat.getColorStateList(AppManager.getInstance().getContext(),
                        R.color.colorLightGray));
                break;
            case R.id.ib_roomProfile:
                doTakeAlbumAction();
                break;
            case R.id.btn_createRoom:
                setDialogMessage();
                setDialogListener();
                confirmDialog.show();
                break;
            case R.id.btn_ok_dialog:
                if (isTitleSuitable() && isCommentSuitable()) {
                    addRoomInList(); //  임시 여기서 서버 호출해서 방 생성
                    confirmDialog.dismiss();
                    dismiss();
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
        Log.d("sssong:CRFragment", "requestCode : " + requestCode);
        Log.d("sssong:CRFragment", "RESULTCODE : " + ((Activity) AppManager.getInstance().getContext()).RESULT_OK);

        if (resultCode == ((Activity) AppManager.getInstance().getContext()).RESULT_OK) {

            if (requestCode == PICK_FROM_ALBUM) {
                //앨범 선택
                Uri uri = data.getData();
                roomProfileUri = uri;
                ExifInterface exif = null;
                String imagePath = getRealPathFromURI(uri);
                Log.d("sssong:CRFragment", imagePath);

                try {
                    exif = new ExifInterface(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
                roomProfileIb.setBackgroundColor(Color.WHITE);
                roomProfile = BitmapFactory.decodeFile(imagePath);
                roomProfileIb.setImageBitmap(rotate(roomProfile, exifDegree));
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
            confirmDialog.setMessage("방을 생성하시겠습니까?");
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

    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToFirst();
        column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        Log.d("sssong:getPathFromURI", cursor.getString(column_index));
        return cursor.getString(column_index);
    }

    private Bitmap rotate(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree); // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    // 이미지 각도 조절
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    // 앨범에서 이미지 가져오기
    private void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private void addRoomInList() {

        // 여기서 room 생성 서버 통신

        // Room (int id, String name, Bitmap profileImage, String profileString, String comment, String hostID)

        /*
        Room newRoom = new Room(1, roomNameEt.toString(), roomProfile,
                "image.url", voicePermission, roomCommentEt.toString(),
                AppManager.getInstance().getUser().getID());
*/

        Log.d("sssong:CRFragment", "addRoomInList");

        // 프로필 없을 경우 임시 프로필 추가
        if (roomProfile == null) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_user_main);
            roomProfile = ((BitmapDrawable) drawable).getBitmap();
        }
        // 임시 추가
        Room newRoom = new Room(1, roomTitleEt.getText().toString(), roomProfile,
                "image.url", voicePermission, roomCommentEt.getText().toString(),
                AppManager.getInstance().getUser().getID(), "123");

        AppManager.getInstance().getRoomList().add(newRoom);

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
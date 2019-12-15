package com.example.voicepaper.fragment.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.voicepaper.R;
import com.example.voicepaper.activity.MainActivity;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.manager.ImageManager;
import com.example.voicepaper.network.AsyncCallback;
import com.example.voicepaper.network.InputRoomCodeTask;
import com.example.voicepaper.util.ConfirmDialog;

public class InputRoomCodeFragment extends DialogFragment implements View.OnClickListener {

    Button createRoomBtn, cancelBtn;
    EditText inputRoomCodeEt;

    private ConfirmDialog confirmDialog;


    private InputRoomCodeFragment() {
    }

    public static InputRoomCodeFragment newInstance() {
        InputRoomCodeFragment fragment = new InputRoomCodeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inputroomcode, container, false);

        initView(view);
        initListener();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_createRoom:
                if (inputRoomCodeEt.getText().toString().equals("")) {
                    Toast.makeText(AppManager.getInstance().getContext(), "코드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    createRoomBtn.setEnabled(false);
                    progressON("방 생성 중...");
                    participateRoom();
                }
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    private void initView(View view) {
        createRoomBtn = (Button) view.findViewById(R.id.btn_createRoom);
        cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        inputRoomCodeEt = (EditText) view.findViewById(R.id.et_inputRoomCode);

        confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());

    }

    private void initListener() {
        createRoomBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    private void participateRoom() {
        ContentValues values = new ContentValues();
        //values.put("id", AppManager.getInstance().getUser().getID());
        values.put("userID", AppManager.getInstance().getUser().getID());
        values.put("roomCode", inputRoomCodeEt.getText().toString());

        InputRoomCodeTask inputRoomCodeTask = new InputRoomCodeTask(values, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                confirmDialog.setMessage("방 생성 완료!");
                confirmDialog.setOkBtnDismiss();
                confirmDialog.show();
                AppManager.getInstance().getRoomList().add((Room)object);
                ((MainActivity)AppManager.getInstance().getContext()).showRoomParticipateSuccessDialog();
                dismiss();
                progressOFF();
            }

            @Override
            public void onFailure(Exception e) {
                confirmDialog.setMessage("방 생성 실패!");
                confirmDialog.setOkBtnDismiss();
                confirmDialog.show();
                dismiss();
                progressOFF();
            }
        });
        inputRoomCodeTask.execute();
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

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
}

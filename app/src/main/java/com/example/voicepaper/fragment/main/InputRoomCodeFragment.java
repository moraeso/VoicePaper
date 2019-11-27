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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.voicepaper.R;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.network.AsyncCallback;
import com.example.voicepaper.network.InputRoomCodeTask;

public class InputRoomCodeFragment extends DialogFragment implements View.OnClickListener {

    Button createRoomBtn, cancelBtn;
    EditText inputRoomCodeEt;

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

        createRoomBtn = (Button) view.findViewById(R.id.btn_createRoom);
        cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        inputRoomCodeEt = (EditText) view.findViewById(R.id.et_inputRoomCode);

        createRoomBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_createRoom:



                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    private void participateRoom() {
        ContentValues values = new ContentValues();
        values.put("id", AppManager.getInstance().getUser().getID());
        values.put("roomCode", inputRoomCodeEt.getText().toString());

        InputRoomCodeTask inputRoomCodeTask = new InputRoomCodeTask(values, new AsyncCallback() {
            @Override
            public void onSuccess(Object object) {
                Log.d("sssong:InputRoomFrgmt", "onSuccess : participate room");
                AppManager.getInstance().getRoomList().add((Room)object);
                dismiss();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
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

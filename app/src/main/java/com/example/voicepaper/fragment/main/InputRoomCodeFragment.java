package com.example.voicepaper.fragment.main;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.voicepaper.R;
import com.example.voicepaper.manager.AppManager;

public class InputRoomCodeFragment extends DialogFragment implements View.OnClickListener {

    Button createRoomBtn, cancelBtn;

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

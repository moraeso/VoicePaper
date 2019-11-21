package com.example.voicepaper.fragment.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Layout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.voicepaper.R;
import com.example.voicepaper.manager.AppManager;

public class SignUpFragment extends DialogFragment implements Button.OnClickListener {
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

    public SignUpFragment(){ }

    public static SignUpFragment newInstance(){
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

        //view
        iv_userImage = view.findViewById(R.id.iv_userImage);

        et_name = view.findViewById(R.id.et_name);
        et_id = view.findViewById(R.id.et_id);
        et_pw = view.findViewById(R.id.et_pw);
        et_rePw = view.findViewById(R.id.et_rePw);

        btn_imageSet = view.findViewById(R.id.btn_userIamgeSet);
        btn_signUp = view.findViewById(R.id.btn_signUp);
        //btn listener
        btn_imageSet.setOnClickListener(this);
        btn_signUp.setOnClickListener(this);

        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        getDialog().getWindow().setLayout(1000, 1000);
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_userIamgeSet:
                break;
            case R.id.btn_signUp:
                break;
        }
    }
}


package com.example.voicepaper.fragment.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.voicepaper.R;

public class SignUpFragment extends Fragment implements Button.OnClickListener {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

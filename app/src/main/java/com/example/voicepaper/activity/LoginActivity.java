package com.example.voicepaper.activity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.voicepaper.R;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.network.SignInTask;

public class LoginActivity extends AppCompatActivity implements Button.OnClickListener {
    Button btn_signUp;
    Button btn_signIn;

    EditText et_id;
    EditText et_pw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());
        setContentView(R.layout.activity_login);

        initView();
        initListener();
    }

    void initView(){
        btn_signIn = findViewById(R.id.btn_signIn);
        btn_signUp = findViewById(R.id.btn_signUp);
        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
    }

    void initListener(){
        btn_signIn.setOnClickListener(this);
        btn_signUp.setOnClickListener(this);
        et_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){

                }
                else{

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signIn:
                //아이디 비밀번호를 받아와 서버와 통신
                break;
            case R.id.btn_signUp:
                //회원가입 화면 띄우기
                break;
        }

    }
}

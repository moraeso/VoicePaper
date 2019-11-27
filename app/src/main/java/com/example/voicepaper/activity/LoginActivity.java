package com.example.voicepaper.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voicepaper.R;
import com.example.voicepaper.fragment.login.SignUpFragment;
import com.example.voicepaper.fragment.room.RecordFragment;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.network.AsyncCallback;
import com.example.voicepaper.network.SignInTask;
import com.example.voicepaper.util.ConfirmDialog;

public class LoginActivity extends AppCompatActivity implements Button.OnClickListener, View.OnFocusChangeListener {
    Button btn_signUp;
    Button btn_signIn;
    EditText et_id;
    EditText et_pw;
    ScrollView scrollView;

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
        scrollView = findViewById(R.id.sv_root);
    }

    void initListener(){
        btn_signIn.setOnClickListener(this);
        btn_signUp.setOnClickListener(this);
        et_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        et_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, scrollView.getBottom());
                    }
                }, 200);
            }

        });

        et_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        et_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, scrollView.getBottom());
                    }
                }, 200);
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signIn:
                //아이디 비밀번호를 받아와 서버와 통신
                ContentValues values = new ContentValues();
                values.put("id",et_id.getText().toString());
                values.put("pw",et_pw.getText().toString());

                //로그인 통신
                SignInTask signInTask = new SignInTask(values, new AsyncCallback(){
                    @Override
                    public void onSuccess(Object object) {
                        Intent intent = new Intent(AppManager.getInstance().getContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        ConfirmDialog confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());
                        confirmDialog.setMessage("로그인 실패");
                        //confirmDialog.getOkBtn().setOnClickListener(); -> ok버튼 클릭시 뭔가 하고 싶으면 이거 하십쇼
                    }
                });

                signInTask.execute();
                //RecordFragment recordFragment = RecordFragment.newInstance();
                //recordFragment.show(getSupportFragmentManager(),null);

//                Intent intent = new Intent(AppManager.getInstance().getContext(),MainActivity.class);
//                startActivity(intent);
//                finish();

                break;
            case R.id.btn_signUp:
                //회원가입 화면 띄우기
                SignUpFragment signUpFragment = SignUpFragment.newInstance();
                signUpFragment.show(getSupportFragmentManager(),null);
                break;
        }

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        //리스너뺄지 고민중
    }
}

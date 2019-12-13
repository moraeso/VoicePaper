package com.example.voicepaper.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.voicepaper.R;
import com.example.voicepaper.fragment.login.SignUpFragment;
import com.example.voicepaper.fragment.room.RecordFragment;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.manager.ImageManager;
import com.example.voicepaper.network.AsyncCallback;
import com.example.voicepaper.network.SignInTask;
import com.example.voicepaper.util.ConfirmDialog;

public class LoginActivity extends AppCompatActivity implements Button.OnClickListener, View.OnFocusChangeListener {
    Button btn_signUp;
    Button btn_signIn;
    EditText et_id;
    EditText et_pw;
    ScrollView scrollView;

    ConfirmDialog confirmDialog;

    private final static int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getInstance().setContext(this);
        AppManager.getInstance().setResources(getResources());
        setContentView(R.layout.activity_login);

        initView();
        initListener();

        checkPermission(); // 갤러리 접근 권한
    }

    void initView(){
        btn_signIn = findViewById(R.id.btn_signIn);
        btn_signUp = findViewById(R.id.btn_signUp);
        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        scrollView = findViewById(R.id.sv_root);

        confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());
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

    private void loginTask() {
        final ConfirmDialog confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());

        ContentValues values = new ContentValues();
        values.put("id",et_id.getText().toString());
        values.put("pw",et_pw.getText().toString());

        //로그인 통신
        SignInTask signInTask = new SignInTask(values, new AsyncCallback(){
            @Override
            public void onSuccess(Object object) {
                progressOFF();
                Intent intent = new Intent(AppManager.getInstance().getContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                progressOFF();
                confirmDialog.setMessage("로그인 실패");
                //confirmDialog.getOkBtn().setOnClickListener(); -> ok버튼 클릭시 뭔가 하고 싶으면 이거 하십쇼
            }
        });
        signInTask.execute();
    }

    private boolean isLoginDataEmpty() {
        return et_id.getText().toString().length() == 0 || et_pw.getText().toString().length() == 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signIn:
                //아이디 비밀번호를 받아와 서버와 통신
                if (isLoginDataEmpty()) {
                    confirmDialog.setMessage("아이디 혹은 비밀번호를 확인해주세요");
                    confirmDialog.show();
                    break;
                }

                progressON("로그인 중...");
                loginTask();

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

    public void progressON(String message) {
        ImageManager.getInstance().progressON((Activity)AppManager.getInstance().getContext(), message);
    }
    public void progressOFF() {
        ImageManager.getInstance().progressOFF();
    }

    //갤러리 접근 권한 설정
    private void checkPermission() {

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_READ_EXTERNAL_STORAGE);

            Log.v("갤러리 권한", "갤러리 사용을 위해 권한이 필요합니다.");
        }

    }
}

package com.example.voicepaper.fragment.setting;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.voicepaper.R;
import com.example.voicepaper.fragment.login.SignUpFragment;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.network.AsyncCallback;
import com.example.voicepaper.network.ChangePasswordTask;
import com.example.voicepaper.util.ConfirmDialog;

public class PasswordSettingFragment extends DialogFragment implements Button.OnClickListener {

    private Button btn_changePW;
    private Button btn_cancel;

    private EditText et_CurrentPassword;
    private EditText et_ChangePassword;
    private EditText et_reChangePassword;

    private String confirmString;

    public PasswordSettingFragment() {
    }

    public static PasswordSettingFragment newInstance() {
        PasswordSettingFragment fragment = new PasswordSettingFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passwordsetting, container, false);

        initView(view);
        initListener();

        return view;
    }

    void initView(View view) {
        et_CurrentPassword = view.findViewById(R.id.et_CurrentPassword);
        et_ChangePassword = view.findViewById(R.id.et_ChangePassword);
        et_reChangePassword = view.findViewById(R.id.et_reChangePassword);

        btn_changePW = view.findViewById(R.id.btn_changePW);
        btn_cancel = view.findViewById(R.id.btn_cancel);
    }

    void initListener() {
        btn_cancel.setOnClickListener(this);
        btn_changePW.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_changePW:
                changePassword();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    void changePassword() {
        final ConfirmDialog confirmDialog = new ConfirmDialog(AppManager.getInstance().getContext());

        if (checkCurrentPassword() == true &&
                checkNewPassword() == true &&
                checkTextLength()) {
            ContentValues values = new ContentValues();
            values.put("userID", AppManager.getInstance().getUser().getID());
            values.put("oldPW", et_CurrentPassword.getText().toString());
            values.put("newPW", et_ChangePassword.getText().toString());

            ChangePasswordTask changePasswordTask = new ChangePasswordTask(values, new AsyncCallback() {
                @Override
                public void onSuccess(Object object) {
                    confirmDialog.setMessage("비밀번호가 변경되었습니다.");
                    confirmDialog.show();
                    dismiss();
                }

                @Override
                public void onFailure(Exception e) {
                    confirmDialog.setMessage("비밀번호 변경을 실패하였습니다.");
                    confirmDialog.show();
                }
            });
            changePasswordTask.execute();
        } else {
            confirmDialog.setMessage(confirmString);
            confirmDialog.show();
        }
    }

    Boolean checkNewPassword() {
        if (et_CurrentPassword.getText().toString().equals(et_ChangePassword.getText().toString())) {
            confirmString = "새 비밀번호가 기존 비밀번호와 일치합니다.";
            return false;
        } else {
            return true;
        }
    }

    Boolean checkCurrentPassword() {
        if (et_ChangePassword.getText().toString().equals(et_reChangePassword.getText().toString())) {
            return true;
        } else {
            confirmString = "새 비밀번호 입력이 서로 일치하지 않습니다.";
            return false;
        }
    }

    private boolean checkTextLength() {
        if (et_CurrentPassword.getText().length() < 6) {
            confirmString = "비밀번호는 6자 이상 12자 이하입니다.";
            return false;
        } else if (et_ChangePassword.getText().length() < 2) {
            confirmString = "새 비밀번호는 6자 이상 12자 이하로 작성해주세요.";
            return false;
        }
        return true;
    }
}

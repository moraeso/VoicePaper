package com.example.voicepaper.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import com.example.voicepaper.R;

public class ConfirmDialog extends Dialog implements View.OnClickListener {

    TextView messageTv;
    Button okBtn, cancelBtn;

    public ConfirmDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setContentView(R.layout.dialog_confirm);     //다이얼로그에서 사용할 레이아웃입니다.

        messageTv = (TextView) findViewById(R.id.tv_message_dialog);

        okBtn = (Button) findViewById(R.id.btn_ok_dialog);
        okBtn.setOnClickListener(this);

        cancelBtn = (Button) findViewById(R.id.btn_cancel_dialog);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_ok_dialog:
                dismiss();
                break;
            case R.id.btn_cancel_dialog:
                dismiss();
                break;
        }
    }

    public Button getOkBtn() {
        return okBtn;
    }

    public void setMessage(String message) {
        messageTv.setText(message);
    }

    public void setOkBtnDismiss() {
        okBtn.setOnClickListener(this);
    }
}

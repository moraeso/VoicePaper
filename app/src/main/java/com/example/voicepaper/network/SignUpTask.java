package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.example.voicepaper.manager.AppManager;

import org.json.JSONObject;

public class SignUpTask extends AsyncTask<Void, Boolean, Boolean> {
    String url;
    ContentValues values;
    AsyncCallback asyncCallback;
    private Exception exception;

    public SignUpTask(ContentValues values, AsyncCallback asyncCallback){
        this.url = "db";
        this.values = values;
        this.asyncCallback = asyncCallback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String result;

        try {
            HttpConnection requestHttpConnection = new HttpConnection();
            result = requestHttpConnection.request(url, values); // post token

            JSONObject job = new JSONObject(result);
            int code = job.getInt("code");

            if (!isSignUpDataValid(code)) {
                throw new Exception("SignUp data is not valid");
            }
            // 토큰 설정
            AppManager.getInstance().getUser().setToken(job.getString("token"));
            // 유저 ID, Password 설정
            AppManager.getInstance().getUser().setID(values.getAsString("id"));
            AppManager.getInstance().getUser().setPw(values.getAsString("pw"));

        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
            return false;
        }

        return true; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if(result == true){
            asyncCallback.onSuccess(true);
        }else{
            asyncCallback.onFailure(exception);
        }
    }

    private boolean isSignUpDataValid(int code) {
        // 성공 : 200, 실패 : 204
        if (code == 204) {
            return false;
        } else if (code == 200) {
            return true;
        }
        return false;
    }//수정 필요
}
package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.example.voicepaper.data.User;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.util.Constants;

import org.json.JSONObject;

public class SignUpTask extends AsyncTask<Void, Boolean, Boolean> {
    String url;
    ContentValues values;
    AsyncCallback asyncCallback;
    private Exception exception;

    User newUser;

    public SignUpTask(ContentValues values, AsyncCallback asyncCallback){
        this.url = Constants.URL+ "/auth/register";
        this.values = values;
        this.asyncCallback = asyncCallback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String result;

        try {
            HttpConnection requestHttpConnection = new HttpConnection();
            result = requestHttpConnection.request(url, values);

            JSONObject job = new JSONObject(result);
            int code = job.getInt("code");
            Log.d("smh:signUp",""+code);
            if (!isSignUpDataValid(code)) {
                throw new Exception("SignUp data is not valid");
            }

            newUser = new User();
            newUser.setID(values.getAsString("id"));
            newUser.setPw(values.getAsString("pw"));

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
            asyncCallback.onSuccess(newUser);
        }else{
            asyncCallback.onFailure(exception);
        }
    }

    private boolean isSignUpDataValid(int code) {
        // 성공 : 100, 실패 : 101
        if (code == 100) {
            return true;
        } else {
            return false;
        }
    }//수정 필요
}
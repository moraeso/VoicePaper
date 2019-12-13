package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.example.voicepaper.data.User;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.util.Constants;

import org.json.JSONObject;

public class SignUpTask extends AsyncTask<Void, Boolean, String> {
    private String url;
    private ContentValues values;
    private AsyncCallback asyncCallback;
    private Exception exception;

    private static final int SUCCESS = 200;

    public SignUpTask(ContentValues values, AsyncCallback asyncCallback){
        this.url = Constants.URL+ "/user/register";
        this.values = values;
        this.asyncCallback = asyncCallback;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result;

        try {
            HttpConnection requestHttpConnection = new HttpConnection();
            result = requestHttpConnection.request(url, values);

            JSONObject job = new JSONObject(result);
            int code = job.getInt("code");

            if (!isSignUpDataValid(code)) {
                throw new Exception("SignUp data is not valid");
            }

            return values.get("id").toString();
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
            return null;
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if(result != null){
            asyncCallback.onSuccess(result);
        }else{
            asyncCallback.onFailure(exception);
        }
    }

    private boolean isSignUpDataValid(int code) {
        if (code == SUCCESS) {
            return true;
        } else {
            return false;
        }
    }//수정 필요
}
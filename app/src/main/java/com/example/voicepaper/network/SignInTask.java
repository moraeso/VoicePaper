package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.example.voicepaper.manager.AppManager;

import org.json.JSONObject;

public class SignInTask extends AsyncTask<Void, Boolean, Boolean> {
    String url;
    ContentValues values;
    AsyncCallback asyncCallback;
    private Exception exception;

    public SignInTask(String url, ContentValues values, AsyncCallback asyncCallback){
        this.url = url;
        this.values = values;
        this.asyncCallback = asyncCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //실행 전에 할 행동들.
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String result;

        // MyInfo에 토큰 설정
        try {
            HttpConnection requestHttpConnection = new HttpConnection();
            result = requestHttpConnection.request(url, values); // post token

            if (!isSignUpDataValid(result)) {
                throw new Exception("SignUp data is not valid");
            }

            // 토큰 설정
            JSONObject job = new JSONObject(result);
            String token = job.getString("token");
            AppManager.getInstance().getUser().setToken(token);

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

    private boolean isSignUpDataValid(String json_str) {
        // 성공 : 200, 실패 : 204
        try {
            JSONObject jsonObj = new JSONObject(json_str);

            int code = jsonObj.getInt("code");

            if (code == 204) {
                return false;
            } else if (code == 200) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
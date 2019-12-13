package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.example.voicepaper.data.Room;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DeleteVoiceTask extends AsyncTask<Void, Boolean, Boolean> {
    String url;
    ContentValues values;
    AsyncCallback asyncCallback;
    private Exception exception;

    public DeleteVoiceTask(ContentValues values, AsyncCallback asyncCallback){
        this.url = Constants.URL + "/auth/login";
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

            if (!isSignInDataValid(code)) {
                throw new Exception("Signin data is not valid");
            }

        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
            return false;
        }

        return true;
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

    private boolean isSignInDataValid(int code) {
        // 성공 : 200, 실패 : 204
        if(code == 200){
            return true;
        }
        else{
            return false;
        }
    }
}

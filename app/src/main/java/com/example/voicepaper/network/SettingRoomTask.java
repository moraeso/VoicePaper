package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.example.voicepaper.data.Room;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.util.Constants;

import org.json.JSONObject;

import java.util.ArrayList;

public class SettingRoomTask extends AsyncTask<Void, Void, Void> {

    ContentValues roomSettingValues;

    private AsyncCallback callback;
    private Exception exception;
    String url;
    ContentValues values;

    public SettingRoomTask(ContentValues values, AsyncCallback callback) {
        this.callback = callback;
        this.url = Constants.URL + "/room/roomSetting";
        this.values = values;
        roomSettingValues = new ContentValues();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... voids) {
        String result;

        try {
            HttpConnection requestHttpURLConnection = new HttpConnection();
            result = requestHttpURLConnection.request(url, values); // post token

            if (!isConnectionSuccess(result)) {
                throw new Exception("Setting room failed");
            }
            settingRoomFromJson(result);
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }

        return null; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (callback != null && exception == null) {
            callback.onSuccess(roomSettingValues);
        } else {
            callback.onFailure(exception);
        }
    }

    private boolean isConnectionSuccess(String json_str) {
        // 성공 : 200, 실패 :
        /*
        room setting success : 300
         */
        try {
            JSONObject jsonObj = new JSONObject(json_str);

            int code = jsonObj.getInt("code");

            if (code == 200) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }
        return true;
    }

    private void settingRoomFromJson(String json_str) {

        try {
            JSONObject jsonObj = new JSONObject(json_str);

            roomSettingValues.put("title", jsonObj.getString("roomName"));
            roomSettingValues.put("comment", jsonObj.getString("roomText"));
            roomSettingValues.put("permission", jsonObj.getInt("roomPermission"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

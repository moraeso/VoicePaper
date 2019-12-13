package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.example.voicepaper.util.Constants;

import org.json.JSONObject;

public class SettingRoomTask extends AsyncTask<Void, Void, Void> {

    ContentValues roomSettingValues;

    private AsyncCallback callback;
    private Exception exception;
    String url;
    ContentValues values;

    int code;

    public static final int SUCCESS = 200;
    public static final int NOT_CHANGED = 601;

    public SettingRoomTask(ContentValues values, AsyncCallback callback) {
        this.callback = callback;
        this.url = Constants.URL + "/room/roomSetting";
        this.values = values;
        roomSettingValues = new ContentValues();
        roomSettingValues = null;
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
            JSONObject jsonObj = new JSONObject(result);

            if (!isConnectionSuccess(jsonObj)) {
                throw new Exception("Setting room failed");
            }

            if (code == SUCCESS)
                settingRoomFromJson(jsonObj);

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

    private boolean isConnectionSuccess(JSONObject json_str) {
        try {
            code = json_str.getInt("code");
            if (code == SUCCESS || code == NOT_CHANGED) {
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }
        return true;
    }

    private void settingRoomFromJson(JSONObject jsonObj) {

        try {
            roomSettingValues.put("title", jsonObj.getString("roomName"));
            roomSettingValues.put("comment", jsonObj.getString("roomText"));
            roomSettingValues.put("permission", jsonObj.getInt("roomPermission"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

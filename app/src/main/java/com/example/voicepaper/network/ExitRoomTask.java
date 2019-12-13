package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.example.voicepaper.util.Constants;

import org.json.JSONObject;

public class ExitRoomTask extends AsyncTask<Void, Void, Integer> {

    private AsyncCallback callback;
    private Exception exception;
    String url;
    ContentValues values;

    public static final int SUCCESS = 200;

    public ExitRoomTask(ContentValues values, AsyncCallback callback) {
        this.callback = callback;
        this.url = Constants.URL + "/member/leave";
        this.values = values;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        String result;

        try {
            HttpConnection requestHttpURLConnection = new HttpConnection();
            result = requestHttpURLConnection.request(url, values);

            if (!isConnectionSuccess(result)) {
                throw new Exception("Exit room failed");
            }

            return values.getAsInteger("roomPos");
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer roomId) {
        super.onPostExecute(roomId);
        if (callback != null && exception == null) {
            callback.onSuccess(roomId);
        } else {
            callback.onFailure(exception);
        }
    }

    private boolean isConnectionSuccess(String json_str) {
        try {
            JSONObject jsonObj = new JSONObject(json_str);

            int code = jsonObj.getInt("code");

            if (code == SUCCESS) {
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
}

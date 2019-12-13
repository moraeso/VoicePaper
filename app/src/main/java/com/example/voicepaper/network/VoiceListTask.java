package com.example.voicepaper.network;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.voicepaper.data.Voice;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VoiceListTask extends AsyncTask<Void,Void,ArrayList<Voice>> {
    private ContentValues values;
    private AsyncCallback callback;
    private Exception exception;
    private String url;

    static private final int SUCCESS = 200;

    public VoiceListTask(ContentValues values, AsyncCallback callback) {
        this.url = Constants.URL +"/file/voicelist";
        this.values = values;
        this.callback = callback;
    }

    @Override
    protected ArrayList<Voice> doInBackground(Void... params) {
        String result;
        ArrayList<Voice> arrayList;
        try {
            HttpConnection requestHttpConnection = new HttpConnection();
            result = requestHttpConnection.request(url, values); // post token

            if (result == null)
                return null;

            JSONObject jsonObject= new JSONObject(result);
            int code = jsonObject.getInt("code");

            if (!isSignInDataValid(code)) {
                throw new Exception("Voice data is not valid");
            }

            arrayList = makeVoiceList(jsonObject.getString("voiceList"));
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Voice> result) {
        super.onPostExecute(result);
        if(result == null || result.size() != 0){
            callback.onSuccess(result);
        } else {
            callback.onFailure(exception);
        }
    }

    private boolean isSignInDataValid(int code) {
        // 성공 : 200, 실패 : 204
        if (code == SUCCESS) {
            return true;
        } else {
            return false;
        }
    }

    private ArrayList makeVoiceList(String _jsonArray){
        ArrayList<Voice> voiceArrayList = new ArrayList<Voice>();
        Log.d("smh:json",_jsonArray);
        try {
            JSONArray jsonArray = new JSONArray(_jsonArray);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Voice voice = new Voice(
                        jsonObject.getInt("fileID"),
                        jsonObject.getString("userID"),
                        jsonObject.getString("userName"),
                        jsonObject.getInt("roomID"),
                        jsonObject.getString("filePath")
                );

                voiceArrayList.add(voice);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return voiceArrayList;
    }
}

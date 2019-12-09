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

    public VoiceListTask(ContentValues values, AsyncCallback callback) {
        this.url = Constants.URL +"/room/voicedatalist";
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

            //방 입장하면, 음성을 기록한 사람들에 대한 리스트 받잖아
            //이 리스트를 받아와서 어디서 add를 해줘야하냐?
            //통신만하면 끝 테스크안에서, 리스트를 만들어, 리스트를 남겨준다
            //json 파일 -> 리스트 변환

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
        if (code == 200) {
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

//----------여기 부분 이상하니 서버랑 이야기 해봐야함 이름이 너무 이상함, 유저 이름을 안넘겨줌
//                private int id;
//                private String userId;
//                private String userName;
//                private int roomId;
//                private String voiceFile;

//                        "fileID": 38,
//                        "userID": "1234",
//                        "roomID": 79,
//                        "filePath": "./voice/test.mp3",
//                        "recordedTime": null
                Voice voice = new Voice(
                        jsonObject.getInt("fileID"),
                        jsonObject.getString("userID"),
                        jsonObject.getString("userID"),
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

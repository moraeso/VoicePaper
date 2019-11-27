package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.example.voicepaper.data.Voice;
import com.example.voicepaper.manager.AppManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VoiceListTask extends AsyncTask<Void,Boolean,Boolean> {
    private ContentValues values;
    private AsyncCallback callback;
    private Exception exception;
    private String url;

    public VoiceListTask(ContentValues values, AsyncCallback callback, Exception exception) {
        this.url = "15011066.iptime.org:8000/voicedatalist";
        this.values = values;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String result;

        try {
            HttpConnection requestHttpConnection = new HttpConnection();
            result = requestHttpConnection.request(url, values); // post token

            if (!isSignInDataValid(result)) {
                throw new Exception("SignUp data is not valid");
            }

            //방 입장하면, 음성을 기록한 사람들에 대한 리스트 받잖아
            //이 리스트를 받아와서 어디서 add를 해줘야하냐?
            //통신만하면 끝 테스크안에서, 리스트를 만들어, 리스트를 남겨준다
            //json 파일 -> 리스트 변환

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
            callback.onSuccess(true);
        }else{
            callback.onFailure(exception);
        }
    }

    private boolean isSignInDataValid(String json_str) {
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

    private ArrayList makeVoiceList(String _jsonArray){
        ArrayList<Voice> voiceArrayList = new ArrayList<Voice>();

        try {
            JSONArray jsonArray = new JSONArray(_jsonArray);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

//                private int id;
//                private int userId;
//                private String userName;
//                private int roomId;
//                private String voiceFile;

                Voice voice = new Voice(
                        jsonObject.getInt("id"),
                        jsonObject.getInt("userId"),
                        jsonObject.getString("userName"),
                        jsonObject.getInt("roomId"),
                        jsonObject.getString("voiceFile")
                );
                voiceArrayList.add(voice);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return voiceArrayList;
    }

}

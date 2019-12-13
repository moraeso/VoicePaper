package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.example.voicepaper.data.RoomMember;
import com.example.voicepaper.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomMemberTask extends AsyncTask<Void, Void, Void> {

    private ArrayList<RoomMember> roomMemberList;

    private String url;
    private ContentValues values;
    private AsyncCallback callback;
    private Exception exception;

    public static final int SUCCESS_CODE = 200;

    public RoomMemberTask(ContentValues values, AsyncCallback asyncCallback) {
        this.url = Constants.URL + "/member/list";
        this.values = values;
        this.callback = asyncCallback;

        roomMemberList = new ArrayList<>();
    }


    @Override
    protected Void doInBackground(Void... voids) {
        String result;

        try {
            HttpConnection requestHttpConnection = new HttpConnection();
            result = requestHttpConnection.request(url, values);

            if (!isConnectionSuccess(result)) {
                throw new Exception("Participate room failed");
            }

            loadRoomMemberFromJson(new JSONObject(result).getString("memberlist"));
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }
        return null;
    }

    private boolean isConnectionSuccess(String json_str) {
        try {
            JSONObject jsonObj = new JSONObject(json_str);

            int code = jsonObj.getInt("code");

            if (code == SUCCESS_CODE) {
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

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (callback != null && exception == null) {
            callback.onSuccess(roomMemberList);
        } else {
            callback.onFailure(exception);
        }
    }

    private void loadRoomMemberFromJson(String json_str) {

        try {
            JSONArray jsonArray = new JSONArray(json_str);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                String userId = jsonObj.getString("userID");
                String userName = jsonObj.getString("userName");

                Log.d("sssong:RoomMemberTask","id : " + userId + " / name : " + userName);

                roomMemberList.add(new RoomMember(userId, userName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

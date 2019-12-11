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

    public RoomMemberTask(ContentValues values, AsyncCallback asyncCallback) {
        this.url = Constants.URL + "/room/memberlist";
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

            loadRoomMemberFromJson(new JSONObject(result).getString("memberlist"));
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }
        return null;
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

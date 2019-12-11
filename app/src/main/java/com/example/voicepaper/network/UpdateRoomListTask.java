package com.example.voicepaper.network;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.example.voicepaper.data.Room;
import com.example.voicepaper.manager.ImageManager;
import com.example.voicepaper.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class UpdateRoomListTask extends AsyncTask<Void, Void, Void> {

    private ArrayList<Room> updatedRoomList;

    private AsyncCallback callback;
    private Exception exception;
    private String url;
    private ContentValues values;

    public UpdateRoomListTask(ContentValues values, AsyncCallback callback) {
        this.callback = callback;
        this.url = Constants.URL + "/room/grouplist";
        this.values = values;

        updatedRoomList = new ArrayList<>();
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
                throw new Exception("Participate room failed");
            }

            updateUserRoomListFromJson(new JSONObject(result).getString("roomList"));
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
            callback.onSuccess(updatedRoomList);
        } else {
            callback.onFailure(exception);
        }
    }

    private boolean isConnectionSuccess(String json_str) {
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
        }
        return true;
    }

    private void updateUserRoomListFromJson(String json_str) {
        try {
            JSONArray jsonArray = new JSONArray(json_str);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                int roomId = jsonObj.getInt("roomID");
                String roomName = jsonObj.getString("roomName");
                String roomProfileString = jsonObj.getString("roomImage");
                String roomComment = jsonObj.getString("roomText");
                int roomPermission = jsonObj.getInt("roomPermission");
                String hostId = jsonObj.getString("hostID");
                String roomCode = jsonObj.getString("roomCode");

                Room item = new Room(roomId, roomName, roomPermission, roomComment, hostId, roomCode);
                item.setProfileString(roomProfileString);

                updatedRoomList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

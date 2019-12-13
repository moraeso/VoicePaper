package com.example.voicepaper.network;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.example.voicepaper.data.Room;
import com.example.voicepaper.util.Constants;


import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CreateRoomTask extends AsyncTask<Void, Void, Void> {

    private Room createdRoom;

    private AsyncCallback callback;
    private Exception exception;
    String url;
    ContentValues values;

    public static final int SUCCESS = 200;
    public static final int NO_USER_ID_FOR_HOST = 301;
    public static final int MAXIMUM_ROOM_NUMBER_LIMIT = 302;
    public static final int MAXIMUM_ROOM_NUMBER_FOR_USER_LIMIT = 303;
    public static final int ROOM_CREATE_ERROR = 304;

    public CreateRoomTask(ContentValues values, AsyncCallback callback) {
        this.callback = callback;
        this.url = Constants.URL + "/room/create";
        this.values = values;
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
            JSONObject jobResult = new JSONObject(result);

            if (!isConnectionSuccess(jobResult.getInt("code"))) {
                throw new Exception("Create room failed");
            }
            createRoomFromJson(jobResult);
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
            callback.onSuccess(createdRoom);
        } else {
            callback.onFailure(exception);
        }
    }

    private boolean isConnectionSuccess(int code) {

        if (code == SUCCESS) {
            return true;
        } else {
            return false;
        }
    }

    private void createRoomFromJson(JSONObject jsonObj) {

        try {
            int roomId = jsonObj.getInt("roomID");
            String roomCode = jsonObj.getString("roomCode");
            String roomName = jsonObj.getString("roomName");
            String roomComment = jsonObj.getString("roomText");
            int roomPermission = jsonObj.getInt("roomPermission");
            String userId = jsonObj.getString("userID");

            createdRoom = new Room(roomId, roomName, roomPermission, roomComment, userId, roomCode);

            //int loadPercent = (int)((i + 1) / (float)jsonArray.length() * 100.0f);
            //MountManager.getInstance().setLoadPercent(loadPercent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

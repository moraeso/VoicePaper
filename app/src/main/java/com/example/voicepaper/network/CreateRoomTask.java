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

    public CreateRoomTask(ContentValues values, AsyncCallback callback) {
        this.callback = callback;
        this.url = Constants.URL + "/room/roomCreate";
        this.values = values;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... voids) {
        String result;

        // MyInfo에 토큰 설정
        try {
            HttpConnection requestHttpURLConnection = new HttpConnection();
            result = requestHttpURLConnection.request(url, values); // post token

            if (!isConnectionSuccess(result)) {
                throw new Exception("Init room failed");
            }
            createRoomFromJson(result);
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

    private boolean isConnectionSuccess(String json_str) {
        // 성공 : 300, 실패 : 301, 302, 303, 304
        /*
        room create success : 300
        no user id for host : 301
        maximum room number limit error : 302
        maximum room number for user limit error : 303
        room create error : 304
         */
        try {
            JSONObject jsonObj = new JSONObject(json_str);

            int code = jsonObj.getInt("code");

            if (code == 300) {
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

    public Bitmap getBitmapFromURL(String url, String srcName) {
        InputStream is;
        Drawable drawable = null;
        Bitmap bitmap = null;

        try {
            is = (InputStream) new URL(url).getContent();
            drawable = Drawable.createFromStream(is, srcName);
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void createRoomFromJson(String json_str) {

        try {
            JSONObject jsonObj = new JSONObject(json_str);

            int roomId = jsonObj.getInt("roomID");
            String roomCode = jsonObj.getString("roomCode");
            String roomName = jsonObj.getString("roomName");
            String roomComment = jsonObj.getString("roomText");
            int roomPermission = jsonObj.getInt("roomPermission");
            String hostId = jsonObj.getString("hostID");

            createdRoom = new Room(roomId, roomName, roomPermission, roomComment, hostId, roomCode);

            //int loadPercent = (int)((i + 1) / (float)jsonArray.length() * 100.0f);
            //MountManager.getInstance().setLoadPercent(loadPercent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

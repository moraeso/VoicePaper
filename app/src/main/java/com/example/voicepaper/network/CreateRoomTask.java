package com.example.voicepaper.network;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.example.voicepaper.data.Room;
import com.example.voicepaper.manager.AppManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class CreateRoomTask extends AsyncTask<Void, Void, Void> {

    /*
    - send : roomName, roomImage, roomText, roomPermission, hostID
    - get

	i) 성공 : success code, roomID, roomCode, roomName, roomImage, roomText, roomPermission, hostID
	ii) 실패 : failure code
     */

    private AsyncCallback callback;
    private Exception exception;
    String url;
    ContentValues values;

    public CreateRoomTask(String url, ContentValues values, AsyncCallback callback) {
        this.callback = callback;
        this.url = url;
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

            createNewRoomFromJson(result);

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
            callback.onSuccess(true);
        } else {
            callback.onFailure(exception);
        }
    }

    private boolean isConnectionSuccess(String json_str) {
        // 성공 : 300, 실패 : 301, 302, 303, 304
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

    private void createNewRoomFromJson(String json_str) {

        try {
            JSONArray jsonArray = new JSONArray(json_str);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObj = jsonArray.getJSONObject(i);

                int roomId = jsonObj.getInt("roomID");
                String roomCode = jsonObj.getString("roomCode");
                String roomName = jsonObj.getString("roomName");
                String roomProfileUrl = jsonObj.getString("roomProfile");
                String roomComment = jsonObj.getString("roomText");
                int roomPermission = jsonObj.getInt("roomPermission");
                String hostId = jsonObj.getString("hostID");

                Room newItem = new Room(roomId, roomName, null, roomProfileUrl, roomPermission, roomComment, hostId, roomCode);

                //newItem.setProfileImage(getBitmapFromURL(roomProfileUrl, "room" + (i + 1)));
                //Log.d("sssong:CreateRoomTask", "get room resource " + (i + 1));

                AppManager.getInstance().getRoomList().add(newItem);

                //int loadPercent = (int)((i + 1) / (float)jsonArray.length() * 100.0f);
                //MountManager.getInstance().setLoadPercent(loadPercent);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

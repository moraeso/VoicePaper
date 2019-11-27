package com.example.voicepaper.network;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.example.voicepaper.data.Room;
import com.example.voicepaper.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class UpdateRoom extends AsyncTask<Void, Void, Void> {

    private ArrayList<Room> updatedRoomList;

    private AsyncCallback callback;
    private Exception exception;
    String url;
    ContentValues values;

    public UpdateRoom(ContentValues values, AsyncCallback callback) {
        this.callback = callback;
        this.url = Constants.URL + "/grouplist";
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
            updateUserRoomListFromJson(result);
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
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

    private void updateUserRoomListFromJson(String json_str) {
        try {
            JSONArray jsonArray = new JSONArray(json_str);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = new JSONObject(json_str);

                int roomId = jsonObj.getInt("roomID");
                String roomName = jsonObj.getString("roomName");
                String roomProfileString = jsonObj.getString("roomImage");
                String roomComment = jsonObj.getString("roomText");
                int roomPermission = jsonObj.getInt("roomPermission");
                String hostId = jsonObj.getString("hostID");
                String roomCode = jsonObj.getString("roomCode");

                Room item = new Room(roomId, roomName, roomPermission, roomComment, hostId, roomCode);
                item.setProfileString(roomProfileString);
                item.setProfileImage(getBitmapFromURL(roomProfileString,"room" + (i + 1)));

                updatedRoomList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

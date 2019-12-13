package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.example.voicepaper.data.Room;
import com.example.voicepaper.util.Constants;

import org.json.JSONObject;

public class RoomTask extends AsyncTask<Void, Void, Void> {
    private Room room;

    private String url;
    private ContentValues values;
    private AsyncCallback callback;
    private Exception exception;

    public static final int SUCCESS = 200;

    public RoomTask(ContentValues values, AsyncCallback asyncCallback){
        this.url = Constants.URL + "/room/info";
        this.values = values;
        this.callback = asyncCallback;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String result;

        try {
            HttpConnection requestHttpConnection = new HttpConnection();
            result = requestHttpConnection.request(url, values); // post token

            if (!isConnectionSuccess(result)) {
                throw new Exception("Room load failed");
            }

            loadRoomInfoFromJson(new JSONObject(result).getString("roomInfo"));
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
            callback.onSuccess(room);
        } else {
            callback.onFailure(exception);
        }
    }

    private boolean isConnectionSuccess(String json_str) {
        try {
            JSONObject jsonObj = new JSONObject(json_str);

            int code = jsonObj.getInt("code");

            if (code == SUCCESS) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void loadRoomInfoFromJson(String json_str) {
        try {
            JSONObject jsonObj = new JSONObject(json_str);

            int roomId = jsonObj.getInt("roomID");
            //int roomId = (int)values.get("roomID");
            String roomName = jsonObj.getString("roomName");
            String roomProfileString = jsonObj.getString("roomImage");
            String roomComment = jsonObj.getString("roomText");
            int roomPermission = jsonObj.getInt("roomPermission");
            String hostId = jsonObj.getString("hostID");
            String roomCode = jsonObj.getString("roomCode");

            room = new Room(roomId, roomName, roomPermission, roomComment, hostId, roomCode);
            room.setProfileString(roomProfileString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

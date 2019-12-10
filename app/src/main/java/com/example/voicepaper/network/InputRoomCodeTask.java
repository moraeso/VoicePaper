package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.example.voicepaper.data.Room;
import com.example.voicepaper.util.Constants;

import org.json.JSONObject;

public class InputRoomCodeTask extends AsyncTask<Void, Void, Void> {

    private Room participatedRoom;

    private AsyncCallback callback;
    private Exception exception;
    String url;
    ContentValues values;

    public InputRoomCodeTask(ContentValues values, AsyncCallback callback) {
        this.callback = callback;
        this.url = Constants.URL + "/room/roomParticipate";
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

            Log.d("sssong:InputRoomFrgmt", "result : " + result);

            if (!isConnectionSuccess(result)) {
                throw new Exception("Participate room failed");
            }
            inputRoomCodeFromJson(new JSONObject(result).getString("Room"));
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
            Log.d("sssong:InputRoomFrgmt", "error(doInbackground) : " + e);

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (callback != null && exception == null) {
            callback.onSuccess(participatedRoom);
        } else {
            callback.onFailure(exception);
        }
    }

    private boolean isConnectionSuccess(String json_str) {
        // 성공 : 400, 실패 : 401, 402
        /*
        room participate success : 400
        no room found : 401
        room participate fail : 402
         */
        try {
            JSONObject jsonObj = new JSONObject(json_str);

            int code = jsonObj.getInt("code");
            Log.d("sssong:InputRoomFrgmt", "error code : " + code);

            if (code == 400) {
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

    private void inputRoomCodeFromJson(String json_str) {
        try {
            JSONObject jsonObj = new JSONObject(json_str);

            int roomId = jsonObj.getInt("id");
            String roomName = jsonObj.getString("roomName");
            String roomCode = jsonObj.getString("roomCode");
            String roomProfileString = jsonObj.getString("profileString");
            String roomComment = jsonObj.getString("comment");
            int roomPermission = jsonObj.getInt("roomPermission");
            String hostId = jsonObj.getString("hostID");

            participatedRoom = new Room(roomId, roomName, roomPermission, roomComment, hostId, roomCode);
            participatedRoom.setProfileString(roomProfileString);

        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }
    }

}

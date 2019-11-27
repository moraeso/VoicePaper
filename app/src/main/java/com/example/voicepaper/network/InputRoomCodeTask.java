package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;

import com.example.voicepaper.data.Room;
import com.example.voicepaper.fragment.main.InputRoomCodeFragment;
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

            if (!isConnectionSuccess(result)) {
                throw new Exception("Participate room failed");
            }
            inputRoomCodeFromJson(result);
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
        // 성공 : 400, 실패 : 401, 402
        /*
        room participate success : 400
        no room found : 401
        room participate fail : 402
         */
        try {
            JSONObject jsonObj = new JSONObject(json_str);

            int code = jsonObj.getInt("code");

            if (code == 400) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void inputRoomCodeFromJson(String json_str) {
        try {
            JSONObject jsonObj = new JSONObject(json_str);

            int roomId = jsonObj.getInt("roomID");
            String roomCode = jsonObj.getString("roomCode");
            String roomName = jsonObj.getString("roomName");
            String roomComment = jsonObj.getString("roomText");
            int roomPermission = jsonObj.getInt("roomPermission");
            String hostId = jsonObj.getString("hostID");

            participatedRoom = new Room(roomId, roomName, roomPermission, roomComment, hostId, roomCode);

            //int loadPercent = (int)((i + 1) / (float)jsonArray.length() * 100.0f);
            //MountManager.getInstance().setLoadPercent(loadPercent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

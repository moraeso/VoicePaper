package com.example.voicepaper.network;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.voicepaper.data.Room;
import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SignInTask extends AsyncTask<Void, Boolean, Boolean> {
    String url;
    ContentValues values;
    AsyncCallback asyncCallback;
    private Exception exception;

    static private final int SUCCESS = 200;
    static private final int INVALIDEMAIL = 201;
    static private final int INCORRECTPASSWORD = 202;

    public SignInTask(ContentValues values, AsyncCallback asyncCallback) {
        this.url = Constants.URL + "/member/login";
        this.values = values;
        this.asyncCallback = asyncCallback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String result;

        try {
            HttpConnection requestHttpConnection = new HttpConnection();
            result = requestHttpConnection.request(url, values); // post token

            JSONObject job = new JSONObject(result);
            int code = job.getInt("code");

            if (!isSignInDataValid(code)) {
                return false;
            }

            setUser(job.getString("id"),
                    job.getString("name"),
                    job.getString("pw"),
                    job.getString("profileString"),
                    job.getString("token"));

            receiveRoomList(job.getString("roomList"));

        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
            return false;
        }

        return true; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (result == true) {
            asyncCallback.onSuccess(true);
        } else {
            asyncCallback.onFailure(exception);
        }
    }

    private boolean isSignInDataValid(int code) {
        if (code == SUCCESS) {
            return true;
        } else if (code == INVALIDEMAIL) {
            return false;
        } else if (code == INCORRECTPASSWORD) {
            return false;
        }
        return false;
    }

    private void receiveRoomList(String _jsonArray) {
        ArrayList<Room> roomArrayList = new ArrayList<Room>();

        try {
            JSONArray jsonArray = new JSONArray(_jsonArray);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Room room = new Room(
                        jsonObject.getInt("roomID"),
                        jsonObject.getString("roomName"),
                        jsonObject.getInt("roomPermission"),
                        jsonObject.getString("roomText"),
                        jsonObject.getString("hostID"),
                        jsonObject.getString("roomCode")
                );
                room.setProfileString(jsonObject.getString("roomImage"));

                roomArrayList.add(room);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppManager.getInstance().setRoomList(roomArrayList);
    }

    private void setUser(String id, String name, String pw, String profileString,String token){
        AppManager.getInstance().getUser().setID(id);
        AppManager.getInstance().getUser().setName(name);
        AppManager.getInstance().getUser().setPw(pw);
        AppManager.getInstance().getUser().setProfileString(profileString);
        AppManager.getInstance().getUser().setToken(token);
    }
}
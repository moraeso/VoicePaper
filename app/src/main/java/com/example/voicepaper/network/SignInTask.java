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

    public SignInTask(ContentValues values, AsyncCallback asyncCallback){
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
            Log.d("smh:signin",""+job.toString());
            Log.d("smh:signin code",""+code);
            if (!isSignInDataValid(code)) {
                throw new Exception("Signin data is not valid");
            }


            String token = job.getString("token");
            AppManager.getInstance().getUser().setToken(token);

            // 유저 ID, Password 설정
            AppManager.getInstance().getUser().setID(job.getString("id"));
            AppManager.getInstance().getUser().setPw(job.getString("pw"));
            AppManager.getInstance().getUser().setName(job.getString("name"));
            AppManager.getInstance().getUser().setProfileString(job.getString("profileString"));
            reseiveRoomList(job.getString("roomList"));

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

        if(result == true){
            asyncCallback.onSuccess(true);
        }else{
            asyncCallback.onFailure(exception);
        }
    }

    private boolean isSignInDataValid(int code) {
        // 성공 : 200, 실패 : 204
        if(code == 200){
            return true;
        }
        else{
            return false;
        }
    }

    private void reseiveRoomList(String _jsonArray){
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
}
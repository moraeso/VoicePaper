package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;
import com.example.voicepaper.util.Constants;
import org.json.JSONObject;

public class ChangePasswordTask extends AsyncTask<Void, Void,  Boolean> {
    private AsyncCallback callback;
    private Exception exception;
    private String url;
    private ContentValues values;

    private static final int SUCCESS = 200;

    //newPW. userID 바디에 newPW랑 userID 보내면 이전값이랑 비교하고 같으면 202 다르면 200 성공

    public ChangePasswordTask(ContentValues values, AsyncCallback callback) {
        this.url = Constants.URL + "/user/change";

        this.exception = null;
        this.callback = callback;
        this.values = values;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        String result;

        try {
            HttpConnection requestHttpURLConnection = new HttpConnection();
            result = requestHttpURLConnection.request(url, values); // post token
            JSONObject resultObject = new JSONObject(result);
            int code = resultObject.getInt("code");
            if (isConnectionSuccess(code)) {
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if(result == true){
            callback.onSuccess(true);
        }else{
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
}

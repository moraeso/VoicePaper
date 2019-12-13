package com.example.voicepaper.network;
import android.content.ContentValues;
import android.os.AsyncTask;
import com.example.voicepaper.util.Constants;
import org.json.JSONObject;


public class DeleteVoiceTask extends AsyncTask<Void, Boolean, Boolean> {
    String url;
    ContentValues values;
    AsyncCallback asyncCallback;
    private Exception exception;

    private static final int SUCCESS_CODE = 200;

    public DeleteVoiceTask(ContentValues values, AsyncCallback asyncCallback){
        this.url = Constants.URL + "/file/deletevoice";
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
            if (!isSuccess(code)) {
                throw new Exception("voice delete error");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
            return false;
        }
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

    private boolean isSuccess(int code) {
        if(code == SUCCESS_CODE){
            return true;
        }
        else{
            return false;
        }
    }
}

package com.example.voicepaper.network;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.example.voicepaper.manager.AppManager;
import com.example.voicepaper.util.Constants;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadFile extends AsyncTask<Void, Void, String> {
    private AsyncCallback callback;
    private Exception exception;

    private int type;
    private String m_url;
    private String filePath;

    private ContentValues values;
    private String roomId;
    private String userId;

    public static final int UPLOAD_IMAGE_USER = 1;
    public static final int UPLOAD_IMAGE_ROOM = 2;
    public static final int UPLOAD_AUDIO = 3;

    public UploadFile(int type, ContentValues _values, String filePath, AsyncCallback callback) {
        this.type = type;
        this.callback = callback;
        this.values = _values;
        this.filePath = filePath;

        if(type == UPLOAD_IMAGE_USER){
            this.m_url = Constants.URL+"/file/uploaduserimage";
            this.userId = _values.get("userId").toString();
            Log.d("smh:userId",this.userId);
        }
        else if(type == UPLOAD_IMAGE_ROOM){
            this.m_url = Constants.URL+"/file/uploadroomimage";
            this.roomId = values.get("roomId").toString();
        }
        else if(type == UPLOAD_AUDIO){
            this.m_url = Constants.URL+"/file/uploadvoice";
            this.roomId = values.get("roomId").toString();
            this.userId = AppManager.getInstance().getUser().getID();
            Log.d("smh:roomId",this.roomId);
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result;
        try {
            result = upload();
            JSONObject jsonObject = new JSONObject(result);
            String path = jsonObject.getString("path");
            Log.d("smh:return",""+result);
            return path;
        } catch (Exception e){
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Log.d("smh:exception",""+result+exception.toString());

        if (callback != null && exception == null) {
            Log.d("smh:returnt",""+result);
            callback.onSuccess(result);
        } else {
            Log.d("smh:returnf",""+result);
            callback.onFailure(exception);
        }
    }

    private String upload() {
        URL url = null;
        try {
            url = new URL(m_url);
            String boundary = "-------";
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setDoInput(true);
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            DataOutputStream wr = new DataOutputStream(os);

            if(type == UPLOAD_AUDIO) {
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"" + "userID" + "\"\r\n\r\n" + userId);
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"" + "roomID" + "\"\r\n\r\n" + roomId);
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"" + "filename" + "\"\r\n\r\n" + "test"); //임시로 넣어둠
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"voice\"; filename=\"test.mp3\"\r\n");
                wr.writeBytes("Content-Type: audio/mpeg\r\n\r\n");
            }
            else if(type == UPLOAD_IMAGE_USER){
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"" + "userID" + "\"\r\n\r\n" + userId);
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"" + userId +".jpg"+ "\r\n");
                wr.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
            }
            else if(type == UPLOAD_IMAGE_ROOM){
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"" + "roomID" + "\"\r\n\r\n" + roomId);
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"" + "filename" + "\"\r\n\r\n" + roomId +".jpg");
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"" + roomId +".jpg"+ "\r\n");
                wr.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
            }

            FileInputStream fileInputStream = new FileInputStream(filePath);
            int bytesAvailable = fileInputStream.available();
            int maxBufferSize = 4096;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                // Upload file part(s)
                DataOutputStream dataWrite = new DataOutputStream(con.getOutputStream());
                dataWrite.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            fileInputStream.close();
            wr.writeBytes("\r\n--" + boundary + "--\r\n");
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            String line;
            String result = "";

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null){
                result += line;
            }

            return result;
            //나중에 결과 반환 정리할 필요가 잇을듯

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}


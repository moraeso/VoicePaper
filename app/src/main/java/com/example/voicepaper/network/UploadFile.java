package com.example.voicepaper.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadFile extends AsyncTask<Void, Void, Void> {
    private AsyncCallback callback;
    private Exception exception;

    private String m_url;
    private String key;
    private String value;
    private String filePath;

    public UploadFile(String url, String key, String value, String filePath) {
        this.m_url = url;
        this.key = key;
        this.value = value;
        this.filePath = filePath;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String result;
        try{
            upload();
        } catch (Exception e){
            exception = e;
        }
        return null;
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
            wr.writeBytes("\r\n--" + boundary + "\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n" + value);
            wr.writeBytes("\r\n--" + boundary + "\r\n");
            wr.writeBytes("Content-Disposition: form-data; name=\"voice\"; filename=\"test.mp3\"\r\n");
            //wr.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
            wr.writeBytes("Content-Type: audio/mpeg\r\n\r\n");

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

            Log.d("smh:getResponsecode", "" + con.getResponseCode());

            BufferedReader rd = null;
            rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            String line = null;
            while ((line = rd.readLine()) != null) {
                Log.d("smh:ConnectionResult", line);
            }
            return con.getInputStream().toString();
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


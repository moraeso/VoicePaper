package com.example.voicepaper.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
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
            File file = new File(filePath);
            URL url = new URL(m_url);

            upload();
            //upload(url,file);

        } catch (Exception e){
            exception = e;
        }
        return null;
    }

    private void upload() {
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

            BufferedReader rd = null;

            Log.d("smh:getResponsecode", "" + con.getResponseCode());

            rd = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String line = null;

            while ((line = rd.readLine()) != null) {
                Log.i("Lifeclue", line);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//
//    static String lineEnd = "\r\n";
//    static String twoHyphens = "--";
//    static String boundary = "AaB03x87yxdkjnxvi7";
//
//    public String upload(URL url, File file) // , String fileParameterName, HashMap<String, String> parameters
//            throws IOException {
//        HttpURLConnection conn = null;
//        DataOutputStream dos = null;
//        DataInputStream dis = null;
//        FileInputStream fileInputStream = null;
//
//        byte[] buffer;
//        int maxBufferSize = 20 * 1024;
//        try {
//            //------------------ CLIENT REQUEST
//            fileInputStream = new FileInputStream(file);
//
//            // open a URL connection to the Servlet
//            // Open a HTTP connection to the URL
//            conn = (HttpURLConnection) url.openConnection();
//            // Allow Inputs
//            conn.setDoInput(true);
//            // Allow Outputs
//            conn.setDoOutput(true);
//            // Don't use a cached copy.
//            conn.setUseCaches(false);
//            // Use a post method.
//            conn.setRequestMethod("POST");
//            //conn.setRequestProperty("Content-Type", "audio/m4a");
//
//            dos = new DataOutputStream(conn.getOutputStream());
//
//            dos.writeBytes(twoHyphens + boundary + lineEnd);
//            //dos.writeBytes("Content-Disposition: form-data; filename=\"" + file.toString() + "\"" + lineEnd); // after form-data; --> name="" + fileParameterName + "";
//            dos.writeBytes("Content-Disposition: form-data; name=\""+ "id" +"\"\r\n\r\n" + "test");
//            dos.writeBytes("Content-Disposition: form-data; name=\""+ "voice" +"\"\r\n\r\n" + filePath);
//            //dos.writeBytes("Content-Type: audio/m4a" + lineEnd);
//            dos.writeBytes(lineEnd);
//
//            // create a buffer of maximum size
//            buffer = new byte[Math.min((int) file.length(), maxBufferSize)];
//            int length;
//            // read file and write it into form...
//            while ((length = fileInputStream.read(buffer)) != -1) {
//                dos.write(buffer, 0, length);
//            }
//
//            //for (String name : parameters.keySet()) {
//            //    dos.writeBytes(lineEnd);
//            //    dos.writeBytes(twoHyphens + boundary + lineEnd);
//            //    dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + lineEnd);
//            //    dos.writeBytes(lineEnd);
//            //    dos.writeBytes(parameters.get(name));
//            //}
//
//            // send multipart form data necessary after file data...
//            dos.writeBytes(lineEnd);
//            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//            dos.flush();
//        } finally {
//            if (fileInputStream != null) fileInputStream.close();
//            if (dos != null) dos.close();
//        }
//
//        //------------------ read the SERVER RESPONSE
//        try {
//            dis = new DataInputStream(conn.getInputStream());
//            StringBuilder response = new StringBuilder();
//            Log.d("smh:result",response.toString());
//            return response.toString();
//        }finally {
//            if (dis != null) dis.close();
//        }
//    }

}


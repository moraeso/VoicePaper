package com.example.voicepaper.network;

import android.content.ContentValues;

import com.example.voicepaper.manager.AppManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HttpConnection {

    public String request(String _url, ContentValues _parameters){
        HttpURLConnection conn = null;
        StringBuffer sbParams = new StringBuffer();

        if (_parameters == null) { // 보낼 데이터가 없으면 파라미터를 비운다.
            sbParams.append("");
        }
        else {                     // 보낼 데이터가 있으면 파라미터를 채운다.
            boolean isAnd = false; // 보낼 파라미터가 2개 이상이면, 사이에 &(and)가 들어가야해서 그걸 위한 변수
            // 파라미터 키와 값.
            String key;
            String value;

            for(Map.Entry<String, Object> parameter : _parameters.valueSet()){
                key = parameter.getKey();
                value = parameter.getValue().toString();

                // 파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
                if (isAnd) {
                    sbParams.append("&");
                }

                sbParams.append(key).append("=").append(value);

                // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                if (!isAnd) {
                    if (_parameters.size() >= 2) {
                        isAnd = true;
                    }
                }
            }
        }

        try {
            URL url = new URL(_url);
            conn = (HttpURLConnection) url.openConnection(); //httpURLConnection 객체 만들기

            //conn 설정
            conn.setConnectTimeout(10000); // 연결 대기시간 설정
            conn.setRequestMethod("POST"); // 전송방식 POST

            //header 부분 설정.
            conn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정
            conn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

            //추후 어떻게 보내느냐에 따라 변경될 수 있음. id와 token을 보냄
            conn.setRequestProperty("id", AppManager.getInstance().getUser().getID());
            conn.setRequestProperty("x-access-token", AppManager.getInstance().getUser().getToken());

            conn.setDoInput(true);  //서버에서 읽기 모드 지정
            conn.setDoOutput(true); //서버에서 쓰기 모드 지정

            String strParams = sbParams.toString(); //sbParams에 정리한 파라미터들을 스트링으로 저장.

            OutputStream os = conn.getOutputStream();
            os.write(strParams.getBytes("UTF-8")); // 출력 스트림에 출력.
            os.flush(); // 버퍼에 남아있는 것을 모두 출력하고, 비움.
            os.close(); // 시스템 자원 해제.

            //연결 실패시 null을 리턴하면서 종료.
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            //결과물을 BufferedReader로 받음
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            // 왜 입력할때 outputstream을 쓰고, 출력할때 inputstream을 쓰는가?

            // 출력물의 라인과 그 합에 대한 변수.
            String line;
            String result = "";

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null){
                result += line;
            }

            return result;

        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        return null;
    }
}

package com.example.huanghuidong.weatherreport.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by huanghuidong on 2016/1/31.
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address, final HttpCallBackListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;

                try {
                    URL url = new URL(address);

                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream)));

                    StringBuilder response = new StringBuilder();
                    String line;

                    while((line=bufferedReader.readLine())!=null){
                        response.append(line);
                    }

                    if (listener!=null){
                        listener.onFinish(response.toString());
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (connection !=null){
                        connection.disconnect();
                    }
                }

            }
        }).start();

    }
}

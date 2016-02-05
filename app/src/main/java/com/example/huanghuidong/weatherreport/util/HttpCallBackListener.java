package com.example.huanghuidong.weatherreport.util;

/**
 * Created by huanghuidong on 2016/1/31.
 */
public interface HttpCallBackListener {

    void onFinish(String response);

    void onError(Exception e);
}

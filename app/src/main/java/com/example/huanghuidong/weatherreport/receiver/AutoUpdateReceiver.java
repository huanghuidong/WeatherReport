package com.example.huanghuidong.weatherreport.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.huanghuidong.weatherreport.service.AutoUpdateService;

/**
 * Created by huanghuidong on 2016/2/12.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i= new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}

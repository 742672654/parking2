package com.example.parking.service;

import android.app.Service;
import android.content.Intent;
import android.device.PrinterManager;
import android.os.IBinder;
import android.util.Log;

import com.example.parking.Static_bean;
import com.example.parking.http.HttpCallBack2;
import com.example.parking.http.HttpManager2;

import java.util.HashMap;
import java.util.Map;

public class TokenServiceF5 extends Service implements HttpCallBack2 {

    private static final String TAG = "TokenServiceF5";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


//        Map<String,String> param = new HashMap<String,String>(1);
//        param.put("token",intent.getStringExtra("token"));
//        HttpManager2.requestPost(Static_bean.tokenF5,  param, this, "tokenF5");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onResponseGET(String url, Map<String, String> param, String sign, String object) { }

    @Override
    public void onResponsePOST(String url, Map<String, String> param, String sign, String object) {
        Log.i(TAG, "url="+url + "----param="+param +";sign="+sign + ";object="+object+"'****");
    }

    @Override
    public void onResponseFile(String url, Map<String, String> param, String sign, String object) { }
}

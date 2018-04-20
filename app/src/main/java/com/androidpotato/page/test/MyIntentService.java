package com.androidpotato.page.test;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.davidzhou.library.util.LogUtil;

/**
 * Created by David on 2018/4/20 0020.
 */

public class MyIntentService extends IntentService {
    private static final String TAG = "MyIntentService";
    private LocalBroadcastManager localBroadcastManager;

    public MyIntentService() {
        super("MyIntentService");
    }
    public MyIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        LogUtil.i(TAG, "onCreate");
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onDestroy() {
        LogUtil.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        LogUtil.i(TAG, "before doSomeThing");
        sendBroadcast("this is send to main thread: before doSomeThing");
        doSomeThing();
        LogUtil.i(TAG, "after doSomeThing");
        sendBroadcast("this is send to main thread: after doSomeThing");
    }
    private void doSomeThing() {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendBroadcast(String msg) {
        Intent intent = new Intent(TestActivity.LOCAL_ACTION);
        intent.putExtra("msg", msg);
        localBroadcastManager.sendBroadcast(intent);
    }
}
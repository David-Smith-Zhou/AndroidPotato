package com.androidpotato.application;

import android.app.Application;

import org.xutils.x;

/**
 * Created by David on 2018/3/14 0014.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}

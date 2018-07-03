package com.androidpotato.application;

import android.app.Activity;
import android.app.Application;

import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by David on 2018/3/14 0014.
 */

public class MyApp extends Application {
    private static final String TAG = "MyApp";
    private static final String DATABASE_NAME = "ANDROID_POTATO_DATABASE";
    private CopyOnWriteArrayList<Activity> activities;

    @Override
    public void onCreate() {
        super.onCreate();
        activities = new CopyOnWriteArrayList<>();
    }
    public void addToList(Activity activity) {
        activities.add(activity);
    }
    public void removeFromList(Activity activity) {
        activities.remove(activity);
    }
    public void finishAll() {
        for (Activity activity: activities) {
            activity.finish();
        }
    }
}

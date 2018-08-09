package com.androidpotato.widget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.androidpotato.utils.TimeUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.androidpotato.widget.PotatoWidget.ACTION;
import static com.androidpotato.widget.PotatoWidget.KEY_DATA;
import static com.androidpotato.widget.PotatoWidget.SERVICE_ID;

public class WidgetService extends Service {
    private static final String TAG = "WidgetServices";
    private static final int UPDATE_INTERVAL = 1;
    private ScheduledExecutorService mScheduledExecutorService;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mScheduledExecutorService = Executors.newScheduledThreadPool(1);
        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                sendIntent(WidgetService.this, SERVICE_ID);
            }
        }, 0, UPDATE_INTERVAL, TimeUnit.SECONDS);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdown();
            mScheduledExecutorService = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void sendIntent(Context context, int resId) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.setClass(context, PotatoWidget.class);
        intent.setData(Uri.parse("id:" + resId));
        if (resId == SERVICE_ID) {
            intent.putExtra(KEY_DATA, TimeUtil.currDateStr());
        }
        context.sendBroadcast(intent);
    }
}

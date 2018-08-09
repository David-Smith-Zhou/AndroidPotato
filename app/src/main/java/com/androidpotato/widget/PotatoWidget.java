package com.androidpotato.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.androidpotato.R;
import com.androidpotato.utils.TimeUtil;
import com.davidzhou.library.util.ULog;

/**
 * Implementation of App Widget functionality.
 */
public class PotatoWidget extends AppWidgetProvider {
    private static final String TAG = "PotatoWidget";
    private static final String SERVICE_ACTION = "com.androidpotato.potatowidget.widgetservice";
    public static final String ACTION = "btn.text.com";
    public static final int SERVICE_ID = 0xF3;
    public static final String KEY_DATA = "data";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        ULog.i(TAG, "updateAppWidget");

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.potato_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setOnClickPendingIntent(R.id.appwidget_btn, getPendingIntent(context, R.id.appwidget_btn));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ULog.i(TAG, "onUpdate");
        // There may be multiple widgets active, so update all of them
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.potato_widget);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_btn, getPendingIntent(context, R.id.appwidget_btn));
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        ULog.i(TAG, "onEnabled");
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
        context.startService(new Intent(SERVICE_ACTION));
    }

    @Override
    public void onDisabled(Context context) {
        ULog.i(TAG, "onDisabled");
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
        context.stopService(new Intent(SERVICE_ACTION));
    }

    private static PendingIntent getPendingIntent(Context context, int resId) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.setClass(context, PotatoWidget.class);
        intent.setData(Uri.parse("id:" + resId));
        if (resId == SERVICE_ID) {
            intent.putExtra(KEY_DATA, TimeUtil.currDateStr());
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ULog.i(TAG, "onReceive: " + intent);
        if (intent.getAction() != null
                && intent.getAction().equals(ACTION)) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.potato_widget);
            Uri data = intent.getData();
            int resId = -1;
            if (data != null) {
                resId = Integer.parseInt(data.getSchemeSpecificPart());
            }
            switch (resId) {
                case R.id.appwidget_btn:
                    ULog.i(TAG, "onClick");
                    break;
                case SERVICE_ID:
                    String dataStr = intent.getStringExtra(KEY_DATA);
                    ULog.i(TAG, "dataStr: " + dataStr);
                    remoteViews.setTextViewText(R.id.appwidget_text, context.getString(R.string.appwidget_text) + ": " + dataStr);
                    break;
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisName = new ComponentName(context, PotatoWidget.class);
            appWidgetManager.updateAppWidget(thisName, remoteViews);
        }
        super.onReceive(context, intent);
    }
}


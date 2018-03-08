package com.androidpotato.page.wifi;

import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import com.androidpotato.page.wifi.interfaces.OnWifiEventsListener;

import java.util.List;

/**
 * Created by David on 2017/6/6 0006.
 */

public class WifiHandler {
    private static final String TAG = "WifiHandler";

    public static final int MSG_TYPE_WIFI_STATUS_CHANGED = 0x01;
    public static final int MSG_TYPE_WIFI_ON_SCAN_RESULT_AVAILABLE = 0x02;
    public static final int MSG_TYPE_WIFI_CONNECTING = 0x03;
    public static final int MSG_TYPE_NETWORK_STATUS_CHANGED = 0x04;


    public static final int ERROR_CONNECT_FAILURE = 0x21;

    private Handler handler;
    private OnWifiEventsListener onWifiEventsListener;

    public WifiHandler(OnWifiEventsListener onWifiEventsListener) {
        this.onWifiEventsListener = onWifiEventsListener;
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_TYPE_WIFI_STATUS_CHANGED:
                        callStatusChangedListener(msg);
                        break;
                    case MSG_TYPE_WIFI_ON_SCAN_RESULT_AVAILABLE:
                        callOnScanResultAvailableListener(msg);
                        break;
                    case MSG_TYPE_NETWORK_STATUS_CHANGED:
                        callOnConnectListener(msg);
                        break;
                    case ERROR_CONNECT_FAILURE:
                        callOnErrorListener(msg);
                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void callOnErrorListener(Message msg) {
        if (onWifiEventsListener != null) {
            onWifiEventsListener.onError(ERROR_CONNECT_FAILURE, (String) msg.obj);
        }
    }
    private void callOnConnectListener(Message msg) {
        NetworkInfo info = (NetworkInfo) msg.obj;
        if ((info != null)) {
            if (onWifiEventsListener != null) {
                onWifiEventsListener.onNetworkStatusChanged(info);
            }
        }
    }
    private void callOnScanResultAvailableListener(Message msg) {
        if (onWifiEventsListener != null) {
            onWifiEventsListener.onScanResultAvailable((List<ScanResult>) msg.obj);
        }
    }

    private void callStatusChangedListener(Message msg) {
        if (onWifiEventsListener != null) {
            onWifiEventsListener.onWifiStatusChanged((int)msg.obj);
        }
    }

    public void sendMessage(int what, Object obj) {
        sendMessage(what, obj, Constant.DEFAULT_INT_ARG);
    }

    public void sendMessage(int what, Object obj, int arg1) {
        sendMessage(what, obj, arg1, Constant.DEFAULT_INT_ARG);
    }

    public void sendMessage(int what, Object obj, int arg1 , int arg2) {
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        handler.sendMessage(msg);
    }
}

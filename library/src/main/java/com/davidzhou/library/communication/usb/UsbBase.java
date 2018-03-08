package com.davidzhou.library.communication.usb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

/**
 * Created by David on 2016/12/6 0006.
 */

public class UsbBase {
    private static String TAG = "UsbBase";
    private Context context;


    public static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    public static final String ACTION_USB_STATE =
            "android.hardware.usb.action.USB_STATE";
    public static final String ACTION_USB_DEVICE_ATTACHED =
            "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    public static final String ACTION_USB_DEVICE_DETACHED =
            "android.hardware.usb.action.USB_DEVICE_DETACHED";

    public static final String USB_CONNECTED = "connected";

    public UsbBase(Context context) {
        this.context = context;

    }
    public void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_USB_DEVICE_ATTACHED);
        intentFilter.addAction(ACTION_USB_DEVICE_DETACHED);
        intentFilter.addAction(ACTION_USB_PERMISSION);
        intentFilter.addAction(ACTION_USB_STATE);
        context.registerReceiver(broadcastReceiver, intentFilter);
    }
    public void unRegisterBroadcast() {
        context.unregisterReceiver(broadcastReceiver);
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                Log.i(TAG, "usb device attached");
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                Log.i(TAG, "usbDevice: " + usbDevice.getDeviceName());
            } else if (ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Log.i(TAG, "usb device detached");
            } else if (ACTION_USB_PERMISSION.equals(action)) {

            }
        }
    };
}

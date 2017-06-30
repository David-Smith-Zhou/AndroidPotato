package com.androidpotato.mylibrary.bluetooth;

import android.bluetooth.BluetoothSocket;

import com.androidpotato.mylibrary.util.UtilLog;

import java.io.IOException;

/**
 * Created by David on 2015/11/19 0019.
 */
public class BTConnectThread extends Thread {
    private static final String TAG = "BTConnectThread";
    private BluetoothSocket bluetoothSocket;
    private OnBluetoothThreadListener onBluetoothThreadListener;


    public BTConnectThread(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }

    @Override
    public void run() {
        super.run();
        //UtilLog.i(TAG, "connecting");
        UtilLog.i(TAG, "Connecting --> name:" + bluetoothSocket.getRemoteDevice().getName() + ", Mac: " + bluetoothSocket.getRemoteDevice().getAddress());
        try {
            bluetoothSocket.connect();
            UtilLog.i(TAG, "connect success --> name: " + bluetoothSocket.getRemoteDevice().getName() + ", Mac: " + bluetoothSocket.getRemoteDevice().getAddress());
            //callback to bluetooth so that i can only manage bluetooth in class bluetooth
            if (onBluetoothThreadListener != null) {
                onBluetoothThreadListener.OnConnect(bluetoothSocket, true);
                onBluetoothThreadListener = null;
            }
        } catch (IOException e) {
            UtilLog.i(TAG, "connect failed --> name: " + bluetoothSocket.getRemoteDevice().getName() + ", Mac: " + bluetoothSocket.getRemoteDevice().getAddress());
            e.printStackTrace();
            if (onBluetoothThreadListener != null) {
                onBluetoothThreadListener.OnConnect(null, false);
                onBluetoothThreadListener = null;
            }
        }
    }

    public interface OnBluetoothThreadListener {
        void OnConnect(BluetoothSocket bluetoothSocket, boolean isSuccess);
    }

    public void setOnBluetoothThreadListener(OnBluetoothThreadListener onBluetoothThreadListener) {
        this.onBluetoothThreadListener = onBluetoothThreadListener;
    }

}

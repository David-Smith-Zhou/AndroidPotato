package com.androidpotato.mylibrary.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.androidpotato.mylibrary.util.TypeConvert;

import java.io.InputStream;
import java.util.Arrays;




/**
 * Created by David on 2015/11/19 0019.
 */
public class BTListenThread extends Thread{
    private static final String TAG = "BTListenThread";

    private InputStream inputStream;
    private boolean isListen = true;
    private byte[] dataBytes;
    private final int BYTES_SIZE = 1024;
    private Handler handler;
    private OnBTListenThreadListener onBTListenThreadListener;

    public BTListenThread(BluetoothSocket bluetoothSocket, Handler handler) {
        try {
            this.handler = handler;
            this.inputStream = bluetoothSocket.getInputStream();
            dataBytes = new byte[BYTES_SIZE];
        } catch (Exception e) {
            Log.e(TAG, "In BTListenThread:" + e.getLocalizedMessage());
        }
    }

    public void setIsListen(boolean isListen) {
        this.isListen = isListen;
        Log.e(TAG, "In setIsListen: set the isListen");
    }

    private void listenFromSocket() {
        int dataLength = -1;
        try {
            Arrays.fill(dataBytes, (byte) 0);
            dataLength = inputStream.read(dataBytes);
        } catch (Exception e) {
            Log.e(TAG, "In listenFromSocket:" + e.getMessage());
        }
        if (dataLength != -1) {
            byte[] data = new byte[dataLength];
            System.arraycopy(dataBytes, 0, data, 0, dataLength);

            if (onBTListenThreadListener != null) {
                onBTListenThreadListener.onRead(dataLength, data);
                Log.i(TAG, "Response(HEX String): " + TypeConvert.bytesToHexString(data));
                return;
            }
            Message msg = new Message();
            msg.what = BluetoothSSP.ON_BLUETOOTH_RECEIVED;
            msg.obj = data;
            msg.arg1 = dataLength;
            handler.sendMessage(msg);
            Log.i(TAG, "Response(HEX String): " + TypeConvert.bytesToHexString(data));
        }
    }

    @Override
    public void run() {
        super.run();
        while(isListen) {
            listenFromSocket();
        }
        Log.v(TAG, "In run: BTListenThread is stopped");
    }

    public void setOnBTListenThreadListener(OnBTListenThreadListener onBTListenThreadListener) {
        this.onBTListenThreadListener = onBTListenThreadListener;
    }

    public interface OnBTListenThreadListener {
        void onRead(int length, byte[] data);
    }
}

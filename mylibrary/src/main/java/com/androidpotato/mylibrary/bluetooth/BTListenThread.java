package com.androidpotato.mylibrary.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import com.androidpotato.mylibrary.util.TypeConvert;
import com.androidpotato.mylibrary.util.UtilLog;

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
            UtilLog.e(TAG, "In BTListenThread:" + e.getLocalizedMessage());
        }
    }

    public void setIsListen(boolean isListen) {
        this.isListen = isListen;
        UtilLog.e(TAG, "In setIsListen: set the isListen");
    }

    private void listenFromSocket() {
        int dataLength = -1;
        try {
            Arrays.fill(dataBytes, (byte) 0);
            dataLength = inputStream.read(dataBytes);
        } catch (Exception e) {
            UtilLog.e(TAG, "In listenFromSocket:" + e.getMessage());
        }
        if (dataLength != -1) {
            byte[] data = new byte[dataLength];
            System.arraycopy(dataBytes, 0, data, 0, dataLength);

            if (onBTListenThreadListener != null) {
                onBTListenThreadListener.onRead(dataLength, data);
                UtilLog.i(TAG, "Response(HEX String): " + TypeConvert.bytesToHexString(data));
                return;
            }
            Message msg = new Message();
            msg.what = BluetoothSSP.ON_BLUETOOTH_RECEIVED;
            msg.obj = data;
            msg.arg1 = dataLength;
            handler.sendMessage(msg);
            UtilLog.i(TAG, "Response(HEX String): " + TypeConvert.bytesToHexString(data));
        }
    }

    @Override
    public void run() {
        super.run();
        while(isListen) {
            listenFromSocket();
        }
        UtilLog.v(TAG, "In run: BTListenThread is stopped");
    }

    public void setOnBTListenThreadListener(OnBTListenThreadListener onBTListenThreadListener) {
        this.onBTListenThreadListener = onBTListenThreadListener;
    }

    public interface OnBTListenThreadListener {
        void onRead(int length, byte[] data);
    }
}

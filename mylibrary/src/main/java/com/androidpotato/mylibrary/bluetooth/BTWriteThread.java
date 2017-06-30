package com.androidpotato.mylibrary.bluetooth;

import android.bluetooth.BluetoothSocket;

import com.androidpotato.mylibrary.util.TypeConvert;
import com.androidpotato.mylibrary.util.UtilLog;

import java.io.OutputStream;
import java.util.Arrays;



/**
 * Created by David on 2015/11/19 0019.
 */
public class BTWriteThread extends Thread{
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private byte[] dataBytes;

    public BTWriteThread(BluetoothSocket bluetoothSocket, byte[] dataBytes) {
        try {
            this.bluetoothSocket = bluetoothSocket;
            this.dataBytes = dataBytes;
            outputStream = bluetoothSocket.getOutputStream();
        } catch (Exception e) {
            UtilLog.e("BTWriteThread", "In BTWriteThread:" + e.getLocalizedMessage());
        }
    }

    private void sendToSocket() {
        try {
            outputStream.write(dataBytes);
            UtilLog.i("BTWriteThread", "Send(byte[]): " + Arrays.toString(dataBytes));
            UtilLog.i("BTWriteThread", "Send(HEX String): " + TypeConvert.bytesToHexString(dataBytes));
        } catch (Exception e) {
            UtilLog.e("BTWriteThread", "In sendBytes:" + e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        super.run();
        sendToSocket();
    }
}

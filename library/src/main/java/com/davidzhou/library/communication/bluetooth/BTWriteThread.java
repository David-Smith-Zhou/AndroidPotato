package com.davidzhou.library.communication.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.davidzhou.library.util.TypeConvert;

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
            Log.e("BTWriteThread", "In BTWriteThread:" + e.getLocalizedMessage());
        }
    }

    private void sendToSocket() {
        try {
            outputStream.write(dataBytes);
            Log.i("BTWriteThread", "Send(byte[]): " + Arrays.toString(dataBytes));
            Log.i("BTWriteThread", "Send(HEX String): " + TypeConvert.bytesToHexString(dataBytes));
        } catch (Exception e) {
            Log.e("BTWriteThread", "In sendBytes:" + e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        super.run();
        sendToSocket();
    }
}

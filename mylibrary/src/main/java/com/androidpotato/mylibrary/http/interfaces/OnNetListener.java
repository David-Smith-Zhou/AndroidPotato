package com.androidpotato.mylibrary.http.interfaces;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by David on 2017/5/24 0024.
 */

public interface OnNetListener {
    void onConnect(InputStream inputStream, OutputStream outputStream);
    void onDisconnect();
    void onError(int errCode, String msg);
    void onRead(byte[] data);
}

package com.androidpotato.mylibrary.http.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import com.androidpotato.mylibrary.http.interfaces.OnNetListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by David on 2017/5/24 0024.
 */

public class HttpClient {
    private static final String TAG = "HttpClient";
    public static final int ERROR_DEFAULT = 0x00;
    public static final int ERROR_TIMEOUT = 0x01;
    public static final int ERROR_CONNECTED_FAILED = 0x02;


    private static final int MSG_TYPE_READ = 0x21;
    private static final int MSG_TYPE_ERROR = 0x22;
    private static final int MSG_TYPE_CONNECTED = 0x23;
    private static final int MSG_TYPE_DISCONNECTED = 0x24;

    private static final int TIME_OUT = 10000;
    private HttpURLConnection urlConn;

    private OnNetListener onNetListener;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_TYPE_READ:
                    if (onNetListener != null) {
                        onNetListener.onRead((byte[]) msg.obj);
                    }
                    break;
                case MSG_TYPE_ERROR:
                    if (onNetListener != null) {
                        onNetListener.onError(msg.arg1, (String) msg.obj);
                    }
                    break;
            }
        }
    };

    public HttpClient(OnNetListener onNetListener) {
        this.onNetListener = onNetListener;
    }

    public synchronized void getActivationCode(String encryptData) {
        getMsg("http://119.29.78.41:3366/mode/administrators/", "data=" + encryptData + "&time=" + System.currentTimeMillis());
    }

    private synchronized void getMsg(final String urlStr, final String param) {
        getMsg(urlStr + "?" + param);
    }

    private synchronized void getMsg(final String urlStr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlStr);
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setRequestMethod("GET");
                    urlConn.setConnectTimeout(TIME_OUT);
                    urlConn.setReadTimeout(TIME_OUT);
                    urlConn.connect();
                    if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader reader = new InputStreamReader(urlConn.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        StringBuffer stringBuffer = new StringBuffer();
                        String readStr;
                        while ((readStr = bufferedReader.readLine()) != null) {
                            stringBuffer.append(readStr);
                        }
                        sendMessage(MSG_TYPE_READ, stringBuffer.toString().getBytes(), ERROR_DEFAULT);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    sendMessage(MSG_TYPE_ERROR, e.getMessage(), ERROR_TIMEOUT);
                } catch (ConnectException e) {
                    e.printStackTrace();
                    sendMessage(MSG_TYPE_ERROR, e.getMessage(), ERROR_CONNECTED_FAILED);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessage(MSG_TYPE_ERROR, e.getMessage(), ERROR_DEFAULT);
                } finally {
                    urlConn.disconnect();
                }
            }
        }).start();
    }
    private void sendMessage(int what, Object obj, int arg1) {
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.arg1 = arg1;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

}

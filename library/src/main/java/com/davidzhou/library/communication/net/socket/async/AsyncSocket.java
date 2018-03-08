package com.davidzhou.library.communication.net.socket.async;


import com.davidzhou.library.communication.common.comminfos.CommInfo;
import com.davidzhou.library.communication.common.dto.ErrorMsg;
import com.davidzhou.library.communication.interfaces.CommEventCallback;
import com.davidzhou.library.communication.net.socket.BaseSocket;

/**
 * Created by David on 2017/12/26 0026.
 */

public class AsyncSocket extends BaseSocket {
    private static final String TAG = "AsyncSocket";
    private AsyncCheckThread mAsyncCheckThread;

    public AsyncSocket(CommInfo info) {
        super(info);
    }

    @Override
    public boolean isConnected() {
        return mAsyncCheckThread != null && mAsyncCheckThread.isConnected();
    }

    @Override
    public void connect() {
        try {
            mAsyncCheckThread = new AsyncCheckThread(getHandler());
            mAsyncCheckThread.setAddrAndPort(getAddr(), getPort());
            mAsyncCheckThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        if (isConnected()) {
           mAsyncCheckThread.disconnect();
        }
    }

    @Override
    public void onWrite(byte[] data) {
        if (isConnected()) {
            mAsyncCheckThread.send(data);
        }
    }

    @Override
    public void setTimeout(int timeout) {

    }
}

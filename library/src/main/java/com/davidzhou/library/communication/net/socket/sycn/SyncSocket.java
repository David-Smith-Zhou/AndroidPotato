package com.davidzhou.library.communication.net.socket.sycn;


import com.davidzhou.library.communication.common.comminfos.CommInfo;
import com.davidzhou.library.communication.net.socket.BaseSocket;

/**
 * Created by David on 2018/1/3 0003.
 */

public class SyncSocket extends BaseSocket {
    private static final String TAG = "SyncSocket";

    public SyncSocket(CommInfo info) {
        super(info);
    }

    @Override
    public void onWrite(byte[] data) {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void connect() {

    }

    @Override
    public void setTimeout(int timeout) {

    }

    @Override
    public void disconnect() {

    }
}

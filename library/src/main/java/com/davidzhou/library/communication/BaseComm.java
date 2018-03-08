package com.davidzhou.library.communication;


import com.davidzhou.library.communication.common.comminfos.CommInfo;
import com.davidzhou.library.communication.handler.CommMainMsgHandler;
import com.davidzhou.library.communication.handler.CommMainMsgHandlerCallback;
import com.davidzhou.library.communication.interfaces.CommWriteCallback;

/**
 * Created by David on 2017/12/29 0029.
 */

public abstract class BaseComm implements CommWriteCallback {
    private static final String TAG = "BaseComm";

    private CommInfo.Type type;
    private CommMainMsgHandler mHandler;

    public BaseComm(CommInfo info) {
        this.type = info.getType();
        mHandler = new CommMainMsgHandler();
    }

    public CommInfo.Type getType() {
        return type;
    }

    public abstract boolean isConnected();
    public abstract void connect();
    public abstract void disconnect();

    protected CommMainMsgHandler getHandler() {
        return mHandler;
    }
    public void setCommMainMsgHandlerCallback(CommMainMsgHandlerCallback commMainMsgHandlerCallback) {
        if (mHandler != null) {
            mHandler.setCommMainMsgHandlerCallback(commMainMsgHandlerCallback);
        }
    }
}

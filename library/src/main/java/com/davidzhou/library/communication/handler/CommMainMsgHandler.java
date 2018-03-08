package com.davidzhou.library.communication.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.davidzhou.library.communication.common.dto.BaseDataInfo;
import com.davidzhou.library.communication.common.dto.ErrorMsg;


/**
 * Created by David on 2017/12/27 0027.
 */

/**
 * 此handler用于通知主线程各种事件
 */
public class CommMainMsgHandler {
    private static final String TAG = "CommMainMsgHandler";

    private static final int MSG_CONNECTED = 0x01;
    private static final int MSG_DISCONNECTED = 0x02;
    private static final int MSG_RECV_DATA = 0x03;
    private static final int MSG_ERROR = 0x04;

    private Handler handler;
    private CommMainMsgHandlerCallback commMainMsgHandlerCallback;

    public CommMainMsgHandler() {
        this.handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_CONNECTED:
                        if (commMainMsgHandlerCallback != null) {
                            commMainMsgHandlerCallback.onConnect();
                        }
                        break;
                    case MSG_RECV_DATA:
                        if (commMainMsgHandlerCallback != null) {
                            commMainMsgHandlerCallback.onRecvData((BaseDataInfo) msg.obj);
                        }
                        break;
                    case MSG_DISCONNECTED:
                        if (commMainMsgHandlerCallback != null) {
                            commMainMsgHandlerCallback.onDisconnect();
                        }
                        break;
                    case MSG_ERROR:
                        if (commMainMsgHandlerCallback != null) {
                            commMainMsgHandlerCallback.onError((ErrorMsg) msg.obj);
                        }
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }
    public void sendReadMsg(BaseDataInfo dataInfo) {
        sendMsg(MSG_RECV_DATA, dataInfo);
    }
    public void sendErrorMsg(ErrorMsg msg) {
        sendMsg(MSG_ERROR, msg);
    }
    public void sendDisconnectMsg() {
        sendMsg(MSG_DISCONNECTED, null);
    }
    public void sendConnectMsg() {
        sendMsg(MSG_CONNECTED, null);
    }
    private void sendMsg(int what, Object obj) {
        Message msg = new Message();
        msg.what = what;
        if (obj != null) {
            msg.obj = obj;
        }
        handler.sendMessage(msg);
    }

    public void setCommMainMsgHandlerCallback(CommMainMsgHandlerCallback commMainMsgHandlerCallback) {
        this.commMainMsgHandlerCallback = commMainMsgHandlerCallback;
    }
}

package com.davidzhou.library.communication.handler;


import com.davidzhou.library.communication.common.dto.BaseDataInfo;
import com.davidzhou.library.communication.common.dto.ErrorMsg;

/**
 * Created by David on 2018/2/27 0027.
 */

public interface CommMainMsgHandlerCallback {
    void onConnect();
    void onDisconnect();
    void onRecvData(BaseDataInfo dataInfo);
    void onError(ErrorMsg msg);
}

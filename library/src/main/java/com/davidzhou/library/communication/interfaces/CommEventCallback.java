package com.davidzhou.library.communication.interfaces;


import com.davidzhou.library.communication.common.comminfos.CommInfo;
import com.davidzhou.library.communication.common.dto.ErrorMsg;

/**
 * Created by David on 2018/2/25 0025.
 */

public interface CommEventCallback {
    void onConnect(CommInfo info);
    void onDisconnect(CommInfo info);
    void onError(ErrorMsg msg);
}

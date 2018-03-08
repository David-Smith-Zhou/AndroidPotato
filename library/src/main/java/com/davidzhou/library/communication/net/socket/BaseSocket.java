package com.davidzhou.library.communication.net.socket;


import com.davidzhou.library.communication.BaseComm;
import com.davidzhou.library.communication.common.comminfos.CommInfo;
import com.davidzhou.library.communication.common.comminfos.EthernetInfo;

/**
 * Created by David on 2017/12/27 0027.
 */

public abstract class BaseSocket extends BaseComm {

    public static final int RECV_BUFF_CAPACITY = 1024;
    public static final int CONNECT_TIMETOUT = 30 * 1000;
    public static final int COMM_TIMETOUT = 30 * 1000;

    private String addr;
    private int port;

    public BaseSocket(CommInfo info) {
        super(info);
        EthernetInfo ethernetInfo = (EthernetInfo) info;
        this.addr = ethernetInfo.getAddress();
        this.port = ethernetInfo.getPort();
    }

    public abstract void setTimeout(int timeout);

    protected int getPort() {
        return port;
    }

    protected String getAddr() {
        return addr;
    }
}

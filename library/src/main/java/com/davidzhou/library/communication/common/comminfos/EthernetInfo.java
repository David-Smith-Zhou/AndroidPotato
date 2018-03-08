package com.davidzhou.library.communication.common.comminfos;

/**
 * Created by David on 2018/2/28 0028.
 */

public class EthernetInfo extends CommInfo {
    private String addr;
    private int port;

    public EthernetInfo(Type type) {
        super(type);
    }

    public void setAddress(String addr) {
        this.addr = addr;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return addr;
    }

    public int getPort() {
        return port;
    }
}

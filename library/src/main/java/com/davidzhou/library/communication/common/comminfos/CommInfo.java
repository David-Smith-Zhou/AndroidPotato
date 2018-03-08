package com.davidzhou.library.communication.common.comminfos;

/**
 * Created by David on 2018/2/27 0027.
 */

public class CommInfo {
    private static final String TAG = "CommInfo";

    private Type mType = Type.UNKNOWN;

    public enum Type {
        UNKNOWN,
        ETHERNET,
        USB,
        SERIAL_PORT,
        BLUETOOTH_SPP,
        BLUETOOTH_BLE
    }

    public CommInfo(Type type) {
        this.mType = type;
    }

    public Type getType() {
        return mType;
    }
}

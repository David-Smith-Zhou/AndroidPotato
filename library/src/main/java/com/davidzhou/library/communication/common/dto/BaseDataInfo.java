package com.davidzhou.library.communication.common.dto;

/**
 * Created by David on 2018/2/27 0027.
 */

public class BaseDataInfo {
    private static final String TAG = "BaseDataInfo";

    private byte[] mData;

    public BaseDataInfo(byte[] data) {
        this.mData = data;
    }
    public byte[] getData() {
        return this.mData;
    }
}

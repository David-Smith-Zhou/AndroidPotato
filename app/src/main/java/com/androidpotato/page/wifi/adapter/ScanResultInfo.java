package com.androidpotato.page.wifi.adapter;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;

/**
 * Created by David on 2017/12/11 0011.
 */

public class ScanResultInfo {
    private ScanResult scanResult;
    private boolean isConnect = false;
    private boolean isSaved = false;
    private WifiInfo info;

    public ScanResultInfo(WifiInfo info) {
        this.info = info;
    }

    public ScanResultInfo(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public ScanResult getScanResult() {
        return scanResult;
    }

    public WifiInfo getWifiInfo() {
        return info;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }
}

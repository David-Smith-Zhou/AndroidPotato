package com.androidpotato.page.wifi.interfaces;

import android.net.NetworkInfo;
import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by David on 2017/6/6 0006.
 */

public interface OnWifiEventsListener {
    void onWifiStatusChanged(int status);
    void onScanResultAvailable(List<ScanResult> results);
    void onNetworkStatusChanged(NetworkInfo info);
    void onError(int errCode, String msg);
}

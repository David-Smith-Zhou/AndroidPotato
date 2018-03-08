package com.androidpotato.page.wifi.widget.wifi;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.ScanResult;
import android.support.annotation.Nullable;

import com.androidpotato.R;


/**
 * Created by David on 2017/6/8 0008.
 */

public class WifiConnectDialogFactory {
    private static WifiConnectDialogFactory factory;

    private WifiConnectDialogFactory() {

    }

    public static WifiConnectDialogFactory getInstance() {
        if (factory == null) {
            factory = new WifiConnectDialogFactory();
        }
        return factory;
    }
    @Nullable
    public BaseWifiConnectDialog getDialog(Context context, ScanResult result) {
        StringBuffer buffer = new StringBuffer();
        Resources resources = context.getResources();
        String str = result.capabilities;

        String wpa = resources.getString(R.string.wifi_protocol_wpa);
        String wpa2 = resources.getString(R.string.wifi_protocol_wpa2);
        String wps = resources.getString(R.string.wifi_protocol_wps);
        String wep = resources.getString(R.string.wifi_protocol_wep);
        String none = resources.getString(R.string.wifi_protocol_none);

        if (str.contains(wpa)) {
            buffer.append(wpa + "/");
        }
        if (str.contains(wpa2)) {
            buffer.append(wpa2 + "/");
        }
        if (str.contains(wps)) {
            buffer.append(wps + "/");
        }
        if (str.contains(wep)) {
            buffer.append(wep + "/");
        }

        // no encrypt
        if (buffer.length() == 0) {
            return new NonePinWifiConnectDialog(context, result);
        }
        buffer.setLength(buffer.length() - 1);
        String rst = buffer.toString();
        if (rst.contains(wpa)) {
            return new WPAWifiConnectDialog(context, result);
        }
        if (rst.contains(wep)) {
            return new WEPWifiConnectDialog(context, result);
        }
        throw new UnsupportedOperationException("not support " + rst + " type connection yet");
    }
}

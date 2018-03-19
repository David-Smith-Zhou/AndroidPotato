package com.androidpotato.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.androidpotato.R;


/**
 * Created by David on 2017/6/2 0002.
 */

public class WifiUtil {

    public static boolean isWifiConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiNetworkInfo.isConnected())
        {
            return true ;
        }
        return false ;
    }

    public static String getRssiLevel(Context ctx, int rssi) {
        int tmp = rssi + 100;
        if (tmp >= 50) {
            return ctx.getResources().getString(R.string.item_rssi_level_high);
        } else if (tmp < 50 && tmp >= 30) {
            return ctx.getResources().getString(R.string.item_rssi_level_middle);
        } else {
            return ctx.getResources().getString(R.string.item_rssi_level_low);
        }
    }

    public static String getSupportEncryptType(Context context, String str) {
        StringBuffer buffer = new StringBuffer();
        Resources resources = context.getResources();

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
            buffer.append(none + "/");
        }
        buffer.setLength(buffer.length() - 1);
        return buffer.toString();
    }
}

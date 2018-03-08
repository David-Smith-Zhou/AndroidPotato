package com.androidpotato.page.wifi.widget.wifi;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.WifiConfiguration;

/**
 * Created by David on 2017/6/7 0007.
 */

public abstract class BaseWifiConnectDialog extends Dialog {

    public BaseWifiConnectDialog(Context context) {
        super(context);
    }
    public abstract void setOnWifiConnectDialogListener(OnWifiConnectDialogListener onWifiConnectDialogListener);

    public interface OnWifiConnectDialogListener {
        void onConfirm(WifiConfiguration config);
        void onCancel();
    }
}

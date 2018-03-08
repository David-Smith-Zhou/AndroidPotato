package com.androidpotato.page.wifi.widget.wifi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.androidpotato.R;


/**
 * Created by David on 2017/6/12 0012.
 */

public class WPSWifiConnectDialog extends BaseWifiConnectDialog {
    public static final int WPS_TYPE_BTN = 0x01;
    public static final int WPS_TYPE_PIN = 0x02;

    private int type;
    private TextView hintTextView;
    private OnWifiConnectDialogListener onWifiConnectDialogListener;
    private WifiManager manager;

    public WPSWifiConnectDialog(Context context, int type, WifiManager manager) {
        super(context);
        this.type = type;
        this.manager = manager;
    }

    WifiManager.WpsCallback wpsCallback = new WifiManager.WpsCallback() {
        @Override
        public void onStarted(String s) {

        }

        @Override
        public void onSucceeded() {

        }

        @Override
        public void onFailed(int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wps);
        initViews();

    }
    private void initViews() {
        hintTextView = (TextView) this.findViewById(R.id.dialog_wps_textView);
        WpsInfo wpsInfo = new WpsInfo();
        if (type == WPS_TYPE_BTN) {
            this.setTitle(getContext().getString(R.string.menu_wps_title));
            wpsInfo.setup = WPS_TYPE_BTN;
        } else {
            this.setTitle(getContext().getString(R.string.menu_wps_with_pin_title));
            wpsInfo.setup = WPS_TYPE_PIN;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            manager.startWps(wpsInfo, new WifiManager.WpsCallback() {
                @Override
                public void onStarted(String pin) {
                    if (type == WPS_TYPE_BTN) {
                        hintTextView.setText(WPSWifiConnectDialog.this.getContext().getString(R.string.hint_wps_btn));
                    } else {
                        hintTextView.setText(WPSWifiConnectDialog.this.getContext().getString(R.string.hint_wps_pin, pin));
                    }
                }

                @Override
                public void onSucceeded() {

                }

                @Override
                public void onFailed(int reason) {

                }
            });
        }
    }



    @Override
    public void setOnWifiConnectDialogListener(OnWifiConnectDialogListener onWifiConnectDialogListener) {
        this.onWifiConnectDialogListener = onWifiConnectDialogListener;
    }
}

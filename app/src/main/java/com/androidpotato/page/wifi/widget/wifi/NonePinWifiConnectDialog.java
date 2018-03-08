package com.androidpotato.page.wifi.widget.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidpotato.R;
import com.davidzhou.library.util.WifiUtil;


/**
 * Created by David on 2017/6/8 0008.
 */

public class NonePinWifiConnectDialog extends BaseWifiConnectDialog {
    private ScanResult result;
    private OnWifiConnectDialogListener onWifiConnectDialogListener;

    public NonePinWifiConnectDialog(Context ctx, ScanResult result) {
        super(ctx);
        this.result = result;
    }

    @Override
    public void setOnWifiConnectDialogListener(OnWifiConnectDialogListener onWifiConnectDialogListener) {
        this.onWifiConnectDialogListener = onWifiConnectDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_none_pin);
        initViews();
    }

    private void initViews() {
        TextView rssiLevel = (TextView) this.findViewById(R.id.dialog_none_pin_rssi_value_tv);
        TextView authType = (TextView) this.findViewById(R.id.dialog_none_pin_auth_type_value_tv);
        rssiLevel.setText(WifiUtil.getRssiLevel(this.getContext(), result.level));
        authType.setText(this.getContext().getResources().getString(R.string.wifi_protocol_none));
        this.setTitle(result.SSID);
        Button connectBtn = (Button) this.findViewById(R.id.dialog_common_connect_btn);
        Button cancelBtn = (Button) this.findViewById(R.id.dialog_common_cancel_btn);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onWifiConnectDialogListener != null) {
                    onWifiConnectDialogListener.onConfirm(createNonePinConfig(result));
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onWifiConnectDialogListener != null) {
                    onWifiConnectDialogListener.onCancel();
                }
            }
        });
    }

    private WifiConfiguration createNonePinConfig(ScanResult result) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + result.SSID + "\"";

        // do not add properties about wep !!!
//        config.wepKeys[0] = "";
//        config.wepTxKeyIndex = 0;

        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        return config;
    }

}

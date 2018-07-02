package com.androidpotato.page.wifi.widget.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.androidpotato.R;
import com.davidzhou.library.util.ULog;
import com.androidpotato.utils.WifiUtil;


/**
 * Created by David on 2017/6/8 0008.
 */

public class WPAWifiConnectDialog extends BaseWifiConnectDialog {
    private static final String TAG = "WPAWifiConnectDialog";
    private OnWifiConnectDialogListener onWifiConnectDialogListener;
    private ScanResult result;

    public WPAWifiConnectDialog(Context ctx, ScanResult result) {
        super(ctx);
        this.result = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wpa_pin);
        initViews();
    }

    private void initViews() {
        TextView rssi = (TextView) findViewById(R.id.dialog_wpa_pin_rssi_value_tv);
        TextView authType = (TextView) findViewById(R.id.dialog_wpa_pin_auth_type_value_tv);
        rssi.setText(WifiUtil.getRssiLevel(this.getContext(), result.level));
        authType.setText(WifiUtil.getSupportEncryptType(this.getContext(), result.capabilities));
        this.setTitle(result.SSID);
        final Button connect = (Button) this.findViewById(R.id.dialog_common_connect_btn);
        Button cancel = (Button) this.findViewById(R.id.dialog_common_cancel_btn);

        final EditText password = (EditText) this.findViewById(R.id.dialog_wpa_pin_password_et);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    connect.setEnabled(false);
                } else {
                    connect.setEnabled(true);
                }
            }
        });


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onWifiConnectDialogListener != null) {
                    onWifiConnectDialogListener.onConfirm(createWPAPinConfig(password.getText().toString()));
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onWifiConnectDialogListener != null) {
                    onWifiConnectDialogListener.onCancel();
                }
            }
        });
        CheckBox showPassword = (CheckBox) this.findViewById(R.id.dialog_wpa_pin_show_password_cb);
        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        if (password.getText().length() == 0) {
            connect.setEnabled(false);
        }
    }

    private WifiConfiguration createWPAPinConfig(String password) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();

        String capabilities = result.capabilities;

        config.SSID = "\"" + result.SSID + "\"";
        config.preSharedKey = "\"" + password + "\"";
        ULog.i(TAG, "password: " + config.preSharedKey);

        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        if (capabilities.contains("PSK")) {
            ULog.i(TAG, "add PSK");
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        }

        if (capabilities.contains("TKIP")) {
            ULog.i(TAG, "add TKIP");
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        }

        if (capabilities.contains("CCMP")) {
            ULog.i(TAG, "add CCMP");
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        }

        // use WPA
        if (capabilities.contains("WPA-")) {
            ULog.i(TAG, "add WPA");
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        }
        // use WPA2
        if (capabilities.contains("WPA2")) {
            ULog.i(TAG, "add WPA2");
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        }

        return config;
    }

    @Override
    public void setOnWifiConnectDialogListener(OnWifiConnectDialogListener onWifiConnectDialogListener) {
        this.onWifiConnectDialogListener = onWifiConnectDialogListener;
    }
}

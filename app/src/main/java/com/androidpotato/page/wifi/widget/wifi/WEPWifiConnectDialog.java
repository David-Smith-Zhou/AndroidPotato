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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidpotato.R;
import com.davidzhou.library.util.WifiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 2017/6/8 0008.
 */

public class WEPWifiConnectDialog extends BaseWifiConnectDialog {
    private OnWifiConnectDialogListener onWifiConnectDialogListener;
    private ScanResult result;
    private int keyIndex;

    public WEPWifiConnectDialog(Context ctx, ScanResult result) {
        super(ctx);
        this.result = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wep_pin);
        initViews();
    }
    private void initViews() {
        TextView rssi = (TextView) findViewById(R.id.dialog_wep_pin_rssi_value_tv);
        TextView authType = (TextView) findViewById(R.id.dialog_wep_pin_auth_type_value_tv);
        rssi.setText(WifiUtil.getRssiLevel(this.getContext(), result.level));
        authType.setText(WifiUtil.getSupportEncryptType(this.getContext(), result.capabilities));
        this.setTitle(result.SSID);
        final Button connect = (Button) this.findViewById(R.id.dialog_common_connect_btn);
        Button cancel = (Button) this.findViewById(R.id.dialog_common_cancel_btn);

        final EditText password = (EditText) this.findViewById(R.id.dialog_wep_pin_password_et);
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
        Spinner keySpinner = (Spinner) this.findViewById(R.id.dialog_wep_pin_key_index_spinner);
        List<String> keyList = new ArrayList<>(4);
        keyList.add("1");
        keyList.add("2");
        keyList.add("3");
        keyList.add("4");
        keySpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, keyList));
        keySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                keyIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onWifiConnectDialogListener != null) {
                    onWifiConnectDialogListener.onConfirm(createWEPPinConfig(password.getText().toString(), keyIndex));
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
        CheckBox showPassword = (CheckBox) this.findViewById(R.id.dialog_wep_pin_show_password_cb);
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

    private WifiConfiguration createWEPPinConfig(String password, int keyIndex) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();

        String capabilities = result.capabilities;
        config.SSID = "\"" + result.SSID + "\"";
        config.wepKeys[keyIndex] = "\"" + password + "\"";
        config.wepTxKeyIndex = keyIndex;
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
        if (capabilities.contains("TKIP")) {
            config.allowedKeyManagement.set(WifiConfiguration.PairwiseCipher.TKIP);
        }
        if (capabilities.contains("CCMP")) {
            config.allowedKeyManagement.set(WifiConfiguration.PairwiseCipher.CCMP);
        }
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        return config;
    }

    @Override
    public void setOnWifiConnectDialogListener(OnWifiConnectDialogListener onWifiConnectDialogListener) {
        this.onWifiConnectDialogListener = onWifiConnectDialogListener;
    }
}

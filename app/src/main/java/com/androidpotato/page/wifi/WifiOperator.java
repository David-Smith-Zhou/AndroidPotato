package com.androidpotato.page.wifi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;

import com.androidpotato.R;
import com.androidpotato.page.wifi.adapter.ScanResultInfo;
import com.androidpotato.page.wifi.interfaces.OnWifiEventsListener;
import com.androidpotato.page.wifi.widget.wifi.BaseWifiConnectDialog;
import com.androidpotato.page.wifi.widget.wifi.WifiConnectDialogFactory;

import com.davidzhou.library.util.LogUtil;
import com.davidzhou.library.util.ToastUtil;
import com.androidpotato.utils.WifiUtil;

import java.util.List;

/**
 * Created by David on 2017/6/6 0006.
 */

public class WifiOperator {
    private static final String TAG = "WifiOperator";
    private WifiHandler handler;
    private Context context;
    private BroadcastReceiver receiver;

    public WifiOperator(Context context, OnWifiEventsListener onWifiEventsListener) {
        handler = new WifiHandler(onWifiEventsListener);
        this.context = context;
    }

    public boolean isWifiEnable() {
        return getWifiManager().isWifiEnabled();
    }

    public boolean openWifi() {
        return getWifiManager().setWifiEnabled(true);
    }

    public WifiManager getWifiManager() {
        return (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void startScanWifi() {
        if (getWifiManager().isWifiEnabled()) {
            getWifiManager().startScan();
        }
    }

    public void connect(ScanResult result) {
        // 连接分两种情况，一种是已经保存过相关配置，另一种是没保存过相关配置。
        WifiConfiguration config = isSavedConfig(result.SSID);
        if (config != null) {
            connectWithSavedConfig(config);
        } else {
            // 未保存wifi配置的连接
            try {
                BaseWifiConnectDialog dialog = WifiConnectDialogFactory.getInstance().getDialog(context, result);
                dialog.setOnWifiConnectDialogListener(onWifiConnectDialogListener);
                dialog.show();
            } catch (UnsupportedOperationException e) {
                ToastUtil.ToastShort(context, context.getString(R.string.hint_no_suport_wps));
            }
        }
    }

    public void saveConfig() {
        getWifiManager().saveConfiguration();
    }

    public WifiInfo getConnectedWifiInfo() {
        WifiInfo info = getWifiManager().getConnectionInfo();
        if (!"00:00:00:00:00:00".equals(info.getBSSID())) {
            LogUtil.i(TAG, "connected info: " + info.toString());
            return info;
        }
        return null;
    }

    private BaseWifiConnectDialog.OnWifiConnectDialogListener onWifiConnectDialogListener = new BaseWifiConnectDialog.OnWifiConnectDialogListener() {
        @Override
        public void onConfirm(WifiConfiguration config) {
            connectWithConfig(config);
        }

        @Override
        public void onCancel() {

        }
    };

    public void disconnect() {
        if (WifiUtil.isWifiConnected(context)) {
            getWifiManager().disconnect();
        }
    }
    private void connectWithSavedConfig(WifiConfiguration config) {
        if (WifiUtil.isWifiConnected(context)) {
            getWifiManager().disconnect();
        }
        boolean bRst = getWifiManager().enableNetwork(config.networkId, true);
        LogUtil.i(TAG, "bRst = " + bRst);
    }

    private void connectWithConfig(WifiConfiguration config) {
        if (WifiUtil.isWifiConnected(context)) {
            getWifiManager().disconnect();
        }
        int netID = getWifiManager().addNetwork(config);
        LogUtil.i(TAG, "netID: " + netID);
        if (netID != -1) { // add network not failure
            boolean bRst = getWifiManager().enableNetwork(netID, true);
            LogUtil.i(TAG, "bRst = " + bRst);
            getWifiManager().updateNetwork(config);
        } else {
            handler.sendMessage(WifiHandler.ERROR_CONNECT_FAILURE, context.getString(R.string.result_connect_failure));
        }
    }

    public void removeSavedConfig(ScanResultInfo info) {
        String ssid = null;
        if (info.isConnect()) {
            ssid = info.getWifiInfo().getSSID().substring(1, info.getWifiInfo().getSSID().length() - 1);
        } else {
            ssid = info.getScanResult().SSID;
        }
        if (ssid != null) {
            WifiConfiguration savedConfig = isSavedConfig(ssid);
            if (savedConfig != null) {
                LogUtil.i(TAG, "delete network id: " + savedConfig.networkId);
//            getWifiManager().disableNetwork(savedConfig.networkId);
                getWifiManager().removeNetwork(savedConfig.networkId);
            }
        }
    }
    public List<WifiConfiguration> getConfigs() {
        return getWifiManager().getConfiguredNetworks();
    }

    @Nullable
    public WifiConfiguration isSavedConfig(String ssid) {
        List<WifiConfiguration> configs = getConfigs();
        LogUtil.i(TAG, "configs size is : " + configs.size());
        if (configs.size() != 0) {
            for (WifiConfiguration each : configs) {
                LogUtil.i(TAG, "saved config: " + each.SSID);

                if (("\"" + ssid + "\"").equals(each.SSID)) {
                    LogUtil.i(TAG, "find saved config");
                    return each;
                }
            }
        }
        return null;
    }

    public List<ScanResult> getScanResults() {
        return getWifiManager().getScanResults();
    }

    public void registerBroadcastReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LogUtil.d(TAG, "go into receiver");
                LogUtil.d(TAG, "action: " + intent.getAction());
                switch (intent.getAction()) {
                    case WifiManager.WIFI_STATE_CHANGED_ACTION:
                        handler.sendMessage(WifiHandler.MSG_TYPE_WIFI_STATUS_CHANGED,
                                getWifiManager().getWifiState());
                        break;
                    case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                        handler.sendMessage(WifiHandler.MSG_TYPE_WIFI_ON_SCAN_RESULT_AVAILABLE,
                                getWifiManager().getScanResults());
                        break;
                    case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                        handler.sendMessage(WifiHandler.MSG_TYPE_NETWORK_STATUS_CHANGED, info);
                    default:
                        break;
                }
            }
        };
        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.ACTION_PICK_WIFI_NETWORK);

        context.registerReceiver(receiver, filter);
    }

    public void unregisterReceiver() {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }
}

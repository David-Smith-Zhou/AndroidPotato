package com.androidpotato.page.wifi;

import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidpotato.R;
import com.androidpotato.page.wifi.adapter.ScanResultInfo;
import com.androidpotato.page.wifi.adapter.WifiItemAdapter;
import com.androidpotato.page.wifi.interfaces.OnWifiEventsListener;
import com.androidpotato.page.wifi.widget.editor.WifiEditorDialog;
import com.davidzhou.library.util.ULog;
import com.davidzhou.library.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 2017/6/5 0005.
 */

public class WifiActivity extends AppCompatActivity {
    private static final String TAG = "WifiActivity";
    private static final int delayTime = 1500;
    private List<ScanResultInfo> scanResults;
    private WifiItemAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private TextView textView;
    private WifiOperator wifiOperator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_wifi);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.wifi_toolbar);
        toolbar.setTitle(getResources().getString(R.string.wifiSettings));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scanResults = new ArrayList<>();

        swipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.content_wifi_SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reScanWifi();
            }
        });

        listView = (ListView) this.findViewById(R.id.content_wifi_ListView);
        listView.setOnScrollListener(onScrollListener);

        wifiOperator = new WifiOperator(WifiActivity.this, onWifiEventsListener);

        adapter = new WifiItemAdapter(this, scanResults);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);


        textView = (TextView) this.findViewById(R.id.content_wifi_constant_hint_tv);
        setHint(getResources().getString(R.string.loading));
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ScanResultInfo info = scanResults.get(position);
            if (info.isConnect()) {
                WifiEditorDialog dialog = new WifiEditorDialog(WifiActivity.this, info);
                dialog.setOnWifiEditorDialogCallback(onWifiEditorDialogCallback);
                dialog.show();
            } else if (info.isSaved()) {
                WifiEditorDialog dialog = new WifiEditorDialog(WifiActivity.this, info);
                dialog.setOnWifiEditorDialogCallback(onWifiEditorDialogCallback);
                dialog.show();
            } else {
                wifiOperator.connect(scanResults.get(position).getScanResult());
            }
        }
    };

    private WifiEditorDialog.OnWifiEditorDialogCallback onWifiEditorDialogCallback = new WifiEditorDialog.OnWifiEditorDialogCallback() {
        @Override
        public void onConfirm(ScanResultInfo info) {
            if (info.isConnect()) {
                wifiOperator.disconnect();
                reScanWifi();
            } else {
                wifiOperator.connect(info.getScanResult());
            }
        }

        @Override
        public void onNoSaving(ScanResultInfo info) {
            wifiOperator.removeSavedConfig(info);
            reScanWifi();
        }

        @Override
        public void onCancel() {

        }
    };

    private OnWifiEventsListener onWifiEventsListener = new OnWifiEventsListener() {
        @Override
        public void onWifiStatusChanged(int status) {
            String statusStr;
            switch (status) {
                case WifiManager.WIFI_STATE_DISABLED:
                    statusStr = "WIFI_STATE_DISABLED";
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    statusStr = "WIFI_STATE_DISABLING";
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    statusStr = "WIFI_STATE_ENABLING";
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    statusStr = "WIFI_STATE_ENABLED";
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                default:
                    statusStr = "WIFI_STATE_UNKNOWN";
                    break;
            }
            ULog.i(TAG, "wifi status changed: " + statusStr);
        }

        @Override
        public void onScanResultAvailable(List<ScanResult> results) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            ULog.i(TAG, "scan scanResults available");
            displayScanResult(wifiOperator.getScanResults());
        }

        @Override
        public void onNetworkStatusChanged(NetworkInfo info) {
            NetworkInfo.DetailedState status = info.getDetailedState();
            ULog.d(TAG, "status: " + status);
            if (status == NetworkInfo.DetailedState.CONNECTED) {
                ULog.i(TAG, "CONNECTED");
                //保存连接信息，才能实现开机自动连接wifi
                wifiOperator.saveConfig();
                reScanWifi();
                ToastUtil.ToastShort(WifiActivity.this, getString(R.string.result_connected));
//                SharedPreferencesOperator.setIsBoot(WifiActivity.this, false);
                exit();
            }
            if (status == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                ULog.i(TAG, "OBTAINING_IPADDR");
                ToastUtil.ToastShort(WifiActivity.this, getString(R.string.result_obtant_ip_addr));
            }
            if (status == NetworkInfo.DetailedState.AUTHENTICATING) {
                ULog.i(TAG, "AUTHENTICATING");
                ToastUtil.ToastShort(WifiActivity.this, getString(R.string.result_authenticating));
            }
            if (status == NetworkInfo.DetailedState.FAILED) {
                ULog.i(TAG, "FAILED");
                ToastUtil.ToastShort(WifiActivity.this, getString(R.string.result_connect_failure));
            }
            if (status == NetworkInfo.DetailedState.CONNECTING) {
                ULog.i(TAG, "CONNECTING");
//                ToastUtil.ToastShort(WifiActivity.this, "CONNECTING");
            }
            if (status == NetworkInfo.DetailedState.DISCONNECTED) {
                ULog.i(TAG, "DISCONNECTED");
//                reScanWifi();
//                ToastUtil.ToastShort(WifiActivity.this, "DISCONNECTED");
            }
            if (status == NetworkInfo.DetailedState.DISCONNECTING) {
                ULog.i(TAG, "DISCONNECTING");
//                ToastUtil.ToastShort(WifiActivity.this, "DISCONNECTING");
            }

        }

        @Override
        public void onError(int errCode, String msg) {
            ToastUtil.ToastLong(WifiActivity.this, msg);
        }
    };

    private void exit() {
//        Handler handler = new Handler(Looper.getMainLooper());
//        final BootWizardApplication application = (BootWizardApplication) getApplication();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                application.addActivity(WifiActivity.this);
//                application.exit();
//            }
//        }, delayTime);
    }

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            boolean enable = false;
            if (listView != null && listView.getChildCount() > 0) {
                boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                enable = firstItemVisible && topOfFirstItemVisible;
            }
            swipeRefreshLayout.setEnabled(enable);
        }
    };

    private void displayScanResult(List<ScanResult> results) {
        if (results.size() == 0) {
            setHint(getResources().getString(R.string.noFoundHint));
            swipeRefreshLayout.setEnabled(true);
        } else {
            addItemsToListView(results);
            adapter.notifyDataSetChanged();
            showListView();
        }
    }

    private void reScanWifi() {
        scanResults.clear();
        adapter.notifyDataSetChanged();
        setHint(getResources().getString(R.string.loading));
        wifiOperator.startScanWifi();
    }

    private void addItemsToListView(List<ScanResult> results) {
        WifiInfo info = wifiOperator.getConnectedWifiInfo();
        addConnectedItems(info);
        addOtherItems(results, info);
    }

    private void addOtherItems(List<ScanResult> results, WifiInfo info) {
        if (info == null) {
            addOtherItemsWithoutConnectItem(results);
        } else {
            addOtherItemsWithConnectItem(results, info);
        }
    }

    private void addOtherItemsWithConnectItem(List<ScanResult> results, WifiInfo info) {
        for (ScanResult each : results) {
            ULog.i(TAG, "get wifi: " + each.SSID + ", auth type: " + each.capabilities + ", level: " + each.level + ", bssid: " + each.BSSID);
            if (!info.getBSSID().equals(each.BSSID)) {
                ScanResultInfo scanResultInfo = new ScanResultInfo(each);
                if (isSavedConfig(each)) {
                    scanResultInfo.setSaved(true);
                    if (!scanResults.contains(scanResultInfo)) {
                        scanResults.add(1, scanResultInfo);
                    }
                } else {
                    if (!scanResults.contains(scanResultInfo)) {
                        scanResults.add(scanResultInfo);
                    }
                }
            }
        }
    }

    private void addOtherItemsWithoutConnectItem(List<ScanResult> results) {
        for (ScanResult each : results) {
            ULog.i(TAG, "get wifi: " + each.SSID + ", auth type: " + each.capabilities + ", level: " + each.level + ", bssid: " + each.BSSID);
            ScanResultInfo scanResultInfo = new ScanResultInfo(each);
            if (isSavedConfig(each)) {
                scanResultInfo.setSaved(true);
                if (!scanResults.contains(scanResultInfo)) {
                    scanResults.add(0, scanResultInfo);
                }
            } else {
                if (!scanResults.contains(scanResultInfo)) {
                    scanResults.add(scanResultInfo);
                }
            }
        }
    }

    private void addConnectedItems(WifiInfo info) {
        if (info != null) {
            ScanResultInfo scanResultInfo = new ScanResultInfo(info);
            scanResultInfo.setConnect(true);
            scanResults.add(scanResultInfo);
        }
    }

    private boolean isSavedConfig(ScanResult result) {
        List<WifiConfiguration> configs = wifiOperator.getConfigs();
        for (WifiConfiguration config : configs) {
            if (config.SSID != null) {
                String ssid = config.SSID.substring(1, config.SSID.length() - 1);
//            ULog.i(TAG, "saved config: SSID: " + ssid + ", BSSID: " + config.BSSID);
                if (result.SSID.equals(ssid)) {
                    return true;
                }
            }

        }
        return false;
    }

    private void setHint(String hint) {
        listView.setVisibility(View.GONE);
        textView.setText(hint);
        textView.setVisibility(View.VISIBLE);
    }

    private void showListView() {
        listView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        wifiOperator.unregisterReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wifiOperator.registerBroadcastReceiver();
        if (!wifiOperator.isWifiEnable()) {
            wifiOperator.openWifi();
        } else {
            reScanWifi();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main_acitivity, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//
//            case R.id.action_wps:
//                ToastUtil.ToastShort(this, "wps");
//                return true;
//
//            case R.id.action_wps_with_pin:
//                ToastUtil.ToastShort(this, "wps with pin");
//                return true;
//
//            case R.id.action_settings:
//                ToastUtil.ToastShort(this, "settings");
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}

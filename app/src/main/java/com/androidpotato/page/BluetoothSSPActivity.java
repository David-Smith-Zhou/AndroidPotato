package com.androidpotato.page;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.androidpotato.R;
import com.androidpotato.adapter.BtSSPDeviceAdapter;
import com.androidpotato.mylibrary.bluetooth.BluetoothBase;
import com.androidpotato.mylibrary.bluetooth.BluetoothSSP;
import com.androidpotato.mylibrary.util.UtilLog;
import com.androidpotato.mylibrary.util.UtilToast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by David on 2017/7/6 0006.
 */

public class BluetoothSSPActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "BluetoothSSPActivity";
    private static final int OPEN_BLUETOOTH_CODE = 0x02;

    private List<BluetoothDevice> devices;
    private BtSSPDeviceAdapter adapter;
    private BluetoothBase bluetoothBase;
    private int requestCode = 0x01;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_bluetooth_ssp);
        initView();
    }

    private void initView() {
        bluetoothBase = new BluetoothSSP(this);
        bluetoothBase.setOnBluetoothListener(onBluetoothListener);
        bluetoothBase.start();
        devices = new ArrayList<BluetoothDevice>();
        adapter = new BtSSPDeviceAdapter(this, devices);
        ListView listView = (ListView) this.findViewById(R.id.bluetooth_ssp_lv);
        listView.setAdapter(adapter);
        bluetoothBase.requestPermission(this, requestCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothBase.registerBroadcast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bluetoothBase.unRegisterBroadcast();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int index = 0; index < permissions.length; index++) {
            if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permissions[index])) {
                if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "用户允许使用蓝牙", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "用户拒绝使用蓝牙", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void turnOnBluetooth() {
        Intent requestBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//        requestBluetooth.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        requestBluetooth.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);     // 可发现时间，默认120秒
        startActivityForResult(requestBluetooth, OPEN_BLUETOOTH_CODE);
    }

    private BluetoothBase.OnBluetoothListener onBluetoothListener = new BluetoothBase.OnBluetoothListener() {
        @Override
        public void onFind(List<BluetoothDevice> bluetoothDevices, BluetoothDevice device, int rssi) {
            if (!devices.contains(device)) {
                devices.add(device);
                UtilLog.i(TAG, "devices = " + devices.size());
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onScanFinished(List<BluetoothDevice> bluetoothDevices) {

        }

        @Override
        public void onRead(byte[] data, BluetoothDevice device) {

        }

        @Override
        public void onConnected(BluetoothDevice device) {

        }

        @Override
        public void onDisconnected(BluetoothDevice device) {

        }

        @Override
        public void onTick(int interval, boolean isBluetoothEnabled, boolean isConnected, List<BluetoothDevice> connectedDevices) {

        }

        @Override
        public void onUpdateDeviceRssi(BluetoothDevice device, short rssi) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_BLUETOOTH_CODE:
                UtilLog.i(TAG, "go into OPEN_BLUETOOTH_CODE" + ", resultCode = " + resultCode);
                if (resultCode == Activity.RESULT_OK) {
                    UtilToast.ToastShort(this, "用户允许使用蓝牙");
                } else {
                    UtilToast.ToastShort(this, "用户拒绝使用蓝牙");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bluetooth_ssp_scan_btn:
                UtilLog.i(TAG, "scan");
                bluetoothBase.scan();
                break;
            case R.id.bluetooth_ssp_disconnect_btn:
                bluetoothBase.disconnect("dfd");
                break;
            case R.id.bluetooth_ssp_debug_btn:
                turnOnBluetooth();
                break;
            default:
                break;
        }
    }
}

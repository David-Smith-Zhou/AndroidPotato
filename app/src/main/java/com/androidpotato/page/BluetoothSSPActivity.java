package com.androidpotato.page;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.androidpotato.R;
import com.androidpotato.adapter.BtSSPDeviceAdapter;
import com.androidpotato.mylibrary.bluetooth.BluetoothBase;
import com.androidpotato.mylibrary.bluetooth.BluetoothSSP;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 2017/7/6 0006.
 */

public class BluetoothSSPActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothSSPActivity";

    private List<BluetoothDevice> devices;
    private BtSSPDeviceAdapter adapter;
    private BluetoothBase bluetoothBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_bluetooth_ssp);
        initView();
    }

    private void initView() {
        bluetoothBase = new BluetoothSSP(this);
        bluetoothBase.setOnBluetoothListener(onBluetoothListener);
        devices = new ArrayList<>();
        adapter = new BtSSPDeviceAdapter(this, devices);
        ListView listView = (ListView) this.findViewById(R.id.bluetooth_ssp_lv);
        listView.setAdapter(adapter);
    }

    private BluetoothBase.OnBluetoothListener onBluetoothListener = new BluetoothBase.OnBluetoothListener() {
        @Override
        public void onFind(List<BluetoothDevice> bluetoothDevices, BluetoothDevice device, int rssi) {
            if (!devices.contains(device)) {
                devices.add(device);
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

}

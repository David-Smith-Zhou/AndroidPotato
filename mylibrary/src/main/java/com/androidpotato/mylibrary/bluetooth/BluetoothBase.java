package com.androidpotato.mylibrary.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class BluetoothBase {
    public static final int PROTOCOL_TYPE_SPP = 0;
    public static final int PROTOCOL_TYPE_GATT = 1;
    public static final int PROTOCOL_TYPE_AUTO = 2;


    public static final int ON_CONNECTED_DEVICE = 1;
    public static final int ON_BLUETOOTH_RECEIVED = 2;

    protected OnBluetoothListener onBluetoothListener;
    protected List<OnBluetoothListener> onBluetoothListeners = new ArrayList<OnBluetoothListener>();

    public abstract void start(String uuidService, String uuidCharacteristicWrite, String uuidCharacteristicRead, String uuidCharacteristicReadDescriptor);

    public abstract void stop();

    public abstract void setAutoEnableBluetooth(boolean isAutoEnable);

    public abstract void enable();

    public abstract void disable();

    public abstract List<BluetoothDevice> getPairedDevices();

    public abstract void scan();

    public abstract void cancelScan();

    public abstract void write(byte[] request, String address);

    public abstract boolean connect(String address);

    public abstract boolean connect(BluetoothDevice device);

    public abstract void disconnect(String address);

    public abstract boolean isConnected(String address);

    public interface OnBluetoothListener {
        void onFind(List<BluetoothDevice> bluetoothDevices, BluetoothDevice device, int rssi);

        void onScanFinished(List<BluetoothDevice> bluetoothDevices);

        void onRead(byte[] data, BluetoothDevice device);

        void onConnected(BluetoothDevice device);

        void onDisconnected(BluetoothDevice device);

        void onTick(int interval, boolean isBluetoothEnabled, boolean isConnected, List<BluetoothDevice> connectedDevices);

        void onUpdateDeviceRssi(BluetoothDevice device, short rssi);
    }

    public void setOnBluetoothListener(OnBluetoothListener listener) {
        onBluetoothListener = listener;
    }

    public void addOnBluetoothListener(OnBluetoothListener listener) {
        if (listener != null) {
            onBluetoothListeners.add(listener);
//			listeners.add(onBluetoothListener);
        }
    }

    public void removeOnBluetoothListener(OnBluetoothListener listener) {
        if (listener != null) {
            onBluetoothListeners.remove(listener);
        }
    }

    public static boolean isSupportBLE(Context context) {
        boolean isSupportBLE = context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le");
        return isSupportBLE;
    }
}

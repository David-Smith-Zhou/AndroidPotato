package com.androidpotato.mylibrary.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.androidpotato.mylibrary.util.TypeConvert;
import com.androidpotato.mylibrary.util.UtilLog;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



/**
 * Created by David on 2016/10/13 0013.
 */

public class BluetoothSSP extends BluetoothBase {
    private static final String TAG = "BluetoothSSP";
    private Context context;
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter bluetoothAdapter;
    private boolean isConnected = false;
    private BluetoothDevice bondedDevice;
    private List<BluetoothDevice> bluetoothDevices;
    private BluetoothSocket clientSocket;
    private BTListenThread btListenThread;
    private Handler handler;

    public BluetoothSSP(Context context) {
        super();
        this.context = context;
        bluetoothDevices = new ArrayList<BluetoothDevice>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case ON_CONNECTED_DEVICE:
                        boolean isSuccess = (Boolean) msg.obj;
                        if (onBluetoothListener != null) {
                            onBluetoothListener.onConnected(isSuccess ? bondedDevice : null);
                        }
                        break;
                    case ON_BLUETOOTH_RECEIVED:
                        int length = msg.arg1;
                        byte[] data = (byte[]) msg.obj;
                        if (onBluetoothListener != null) {
                            onBluetoothListener.onRead(data, bondedDevice);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void start() {
        start(null, null, null, null);
    }

    @Override
    public void start(String uuidService, String uuidCharacteristicWrite, String uuidCharacteristicRead, String uuidCharacteristicReadDescriptor) {
        registerBroadcast();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        } else {
            // 更新蓝牙使用的状态，bluetoothAdapter.isEnabled()，没别的作用
            if (onBluetoothListener != null) {
                onBluetoothListener.onTick(1000, bluetoothAdapter.isEnabled(), isConnected, bluetoothDevices);
            }
        }

    }

    @Override
    public void stop() {
        cancelScan();
        if (clientSocket != null) {
            try {
                clientSocket.close();
                clientSocket = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (btListenThread != null) {
            btListenThread.setIsListen(false);
            btListenThread = null;
        }

        unRegisterBroadcast();

    }

    @Override
    public void scan() {
        if (bluetoothAdapter.isEnabled()) {
            cancelScan();
            bluetoothDevices.clear();
            bluetoothAdapter.startDiscovery();
        }

    }

    @Override
    public void cancelScan() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    @Override
    public void disconnect(String address) {
        try {
            if (clientSocket != null) {
                if (btListenThread != null) {
                    btListenThread.setIsListen(false);
                }
                clientSocket.close();
                clientSocket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enable() {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.enable();
        }
    }

    @Override
    public void disable() {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.disable();
        }
    }

    @Override
    public boolean connect(String address) {
        boolean rst = false;
        cancelScan();
        bondedDevice = bluetoothAdapter.getRemoteDevice(address);
        clientSocket = getBluetoothSocket(bondedDevice);
        if (clientSocket != null) {
            BTConnectThread btConnectThread = new BTConnectThread(clientSocket);
            btConnectThread.setOnBluetoothThreadListener(onBluetoothThreadListener);
            rst = true;
        }
        return rst;
    }

    @Override
    public boolean connect(BluetoothDevice device) {
        boolean rst = false;
        cancelScan();
        bondedDevice = bluetoothAdapter.getRemoteDevice(device.getAddress());
        clientSocket = getBluetoothSocket(bondedDevice);
        if (clientSocket != null) {
            BTConnectThread btConnectThread = new BTConnectThread(clientSocket);
            btConnectThread.setOnBluetoothThreadListener(onBluetoothThreadListener);
            btConnectThread.start();
            rst = true;
        }
        return rst;
    }

    @Override
    public boolean isConnected(String address) {
        return isConnected;
    }

    @Override
    public void setAutoEnableBluetooth(boolean isAutoEnable) {

    }

    @Override
    public void write(byte[] request, String address) {
        if (clientSocket == null) {
            UtilLog.e(TAG, "clientSocket is NULL");
        } else {
            try {
                OutputStream outputStream = clientSocket.getOutputStream();
                outputStream.write(request);
                UtilLog.i(TAG, "send: " + TypeConvert.bytesToHexString(request));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<BluetoothDevice> getPairedDevices() {
        return new ArrayList<BluetoothDevice>(bluetoothAdapter.getBondedDevices());
    }

    @Nullable
    private BluetoothSocket getBluetoothSocket(BluetoothDevice bluetoothDevice) {
        try {
            UtilLog.v(TAG, "Create Insecure socket");
            BluetoothSocket insecureSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID_SECURE);
            return insecureSocket;
        } catch (IOException ioE) {
            UtilLog.v(TAG, "Create Insecure socket failed");
            ioE.printStackTrace();
            try {
                UtilLog.v(TAG, "Create Secure socket");
                BluetoothSocket secureSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
                return secureSocket;
            } catch (Exception e) {
                UtilLog.v(TAG, "Create Secure socket failed");
                e.printStackTrace();
                return null;
            }
        }
    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(btDiscoveryReceiver, filter);
    }

    private void unRegisterBroadcast() {
        context.unregisterReceiver(btDiscoveryReceiver);
    }

    private BroadcastReceiver btDiscoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                UtilLog.i(TAG, "Discovery Started...");
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                if (onBluetoothListener != null) {
                    onBluetoothListener.onScanFinished(bluetoothDevices);
                }
                UtilLog.i(TAG, "Discovery Finished");
            } else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice findDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                UtilLog.i(TAG, "Find -- name: " + findDevice.getName() + " addr: " + findDevice.getAddress());
                if (!bluetoothDevices.contains(findDevice)) {
                    bluetoothDevices.add(findDevice);
                    if (onBluetoothListener != null) {
                        onBluetoothListener.onFind(bluetoothDevices, findDevice, rssi);
                    }
                }
                if (onBluetoothListener != null) {
                    onBluetoothListener.onUpdateDeviceRssi(findDevice, rssi);
                }

            } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                if (btListenThread != null) {
                    btListenThread.setIsListen(false);
                    btListenThread = null;
                }
                isConnected = false;
                BluetoothDevice tempDevice = bondedDevice;
                bondedDevice = null;
                if (onBluetoothListener != null) {
                    onBluetoothListener.onDisconnected(tempDevice);
                }
//                removePairedDevice(bondedDevice);
            } else if(action.equals((BluetoothAdapter.ACTION_STATE_CHANGED))) {
                int status = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (status) {
                    case BluetoothAdapter.STATE_OFF:
                        UtilLog.d(TAG, "Bluetooth: STATE_OFF");
                        if (onBluetoothListener != null) {
                            onBluetoothListener.onTick(1000, bluetoothAdapter.isEnabled(), isConnected, bluetoothDevices);
                        }
                        break;
                    case BluetoothAdapter.STATE_ON:
                        UtilLog.d(TAG, "Bluetooth: STATE_ON");
                        if (onBluetoothListener != null) {
                            onBluetoothListener.onTick(1000, bluetoothAdapter.isEnabled(), isConnected, bluetoothDevices);
                        }
                        break;
                    default:
                        break;
                }

            } else {
                UtilLog.i(TAG, "Discovery get unknown result");
            }
        }
    };

    private BTConnectThread.OnBluetoothThreadListener onBluetoothThreadListener = new BTConnectThread.OnBluetoothThreadListener() {
        @Override
        public void OnConnect(BluetoothSocket bluetoothSocket, boolean isSuccess) {
            try {
                if (isSuccess) {
                    btListenThread = new BTListenThread(bluetoothSocket, handler);
                    btListenThread.start();
                    Message msg = new Message();
                    msg.what = ON_CONNECTED_DEVICE;
                    msg.obj = true;
                    isConnected = true;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = ON_CONNECTED_DEVICE;
                    msg.obj = false;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = ON_CONNECTED_DEVICE;
                msg.obj = false;
                handler.sendMessage(msg);
            }
        }
    };
}

package com.androidpotato.page.bluetoothphone;

import android.app.Activity;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by David on 2018/7/2.
 */

public class BtPhoneManager {
    private static BtPhoneManager mBtPhoneManager;

    private BtPhoneManager() {

    }
    public static BtPhoneManager getInstance() {
        if (mBtPhoneManager == null) {
            mBtPhoneManager = new BtPhoneManager();
        }
        return mBtPhoneManager;
    }
    public void goToBtSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        activity.startActivity(intent);
    }
    public void connect() {
    }
}

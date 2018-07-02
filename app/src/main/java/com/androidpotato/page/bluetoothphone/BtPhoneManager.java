package com.androidpotato.page.bluetoothphone;

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
}

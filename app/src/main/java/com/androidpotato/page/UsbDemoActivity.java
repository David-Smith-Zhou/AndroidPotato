package com.androidpotato.page;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.androidpotato.R;
import com.davidzhou.library.communication.usb.UsbBase;

/**
 * Created by David on 2016/12/6 0006.
 */

public class UsbDemoActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "usbDemoActivity";
    private UsbBase mUsbBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_usb_demo);
        mUsbBase = new UsbBase(UsbDemoActivity.this);
        mUsbBase.registerBroadcast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUsbBase.unRegisterBroadcast();
    }

    @Override
    public void onClick(View v) {

    }
}

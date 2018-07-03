package com.androidpotato.page.bluetoothphone;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.androidpotato.common.CommonTestCallback;
import com.androidpotato.page.test.TestTemplateActivity;
import com.davidzhou.library.util.ULog;

/**
 * Created by David on 2018/7/2.
 */

public class BluetoothPhoneActivity extends TestTemplateActivity implements CommonTestCallback{

    private static final String TAG = "BluetoothPhoneActivity";
    private BtPhoneManager mManger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCommonTestCallback(this);
        mManger = BtPhoneManager.getInstance();
    }

    @Override
    public void start() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onBtnClicked(BtnIndex position) {
        switch (position) {
            case BTN_INDEX_1:
                ULog.i(TAG, "BTN_INDEX_1");
                mManger.goToBtSettings(this);
                break;
            case BTN_INDEX_2:
                ULog.i(TAG, "BTN_INDEX_2");
                break;
            case BTN_INDEX_3:
                ULog.i(TAG, "BTN_INDEX_3");
                break;
            case BTN_INDEX_4:
                ULog.i(TAG, "BTN_INDEX_4");
                break;
            case BTN_INDEX_5:
                ULog.i(TAG, "BTN_INDEX_5");
                break;
            case BTN_INDEX_6:
                ULog.i(TAG, "BTN_INDEX_6");
                break;
            case BTN_UNKNOWN:
                ULog.i(TAG, "BTN_UNKNOWN");
                break;
        }
    }
}

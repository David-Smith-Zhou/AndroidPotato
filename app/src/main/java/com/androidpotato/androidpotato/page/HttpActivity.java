package com.androidpotato.androidpotato.page;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.androidpotato.androidpotato.R;
import com.androidpotato.mylibrary.util.UtilLog;


/**
 * Created by David on 2017/6/26 0026.
 */

public class HttpActivity extends Activity {
    private static final String TAG = "HttpActivity";
    private static final int BRANCH_CODE = 1;
    private static final String DEVICE_SN = "testvirtualdevice";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_http);
        connect();
    }

    private void connect() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("pay.smit.com.cn")
                .appendPath("smitup")
                .appendPath("do")
                .appendPath("update")
                .appendPath("check")
                .appendPath("apk")
                .appendQueryParameter("device_sn", DEVICE_SN)
                .appendQueryParameter("version_code", "" + 1)
                .appendQueryParameter("version_name", "V1.0")
                .appendQueryParameter("package_name", "com.smit.terminalapp")
                .appendQueryParameter("branch_code", "" + BRANCH_CODE);
        String url = builder.build().toString();
        UtilLog.i(TAG, "url: " + url);

    }
}

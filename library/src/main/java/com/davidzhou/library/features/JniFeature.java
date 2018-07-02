package com.davidzhou.library.features;

import android.support.annotation.Nullable;

import com.davidzhou.library.util.ULog;

import java.io.UnsupportedEncodingException;

/**
 * Created by David on 2018/4/26 0026.
 */

public class JniFeature {
    private static final String TAG = "JniFeature";
    static {
        System.loadLibrary("native_lib");
    }
    public String getStringWithNoneStaticMethod() {
        ULog.i(TAG, "getStringWithNoneStaticMethod");
        return "getStringWithNoneStaticMethod";
    }
    @Nullable
    public static byte[] getBytesWithStaticMethod() {
        ULog.i(TAG, "getBytesWithStaticMethod");
        try {
            return "getBytesWithStaticMethod".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static native int getRandom();
    public static native String getFormatString(String msg);
}

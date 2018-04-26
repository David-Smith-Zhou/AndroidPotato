package com.davidzhou.library.features;

/**
 * Created by David on 2018/4/26 0026.
 */

public class JniFeature {
    static {
        System.loadLibrary("native_lib");
    }
    public static native int getRandom();
    public static native String getFormatString(String msg);
}

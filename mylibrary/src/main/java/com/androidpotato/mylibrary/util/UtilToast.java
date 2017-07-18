package com.androidpotato.mylibrary.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by David on 2017/5/26 0026.
 */

public class UtilToast {
    private static final boolean isShow = true;

    public static void ToastShort(Context context, String msg) {
        if (isShow) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
    public static void ToastLong(Context context, String msg) {
        if (isShow) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }
}

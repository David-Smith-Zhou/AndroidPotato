package com.davidzhou.library.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by David on 2017/5/24 0024.
 */

public class ToastUtil {
    private static final boolean isShow = true;

    public static void ToastShort(Context context, String msg) {
        if (isShow) {
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
    public static void ToastLong(Context context, String msg) {
        if (isShow) {
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}

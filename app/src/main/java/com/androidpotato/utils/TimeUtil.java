package com.androidpotato.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    private static final String DEFAULT_FMT = "yyyy-MM-dd HH:mm:ss";
    /**
     * @param time void
     * @Description: 当前线程暂停time毫秒
     */
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static String currDateStr() {
        return format(DEFAULT_FMT);
    }

    /**
     * 获得当前时间的指定格式日期
     *
     * @param style yyyy-MM-dd HH:mm:ss.SSS
     * @return String
     */
    public static String format(String style) {
        return format(style, new Date());
    }

    /**
     * 获得指定时间的指定格式日期
     *
     * @param style yyyy-MM-dd HH:mm:ss.SSS
     * @param time
     * @return String
     */
    public static String format(String style, long time) {
        return format(style, new Date(time));
    }

    /**
     * 获得指定时间的指定格式日期
     *
     * @param style yyyy-MM-dd HH:mm:ss.SSS
     * @param time
     * @return String 格式化失败返回空字符串
     */
    public static String format(String style, String time) {
        if (TextUtils.isEmpty(time)) return "";
        long temp = 0;
        try {
            temp = Long.parseLong(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (temp == 0) return "";
        return format(style, new Date(temp));
    }

    /**
     * 获得指定时间的指定格式日期
     *
     * @param style yyyy-MM-dd HH:mm:ss.SSS
     * @param time
     * @return String 格式化失败返回空字符串
     */
    public static String format(String style, Date time) {
        if (TextUtils.isEmpty(style)) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(style, Locale.CHINA);
        return sdf.format(time);
    }
}

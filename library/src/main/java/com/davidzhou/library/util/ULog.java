package com.davidzhou.library.util;

import android.util.Log;

import java.util.Locale;

/**
 * Created by David on 2017/5/4 0004.
 */

public class ULog {
    public static final boolean isShow = true;

    public static final int LOG_DIRECTION_LOGCAT = 0x01;
    public static final int LOG_DIRECTION_JAVA_CONSOLE = 0x02;
    public static final int LOG_DIRECTION_FILE = 0x03;

    public static final int LEVLE_DEBUG = 0x21;
    public static final int LEVLE_INFO = 0x22;
    public static final int LEVLE_WARING = 0x23;
    public static final int LEVLE_ERROR = 0x24;

    private static int mLogDir = LOG_DIRECTION_LOGCAT;

    public static void i(String tag, String msg) {
        log(ULog.LEVLE_INFO, tag, msg);
    }
    public static void d(String tag, String msg) {
        log(ULog.LEVLE_DEBUG, tag, msg);
    }
    public static void w(String tag, String msg) {
        log(ULog.LEVLE_WARING, tag, msg);
    }
    public static void e(String tag, String msg) {
        log(ULog.LEVLE_ERROR, tag, msg);
    }

    public static void log(int level, String tag, String msg) {
        if (isShow) {
            switch (mLogDir) {
                case LOG_DIRECTION_LOGCAT:
                    logToLogCat(level, tag, msg);
                    break;
                case LOG_DIRECTION_JAVA_CONSOLE:
                    logToConsole(level, tag, msg);
                    break;
                case LOG_DIRECTION_FILE:

                    break;
                default:
                    break;
            }
        }
    }
    private static void logToConsole(int level, String tag, String msg) {
        switch (level) {
            case LEVLE_DEBUG:
                print("DEBUG", tag, msg);
                break;
            case LEVLE_ERROR:
                print("ERROR", tag, msg);
                break;
            case LEVLE_INFO:
                print("INFO", tag, msg);
                break;
            case LEVLE_WARING:
                print("WARING", tag, msg);
                break;
            default:
                print("INFO", tag, msg);
                break;
        }
    }
    private static void print(String level, String tag, String msg) {
        System.out.print("[" + level + "][" + tag + "]: " + msg + '\n');
    }
    private static void logToLogCat(int level, String tag, String msg) {
        String fmt = "[Thread id: %1$d, name- %2$s]: %3$s";
        String data = String.format(Locale.getDefault(), fmt,
                Thread.currentThread().getId(),
                Thread.currentThread().getName(),
                msg);
        switch (level) {
            case LEVLE_DEBUG:
                Log.d(tag, data);
                break;
            case LEVLE_ERROR:
                Log.e(tag, data);
                break;
            case LEVLE_INFO:
                Log.i(tag, data);
                break;
            case LEVLE_WARING:
                Log.w(tag, data);
                break;
            default:
                Log.i(tag, data);
                break;
        }
    }
    public void setLogDir(int logDir) {
        this.mLogDir = logDir;
    }
}

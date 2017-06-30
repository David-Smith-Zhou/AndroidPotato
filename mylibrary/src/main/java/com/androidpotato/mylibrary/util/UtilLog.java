package com.androidpotato.mylibrary.util;

import android.util.Log;

/**
 * Created by David on 2017/5/4 0004.
 */

public class UtilLog {
    public static final boolean isShow = true;

    public static final int LOG_DIRECTION_LOGCAT = 0x01;
    public static final int LOG_DIRECTION_JAVA_CONSOLE = 0x02;
    public static final int LOG_DIRECTION_FILE = 0x03;

    public static final int LEVLE_DEBUG = 0x21;
    public static final int LEVLE_INFO = 0x22;
    public static final int LEVLE_WARING = 0x23;
    public static final int LEVLE_ERROR = 0x24;
    public static final int LEVLE_VERBOSE = 0x25;

    private static int mLogDir = LOG_DIRECTION_LOGCAT;

    public static void i(String tag, String msg) {
        log(UtilLog.LEVLE_INFO, tag, msg);
    }
    public static void d(String tag, String msg) {
        log(UtilLog.LEVLE_DEBUG, tag, msg);
    }
    public static void w(String tag, String msg) {
        log(UtilLog.LEVLE_WARING, tag, msg);
    }
    public static void e(String tag, String msg) {
        log(UtilLog.LEVLE_ERROR, tag, msg);
    }
    public static void v(String tag, String msg) {
        log(UtilLog.LEVLE_VERBOSE, tag, msg);
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
            case LEVLE_VERBOSE:
                print("VERBOSE", tag, msg);
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
        switch (level) {
            case LEVLE_DEBUG:
                Log.d(tag, msg);
                break;
            case LEVLE_ERROR:
                Log.e(tag, msg);
                break;
            case LEVLE_INFO:
                Log.i(tag, msg);
                break;
            case LEVLE_WARING:
                Log.w(tag, msg);
                break;
            case LEVLE_VERBOSE:
                Log.v(tag, msg);
            default:
                Log.i(tag, msg);
                break;
        }
    }
    public void setLogDir(int logDir) {
        this.mLogDir = logDir;
    }
}

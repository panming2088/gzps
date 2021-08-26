package com.augurit.am.fw.log.util;

import android.util.Log;


import com.augurit.am.fw.log.AMLogReport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Log打印
 */
public final class AMLog {
    private static boolean DEBUG = true;

    private AMLog() {
    }

    public static void i(String msg) {
        if (AMLogReport.isDebug()) {
            Log.i(_FILE_(), _LINE_() + "  " + msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (AMLogReport.isDebug()) {
            Log.i(TAG, _FILE_() + ":" + _LINE_() + "  " + msg);
        }
    }

    public static void w(String msg) {
        if (AMLogReport.isDebug()) {
            Log.w(_FILE_(), _LINE_() + "  " + msg);
        }
    }

    public static void w(String TAG, String msg) {
        if (AMLogReport.isDebug()) {
            Log.w(TAG, _FILE_() + ":" + _LINE_() + "  " + msg);
        }
    }

    public static void v(String msg) {
        if (AMLogReport.isDebug()) {
            Log.v(_FILE_(), _LINE_() + "  " + msg);
        }
    }

    public static void v(String TAG, String msg) {
        if (AMLogReport.isDebug()) {
            Log.v(TAG, _FILE_() + ":" + _LINE_() + "  " + msg);
        }
    }

    public static void d(String msg) {
        if (AMLogReport.isDebug()) {
            Log.d(_FILE_(), _LINE_() + "  " + msg);
        }
    }

    public static void d(String TAG, String msg) {
        if (AMLogReport.isDebug()) {
            Log.d(TAG, _FILE_() + ":" + _LINE_() + "  " + msg);
        }
    }

    public static void e(String msg) {
        if (AMLogReport.isDebug()) {
            Log.e(_FILE_(), _LINE_() + "  " + msg);
        }
    }

    public static void e(String TAG, String msg) {
        if (AMLogReport.isDebug()) {
            Log.e(TAG, _FILE_() + ":" + _LINE_() + "  " + msg);
        }
    }




    public static String _FILE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        return traceElement.getFileName();
    }

    public static String _FUNC_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        return traceElement.getMethodName();
    }

    public static int _LINE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        return traceElement.getLineNumber();
    }

    public static String _TIME_() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        return sdf.format(now);
    }

}
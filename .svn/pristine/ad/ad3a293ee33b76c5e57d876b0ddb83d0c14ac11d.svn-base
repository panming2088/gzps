package com.augurit.am.fw.utils.log;

import android.support.annotation.Nullable;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * 打印log的工具类,直接调用LogUtil.XXX()就可以打印出log信息，该工具类支持以下形式的Log打印：
 * 支持JSON字符串解析打印
 * 支持XML字符串解析打印
 * 支持无限长字符串打印，
 * 无Logcat4000字符限制
 */
public final class LogUtil {

    public static final String TAG = "AGMobile";

    private LogUtil() {
    }

    /**
     * 设置是否打印Log，默认是进行打印，如果想不打印，通过这个方法，传入false即可（虽然是init方法，但并非一定要调用）
     * 一般是在Application中进行调用，用于管理全局的Log。在单独的lib中，如果非必要，不需要调用
     *
     * @param ifShow 是否打印Log
     */
    public static void init(boolean ifShow) {
        if (!ifShow) {
            Logger.init().
                    methodCount(3).
                    logLevel(LogLevel.NONE);
        } else {
            Logger.init();
        }
    }

    /**
     * 设置是否打印log并且设置全局TAG,设置了这个TAG之后，你在程序中自定义的TAG将被覆盖（虽然是init方法，但并非一定要调用）
     * 一般是在Application中进行调用，用于管理全局的Log。在单独的lib中，如果非必要，不需要调用
     *
     * @param isShowLog 是否显示Log
     * @param tag       全局TAG
     */
    public static void init(boolean isShowLog, @Nullable String tag) {
        if (!isShowLog) {
            Logger.init(tag).
                    logLevel(LogLevel.NONE);
        } else {
            Logger.init(tag);
        }
    }


    public static void v(String msg) {
        Logger.v(msg);
    }

    public static void v(String tag, String msg) {
        Logger.t(tag).v(msg);
    }

    public static void v(Object msg) {
        Logger.d(msg);
    }

    public static void d(String msg) {
        Logger.d(msg);
    }

    public static void d(String tag, String msg) {
        Logger.t(tag).d(msg);
    }

    public static void d(Object msg) {
        Logger.d(msg);
    }

    public static void i(Object msg) {
        Logger.i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        Logger.t(tag).i(msg);
    }

    public static void w(String msg) {
        Logger.w(msg);
    }

    public static void w(String tag, String msg) {
        Logger.t(tag).w(msg);
    }


    public static void e(String msg) {
        Logger.e(msg);
    }

    public static void e(Object msg) {
        Logger.d(msg);
    }

    public static void e(String tag, String msg) {
        Logger.t(tag).e(msg);
    }


    /**
     * 打印Json格式的数据
     *
     * @param jsonFormat Json数据
     */
    public static void json(String jsonFormat) {
        Logger.json(jsonFormat);
    }

    public static void json(String tag, String jsonFormat) {
        Logger.t(tag).json(jsonFormat);
    }

    /**
     * 打印Json格式的数据
     *
     * @param xml xml数据
     */
    public static void xml(String xml) {
        Logger.xml(xml);
    }

    public static void xml(String tag, String xml) {
        Logger.t(tag).xml(xml);
    }
}

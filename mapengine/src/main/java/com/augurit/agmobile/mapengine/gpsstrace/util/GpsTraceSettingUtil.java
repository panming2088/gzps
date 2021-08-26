package com.augurit.agmobile.mapengine.gpsstrace.util;

import android.content.Context;

import com.augurit.am.fw.utils.file.SharedPreferencesUtil;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.gpstrace1.utils
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public final class GpsTraceSettingUtil {
    private GpsTraceSettingUtil() {
    }

    /**
     * 获取定位的时间间隔
     *
     * @param context 上下文
     * @return 定位的时间间隔，单位是s
     */
    public static int getLocationTimeInterval(Context context) {
        // 读取设置参数，初始化纪录变量
        SharedPreferencesUtil sp = new SharedPreferencesUtil(context);
        return sp.getInt(GPSTraceConstant.KEY_TIME_SETTING, GPSTraceConstant.DEFAULT_TIME_SETTING);
    }

    /**
     * 获取定位的距离间隔，也就是当超过多少米时，才记录当前的位置
     *
     * @param context 上下文
     * @return 定位的距离间隔，单位m
     */
    public static int getLocationDistanceInterval(Context context) {
        // 读取设置参数，初始化纪录变量
        SharedPreferencesUtil sp = new SharedPreferencesUtil(context);
        return sp.getInt(GPSTraceConstant.KEY_DISTANCE_SETTING, GPSTraceConstant.DEFAULT_DISTANCE_SETTING);
    }

    // TODO: 此处需要从服务端获取时间间隔
    public static int getTrackUploadInterval() {
        return 10000;
    }
}

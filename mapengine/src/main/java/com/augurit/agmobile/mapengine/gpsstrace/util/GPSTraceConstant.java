package com.augurit.agmobile.mapengine.gpsstrace.util;


/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.gpsstrace.util
 * @createTime 创建时间 ：2017-02-10
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-10
 * @modifyMemo 修改备注：
 * @version 1.0
 */

public final class GPSTraceConstant {
    ///////////轨迹纪录参数////////////
    public static final int TRACE_MODE_TIME = 1;      // 按时间间隔轨迹纪录模式
    public static final int TRACE_MODE_DISTANCE = 2;  // 按距离间隔轨迹纪录模式
    public static final String KEY_TIME_SETTING = "gpstrace_time_setting";    // 存取时间参数的key
    public static final String KEY_DISTANCE_SETTING = "gpstrace_distance_setting";    // 存取距离参数的key
    public static final String KEY_MODE_SETTING = "gpstrace_mode_setting";    // 存取纪录模式的key
    public static final int DEFAULT_TIME_SETTING = 3;     // 默认间隔时间参数，20s
    public static final int DEFAULT_DISTANCE_SETTING = 5;    // 默认间隔距离参数,5m
    public static final int DEFAULT_MODE_SETTING = TRACE_MODE_TIME;    // 默认间隔距离参数
    //////////轨迹实体类///////////
    public static final int POINT_STAT_START = 1;       // 轨迹开始点
    public static final int POINT_STAT_MIDDLE = 2;      // 轨迹中间点
    public static final int POINT_STAT_END = 3;         // 轨迹结束点
    public static final int POINT_STAT_PAUSE = 4;         // 轨迹暂停点
    public static final int POINT_STAT_ABNORMAL = 0;    // 轨迹异常点
    public static final int UPLOAD_STAT_NO = 0;         // 未提交
    public static final int UPLOAD_STAT_SUCCESS = 1;    // 提交成功
    public static final int UPLOAD_STAT_FAILED = -1;    // 提交失败
    ///////轨迹广播///////////////
    public static final String STOP_ACTION = "com.augur.agmobile.ACTION_STOP";


    private GPSTraceConstant() {
    }
}



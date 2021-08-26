package com.augurit.agmobile.gzps.track.util;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.util
 * @createTime 创建时间 ：2017-06-18
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-18
 * @modifyMemo 修改备注：
 */

public class TrackConstant {

    public static final int NOTIFICATION_ID = 1;
    public static final String BROCAST_ACTION = "com.augurit.agmobile.agpatrol.TRACKBROCAST";


    public static int locateIntervalTime = 5;  ///定位时间间隔，单位秒
    public static int locateIntervalDistance = 0;  //定位距离间隔，单位米
    public static int uploadIntervalTime = 1;   //上传轨迹记录的间隔，单位分钟
    public static int minPointAmount = 5;  //允许保存的最少轨迹点数
    public static int minLength = 100;     //允许保存的轨迹最短路程，单位米
    public static int minLinealLength = 50;  //允许保存的最短直线距离，单位米
    public static int minTime = 5;        //允许保存的运动最短时间，单位分钟

    public static final String TRACK_LOCATE = "TRACK_LOCATE";
    public static final String TRACK_FOLLOW = "TRACK_FOLLOW";
    public static final String TRACK_VIEW = "TRACK_VIEW";
    public static final String TRACK_FROM_NET = "TRACK_FROM_NET";
}

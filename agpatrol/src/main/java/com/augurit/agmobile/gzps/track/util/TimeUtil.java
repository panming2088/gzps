package com.augurit.agmobile.gzps.track.util;

import java.util.Calendar;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.util
 * @createTime 创建时间 ：2017-06-20
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-06-20
 * @modifyMemo 修改备注：
 */

public class TimeUtil {

    public static String formatSecond(int totalSecond){
        int hour = totalSecond / 3600;
        int residueSecond = totalSecond % 3600;
        int minute = residueSecond / 60;
        int second = residueSecond % 60;
        String hourStr = String.valueOf(hour);
        if(hour < 10){
            hourStr = "0" + hourStr;
        }
        String minuteStr = String.valueOf(minute);
        if(minute < 10){
            minuteStr = "0" + minuteStr;
        }
        String secondStr = String.valueOf(second);
        if(second < 10){
            secondStr = "0" + secondStr;
        }
        return hourStr + ":" + minuteStr + ":" + secondStr;
    }

    /**
     * 获取当天0时的时间戳
     * @return
     */
    public static long getDayBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return cal.getTimeInMillis();
    }

    public static long getTimestampRamdon(){
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return Long.valueOf(String.valueOf(timestamp).substring(3) + String.valueOf(random));
    }
}

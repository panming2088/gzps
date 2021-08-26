package com.augurit.agmobile.gzps.track.util;

import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.util
 * @createTime 创建时间 ：2017-07-19
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-19
 * @modifyMemo 修改备注：
 */

public interface OnTrackListener {

    void onTime(int second);
    void onLength(double length);
    void onTrack(GPSTrack gpsTrack);
}

package com.augurit.agmobile.mapengine.gpsstrace.service;

/**
 * 在后台定时检测GPS状态
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.gpstrace1.service
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public interface ICheckGpsStateService {

    void monitorGpsState(OnGpsStateListener listener);

    void stopMonitor();

    interface OnGpsStateListener {
        void onGpsOff();
    }
}

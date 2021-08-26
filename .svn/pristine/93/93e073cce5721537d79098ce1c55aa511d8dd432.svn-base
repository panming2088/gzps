package com.augurit.agmobile.mapengine.gpsstrace.view;




import com.augurit.am.fw.common.IPresenter;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.gpstrace1.view.presenter
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public interface IGPSTracePresenter extends IPresenter {

    /*******************给外界调用**************************/
    /**
     * 开始记录轨迹
     */
    void startTrack();

    /**
     * 暂停记录轨迹
     */
    void pauseTrack();

    /**
     * 停止记录轨迹
     */
    void stopTrack();

    /**
     * 继续记录轨迹
     */
    void continueTrack();

    /**
     * 保存轨迹名称
     * @param trackName
     */
    void saveTrackName(String trackName);

    /**
     * 今天是否有开始过GPS轨迹记录
     */
    void checkIfHasGpsTraceRecordToday();

    /**
     * 开始回放轨迹
     */
    void playbackRoute();

    void pausePlayback();

    void continuePlayback();

    /**
     * 停止轨迹回放
     */
    void stopPlayback();

    void drawTrackOnMap(List<GPSTrack> gpsTracks);

    /**
     * 退出gps轨迹功能
     */
    void quit();

    /**
     * 检查gps状态再决定是否进行轨迹记录
     */
    void checkGpsStateAndThenStartRecording();
}

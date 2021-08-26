package com.augurit.agmobile.mapengine.gpsstrace.service;


import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;

/**
 *
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.gpstrace1.service
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public interface IUploadTrackService {

    /**
     * 开启定时上传
     */
    void startTimingUpload();

    void pauseTimingUpload();

    void continueTimingUpload();

    /**
     * 关闭定时上传
     */
    void stopTimingUpload();

    /**
     * 上传上次未上传成功的点
     */
    void uploadAndUpdatePointsOfUnUpload();

    /**
     * 将剩下未上传的轨迹点一次性上传
     */
    void oneTimeUpload();

    /**
     * 新增一个点到等待上传队列中
     */
    void addTrackToUploadQueue(GPSTrack gpsTrack);

    /**
     * 设置当前进行上传的轨迹点的名称和id，这个填充于异常点信息的
     * @param trackName 轨迹点的名称
     * @param trackId 轨迹点id
     */
    void setCurrentUploadTrackNameAndId(String trackName, long trackId);
}

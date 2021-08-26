package com.augurit.agmobile.mapengine.gpsstrace.router;

import android.content.Context;


import com.augurit.agmobile.mapengine.gpsstrace.dao.LocalGPSTraceSQLiteDao;
import com.augurit.agmobile.mapengine.gpsstrace.dao.RemoteGPSTraceDao;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.am.fw.db.AMQueryBuilder;

import java.util.Date;
import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.gpstrace1.router
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public class GPSTraceRouter {

    private Context mContext;
    private LocalGPSTraceSQLiteDao mLocalGPSTraceSQLiteDao;
    private RemoteGPSTraceDao mRemoteGPSTraceDao;

    public GPSTraceRouter(Context context){
        this.mContext = context;
        mLocalGPSTraceSQLiteDao = new LocalGPSTraceSQLiteDao();
        mRemoteGPSTraceDao = new RemoteGPSTraceDao();
    }

    public void saveTrackPoint(String featureLayerUrl,GPSTrack trackPoint) {
        mLocalGPSTraceSQLiteDao.saveTrackPoint(trackPoint);
    }

    public void updateTrackPoints(List<GPSTrack> trackPoints) {
        mLocalGPSTraceSQLiteDao.updateTrackPoints(trackPoints);
    }

    public List<GPSTrack> getAllGPSTracks() {
       return  mLocalGPSTraceSQLiteDao.getAllGPSTracks();
    }

    /**
     * 查询某个时间段内的轨迹
     * @param date 要查询的时间段
     */
    public List<GPSTrack> getGPSTracksByDate(Date date) {
        return mLocalGPSTraceSQLiteDao.getGPSTracksByDate(date);
    }

    /**
     * 获取提交状态为失败的点
     * @return
     */
    public List<GPSTrack> getUnUploadedPoints(){
        return mLocalGPSTraceSQLiteDao.getUnUploadedPoints();
    }

    /**
     * 进行查询轨迹，自定义查询条件进行查询
     * @param queryBuilder 自定义查询条件
     */
    public List<GPSTrack> queryTrack(AMQueryBuilder<GPSTrack> queryBuilder){
        return mLocalGPSTraceSQLiteDao.queryTrack(queryBuilder);
    }

    /**
     * 进行查询轨迹,根据轨迹名称或者时间范围进行查询
     * @param trackName 查询的轨迹名称，可以为空
     * @param startDate 最小时间，可以传入0，如果startDate为0，那么默认endDate也0
     * @param endDate   最大时间，可以传入0，如果endDate为0，那么默认startDate也0
     */
    public List<GPSTrack> queryTrack(String trackName,Long startDate,
                           Long endDate){
        return mLocalGPSTraceSQLiteDao.queryTrack(trackName,startDate,endDate);
    }

    /**
     * 删除不属于当天的记录
     */
    public void deleteTrackBeyondCurrentDay(){
        mLocalGPSTraceSQLiteDao.deleteTrackBeyondCurrentDay();
    }

    public List<GPSTrack>  queryTrackPointsByTrackName(String trackName){
        return mLocalGPSTraceSQLiteDao.queryTrackPointsByTrackName(trackName);
    }
}

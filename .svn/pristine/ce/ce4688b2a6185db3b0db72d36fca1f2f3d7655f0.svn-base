package com.augurit.agmobile.mapengine.gpsstrace.dao;


import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.agmobile.mapengine.gpsstrace.util.GPSTraceConstant;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.db.AMQueryBuilder;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.log.LogUtil;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.gpstrace1.dao
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public class LocalGPSTraceSQLiteDao {

    public void saveTrackPoint(GPSTrack trackPoint) {
        AMDatabase amDatabase = AMDatabase.getInstance();
        amDatabase.save(trackPoint);
    }

    public void updateTrackPoints(List<GPSTrack> trackPoints) {
        AMDatabase amDatabase = AMDatabase.getInstance();
        amDatabase.saveAll(trackPoints);
    }

    public List<GPSTrack> getAllGPSTracks() {
        AMDatabase amDatabase = AMDatabase.getInstance();
        return amDatabase.getQueryAll(GPSTrack.class);
    }

    /**
     * 查询某个时间段内的轨迹
     * @param date 要查询的时间段
     */
    public List<GPSTrack> getGPSTracksByDate(Date date) {
        AMDatabase amDatabase = AMDatabase.getInstance();
        String dateStr = TimeUtil.getStringTimeYMD(date);
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");   // TODO: 时间格式待确定
        String dateStr = dateFormat.format(date);*/
        AMQueryBuilder<GPSTrack> queryBuilder = new AMQueryBuilder<GPSTrack>(GPSTrack.class);
        queryBuilder.where("date > ?", date.getTime());
        List<GPSTrack> tracks = amDatabase.query(queryBuilder);
        return tracks;
    }

    /**
     * 获取提交状态为失败的点
     * @return
     */
    public List<GPSTrack> getUnUploadedPoints(){
        AMDatabase amDatabase = AMDatabase.getInstance();
        AMQueryBuilder<GPSTrack> queryBuilder = new AMQueryBuilder<GPSTrack>(GPSTrack.class);
        queryBuilder.where("uploadState="+ GPSTraceConstant.UPLOAD_STAT_FAILED);
        return amDatabase.query(queryBuilder);
    }

    /**
     * 进行查询轨迹，自定义查询条件进行查询
     * @param queryBuilder 自定义查询条件
     */
    public List<GPSTrack> queryTrack(AMQueryBuilder<GPSTrack> queryBuilder){
        AMDatabase amDatabase = AMDatabase.getInstance();
        return amDatabase.query(queryBuilder);
    }

    /**
     * 进行查询轨迹,根据轨迹名称或者时间范围进行查询
     * @param trackName 查询的轨迹名称，可以为空
     * @param startDate 最小时间，可以传入0，如果startDate为0，那么默认endDate也0
     * @param endDate   最大时间，可以传入0，如果endDate为0，那么默认startDate也0
     */
    public List<GPSTrack> queryTrack(String trackName,Long startDate,
                           Long endDate){
        AMDatabase amDatabase = AMDatabase.getInstance();
        AMQueryBuilder<GPSTrack> query = new AMQueryBuilder<>(GPSTrack.class);

        if (trackName == null && startDate == null && endDate == null){
            LogUtil.e("查询条件全部为空，请直接调用getAllGPSTracks()方法");
            return null;
        }
        List<GPSTrack> queryResult = new ArrayList<>();
        if (trackName != null && startDate != 0 && endDate != 0 && startDate < endDate){
            query.where("date > " + startDate + "and date < " + endDate + " and  trackName LIKE %" +
                    trackName +"%" + "and pointState =" + GPSTraceConstant.POINT_STAT_START); //同一个轨迹名称对应很多点，只取开始点
            queryResult = amDatabase.query(query);
        }else if (trackName != null){
            // onGraphicSelected.where( "trackName LIKE %" + trackName +"%" +" and pointState =" +GPSTraceConstant.POINT_STAT_START);
            // onGraphicSelected.where( "trackName LIKE %" + trackName +"%");
            query.where("trackName LIKE ?","%"+trackName+"%");
            query.whereAnd("pointState = ?", GPSTraceConstant.POINT_STAT_START);
            queryResult = amDatabase.query(query);
        }else if (startDate != null && endDate != null && startDate != 0 && endDate != 0  && startDate < endDate){
            query.where("date > " + startDate + "and date < " + endDate +"and pointState =" + GPSTraceConstant.POINT_STAT_START);
           queryResult = amDatabase.query(query);
        }
        return queryResult;
    }

    public List<GPSTrack> queryTodayGPSTracks(){
        long dayBegin = getDayBegin();
        AMDatabase amDatabase = AMDatabase.getInstance();
        AMQueryBuilder<GPSTrack> queryBuilder = new AMQueryBuilder<GPSTrack>(GPSTrack.class);
        queryBuilder.where("date>?", dayBegin);
        return amDatabase.query(queryBuilder);
    }

    /**
     * 删除不属于当天的记录
     */
    public void deleteTrackBeyondCurrentDay(){
        long dayBegin = getDayBegin();
        AMDatabase amDatabase = AMDatabase.getInstance();
        AMQueryBuilder<GPSTrack> queryBuilder = new AMQueryBuilder<GPSTrack>(GPSTrack.class);
        queryBuilder.where("date<?",  dayBegin);
        ArrayList<GPSTrack> query = amDatabase.query(queryBuilder);
        LogUtil.i("需要删除的点一共："+query.size());
        amDatabase.deleteWhere(queryBuilder.getWhereBuilder());
    }

    /**
     * 获取当天0时的时间戳
     * @return
     */
    public long getDayBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return cal.getTimeInMillis();
    }

    public void deleteGPSTracksByTrackId(String trackId){
        AMDatabase amDatabase = AMDatabase.getInstance();
        AMQueryBuilder<GPSTrack> queryBuilder = new AMQueryBuilder<GPSTrack>(GPSTrack.class);
        queryBuilder.where("trackId=?",  trackId);
        amDatabase.deleteWhere(queryBuilder.getWhereBuilder());
    }

    public void deleteAllGPSTrack(){
        AMDatabase amDatabase = AMDatabase.getInstance();
        amDatabase.deleteAll(GPSTrack.class);
    }

    public List<GPSTrack>  queryTrackPointsByTrackName(String trackName){
        AMDatabase amDatabase = AMDatabase.getInstance();
        AMQueryBuilder<GPSTrack> queryBuilder = new AMQueryBuilder<GPSTrack>(GPSTrack.class);
        queryBuilder.where("trackName = ?", trackName);
        List<GPSTrack> tracks = amDatabase.query(queryBuilder);
        return tracks;
    }

    public List<GPSTrack>  queryTrackPointsByTrackId(String trackId){
        AMDatabase amDatabase = AMDatabase.getInstance();
        AMQueryBuilder<GPSTrack> queryBuilder = new AMQueryBuilder<GPSTrack>(GPSTrack.class);
        queryBuilder.where("trackId=?", trackId);
        List<GPSTrack> tracks = amDatabase.query(queryBuilder);
        return tracks;
    }

    public List<GPSTrack>  queryUnUploadTrackPointsByTrackId(long trackId){
        AMDatabase amDatabase = AMDatabase.getInstance();
        AMQueryBuilder<GPSTrack> queryBuilder = new AMQueryBuilder<GPSTrack>(GPSTrack.class);
        queryBuilder.where("trackId=? and uploadState=?", trackId, 0);
        List<GPSTrack> tracks = amDatabase.query(queryBuilder);
        return tracks;
    }
}

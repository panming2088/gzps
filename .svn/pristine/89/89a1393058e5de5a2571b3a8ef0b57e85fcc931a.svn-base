package com.augurit.agmobile.mapengine.gpsstrace.service;

import android.support.v4.util.ArrayMap;

import com.augurit.agmobile.mapengine.gpsstrace.dao.LocalGPSTraceSQLiteDao;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.gpstrace1.service
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public class LocalTrackServiceImpl implements ILocalGpsTrackService {

    public LocalGPSTraceSQLiteDao mLocalGPSTrackSQLiteDao;

    public LocalTrackServiceImpl(){
        mLocalGPSTrackSQLiteDao = new LocalGPSTraceSQLiteDao();
    }

    @Override
    public void saveTrackPoint(GPSTrack track) {
        mLocalGPSTrackSQLiteDao.saveTrackPoint(track);
    }

    @Override
    public void saveTrackPoint(List<GPSTrack> tracks) {
        mLocalGPSTrackSQLiteDao.updateTrackPoints(tracks);
    }

    public List<GPSTrack> getGPSTracksByTrackId(String trackId){
        return mLocalGPSTrackSQLiteDao.queryTrackPointsByTrackId(trackId);
    }

    public List<GPSTrack>  getUnUploadGPSTrack(long trackId){
        return mLocalGPSTrackSQLiteDao.queryUnUploadTrackPointsByTrackId(trackId);
    }

    @Override
    public void getTrack(int pageNo, int pageSize, final Callback2<GetTodayTrackResponse> callback) {
        Observable.create(new Observable.OnSubscribe<GetTodayTrackResponse>() {
            @Override
            public void call(Subscriber<? super GetTodayTrackResponse> subscriber) {
                List<GPSTrack> records = mLocalGPSTrackSQLiteDao.getAllGPSTracks();
                if (ValidateUtil.isListNull(records)){
                    GetTodayTrackResponse response = new GetTodayTrackResponse(null,"0分钟","0米");
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                }else {
                    DecimalFormat format = new DecimalFormat("###.###");
                    long time = 0;
                    double totalLength = 0;
                    ArrayMap<Long, List<GPSTrack>> sortTracks = sortTracks(records);
                    Set<Map.Entry<Long, List<GPSTrack>>> set = sortTracks.entrySet();
                    for (Map.Entry<Long,  List<GPSTrack>> entry : set){
                        //最后一个的长度就是总长度
                        GPSTrack gpsTrack = entry.getValue().get(entry.getValue().size() - 1);
                        totalLength += gpsTrack.getRecordLength();
                        //计算这段轨迹所用时长
                        //  String startDateStr = entry.getValue().requestLocation(0).getDate(); //开始时间
                        //  String lastDateStr = gpsTrack.getDate();  //结束时间
                        Date startDate = new Date(entry.getValue().get(0).getRecordDate());
                        Date lastDate = new Date(gpsTrack.getRecordDate());
                        long startMills = startDate.getTime() ; //得到毫秒数
                        long lastMills = lastDate .getTime()  ; //得到毫秒数
                        time +=  lastMills - startMills;
                    }
                    String lenthStr = format.format(totalLength).concat("千米");
                    Date date = new Date(time);
                    String stringTimeYMDS = TimeUtil.getStringTimeDS(date);
                    List<List<GPSTrack>> track = mergeTrack(sortTracks);
                    GetTodayTrackResponse response = new GetTodayTrackResponse(track,stringTimeYMDS,lenthStr);
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetTodayTrackResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GetTodayTrackResponse gpsTracks) {
                        callback.onSuccess(gpsTracks);
                    }
                });
    }

    @Override
    public void  getTodaysTrack(final Callback2<GetTodayTrackResponse> callback) {
        Observable.create(new Observable.OnSubscribe<GetTodayTrackResponse>() {
            @Override
            public void call(Subscriber<? super GetTodayTrackResponse> subscriber) {
                List<GPSTrack> records = mLocalGPSTrackSQLiteDao.queryTodayGPSTracks();
                if (ValidateUtil.isListNull(records)){
                    GetTodayTrackResponse response = new GetTodayTrackResponse(null,"0小时","0米");
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                }else {
                    DecimalFormat format = new DecimalFormat("###.###");
                    long time = 0;
                    double totalLength = 0;
                    ArrayMap<Long, List<GPSTrack>> sortTracks = sortTracks(records);
                    Set<Map.Entry<Long, List<GPSTrack>>> set = sortTracks.entrySet();
                    for (Map.Entry<Long,  List<GPSTrack>> entry : set){
                        //最后一个的长度就是总长度
                        GPSTrack gpsTrack = entry.getValue().get(entry.getValue().size() - 1);
                        totalLength += gpsTrack.getRecordLength();
                        //计算这段轨迹所用时长
                        //  String startDateStr = entry.getValue().requestLocation(0).getDate(); //开始时间
                        //  String lastDateStr = gpsTrack.getDate();  //结束时间
                        Date startDate = new Date(entry.getValue().get(0).getRecordDate());
                        Date lastDate = new Date(gpsTrack.getRecordDate());
                        long startMills = startDate.getTime() ; //得到毫秒数
                        long lastMills = lastDate .getTime()  ; //得到毫秒数
                        time +=  lastMills - startMills;
                    }
                    String lenthStr = format.format(totalLength).concat("千米");
                    Date date = new Date(time);
                    String stringTimeYMDS = TimeUtil.getStringTimeDS(date);
                    List<List<GPSTrack>> track = mergeTrack(sortTracks);
                    GetTodayTrackResponse response = new GetTodayTrackResponse(track,stringTimeYMDS,lenthStr);
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Subscriber<GetTodayTrackResponse>() {
              @Override
              public void onCompleted() {

              }

              @Override
              public void onError(Throwable e) {

              }

              @Override
              public void onNext(GetTodayTrackResponse gpsTracks) {
                    callback.onSuccess(gpsTracks);
              }
          });
    }

    /**
     * 将原始GPS轨迹数据按照轨迹ID分类到Map
     * @param trackList GPS轨迹List
     * @return 分类后的轨迹点List
     */
    private ArrayMap<Long, List<GPSTrack>> sortTracks(List<GPSTrack> trackList) {
        // 使用Map对轨迹进行分类
        ArrayMap<Long, List<GPSTrack>> map = new ArrayMap<>();
        for (GPSTrack track : trackList) {
            Long trackId = track.getTrackId();  // 相同轨迹ID放入同一List
            if (map.containsKey(trackId)) {
                map.get(trackId).add(track);
            } else {
                List<GPSTrack> tracks = new ArrayList<>();
                tracks.add(track);
                map.put(trackId, tracks);
            }
        }
        return map;
    }

    /**
     * 将原始GPS轨迹数据按照轨迹ID分类到Map
     * @return 分类后的轨迹点List
     */
    private List<List<GPSTrack>> mergeTrack(ArrayMap<Long, List<GPSTrack>> map) {
        // 存入List,反一下，让最近的纪录排在前 TODO: 之后根据具体情况来
        List<List<GPSTrack>> sortList = new ArrayList<>();
        for (int i = map.size() - 1; i > -1; i--) {
            sortList.add(map.valueAt(i));
        }
        return sortList;
    }

    @Override
    public void clearGpsTrackBeyondToday() {
        mLocalGPSTrackSQLiteDao.deleteTrackBeyondCurrentDay();
    }

    public void deleteGPSTrackSByTrackId(String trackId){
        mLocalGPSTrackSQLiteDao.deleteGPSTracksByTrackId(trackId);
    }

    public void deleteAllGPSTrack(){
        mLocalGPSTrackSQLiteDao.deleteAllGPSTrack();
    }

}

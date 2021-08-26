package com.augurit.agmobile.mapengine.gpsstrace.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.gpsstrace.dao.LocalGPSTraceSQLiteDao;
import com.augurit.agmobile.mapengine.gpsstrace.dao.RemoteGPSTraceDao;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.agmobile.mapengine.gpsstrace.util.GPSTraceConstant;
import com.augurit.agmobile.mapengine.gpsstrace.util.GpsTraceSettingUtil;
import com.augurit.am.fw.db.AMQueryBuilder;
import com.augurit.am.fw.utils.log.LogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 * @author 创建人 ：xiejiexin,xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.gpstrace1.service
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public class UploadTrackServiceImpl implements IUploadTrackService {

    private Subscription mSubscription;
    private boolean stopFlag;       // 是否停止
    private List<GPSTrack> mUploadQueue = new ArrayList<>();
    private Context mContext;
    private String mCurrentTrackName;
    private long mCurrentTrackId;
    private final RemoteGPSTraceDao mRemoteGPSTraceDao;
    private final LocalGPSTraceSQLiteDao mLocalGPSTraceSQLiteDao;

    public UploadTrackServiceImpl(Context context){
        this.mContext = context;
        mRemoteGPSTraceDao = new RemoteGPSTraceDao();
        mLocalGPSTraceSQLiteDao = new LocalGPSTraceSQLiteDao();
    }

    @Override
    public void startTimingUpload() {
        int interval = GpsTraceSettingUtil.getTrackUploadInterval();
        stopFlag = false;
        // 每隔一段时间获取一次轨迹点列表
        mSubscription = Observable.interval(interval, TimeUnit.MILLISECONDS)
                .doOnEach(new Action1<Notification<? super Long>>() {
                    @Override
                    public void call(Notification<? super Long> notification) {
                        if (stopFlag) {  // 若停止则取消订阅
                            mSubscription.unsubscribe();
                            LogUtil.i("GPSTrace Upload: 订阅已取消");
                        }
                    }
                })
                .map(new Func1<Long, List<GPSTrack>>() {
                    @Override
                    public List<GPSTrack> call(Long aLong) {
                        // 获取当前的待上传轨迹点List
                        List<GPSTrack> uploadList = UploadTrackServiceImpl.this.getTrackPointList();
                        // 上传轨迹点
                        List<GPSTrack> resultList = new ArrayList<>();
                        for (GPSTrack trackPoint : uploadList) {
                            resultList.add(UploadTrackServiceImpl.this.uploadTrackPoint(trackPoint));
                        }
                        return resultList;  // 返回上传后的轨迹点List
                    }
                }).map(new Func1<List<GPSTrack>, Void>() {
                    @Override
                    public Void call(List<GPSTrack> gpsTracks) {
                        //上传成功后进行更新本地GPS轨迹信息
                        mLocalGPSTraceSQLiteDao.updateTrackPoints(gpsTracks);
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void pauseTimingUpload() {
        stopFlag = true;
    }

    @Override
    public void continueTimingUpload() {
        stopFlag = false;
        startTimingUpload();
    }

    @Override
    public void stopTimingUpload() {
        stopFlag = true;
    }

    @Override
    public void uploadAndUpdatePointsOfUnUpload() {
        Observable.just(1)
                .map(new Func1<Integer, List<GPSTrack>>() {
                    @Override
                    public List<GPSTrack> call(Integer integer) {
                        List<GPSTrack> points = UploadTrackServiceImpl.this.getUnUploadedPoints();
                        List<GPSTrack> updatedPoints = new ArrayList<GPSTrack>();
                        for (GPSTrack track : points) {
                            GPSTrack trackPoint = UploadTrackServiceImpl.this.uploadTrackPoint(track);
                            updatedPoints.add(trackPoint);
                        }
                        LogUtil.i("正在执行上传未上传点的任务");
                        return updatedPoints;
                    }
                })
                .map(new Func1<List<GPSTrack>, Object>() {
                    @Override
                    public Object call(List<GPSTrack> gpsTracks) {
                        //更新数据库
                        mLocalGPSTraceSQLiteDao.updateTrackPoints(gpsTracks);
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private List<GPSTrack> getUnUploadedPoints(){
        LocalGPSTraceSQLiteDao localGPSTraceSQLiteDao = new LocalGPSTraceSQLiteDao();
        AMQueryBuilder queryBuilder = new AMQueryBuilder(GPSTrack.class);
        queryBuilder.where("uploadState equals"+ GPSTraceConstant.UPLOAD_STAT_FAILED);
        return localGPSTraceSQLiteDao.queryTrack(queryBuilder);
    }

    @Override
    public void oneTimeUpload() {
        Observable.just(1)
                .flatMap(new Func1<Integer, Observable<?>>() {
                    @Override
                    public Observable<?> call(Integer a) {
                        // 获取当前的待上传轨迹点List
                        return Observable.from(UploadTrackServiceImpl.this.getTrackPointList()); // 最后一次上传时需确保List至少包含轨迹结束点
                    }
                })
                .map(new Func1<Object, GPSTrack>() {
                    @Override
                    public GPSTrack call(Object track) {
                        // 逐个上传轨迹点
                        return UploadTrackServiceImpl.this.uploadTrackPoint((GPSTrack) track);  // 返回上传后的轨迹点
                    }
                })
                .collect(new Func0<List<GPSTrack>>() {
                             @Override
                             public List<GPSTrack> call() {
                                 return new ArrayList<GPSTrack>();
                             }
                         },
                        new Action2<List<GPSTrack>,GPSTrack>() {
                            @Override
                            public void call(List<GPSTrack> gpsTrackList, GPSTrack trackPoint) {
                                gpsTrackList.add(trackPoint);
                            }
                        }) // 收集结果
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GPSTrack>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<GPSTrack> gpsTracks) {
                        LogUtil.i("GPSTrace Upload: 一次上传结束, List size:" + gpsTracks.size());
                    }
                });
    }

    @Override
    public void addTrackToUploadQueue(GPSTrack gpsTrack) {
        mUploadQueue.add(gpsTrack);
    }

    @Override
    public void setCurrentUploadTrackNameAndId(String trackName, long trackId) {
        mCurrentTrackName = trackName;
        mCurrentTrackId = trackId;
    }


    /**
     * 复制一份当前待上传的轨迹点List，并将原List清空
     * 若当前待上传轨迹点List为空则添加一个异常点
     * @return 待上传的轨迹点List
     */
    private List<GPSTrack> getTrackPointList() {
        List<GPSTrack> listToUpload = new ArrayList<>();
        List<GPSTrack> trackPointList = mUploadQueue;
        if (trackPointList.size() == 0) {    // 若待上传列表为空则上传一个异常点
            listToUpload.add(getAbnormalTrackPoint());
            LogUtil.i("GPSTrace Upload: 准备上传, 待上传异常点");
        } else {
            listToUpload.addAll(trackPointList);    // 准备上传
            trackPointList.clear(); // 清空本次纪录的待上传轨迹点
            LogUtil.i("GPSTrace Upload: 准备上传, 待上传列表List Size:" + listToUpload.size());
        }
        return listToUpload;
    }

    /**
     * 获取当前纪录轨迹下的异常点
     * @return  状态为异常的轨迹点
     */
    public GPSTrack getAbnormalTrackPoint() {
        GPSTrack trackPoint = new GPSTrack();
        trackPoint.setTrackId(mCurrentTrackId);
        trackPoint.setTrackName(mCurrentTrackName);
        trackPoint.setRecordDate(System.currentTimeMillis());
        trackPoint.setRecordLength(0); //TODO 异常点的距离要怎么办？
        trackPoint.setUploadState(GPSTraceConstant.UPLOAD_STAT_FAILED);
        trackPoint.setLatitude(-1);     // TODO: 待商讨
         trackPoint.setLongitude(-1);
        trackPoint.setPointState(GPSTraceConstant.POINT_STAT_ABNORMAL);
        return trackPoint;
    }

    /**
     * 上传轨迹点
     * @param trackPoint 轨迹点
     * @return  上传后的轨迹点
     */
    private GPSTrack uploadTrackPoint(GPSTrack trackPoint){
        // 模拟上传 TODO: 服务端出来之后需要修改
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        trackPoint.setUploadState(GPSTraceConstant.UPLOAD_STAT_SUCCESS);   // 修改上传状态
        return trackPoint;
    }
}

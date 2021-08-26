package com.augurit.agmobile.gzps.track.service;

import android.content.Context;

import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.common.model.StringResult;
import com.augurit.agmobile.gzps.track.dao.RemoteTrackRestDao;
import com.augurit.agmobile.gzps.track.model.Track;
import com.augurit.agmobile.gzps.track.model.TrackConfig;
import com.augurit.agmobile.gzps.track.model.TrackList;
import com.augurit.agmobile.gzps.track.util.TrackConstant;
import com.augurit.agmobile.mapengine.gpsstrace.model.GPSTrack;
import com.augurit.am.fw.utils.ListUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.service
 * @createTime 创建时间 ：2017-08-14
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-14
 * @modifyMemo 修改备注：
 */

public class TrackNetService {

    private Context mContext;
    private RemoteTrackRestDao mRemoteTrackRestDao;

    public TrackNetService(Context context) {
        this.mContext = context;
        this.mRemoteTrackRestDao = new RemoteTrackRestDao(context);
    }

    public void initTrackConfig() {
        getTraceConfig().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TrackConfig>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TrackConfig trackConfig) {
                        if (trackConfig == null
                                || trackConfig.getCode() != 200
                                || trackConfig.getResult() == null) {
                            return;
                        }
                        TrackConfig.TrackConfigResult configResult = trackConfig.getResult();
                        TrackConstant.locateIntervalTime = configResult.getLocateIntervalTime();
                        TrackConstant.locateIntervalDistance = configResult.getLocateIntervalDis();
                        TrackConstant.uploadIntervalTime = configResult.getUploadIntervalTime();
                        TrackConstant.minPointAmount = configResult.getTrackMinPointAmount();
                        TrackConstant.minLength = configResult.getTrackMinLength();
                        TrackConstant.minTime = configResult.getTrackMinTime();
                    }
                });
    }

    public Observable<TrackConfig> getTraceConfig() {
        return Observable.create(new Observable.OnSubscribe<TrackConfig>() {
            @Override
            public void call(Subscriber<? super TrackConfig> subscriber) {
                TrackConfig trackConfig = mRemoteTrackRestDao.getTraceConfig();
                if (trackConfig == null
                        || trackConfig.getResult() == null) {
                    subscriber.onError(new Exception(""));
                } else {
                    subscriber.onNext(trackConfig);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<Result2<String>> saveTracePoint(final GPSTrack gpsTrack) {
        return Observable.create(new Observable.OnSubscribe<Result2<String>>() {
            @Override
            public void call(Subscriber<? super Result2<String>> subscriber) {
                Result2<String> stringResult = mRemoteTrackRestDao.saveTracePoint(gpsTrack);
                if (stringResult == null) {
                    subscriber.onError(new Exception(""));
                } else {
                    subscriber.onNext(stringResult);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<TrackList> getTraceLinesByUserId(final String userId, final int pageNo, final int pageSize) {
        return Observable.create(new Observable.OnSubscribe<TrackList>() {
            @Override
            public void call(Subscriber<? super TrackList> subscriber) {
                TrackList trackList = mRemoteTrackRestDao.getTraceLinesByUserId(userId, pageNo, pageSize);
                if (trackList == null
                        || ListUtil.isEmpty(trackList.getRows())) {
                    subscriber.onError(new Exception(""));
                } else {
                    subscriber.onNext(trackList);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<Result2<List<GPSTrack>>> getTracePointsByTraceId(final long trackId) {
        return Observable.create(new Observable.OnSubscribe<Result2<List<GPSTrack>>>() {
            @Override
            public void call(Subscriber<? super Result2<List<GPSTrack>>> subscriber) {
                Result2<List<GPSTrack>> result = mRemoteTrackRestDao.getTracePointsByTraceId(trackId);
                if (result == null
                        || result.getData() == null) {
                    subscriber.onError(new Exception(""));
                } else {
                    subscriber.onNext(result);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }


    public Observable<Result2<Long>> saveTraceLine(final Track track) {
        return Observable.create(new Observable.OnSubscribe<Result2<Long>>() {
            @Override
            public void call(Subscriber<? super Result2<Long>> subscriber) {
                Result2<Long> stringResult = mRemoteTrackRestDao.saveTraceLine(track);
                if (stringResult == null || stringResult.getCode() != 200) {
                    subscriber.onError(new Exception(""));
                } else {
                    subscriber.onNext(stringResult);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<StringResult> deleteTraceLine(final long trackId) {
        return Observable.create(new Observable.OnSubscribe<StringResult>() {
            @Override
            public void call(Subscriber<? super StringResult> subscriber) {
                StringResult stringResult = mRemoteTrackRestDao.deleteTraceLine(trackId);
                if (stringResult == null) {
                    subscriber.onError(new Exception(""));
                } else {
                    subscriber.onNext(stringResult);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }
}

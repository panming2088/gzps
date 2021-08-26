package com.augurit.agmobile.gzps.trackmonitor.service;

import android.content.Context;

import com.augurit.agmobile.gzps.trackmonitor.dao.RemoteTrackMonitorRestDao;
import com.augurit.agmobile.gzps.trackmonitor.model.UserLocation;
import com.augurit.agmobile.gzps.trackmonitor.model.UserOrg;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.trackmonitor.service
 * @createTime 创建时间 ：2017-08-21
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-21
 * @modifyMemo 修改备注：
 */

public class TrackMonitorService {

    private RemoteTrackMonitorRestDao trackMonitorDao;

    public TrackMonitorService(Context context){
        trackMonitorDao = new RemoteTrackMonitorRestDao(context);
    }


    public Observable<List<UserLocation>> getOnlineSubordinateLocation(){
        return Observable.create(new Observable.OnSubscribe<List<UserLocation>>() {
            @Override
            public void call(Subscriber<? super List<UserLocation>> subscriber) {

            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<List<UserOrg>> getSubordinateList(){
        return Observable.create(new Observable.OnSubscribe<List<UserOrg>>() {
            @Override
            public void call(Subscriber<? super List<UserOrg>> subscriber) {

            }
        }).subscribeOn(Schedulers.io());
    }
}

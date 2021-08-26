package com.augurit.agmobile.mapengine.gpsstrace.service;

import android.content.Context;

import com.augurit.am.fw.utils.GPSUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 在后台定时检测GPS状态
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.gpstrace1.service
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public class CheckGpsStateServiceImpl implements ICheckGpsStateService {

    private Subscription mGpsStateSubscrption;

    private Context mContext;
    public CheckGpsStateServiceImpl(Context context){
        this.mContext = context;
    }

    @Override
    public void monitorGpsState(final OnGpsStateListener listener) {
        //每20s刷新GPS状态
        mGpsStateSubscrption = Observable.interval(20, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        if(!GPSUtil.isGpsOpen(mContext)){
                            listener.onGpsOff();
                        }
                    }
                });
    }

    @Override
    public void stopMonitor() {
        if (mGpsStateSubscrption!= null && mGpsStateSubscrption.isUnsubscribed()){
            mGpsStateSubscrption.unsubscribe();
        }
    }
}

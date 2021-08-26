package com.augurit.agmobile.gzps.trackmonitor.view.presenter;

import android.content.Context;

import com.augurit.agmobile.gzps.trackmonitor.model.UserLocation;
import com.augurit.agmobile.gzps.trackmonitor.service.TrackMonitorService;
import com.augurit.agmobile.gzps.trackmonitor.view.IOnlineMonitorView;
import com.augurit.am.cmpt.common.Callback1;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.trackmonitor.view.presenter
 * @createTime 创建时间 ：2017-08-21
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-21
 * @modifyMemo 修改备注：
 */

public class OnlineMonitorPresenter implements IOnlineMonitorPresenter {

    private Context mContext;
    private IOnlineMonitorView mOnlineMonitorView;
    private TrackMonitorService mTrackMonitorService;

    private Callback1 mBackListener;
    private Callback1 mShowSubordinateListListener;

    public OnlineMonitorPresenter(Context context, IOnlineMonitorView onlineMonitorView){
        this.mContext = context;
        this.mOnlineMonitorView = onlineMonitorView;
        this.mOnlineMonitorView.setPresenter(this);
        this.mTrackMonitorService = new TrackMonitorService(mContext);
        getOnlineSubordinate();
    }

    private void getOnlineSubordinate(){
        mTrackMonitorService.getOnlineSubordinateLocation()
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UserLocation>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<UserLocation> userLocations) {
                        mOnlineMonitorView.showOnlineSubordinateLocation(userLocations);
                    }
                });
    }

    @Override
    public void back() {
        if(mBackListener != null){
            mBackListener.onCallback(null);
        }
    }

    @Override
    public void setBackListener(Callback1 backListener) {
        this.mBackListener = backListener;
    }

    @Override
    public void showSubordinateList() {
        if(mShowSubordinateListListener != null){
            mShowSubordinateListListener.onCallback(null);
        }
    }

    @Override
    public void setShowSubordinateListListener(Callback1 showSubordinateListListener) {
        this.mShowSubordinateListListener = showSubordinateListListener;
    }
}

package com.augurit.agmobile.gzps.track.view.presenter;

import android.content.Context;

import com.augurit.agmobile.gzps.track.model.TrackList;
import com.augurit.agmobile.gzps.track.service.TrackNetService;
import com.augurit.agmobile.gzps.track.view.ITrackListView;
import com.augurit.am.cmpt.common.Callback1;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.view.presenter
 * @createTime 创建时间 ：2017-08-22
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-22
 * @modifyMemo 修改备注：
 */

public class TrackListPresenter implements ITrackListPresenter {

    private Context mContext;
    private ITrackListView mTrackListView;
    private String mUserId;

    private TrackNetService mTrackNetService;

    private int pageNo = 1;
    private int pageSize = 10;

    private Callback1 mBackListener;
    private Callback1 mViewTrackListener;

    public TrackListPresenter(Context context, ITrackListView trackListView, String userId) {
        this.mContext = context;
        this.mTrackListView = trackListView;
        this.mUserId = userId;
        this.mTrackListView.setPresenter(this);
        mTrackNetService = new TrackNetService(mContext);
        getTrackList(pageNo, pageSize);
    }

    private void getTrackList(final int pageNo, int pageSize) {
        mTrackNetService.getTraceLinesByUserId(mUserId, pageNo, pageSize)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TrackList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mTrackListView.loadMore(null);
                    }

                    @Override
                    public void onNext(TrackList trackList) {
                        if (pageNo == 1) {
                            mTrackListView.initTrackList(trackList.getRows());
                        } else {
                            mTrackListView.loadMore(trackList.getRows());
                        }
                    }
                });
    }

    @Override
    public void loadMore() {
        pageNo++;
        getTrackList(pageNo, pageSize);
    }

    @Override
    public void back() {
        if (mBackListener != null) {
            mBackListener.onCallback(null);
        }
    }

    @Override
    public void setBackListener(Callback1 backListener) {
        this.mBackListener = backListener;
    }

    @Override
    public void viewTrack(long trackId) {
        if (mViewTrackListener != null) {
            mViewTrackListener.onCallback(trackId);
        }
    }

    @Override
    public void setViewTrackListener(Callback1 viewTrackListener) {
        mViewTrackListener = viewTrackListener;
    }
}

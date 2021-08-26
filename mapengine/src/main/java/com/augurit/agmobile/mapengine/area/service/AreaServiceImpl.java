package com.augurit.agmobile.mapengine.area.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.model.AreaLocate;
import com.augurit.agmobile.mapengine.common.model.UserArea;
import com.augurit.agmobile.mapengine.common.router.AgcomRouter;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by liangsh on 2016-12-24.
 */
public class AreaServiceImpl {
    private Context mContext;
    private AgcomRouter mAgcomRouter;

    public AreaServiceImpl(Context context){
        this.mContext = context.getApplicationContext();
        this.mAgcomRouter = new AgcomRouter(mContext);
    }

    public int getDiscodeId(){
        ProjectInfo projectInfo = ProjectDataManager.getInstance().getCurrentProject(mContext);
        return projectInfo.getProjectMapParam().getDiscodeId();
    }

    /**
     * 异步获取区域信息
     * @param callbackListener
     */
    public void getUserArea(final Callback2<UserArea> callbackListener){
        Observable observable = Observable.create(new Observable.OnSubscribe<UserArea>() {
            @Override
            public void call(Subscriber<? super UserArea> subscriber) {
                UserArea userArea = mAgcomRouter.getUserArea(getDiscodeId());
                if(userArea == null){
                    subscriber.onError(new Exception("获取区域信息失败！"));
                    return;
                }
                subscriber.onNext(userArea);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                });
        Subscription subscription =
                observable.subscribe(new Subscriber<UserArea>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callbackListener != null) {
                            callbackListener.onFail(new Exception(e));
                        }
                    }

                    @Override
                    public void onNext(UserArea userArea) {
                        if (callbackListener != null) {
                            callbackListener.onSuccess(userArea);
                        }
                    }
                });
    }

    /**
     * 异步获取区域位置信息
     * @param userAreaId
     * @param discodeLocateId
     * @param callbackListener
     */
    public void getAreaLocate(final int userAreaId, final int discodeLocateId,final Callback2<AreaLocate> callbackListener){
        Observable observable = Observable.create(new Observable.OnSubscribe<AreaLocate>() {
            @Override
            public void call(Subscriber<? super AreaLocate> subscriber) {
                AreaLocate userArea = mAgcomRouter.getAreaLocate(userAreaId, discodeLocateId);
                if(userArea == null){
                    subscriber.onError(new Exception("获取区域信息失败！"));
                    return;
                }
                subscriber.onNext(userArea);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                });
        Subscription subscription =
                observable.subscribe(new Subscriber<AreaLocate>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callbackListener != null) {
                            callbackListener.onFail(new Exception(e));
                        }
                    }

                    @Override
                    public void onNext(AreaLocate areaLocate) {
                        if (callbackListener != null) {
                            callbackListener.onSuccess(areaLocate);
                        }
                    }
                });
    }


}

package com.augurit.agmobile.gzps.doorno.utils;

import com.augurit.agmobile.gzps.doorno.service.MyRetroService;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by xiaoyu on 2017/5/27.
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 */


public abstract class HttpSubCribe<T> extends Subscriber<T> {
    public abstract Observable<T> getObservable(MyRetroService retrofit);

    @Override
    public void onCompleted() {
    }
}

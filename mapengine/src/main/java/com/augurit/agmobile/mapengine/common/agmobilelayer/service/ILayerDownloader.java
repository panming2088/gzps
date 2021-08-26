package com.augurit.agmobile.mapengine.common.agmobilelayer.service;

/**
 * Created by ac on 2016-07-07.
 */
public interface ILayerDownloader {
    interface OnLoadDataListenr{
        void onLoading();
        void onSuccess(String result);
        void onError(Exception e);
    }

    void loadMessageFromUrl(String url, OnLoadDataListenr listenr);
}

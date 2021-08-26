package com.augurit.agmobile.mapengine.layerdownload.view;

/**
 * Created by liangsh on 2016-10-10.
 */
public interface OnLayerDownloadListener<T> {
    void onSuccess(T t);

    /**
     * @param total  总下载量
     * @param downed  已下载
     */
    void onDownloading(double total, double downed);
    void onError(Throwable e);
}

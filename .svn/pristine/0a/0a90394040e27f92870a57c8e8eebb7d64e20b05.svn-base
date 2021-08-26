package com.augurit.agmobile.mapengine.layerdownload.dao;

import com.augurit.agmobile.mapengine.layerdownload.view.OnLayerDownloadListener;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.core.ags.FeatureServiceInfo;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTableEditErrors;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.tasks.geodatabase.GenerateGeodatabaseParameters;
import com.esri.core.tasks.geodatabase.GeodatabaseStatusCallback;
import com.esri.core.tasks.geodatabase.GeodatabaseStatusInfo;
import com.esri.core.tasks.geodatabase.GeodatabaseSyncTask;
import com.esri.core.tasks.geodatabase.SyncGeodatabaseParameters;

import java.io.File;
import java.util.Map;

/**
 * 描述：FeatureLayer下载
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerdownload.dao
 * @createTime 创建时间 ：2017-02-14
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-14
 * @modifyMemo 修改备注：
 */
public class FeatureLayerDownloadDao {

    /**
     * 下载FeatureLayer
     * @param serviceUrl 图层Url
     * @param cachePath 缓存文件路径（包含文件名）
     * @param extent 下载区域
     * @param outSr SpatialReference
     * @param listener 下载监听
     */
    public void downloadFeatures(String serviceUrl,
                                 String cachePath,
                                 Envelope extent,
                                 SpatialReference outSr,
                                 OnLayerDownloadListener listener) {
        GeodatabaseSyncTask task = new GeodatabaseSyncTask(serviceUrl, null);
        FeatureServiceInfo info = null;
        try {
            info = task.fetchFeatureServiceInfo();
        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(e);
            return;
        }
        File file = new File(cachePath);
        if(!file.exists()){
            GenerateGeodatabaseParameters params = new GenerateGeodatabaseParameters(info, extent, outSr, null, true);
            params.setOutSpatialRef(outSr);
            doDownloadFeatures(task, params, cachePath, listener);
        } else {
            doSynchronizeFeatures(task, cachePath, listener);
        }
    }

    /**
     * 下载
     */
    private void doDownloadFeatures(GeodatabaseSyncTask task,
                                    GenerateGeodatabaseParameters params,
                                    final String cachePath,
                                    final OnLayerDownloadListener listener) {
        CallbackListener<String> gdbResponseCallback = new CallbackListener<String>() {

            @Override
            public void onCallback(String path) {
                listener.onSuccess(path);
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

        };
        GeodatabaseStatusCallback statusCallback = new GeodatabaseStatusCallback() {

            @Override
            public void statusUpdated(GeodatabaseStatusInfo status) {
                if (!status.isDownloading()) {
//                    showMessage(activity, status.getStatus().toString());
                    listener.onDownloading(status.getTotalBytesDownloaded(), status.getDownloadSize());
                    LogUtil.i("FeatureLayerDownloader status getDownloadSize=" + status.getDownloadSize()
                            + " getTotalBytesDownloaded=" + status.getTotalBytesDownloaded());
                }

            }
        };
        task.generateGeodatabase(params, cachePath, false, statusCallback, gdbResponseCallback);
    }

    /**
     * 同步
     */
    private void doSynchronizeFeatures(GeodatabaseSyncTask task,
                                       String cachePath,
                                       final OnLayerDownloadListener listener) {
        try {
            Geodatabase gdb = new Geodatabase(cachePath);
            final SyncGeodatabaseParameters syncParams = gdb.getSyncParameters();
            CallbackListener<Map<Integer, GeodatabaseFeatureTableEditErrors>> syncResponseCallback = new CallbackListener<Map<Integer, GeodatabaseFeatureTableEditErrors>>() {

                @Override
                public void onCallback(Map<Integer, GeodatabaseFeatureTableEditErrors> objs) {
                    if (objs != null) {
                        if (objs.size() > 0) {
                            listener.onError(new Exception("同步失败"));
                        } else {
                            listener.onSuccess(null);
                        }

                    } else {
                        listener.onError(null);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    listener.onError(new Exception("同步失败"));
                }

            };
            GeodatabaseStatusCallback statusCallback = new GeodatabaseStatusCallback() {

                @Override
                public void statusUpdated(GeodatabaseStatusInfo status) {
                    listener.onDownloading(status.getTotalBytesDownloaded(), status.getDownloadSize());
//                    showMessage(activity, status.getStatus().toString());
                }
            };
            task.syncGeodatabase(syncParams, gdb, statusCallback, syncResponseCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.augurit.agmobile.mapengine.bufferanalysis.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.bufferanalysis.dao.RemoteBufferAnalysingRestDao;
import com.augurit.agmobile.mapengine.common.GeographyInfoManager;
import com.augurit.agmobile.mapengine.common.utils.AttributeUtil;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.router.LayerRouter;
import com.augurit.agmobile.mapengine.project.router.ProjectRouter;
import com.augurit.agmobile.mapengine.common.utils.LayerUrlUtil;
import com.augurit.am.cmpt.common.Callback2;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 缓冲分析服务类
 * @author 创建人 ：liangshenghong
 * @package 包名 ：com.augurit.agmobile.mapengine.bufferanalysis.service
 * @createTime 创建时间 ：2017-02-06
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-06
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public class BufferAnalysingServiceImpl implements IBufferAnalysingService {

    protected Context mContext;
    private LayerRouter mLayerRouter;
    private ProjectRouter mProjectRouter;
    private RemoteBufferAnalysingRestDao remoteBufferAnalysingRestDao;

    public BufferAnalysingServiceImpl(Context context) {
        this.mContext = context;
        this.mLayerRouter = new LayerRouter();
        this.mProjectRouter = ProjectDataManager.getInstance().getIGetProjectInfoService().getProjectRouter();
        remoteBufferAnalysingRestDao = new RemoteBufferAnalysingRestDao();
    }

    /**
     * 使用identify方法进行缓冲分析，并回调分析结果
     * @param geometry 待分析地图范围
     * @param selectedLayers 待分析图层
     * @param mapMaxExetnt 地图最大范围
     * @param callback2 结果回调
     */
    @Override
    public void getAnalysingResult(final Geometry geometry, final List<LayerInfo> selectedLayers, final Envelope mapMaxExetnt, final Callback2<Map<String, AMFindResult[]>> callback2){
        Observable.create(new Observable.OnSubscribe<Map<String, AMFindResult[]>>() {
            @Override
            public void call(Subscriber<? super Map<String, AMFindResult[]>> subscriber) {
                Map<String, AMFindResult[]> identifyResultMap = new HashMap<>();
                Envelope envelope = new Envelope();
                mapMaxExetnt.queryEnvelope(envelope);
                IdentifyParameters parameters = new IdentifyParameters(geometry,
                        envelope,
                        GeographyInfoManager.getInstance().getSpatialReference(), new int[]{},
                        GeographyInfoManager.getInstance().getMapView().getWidth(),
                        GeographyInfoManager.getInstance().getMapView().getHeight(),
                        98,
                        true);
                for (LayerInfo layerInfo : selectedLayers) {
                    // 从一个图层URL中获取图层的服务URL
                    String url = LayerUrlUtil.getServerUrlFromLayerUrl(layerInfo.getUrl());
                    url = url.replace("FeatureServer", "MapServer");
                    // 从一个图层URL中获取图层ID
                    int layerId = LayerUrlUtil.getChildLayerIdFromLayerUrl(layerInfo.getUrl());
                    if (layerId != -1) {
                        parameters.setLayers(new int[]{layerId});
                    }
                    // 进行缓冲分析
                    IdentifyResult[] results = remoteBufferAnalysingRestDao.getAnalysingResultViaIdentifyTask(url, parameters);
                    AMFindResult[] amFindResults = AttributeUtil.covertIdentifyResultToAgFindResult(results);
                    identifyResultMap.put(layerInfo.getLayerName(), amFindResults);
                }
                subscriber.onNext(identifyResultMap);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })
                .subscribe(new Subscriber<Map<String, AMFindResult[]>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callback2.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(Map<String, AMFindResult[]> identifyResultMap) {
                        callback2.onSuccess(identifyResultMap);
                    }
                });
    }


    /**
     * 获取图层列表
     * @param callback2 回调
     */
    @Override
    public void getLayers(final Callback2<List<LayerInfo>> callback2){
        Observable.create(new Observable.OnSubscribe<List<LayerInfo>>() {
            @Override
            public void call(Subscriber<? super List<LayerInfo>> subscriber) {
                BaseInfoManager manager = new BaseInfoManager();
                String userId = BaseInfoManager.getUserId(mContext);
                // 获取当前专题ID
                String projectId = mProjectRouter.getCurrentProjectId(mContext, userId);
                try {
                    // 获取当前用户、当前专题的图层列表
                    List<LayerInfo> layerInfos = mLayerRouter.getLayerInfos(mContext, projectId, userId);
                    subscriber.onNext(layerInfos);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LayerInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback2.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<LayerInfo> layerInfos) {
                        callback2.onSuccess(layerInfos);
                    }
                });
    }
}

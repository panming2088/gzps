package com.augurit.agmobile.mapengine.bufferanalysis.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.dao.RemoteAgcomRestDao;
import com.augurit.agmobile.mapengine.common.model.FeatureSet;
import com.augurit.agmobile.mapengine.common.model.SpatialBufferParam;
import com.augurit.agmobile.mapengine.common.model.SpatialBufferResult;
import com.augurit.agmobile.mapengine.common.service.AgcomService;
import com.augurit.agmobile.mapengine.common.utils.wktutil.WktUtil;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.bufferanalysis.service
 * @createTime 创建时间 ：2017-03-30
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-30
 * @modifyMemo 修改备注：
 */
public class AgcomBufferAnalysisService extends BufferAnalysingServiceImpl {

    private MapView mMapView;
    private AgcomService mAgcomService;
    private RemoteAgcomRestDao mRemoteAgcomRestDao;

    public AgcomBufferAnalysisService(Context context, MapView mapView) {
        super(context);
        this.mMapView = mapView;
        mAgcomService = new AgcomService(mContext);
        mRemoteAgcomRestDao = new RemoteAgcomRestDao(mContext);
    }

    @Override
    public void getAnalysingResult(final Geometry geometry,
                                   final List<LayerInfo> selectedLayers,
                                   final Envelope mapMaxExetnt,
                                   final Callback2<Map<String, AMFindResult[]>> callback) {
        Observable.create(new Observable.OnSubscribe<Map<String, AMFindResult[]>>() {
            @Override
            public void call(Subscriber<? super Map<String, AMFindResult[]>> subscriber) {
                String wkt = WktUtil.getWKTFromGeometry(mMapView.getSpatialReference(), geometry);
                SpatialBufferParam spatialBufferParam = mAgcomService.getSpatialBufferParam(selectedLayers, wkt, "1", null, 0, 1000, false, true);
                String json = JsonUtil.getJson(spatialBufferParam);
                SpatialBufferResult spatialBufferResult = mRemoteAgcomRestDao.spatialBuffer(json);
                if (spatialBufferResult == null
                        || ListUtil.isEmpty(spatialBufferResult.getFeatureSets())) {
                    subscriber.onError(new Exception("缓冲分析失败！"));
                    return;
                }
                Map<String, AMFindResult[]> amFindResults = new HashMap<String, AMFindResult[]>();
                List<FeatureSet> featureSets = spatialBufferResult.getFeatureSets();
                for(FeatureSet featureSet : featureSets){
                    String layerName = featureSet.getLayerAliases();
                    amFindResults.put(layerName, mAgcomService.convert(featureSet, mMapView));
                }
                subscriber.onNext(amFindResults);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, AMFindResult[]>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(Map<String, AMFindResult[]> amFindResults) {
                        callback.onSuccess(amFindResults);

                    }
                });
    }
}
package com.augurit.agmobile.mapengine.layerquery.service;


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
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerquery.service
 * @createTime 创建时间 ：2017-03-30
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-30
 * @modifyMemo 修改备注：
 */
public class AgcomLayerQueryService extends LayerQueryService {

    private Context mContext;
    private AgcomService mAgcomService;
    private RemoteAgcomRestDao mRemoteAgcomRestDao;

    public AgcomLayerQueryService(Context context){
        super();
        mContext = context;
        mAgcomService = new AgcomService(mContext);
        mRemoteAgcomRestDao = new RemoteAgcomRestDao(mContext);
    }

    @Override
    public void queryLayer(final Context context, final String seachKey, final MapView mapView, final Geometry geometry,
                           final List<LayerInfo> queryLayers, int maxCount, final Callback2<List<AMFindResult>> callback) {
        Observable.create(new Observable.OnSubscribe<List<AMFindResult>>() {
            @Override
            public void call(Subscriber<? super List<AMFindResult>> subscriber) {
                String wkt = WktUtil.getWKTFromGeometry(mapView.getSpatialReference(), geometry);
                SpatialBufferParam spatialBufferParam = null;
                if(StringUtil.isEmpty(seachKey)){
                    spatialBufferParam = mAgcomService.getSpatialBufferParam(queryLayers, wkt, "1", null, 0, 200, false, true);
                } else {
                    spatialBufferParam = mAgcomService.getSpatialBufferParamWithSearchKey(seachKey, queryLayers, wkt, "1", 0, 200, false, true);
                }
                String json = JsonUtil.getJson(spatialBufferParam);
                SpatialBufferResult spatialBufferResult = mRemoteAgcomRestDao.spatialBuffer(json);
                if (spatialBufferResult == null
                        || ListUtil.isEmpty(spatialBufferResult.getFeatureSets())) {
                    subscriber.onError(new Exception("缓冲分析失败！"));
                    return;
                }
                List<AMFindResult> amFindResults = new ArrayList<AMFindResult>();
                List<FeatureSet> featureSets = spatialBufferResult.getFeatureSets();
                for(FeatureSet featureSet : featureSets){
                    AMFindResult[] amFindResultsArray = mAgcomService.convert(featureSet, mapView);
                    amFindResults.addAll(Arrays.asList(amFindResultsArray));
                }
                subscriber.onNext(amFindResults);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AMFindResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast(context, "查询图层失败");
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<AMFindResult> featureResults) {
                        callback.onSuccess(featureResults);
                    }
                });
    }
}

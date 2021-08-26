package com.augurit.agmobile.mapengine.identify.service;

import android.app.Activity;
import android.content.Context;

import com.augurit.agmobile.mapengine.common.dao.RemoteAgcomRestDao;
import com.augurit.agmobile.mapengine.common.model.Feature;
import com.augurit.agmobile.mapengine.common.model.FeatureSet;
import com.augurit.agmobile.mapengine.common.model.Field;
import com.augurit.agmobile.mapengine.common.model.SpatialBufferParam;
import com.augurit.agmobile.mapengine.common.model.SpatialBufferResult;
import com.augurit.agmobile.mapengine.common.model.SpatialQueryParam;
import com.augurit.agmobile.mapengine.common.utils.wktutil.WktUtil;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.AgcomLayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.MultiPlanVectorLayerBean;
import com.augurit.agmobile.mapengine.layermanage.router.LayerRouter;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.log.save.imp.AMCrashWriter;
import com.augurit.am.fw.utils.JsonUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.identify.service
 * @createTime 创建时间 ：2017-03-27
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-27
 * @modifyMemo 修改备注：
 */
public class AgcomIdentifyService implements IIdentifyService {

    private Context mContext;
    private RemoteAgcomRestDao mRemoteAgcomRestDao;
    private LayerRouter mLayerRouter;
    private String mCurrentProjectId;
    private String mUserId;

    @Override
    public void selectedFeature(Activity context, final MapView mapView,
                                final List<LayerInfo> visibleQueryableLayers,
                                final Geometry geometry, int tolerance,
                                final Callback2<AMFindResult[]> callback) {
        this.mContext = context;
        this.mRemoteAgcomRestDao = new RemoteAgcomRestDao(context);
        this.mLayerRouter = new LayerRouter();
        this.mCurrentProjectId = ProjectDataManager.getInstance().getCurrentProjectId(context);
        this.mUserId = BaseInfoManager.getUserId(context);
        Observable.create(new Observable.OnSubscribe<AMFindResult[]>() {
            @Override
            public void call(Subscriber<? super AMFindResult[]> subscriber) {
                List<LayerInfo> layerInfos = visibleQueryableLayers;
                String wkt = WktUtil.getWKTFromGeometry(mapView.getSpatialReference(), geometry);
                List<FeatureSet> featureSets = spatialBuffer(layerInfos, wkt, String.valueOf(5));

                if (ListUtil.isEmpty(featureSets)) {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                    return;
                }
                List<AMFindResult> amFindResultList = new ArrayList<AMFindResult>();
                for (FeatureSet featureSet : featureSets) {
                    if(ListUtil.isEmpty(featureSet.getFeatures())){
                        continue;
                    }
                    List<Field> fieldAliases = featureSet.getFieldAliases();
                    //对Field进行排序
                    Collections.sort(fieldAliases, new Comparator<Field>() {
                        @Override
                        public int compare(Field field, Field t1) {
                            return field.getFieldOrder() - t1.getFieldOrder();
                        }
                    });
                    for(Feature feature : featureSet.getFeatures()){
                        //ListOrderedMap是一个有序的Map，引用自framework中的commons-collections4-4.1.jar
                        ListOrderedMap<String, Object> attributes = new ListOrderedMap<String, Object>();
                        for (Field field : fieldAliases) {
                            //过滤掉不显示的字段
                            if(!field.getShowInResult().equals("1")){
                                continue;
                            }
                            String key = field.getFieldName();
                            Object value = feature.getValues().get(key);
                            String realKey = field.getFieldAlias();
                            attributes.put(realKey, value);
                        }
                        AMFindResult agFindResult = new AMFindResult();
                        agFindResult.setAttributes(attributes);
                        agFindResult.setValue(feature.getAnnoValue());
                        agFindResult.setDisplayName(feature.getAnnoValue());
                        agFindResult.setGeometry(WktUtil.getGeometryFromWKT(feature.getGeometry().getWkt(), mapView.getSpatialReference().getID()));
                        amFindResultList.add(agFindResult);
                    }
                }
                AMFindResult[] amFindResults = new AMFindResult[amFindResultList.size()];
                for(int i=0,j=amFindResultList.size();i<j;i++){
                    amFindResults[i] = amFindResultList.get(i);
                }
                subscriber.onNext(amFindResults);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AMFindResult[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AMCrashWriter.getInstance(mContext).writeCrash(new Exception(e), "subscribe error");
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(AMFindResult[] amFindResults) {
                        callback.onSuccess(amFindResults);

                    }
                });
    }



    private int getVectorProjectLayerId(int tileProjectLayerId){
        AgcomLayerInfo agcomLayerInfo = mLayerRouter.getAgcomLayerInfo(mContext, mCurrentProjectId, mUserId);
        List<AgcomLayerInfo.TiledLayerBean> tiledLayerBeans = agcomLayerInfo.getTiledLayer();
        List<MultiPlanVectorLayerBean> vectorLayerBeans = agcomLayerInfo.getVectorLayer();
        String layerTable = "";
        for(AgcomLayerInfo.TiledLayerBean tiledLayerBean : tiledLayerBeans){
            if(tiledLayerBean.getId()==tileProjectLayerId){
                layerTable = tiledLayerBean.getLayer_table();
                break;
            }
        }
        int projectLayerId = 0;
        for(MultiPlanVectorLayerBean vectorLayerBean : vectorLayerBeans){
            if(vectorLayerBean.getTable().equals(layerTable)){
                projectLayerId =  vectorLayerBean.getId();
            }
        }
        return projectLayerId;
    }

    private String getLayerTableByProjectLayerId(int projectLayerId, boolean isTiledLayer){
        AgcomLayerInfo agcomLayerInfo = mLayerRouter.getAgcomLayerInfo(mContext, mCurrentProjectId, mUserId);
        String layerTable = "";
        if(isTiledLayer) {
            List<AgcomLayerInfo.TiledLayerBean> tiledLayerBeans = agcomLayerInfo.getTiledLayer();
            for (AgcomLayerInfo.TiledLayerBean tiledLayerBean : tiledLayerBeans) {
                if (tiledLayerBean.getId() == projectLayerId) {
                    layerTable = tiledLayerBean.getLayer_table();
                    break;
                }
            }
        } else {
            List<MultiPlanVectorLayerBean> vectorLayerBeans = agcomLayerInfo.getVectorLayer();
            for (MultiPlanVectorLayerBean vectorLayerBean : vectorLayerBeans) {
                if (vectorLayerBean.getId() == projectLayerId) {
                    layerTable = vectorLayerBean.getTable();
                    break;
                }
            }
        }
        return layerTable;
    }

    private String getLayerNameByProjectLayerId(long projectLayerId, boolean isTiledLayer){
        AgcomLayerInfo agcomLayerInfo = mLayerRouter.getAgcomLayerInfo(mContext, mCurrentProjectId, mUserId);

        String layerName = "";
        if(isTiledLayer) {
            List<AgcomLayerInfo.TiledLayerBean> tiledLayerBeans = agcomLayerInfo.getTiledLayer();
            for (AgcomLayerInfo.TiledLayerBean tiledLayerBean : tiledLayerBeans) {
                if (tiledLayerBean.getId() == projectLayerId) {
                    layerName = tiledLayerBean.getName();
                    break;
                }
            }
        } else {
            List<MultiPlanVectorLayerBean> vectorLayerBeans = agcomLayerInfo.getVectorLayer();
            for (MultiPlanVectorLayerBean vectorLayerBean : vectorLayerBeans) {
                if (vectorLayerBean.getId() == projectLayerId) {
                    layerName = vectorLayerBean.getName();
                    break;
                }
            }
        }
        return layerName;
    }

    public SpatialQueryParam getSpatialQueryParams(String projectLayerId, String layerName,
                                                   String geometry, double filtrateNum, String where,
                                                   Integer startRow, Integer endRow, boolean isReturnCount,
                                                   boolean isReturnValues, List<String> outFields) {
        SpatialQueryParam spatialQueryParam = new SpatialQueryParam();
        spatialQueryParam.setProjectLayerId(projectLayerId);
        spatialQueryParam.setLayerName(layerName);
        spatialQueryParam.setFiltrateNum(filtrateNum);
        List<String> geometrys = new ArrayList<String>();
        if (geometry != null) {
            geometrys.add(geometry);
        }
        spatialQueryParam.setGeometry(geometrys);
        spatialQueryParam.setReturnGeometry(true);
        spatialQueryParam.setReturnCount(isReturnCount);
        spatialQueryParam.setReturnMis(false);
        spatialQueryParam.setReturnAlias(true);
        spatialQueryParam.setReturnValues(isReturnValues);
        spatialQueryParam.setWhere(where);
        spatialQueryParam.setStartRow(startRow);
        spatialQueryParam.setEndRow(endRow);
        spatialQueryParam.setOutFields(outFields);
        return spatialQueryParam;
    }

    private List<FeatureSet> spatialBuffer(List<LayerInfo> layerInfos, String wkt, String tolerance){
        List<SpatialQueryParam> spatialQueryParams = new ArrayList<>();
        for (LayerInfo layerInfo : layerInfos) {
            int projectLayerId = layerInfo.getProjectLayerId();
            String layerTable = getLayerTableByProjectLayerId(projectLayerId, layerInfo.isBelongToTile());
            SpatialQueryParam spatialQueryParam = getSpatialQueryParams("null", layerTable, null, 0, null, 0, 100, false, true, null);
            spatialQueryParams.add(spatialQueryParam);
        }
        SpatialBufferParam spatialBufferParam = new SpatialBufferParam();
        spatialBufferParam.setDistance(tolerance);
        List<String> geometry = new ArrayList<String>();
        geometry.add(wkt);
        spatialBufferParam.setGeometry(geometry);
        spatialBufferParam.setLayerParams(spatialQueryParams);
        String json = JsonUtil.getJson(spatialBufferParam);
        SpatialBufferResult spatialBufferResult = mRemoteAgcomRestDao.spatialBuffer(json);
        if(spatialBufferResult == null){
            return null;
        }
        return spatialBufferResult.getFeatureSets();
    }

    private List<FeatureSet> spatialQuery(List<LayerInfo> layerInfos, String wkt){
        List<SpatialQueryParam> spatialQueryParams = new ArrayList<>();
        for (LayerInfo layerInfo : layerInfos) {
            SpatialQueryParam spatialQueryParam = getSpatialQueryParams(String.valueOf(layerInfo.getProjectLayerId()), null, wkt, 0, null, 0, 100, false, true, null);
            spatialQueryParams.add(spatialQueryParam);
        }
        String json = JsonUtil.getJson(spatialQueryParams);
        return mRemoteAgcomRestDao.spatialQuery(json);
    }
}

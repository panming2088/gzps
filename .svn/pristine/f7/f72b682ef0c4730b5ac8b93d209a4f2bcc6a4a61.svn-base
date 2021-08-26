package com.augurit.agmobile.mapengine.common.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.dao.RemoteAgcomRestDao;
import com.augurit.agmobile.mapengine.common.model.Feature;
import com.augurit.agmobile.mapengine.common.model.FeatureSet;
import com.augurit.agmobile.mapengine.common.model.Field;
import com.augurit.agmobile.mapengine.common.model.SpatialBufferParam;
import com.augurit.agmobile.mapengine.common.model.SpatialBufferResult;
import com.augurit.agmobile.mapengine.common.model.SpatialQueryParam;
import com.augurit.agmobile.mapengine.common.router.AgcomRouter;
import com.augurit.agmobile.mapengine.common.utils.wktutil.WktUtil;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.AgcomLayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.MultiPlanVectorLayerBean;
import com.augurit.agmobile.mapengine.layermanage.router.LayerRouter;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
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
 * @package 包名 ：com.augurit.agmobile.mapengine.common
 * @createTime 创建时间 ：2017-03-30
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-03-30
 * @modifyMemo 修改备注：
 */
public class AgcomService {

    private Context mContext;
    private AgcomRouter mAgcomRouter;
    private LayerRouter mLayerRouter;
    private String mCurrentProjectId;
    private String mUserId;

    public AgcomService(Context context){
        this.mContext = context;
        this.mAgcomRouter = new AgcomRouter(context);
        this.mLayerRouter = new LayerRouter();
        this.mCurrentProjectId = ProjectDataManager.getInstance().getCurrentProjectId(context);
        this.mUserId = BaseInfoManager.getUserId(context);
    }

    public void spatialBuffer(final MapView mapView,
                                final List<LayerInfo> layerInfos, final Geometry geometry,
                                final int distance, final Callback2<AMFindResult[]> callback) {
        Observable.create(new Observable.OnSubscribe<AMFindResult[]>() {
            @Override
            public void call(Subscriber<? super AMFindResult[]> subscriber) {
                String wkt = WktUtil.getWKTFromGeometry(mapView.getSpatialReference(), geometry);
                SpatialBufferParam spatialBufferParam = getSpatialBufferParam(layerInfos, wkt, String.valueOf(distance), null, 0, 100, false, true);
                List<FeatureSet> featureSets = doSpatialBuffer(spatialBufferParam);

                if (ListUtil.isEmpty(featureSets)) {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                    return;
                }
                AMFindResult[] amFindResults = convert(featureSets, mapView);
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
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(AMFindResult[] amFindResults) {
                        callback.onSuccess(amFindResults);

                    }
                });
    }

    public void spatialQuery(final MapView mapView,
                             final LayersService layersService, final Geometry geometry,
                             final Callback2<AMFindResult[]> callback){
        Observable.create(new Observable.OnSubscribe<AMFindResult[]>() {
            @Override
            public void call(Subscriber<? super AMFindResult[]> subscriber) {
                List<LayerInfo> layerInfos = layersService.getVisibleQueryableLayers();
                String wkt = WktUtil.getWKTFromGeometry(mapView.getSpatialReference(), geometry);
                List<FeatureSet> featureSets = doSpatialQuery(layerInfos, wkt);

                if (ListUtil.isEmpty(featureSets)) {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                    return;
                }
                AMFindResult[] amFindResults = convert(featureSets, mapView);
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
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(AMFindResult[] amFindResults) {
                        callback.onSuccess(amFindResults);

                    }
                });
    }

    private AMFindResult[] convert(List<FeatureSet> featureSets, MapView mapView){
        List<AMFindResult> amFindResultList = new ArrayList<AMFindResult>();
        for (FeatureSet featureSet : featureSets) {
            if (ListUtil.isEmpty(featureSet.getFeatures())) {
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
            for (Feature feature : featureSet.getFeatures()) {
                //ListOrderedMap是一个有序的Map，引用自framework中的commons-collections4-4.1.jar
                ListOrderedMap<String, Object> attributes = new ListOrderedMap<String, Object>();
                for (Field field : fieldAliases) {
                    //过滤掉不显示的字段
                    if (!field.getShowInResult().equals("1")) {
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
        for (int i = 0, j = amFindResultList.size(); i < j; i++) {
            amFindResults[i] = amFindResultList.get(i);
        }
        return amFindResults;
    }

    public AMFindResult[] convert(FeatureSet featureSet, MapView mapView){
        List<FeatureSet> featureSets = new ArrayList<>(1);
        featureSets.add(featureSet);
        return convert(featureSets, mapView);
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

    private SpatialQueryParam getSpatialQueryParams(String projectLayerId, String layerTable,
                                                   String geometry, double filtrateNum, String where,
                                                   Integer startRow, Integer endRow, boolean isReturnCount,
                                                   boolean isReturnValues, List<String> outFields) {
        SpatialQueryParam spatialQueryParam = new SpatialQueryParam();
        spatialQueryParam.setProjectLayerId(projectLayerId);
        spatialQueryParam.setLayerName(layerTable);
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

    public SpatialBufferParam getSpatialBufferParam(List<LayerInfo> layerInfos, String wkt, String distance,String where, int startRow, int endRow, boolean isReturnCount, boolean isReturnValues){
        List<SpatialQueryParam> spatialQueryParams = new ArrayList<>();
        for (LayerInfo layerInfo : layerInfos) {
            int projectLayerId = layerInfo.getProjectLayerId();
            String layerTable = getLayerTableByProjectLayerId(projectLayerId, layerInfo.isBelongToTile());
            SpatialQueryParam spatialQueryParam = getSpatialQueryParams("null", layerTable, null, 0, where, startRow, endRow, isReturnCount, isReturnValues, null);
            spatialQueryParams.add(spatialQueryParam);
        }
        SpatialBufferParam spatialBufferParam = new SpatialBufferParam();
        spatialBufferParam.setDistance(distance);
        List<String> geometry = new ArrayList<String>();
        geometry.add(wkt);
        spatialBufferParam.setGeometry(geometry);
        spatialBufferParam.setLayerParams(spatialQueryParams);
        return spatialBufferParam;
    }

    public SpatialBufferParam getSpatialBufferParamWithSearchKey(String searchKey, List<LayerInfo> layerInfos, String wkt, String distance, int startRow, int endRow, boolean isReturnCount, boolean isReturnValues){
        List<SpatialQueryParam> spatialQueryParams = new ArrayList<>();
        for (LayerInfo layerInfo : layerInfos) {
            int projectLayerId = layerInfo.getProjectLayerId();
            String layerTable = getLayerTableByProjectLayerId(projectLayerId, layerInfo.isBelongToTile());
            String where = getSearchKeyWhere(String.valueOf(projectLayerId), searchKey);
            SpatialQueryParam spatialQueryParam = getSpatialQueryParams("null", layerTable, null, 0, where, startRow, endRow, isReturnCount, isReturnValues, null);
            spatialQueryParams.add(spatialQueryParam);
        }
        SpatialBufferParam spatialBufferParam = new SpatialBufferParam();
        spatialBufferParam.setDistance(distance);
        List<String> geometry = new ArrayList<String>();
        geometry.add(wkt);
        spatialBufferParam.setGeometry(geometry);
        spatialBufferParam.setLayerParams(spatialQueryParams);
        return spatialBufferParam;
    }

    public String getSearchKeyWhere(String projectLayerId, String searchKey){
        String where = "";
        List<Field> fields = mAgcomRouter.getLayerField(projectLayerId);
        for(Field field : fields){
            if(!field.getSearchable().equals("1")
                    || field.getFieldName().equals("SHAPE")){
                continue;
            }
            where = where + " " + field.getFieldName() + " like '%" + searchKey + "%' or";
        }
        where = where.substring(0, where.length()-2);
        where = "( " + where + " )";
        return where;
    }

    private List<FeatureSet> doSpatialBuffer(SpatialBufferParam spatialBufferParam){
        String json = JsonUtil.getJson(spatialBufferParam);
        SpatialBufferResult spatialBufferResult = mAgcomRouter.spatialBuffer(json);
        if(spatialBufferResult == null){
            return null;
        }
        return spatialBufferResult.getFeatureSets();
    }

    private List<FeatureSet> doSpatialQuery(List<LayerInfo> layerInfos, String wkt){
        List<SpatialQueryParam> spatialQueryParams = new ArrayList<>();
        for (LayerInfo layerInfo : layerInfos) {
            SpatialQueryParam spatialQueryParam = getSpatialQueryParams(String.valueOf(layerInfo.getProjectLayerId()), null, wkt, 0, null, 0, 100, false, true, null);
            spatialQueryParams.add(spatialQueryParam);
        }
        String json = JsonUtil.getJson(spatialQueryParams);
        return mAgcomRouter.spatialQuery(json);
    }
}

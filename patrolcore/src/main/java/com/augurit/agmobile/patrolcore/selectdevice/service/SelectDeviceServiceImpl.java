package com.augurit.agmobile.patrolcore.selectdevice.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.augurit.agmobile.mapengine.common.model.SpatialBufferParam;
import com.augurit.agmobile.mapengine.common.utils.AttributeUtil;
import com.augurit.agmobile.mapengine.common.utils.EsriUtil;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.identify.service.IIdentifyService;
import com.augurit.agmobile.mapengine.identify.util.IdentifyServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.selectdevice.model.Device;
import com.augurit.agmobile.patrolcore.selectdevice.utils.SelectDeviceUtil;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.db.liteorm.db.annotation.Ignore;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Latlon;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.tasks.ags.find.FindParameters;
import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.find.FindTask;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;
import com.esri.core.tasks.query.QueryTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 设施选择Service的默认实现类
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectdevice.service
 * @createTime 创建时间 ：17/7/26
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/26
 * @modifyMemo 修改备注：
 */

public class SelectDeviceServiceImpl implements ISelectDeviceService{

    protected Context mContext;

    protected SpatialReference spatialReference;
    protected Envelope maxExtent;

    public SelectDeviceServiceImpl(Context context){
        mContext = context;
    }

    @Override
    public Observable<List<Device>> getSuggestionDevice(final MapView mapView, final LatLng latLng) {
        List<LayerInfo> visibleQueryableLayers = LayerServiceFactory.provideLayerService(mContext).getVisibleQueryableLayers();

       // final SpatialReference spatialReference = visibleQueryableLayers.get(0).getSpatialReference();
        final Envelope envelope = visibleQueryableLayers.get(0).getMaxExtent();

        return getQueryableLayerMap(mContext, visibleQueryableLayers)
                .map(new Func1<Map<String, int[]>, Map<IdentifyTask, IdentifyParameters>>() {
                    @Override
                    public Map<IdentifyTask, IdentifyParameters> call(Map<String, int[]> layers) {
                        //封装Identify条件
                        return getIdentifyParametersMap(layers, new Point(latLng.getLongitude(),latLng.getLongitude()),mapView,25);
                    }
                })
                .map(new Func1<Map<IdentifyTask, IdentifyParameters>, List<IdentifyResult>>() {
                    @Override
                    public List<IdentifyResult> call(Map<IdentifyTask, IdentifyParameters> identifyMap) {
                        List<IdentifyResult> identifyResults = getIdentifyResults(identifyMap);
                        return identifyResults;
                    }
                })
                .map(new Func1<List<IdentifyResult>, IdentifyResult[]>() {
                    @Override
                    public IdentifyResult[] call(List<IdentifyResult> results) {
                        //将结果根据点/线/面的顺序进行排序
                        return sortedIdentifyResult(results);
                    }
                })
                //进行过滤掉相同的选项
                .map(new Func1<IdentifyResult[], IdentifyResult[]>() {
                    @Override
                    public IdentifyResult[] call(IdentifyResult[] results) {
                        return results;
                    }
                })
                //将IdentifyResult转换成通用的AMFindResult
                .map(new Func1<IdentifyResult[], AMFindResult[]>() {
                    @Override
                    public AMFindResult[] call(IdentifyResult[] results) {
                        return AttributeUtil.covertIdentifyResultToAgFindResult(results);
                    }
                })
                .map(new Func1<AMFindResult[], List<Device>>() {
                    @Override
                    public List<Device> call(AMFindResult[] amFindResults) {
                        if (amFindResults != null && amFindResults.length >=1){
                            List<Device> devices = new ArrayList<Device>();
                           for (AMFindResult amFindResult : amFindResults){
                               Device device = getDevice(amFindResult);
                               devices.add(device);
                           }
                           return devices;
                        }
                        return null;
                    }
                }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Device>> getSuggestionDevice(String text) {
        return new FakeSelectDeviceService(mContext).getSuggestionDevice(text);
    }

    @Override
    public Observable<String> getWktByDeviceId(String deviceId) {
        return new FakeSelectDeviceService(mContext).getWktByDeviceId(deviceId);
    }

    @Override
    public Observable<Geometry> getWktByDeviceId(final String deviceId, final String deviceName) {
        ILayersService layerService2 = LayerServiceFactory.provideLayerService(mContext);
        return layerService2.getSortedLayerInfos()
                .map(new Func1<List<LayerInfo>, Geometry>() {
                    @Override
                    public Geometry call(List<LayerInfo> layerInfos) {
                        List<FindResult> finalResult = new ArrayList<FindResult>();
                        for (LayerInfo layerInfo: layerInfos){
                            Map<String, int[]> findableOrIdentifiableLayer = EsriUtil.getFindableOrIdentifiableLayer(layerInfo);
                            Set<Map.Entry<String, int[]>> entries = findableOrIdentifiableLayer.entrySet();
                            for (Map.Entry<String, int[]> entry : entries){
                                //执行findTask，查找displayField等于deviceName的数据
                                FindTask findTask = new FindTask(entry.getKey());
                                FindParameters findParameters = new FindParameters(deviceName,entry.getValue());
                                try {
                                    List<FindResult> findResults = findTask.execute(findParameters);
                                    if (!ValidateUtil.isListNull(findResults)){
                                        finalResult.addAll(findResults);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        //判断是否有id相同
                        for (FindResult findResult : finalResult){
                            if (deviceId.equals(findResult.getAttributes().get("OBJECTID"))
                                    || deviceId.equals(findResult.getAttributes().get("OBJECTID_1"))
                                    || deviceId.equals(findResult.getAttributes().get("唯一标识"))
                                    || deviceId.equals(findResult.getAttributes().get("FID"))){
                                return findResult.getGeometry();
                            }
                        }
                        return null;
                    }
                }).subscribeOn(Schedulers.io());
    }

    /**
     * 根据当前经纬度查找附近的设施，并默认返回第一个
     * @param longitude
     * @param latitude
     * @return
     */
    @Override
    public Observable<Device> getNearbyDevice(final MapView mapView, final double longitude, final double latitude) {

        return LayerServiceFactory.provideLayerService(mContext).getSortedLayerInfos()
                .flatMap(new Func1<List<LayerInfo>, Observable<Map<String, int[]>>>() {
                    @Override
                    public Observable<Map<String, int[]>> call(List<LayerInfo> layerInfos) {
                        List<LayerInfo> queryableLayers = new ArrayList<LayerInfo>();
                        for (LayerInfo layerInfo : layerInfos){
                            if (layerInfo.isQueryable()){
                                queryableLayers.add(layerInfo);
                            }
                        }
                       /* if (!ListUtil.isEmpty(queryableLayers)){
                            spatialReference = queryableLayers.get(0).getSpatialReference();
                            maxExtent = queryableLayers.get(0).getMaxExtent();
                        }*/
                        return getQueryableLayerMap(mContext, queryableLayers);
                    }
                }).map(new Func1<Map<String, int[]>, Map<IdentifyTask, IdentifyParameters>>() {
                    @Override
                    public Map<IdentifyTask, IdentifyParameters> call(Map<String, int[]> layers) {
                        //封装Identify条件
                        return getIdentifyParametersMap(layers, new Point(longitude,latitude),mapView,25);
                    }
                })
                .map(new Func1<Map<IdentifyTask, IdentifyParameters>, List<IdentifyResult>>() {
                    @Override
                    public List<IdentifyResult> call(Map<IdentifyTask, IdentifyParameters> identifyMap) {
                        List<IdentifyResult> identifyResults = getIdentifyResults(identifyMap);
                        return identifyResults;
                    }
                })
                .map(new Func1<List<IdentifyResult>, IdentifyResult[]>() {
                    @Override
                    public IdentifyResult[] call(List<IdentifyResult> results) {
                        //将结果根据点/线/面的顺序进行排序
                        return sortedIdentifyResult(results);
                    }
                })
                //进行过滤掉相同的选项
                .map(new Func1<IdentifyResult[], IdentifyResult[]>() {
                    @Override
                    public IdentifyResult[] call(IdentifyResult[] results) {
                        return results;
                    }
                })
                //将IdentifyResult转换成通用的AMFindResult
                .map(new Func1<IdentifyResult[], AMFindResult[]>() {
                    @Override
                    public AMFindResult[] call(IdentifyResult[] results) {
                        return AttributeUtil.covertIdentifyResultToAgFindResult(results);
                    }
                })
                .map(new Func1<AMFindResult[], Device>() {
                    @Override
                    public Device call(AMFindResult[] amFindResults) {
                        if (amFindResults != null && amFindResults.length >=1 ){
                            return getDevice(amFindResults[0]);
                        }
                        return null;
                    }
                }).subscribeOn(Schedulers.io());
    }

    @NonNull
    protected Device getDevice(AMFindResult amFindResult) {
        Device device = new Device();
        device.setId(SelectDeviceUtil.getIdFromAMFindResult(amFindResult));
        device.setGeometry(amFindResult.getGeometry());
        device.setName(device.getName());
        return device;
    }

    @NonNull
    protected IdentifyResult[] sortedIdentifyResult(List<IdentifyResult> results) {
        List<IdentifyResult> pointResult = new ArrayList<IdentifyResult>();
        List<IdentifyResult> lineResult = new ArrayList<IdentifyResult>();
        List<IdentifyResult> polygonResult = new ArrayList<IdentifyResult>();
        for (IdentifyResult identifyResult : results) {
            if (identifyResult.getGeometry().getType() == Geometry.Type.POINT) {
                pointResult.add(identifyResult);
            } else if (identifyResult.getGeometry().getType() == Geometry.Type.LINE
                    || identifyResult.getGeometry().getType() == Geometry.Type.POLYLINE) {
                lineResult.add(identifyResult);
            } else if (identifyResult.getGeometry().getType() == Geometry.Type.POLYGON) {
                polygonResult.add(identifyResult);
            }
        }
        results.clear();
        results.addAll(pointResult);
        results.addAll(lineResult);
        results.addAll(polygonResult);
        //将结果传回
        IdentifyResult[] identifyResultsArray = new IdentifyResult[results.size()];
        for (int i = 0; i < results.size(); i++) {
            identifyResultsArray[i] = results.get(i);
        }
        return identifyResultsArray;
    }

    @NonNull
    protected List<IdentifyResult> getIdentifyResults(Map<IdentifyTask, IdentifyParameters> identifyMap) {
        List<IdentifyResult> results = new ArrayList<IdentifyResult>();
        Set<Map.Entry<IdentifyTask, IdentifyParameters>> entries = identifyMap.entrySet();
        for (Map.Entry<IdentifyTask, IdentifyParameters> entry : entries) {
            try {
                IdentifyResult[] identifyResults = entry.getKey().execute(entry.getValue());
                if (identifyResults.length > 0) {
                    //  LogUtil.d("点查结果："+ identifyResults.length + "个");
                    Collections.addAll(results, identifyResults);
                    //  List<IdentifyResult> identifyResults1 = ListUtil.arrayToList(identifyResults);
                    // LogUtil.d("点查结果："+ identifyResults1.size()+ "个");
                    // results.addAll(identifyResults1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LogUtil.d("总共有点查结果：" + results.size() + "个");
        return results;
    }

    @NonNull
    protected Map<IdentifyTask, IdentifyParameters> getIdentifyParametersMap(Map<String, int[]> layers, Geometry geometry,MapView mapView, int tolerance) {
        final Map<IdentifyTask, IdentifyParameters> identifyMap = new HashMap<>();    // 存放Identify任务及其参数
        for (Map.Entry<String, int[]> entry : layers.entrySet()) {
            IdentifyParameters parameters = new IdentifyParameters();
            parameters.setGeometry(geometry);
            //Envelope extent = new Envelope();
           // Envelope extent = layerInfo.getMaxExtent();
            if (mapView != null){
                parameters.setMapExtent(mapView.getMaxExtent());
                parameters.setSpatialReference(mapView.getSpatialReference());
                parameters.setMapWidth(mapView.getWidth());
                parameters.setMapHeight(mapView.getHeight());
            }

            parameters.setDPI(98);  // TODO 是否需要动态设置
            parameters.setLayers(entry.getValue());
            parameters.setReturnGeometry(true);
            parameters.setTolerance(tolerance);//默认20
            parameters.setLayerMode(IdentifyParameters.ALL_LAYERS);
            IdentifyTask identifyTask = new IdentifyTask(entry.getKey());
            LogUtil.d("Identify Url:" + entry.getKey());
            identifyMap.put(identifyTask, parameters);
        }
        return identifyMap;
    }


    /**
     * 获取可以进行identify查询的server
     *
     * @return
     */
    protected Observable<Map<String, int[]>> getQueryableLayerMap(final Context context, final ILayersService layersService) {
        return Observable.create(new Observable.OnSubscribe<Map<String, int[]>>() {
            @Override
            public void call(Subscriber<? super Map<String, int[]>> subscriber) {
                Map<String, int[]> layerMap = new HashMap<>();
                // 遍历并拿到当前可见的查询图层--子图层URL
                for (LayerInfo info : layersService.getVisibleQueryableLayers()) {
                    Map<String, int[]> findableOrIdentifiableLayer = EsriUtil.getFindableOrIdentifiableLayer(info); //可以点查或者find的图层
                    layerMap.putAll(findableOrIdentifiableLayer);
                }
                LogUtil.d("点查", "可以进行点查的服务有：" + layerMap.size() + "个");
                subscriber.onNext(layerMap);
                subscriber.onCompleted();
            }
        });
    }

    /**
     * 获取可以进行identify查询的server
     *
     * @return
     */
    protected Observable<Map<String, int[]>> getQueryableLayerMap(final Context context, final List<LayerInfo> layerInfos) {
        return Observable.create(new Observable.OnSubscribe<Map<String, int[]>>() {
            @Override
            public void call(Subscriber<? super Map<String, int[]>> subscriber) {
                Map<String, int[]> layerMap = new HashMap<>();
                // 遍历并拿到当前可见的查询图层--子图层URL
                for (LayerInfo info : layerInfos) {
                    Map<String, int[]> findableOrIdentifiableLayer = EsriUtil.getFindableOrIdentifiableLayer(info); //可以点查或者find的图层
                    layerMap.putAll(findableOrIdentifiableLayer);
                }
                LogUtil.d("点查", "可以进行点查的服务有：" + layerMap.size() + "个");
                subscriber.onNext(layerMap);
                subscriber.onCompleted();
            }
        });
    }
}

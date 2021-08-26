package com.augurit.agmobile.mapengine.identify.service;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.augurit.agmobile.mapengine.common.utils.AttributeUtil;
import com.augurit.agmobile.mapengine.common.utils.EsriUtil;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.agmobile.mapengine.identify.service
 * @createTime 创建时间 ：17/2/6
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 17/2/6
 */

public class IdentifyService implements IIdentifyService {

    @Override
    public void selectedFeature(Activity context, final MapView mapView, List<LayerInfo> visibleQueryableLayers,
                                final Geometry geometry,
                                final int tolerance,
                                final Callback2<AMFindResult[]> callback) { //xcl 2017-02-10 修改方法参数
        getQueryableLayerMap(context, visibleQueryableLayers)
                .map(new Func1<Map<String, int[]>, Map<IdentifyTask, IdentifyParameters>>() {
                    @Override
                    public Map<IdentifyTask, IdentifyParameters> call(Map<String, int[]> layers) {
                        //封装Identify条件
                        return IdentifyService.this.getIdentifyParametersMap(layers, geometry, mapView, tolerance);
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
                        return IdentifyService.this.sortedIdentifyResult(results);
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
                .subscribeOn(Schedulers.newThread())
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
                    public void onNext(AMFindResult[] identifyResults) {
                        callback.onSuccess(identifyResults);
                    }
                });
    }

    @NonNull
    private IdentifyResult[] sortedIdentifyResult(List<IdentifyResult> results) {
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
    private List<IdentifyResult> getIdentifyResults(Map<IdentifyTask, IdentifyParameters> identifyMap) {
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
        LogUtil.d("总共有点查结果："+ results.size()+ "个");
        return results;
    }

    @NonNull
    private Map<IdentifyTask, IdentifyParameters> getIdentifyParametersMap(Map<String, int[]> layers, Geometry geometry, MapView mapView, int tolerance) {
        final Map<IdentifyTask, IdentifyParameters> identifyMap = new LinkedHashMap<>();    // 存放Identify任务及其参数
        for (Map.Entry<String, int[]> entry : layers.entrySet()) {
            IdentifyParameters parameters = new IdentifyParameters();
            parameters.setGeometry(geometry);
            Envelope extent = new Envelope();
            mapView.getExtent().queryEnvelope(extent);
            parameters.setMapExtent(extent);
            parameters.setMapWidth(mapView.getWidth());
            parameters.setMapHeight(mapView.getHeight());
             parameters.setSpatialReference(mapView.getSpatialReference());
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
    private Observable<Map<String, int[]>> getQueryableLayerMap(final Context context, final ILayersService layersService) {
        return Observable.create(new Observable.OnSubscribe<Map<String, int[]>>() {
            @Override
            public void call(Subscriber<? super Map<String, int[]>> subscriber) {
                Map<String, int[]> layerMap = new HashMap<>();
                // 遍历并拿到当前可见的查询图层--子图层URL
                for (LayerInfo info : layersService.getVisibleQueryableLayers()) {
                    Map<String, int[]> findableOrIdentifiableLayer = EsriUtil.getFindableOrIdentifiableLayer(info); //可以点查或者find的图层
                    layerMap.putAll(findableOrIdentifiableLayer);
                }
                LogUtil.d("点查","可以进行点查的服务有："+ layerMap.size()+"个");
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
    private Observable<Map<String, int[]>> getQueryableLayerMap(final Context context, final List<LayerInfo> layerInfos) {
        return Observable.create(new Observable.OnSubscribe<Map<String, int[]>>() {
            @Override
            public void call(Subscriber<? super Map<String, int[]>> subscriber) {
                Map<String, int[]> layerMap = new LinkedHashMap<>();
                // 遍历并拿到当前可见的查询图层--子图层URL
                List<LayerInfo> temp = new ArrayList<>();
                temp.addAll(layerInfos);
                //将图层顺序逆序
                Collections.reverse(temp);
                for (LayerInfo info : temp) {
                    Map<String, int[]> findableOrIdentifiableLayer = EsriUtil.getFindableOrIdentifiableLayer(info); //可以点查或者find的图层
                    if(findableOrIdentifiableLayer == null
                            || findableOrIdentifiableLayer.size()==0){
                        continue;
                    }
                    Set<String> keySet = findableOrIdentifiableLayer.keySet();
                    List<String> keyList = new ArrayList<String>(keySet);
                    String key = keyList.get(0);
                    if(layerMap.containsKey(key)){
                        if (findableOrIdentifiableLayer.get(key) == null) {
                            layerMap.put(key, null);
                        } else {
                            int[] a1 = findableOrIdentifiableLayer.get(key);
                            int[] a2 = layerMap.get(key);
                            int[] result = new int[a1.length + a2.length];

                            for (int j = 0; j < a1.length; ++j) {
                                result[j] = a1[j];
                            }

                            for (int j = 0; j < a2.length; ++j) {
                                result[a1.length + j] = a2[j];
                            }
                            layerMap.put(key, result);
                        }
                    } else {
                        layerMap.putAll(findableOrIdentifiableLayer);
                    }
                }
                LogUtil.d("点查","可以进行点查的服务有："+ layerMap.size()+"个");
                subscriber.onNext(layerMap);
                subscriber.onCompleted();
            }
        });
    }

}

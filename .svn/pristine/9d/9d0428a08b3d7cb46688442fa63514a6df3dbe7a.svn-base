package com.augurit.agmobile.mapengine.legend.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.legend.dao.CacheLegendDao;
import com.augurit.agmobile.mapengine.legend.dao.LocalLegendDao;
import com.augurit.agmobile.mapengine.legend.dao.RemoteLegendDao;
import com.augurit.agmobile.mapengine.legend.model.LayerLegend;
import com.augurit.agmobile.mapengine.legend.model.Legend;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.android.map.Layer;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.legend.service
 * @createTime 创建时间 ：17/7/19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/19
 * @modifyMemo 修改备注：
 */

public class OnlineLegendService implements ILegendService {


    protected LocalLegendDao mLocalLegendDao;
    protected RemoteLegendDao mRemoteLegendDao;
    protected CacheLegendDao mCacheLegendDao;
    protected Context mContext;

    public OnlineLegendService(Context context) {
        this.mContext = context;
        this.mLocalLegendDao = new LocalLegendDao(context);
        this.mRemoteLegendDao = new RemoteLegendDao(context);
        this.mCacheLegendDao = new CacheLegendDao();
    }


    public void setmLocalLegendDao(LocalLegendDao mLocalLegendDao) {
        this.mLocalLegendDao = mLocalLegendDao;
    }

    public void setmRemoteLegendDao(RemoteLegendDao mRemoteLegendDao) {
        this.mRemoteLegendDao = mRemoteLegendDao;
    }

    public void setmCacheLegendDao(CacheLegendDao mCacheLegendDao) {
        this.mCacheLegendDao = mCacheLegendDao;
    }

    /**
     * 获取当前地图上可见图层的图例
     *
     * @return
     */
    @Override
    public Observable<List<Legend>> getCurrentVisibleLayerLegends() {

        final Observable<List<Legend>> cache = mCacheLegendDao.getAllLegends(); //缓存

        Observable<List<Legend>> remote = mRemoteLegendDao.getAllLegends(); //网络

        final Observable<List<Legend>> local = mLocalLegendDao.getAllLegends(); //本地

        Observable<List<Legend>> allLegends = null;

        if (cache != null) {

            allLegends = cache;

        } else if (local != null) {

            allLegends = local;
        } else {

            allLegends = remote;
        }

        return allLegends
                .map(new Func1<List<Legend>, List<Legend>>() { //将全部图例缓存
                    @Override
                    public List<Legend> call(List<Legend> legends) {

                        if (cache == null) {
                            mCacheLegendDao.putAllLegends(legends);
                        }

                        if (local == null) {
                            mLocalLegendDao.saveToLocal(legends);
                        }
                        return legends;
                    }
                })
                .map(new Func1<List<Legend>, List<Legend>>() { //获取到当前地图上可见图层的图例

                    @Override
                    public List<Legend> call(List<Legend> legends) {

                        List<Legend> result = new ArrayList<Legend>();

                        Map<String, Legend> allLayerLegends = new HashMap<String, Legend>();

                        for (Legend legend : legends) {
                            allLayerLegends.put(legend.getLayerName(), legend);
                        }

                        //当前可见的图层
                        List<LayerInfo> visibleLayerInfos = LayerServiceFactory.provideLayerService(mContext).getVisibleLayerInfos();

                        for (LayerInfo layerInfo : visibleLayerInfos) {
                            if (allLayerLegends.get(layerInfo.getLayerName()) != null) { //根据图层名称找到对应的图例
                                result.add(allLayerLegends.get(layerInfo.getLayerName()));
                            }
                        }
                        return result;
                    }
                }).observeOn(Schedulers.io());
        //return getLegendsFromLocalTileLayers();
    }


    public Observable<List<Legend>> getLegendsFromLocalTileLayers() {

        return Observable.fromCallable(new Callable<List<Legend>>() {
            @Override
            public List<Legend> call() throws Exception {
                List<Legend> legends = new ArrayList<>();

                List<Layer> visibleLayers = LayerServiceFactory.provideLayerService(mContext).getVisibleLayers();
                for (Layer layer : visibleLayers) {
                    if (layer instanceof ArcGISLocalTiledLayer) {
                        List<ArcGISLayerInfo> arcGISLayerInfos = ((ArcGISLocalTiledLayer) layer).getLayers();
                        if (!ListUtil.isEmpty(arcGISLayerInfos)) {
                            for (ArcGISLayerInfo layerInfo : arcGISLayerInfos) {
                                List<com.esri.core.map.Legend> legend = layerInfo.getLegend();
                                Legend legend1 = new Legend();
                                // legend1.setLayerName(legend.get(0).getValues());
                            }
                        }
                    }
                }
                return null;
            }
        });
    }


    public Observable<List<LayerLegend>> getLegendsFromLayers() {
        List<LayerInfo> visibleLayerInfos = LayerServiceFactory.provideLayerService(mContext).getVisibleLayerInfos();
        List<Observable<List<LayerLegend>>> list = new ArrayList<>();
        for (LayerInfo info : visibleLayerInfos) {
            if(info.getType() != LayerType.TianDiTu && !info.isBaseMap()) {
                list.add(mRemoteLegendDao.getAllLegendForLayer(info.getUrl(), info.getLayerName()));
            }
        }
        return Observable.merge(list)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<LayerLegend>> getLegendsFromLayers(List<LayerInfo> visibleLayerInfos) {
       // List<LayerInfo> visibleLayerInfos = LayerServiceFactory.provideLayerService(mContext).getVisibleLayerInfos();
        if (ListUtil.isEmpty(visibleLayerInfos)){
            visibleLayerInfos = LayerServiceFactory.provideLayerService(mContext).getVisibleLayerInfos();
        }
        List<Observable<List<LayerLegend>>> list = new ArrayList<>();
        for (LayerInfo info : visibleLayerInfos) {
            if(info.getType() != LayerType.TianDiTu && !info.isBaseMap()) {
                list.add(mRemoteLegendDao.getAllLegendForLayer(info.getUrl(), info.getLayerName()));
            }
        }
        return Observable.merge(list)
                .subscribeOn(Schedulers.io());
    }


}

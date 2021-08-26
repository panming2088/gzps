package com.augurit.agmobile.mapengine.layermanage.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.esri.core.geometry.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;

/**
 * 离线状态下的图层信息获取
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layermanage.service
 * @createTime 创建时间 ：2017-04-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-11
 * @modifyMemo 修改备注：
 */

public class FakeLayerService extends LayersService {

    public FakeLayerService(Context context) {
        super(context);
    }

    @Override
    public Observable<List<LayerInfo>> getSortedLayerInfos() {
      /*  LayersService layersService = new LayersService(mContext);
        return layersService.getSortedLayerInfos()
                .map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
                    @Override
                    public List<LayerInfo> call(List<LayerInfo> layerInfos) {
                        LayerInfo layerInfo = new LayerInfo();
                        layerInfo.setUrl("sde20141205.geodatabase");
                        layerInfo.setLayerTable("sde20141205.geodatabase");
                        layerInfo.setType(LayerType.FeatureLayer);
                        layerInfo.setLayerName("项目审查图层");
                        layerInfo.setLayerId(5626);
                        layerInfo.setDefaultVisibility(true);
                        layerInfo.setDirTypeName("矢量图层");
                        layerInfo.setIfShowInLayerList(true);
                        layerInfo.setRemarkFunc(0);
                        layerInfo.setDirOrder(1);
                        layerInfo.setLayerOrder(1);
                        layerInfos.add(layerInfo);
                        return layerInfos;
                    }
                });*/
        return Observable.fromCallable(new Callable<List<LayerInfo>>() {
            @Override
            public List<LayerInfo> call() throws Exception {
                List<LayerInfo> list = new ArrayList<LayerInfo>();

                LayerInfo layerInfo = new LayerInfo();
                layerInfo.setUrl("sde20141205.geodatabase");
                layerInfo.setType(LayerType.FeatureLayer);
                layerInfo.setLayerName("项目审查图层");
                layerInfo.setLayerId(5626);
                layerInfo.setDefaultVisibility(true);
                layerInfo.setDirTypeName("矢量图层");
                layerInfo.setIfShowInLayerList(true);
                layerInfo.setRemarkFunc(0);
                layerInfo.setDirOrder(1);
                layerInfo.setLayerOrder(1);
                layerInfo.setLayerTable("审批项目布局图层");
                layerInfo.setQueryable(true);
                list.add(layerInfo);

               /* LayerInfo layerInfo3 = new LayerInfo();
                layerInfo3.setUrl("");
                layerInfo3.setType(LayerType.TileLayer);
                layerInfo3.setLayerName("沈阳底图");
                layerInfo3.setLayerId(5896);
                layerInfo3.setDefaultVisibility(true);
                layerInfo3.setDirTypeName("瓦片图层");
                layerInfo3.setIfShowInLayerList(true);
                layerInfo3.setRemarkFunc(0);
                layerInfo3.setDirOrder(1);
                layerInfo3.setLayerOrder(1);
                layerInfo3.setLocalDirName("BaseMap_xm_sghy_xzqy");
                layerInfo3.setOpacity(1);
                list.add(layerInfo3);*/

                return list;
            }
        });
    }

    @Override
    public boolean ifActiveLayer(LayerInfo amLayerInfo) {
        return true;
    }

    @Override
    public double getProjectInitialResolution() {
        return 0;
    }

    @Override
    public Point getProjectInitialCenter() {
        return new Point(466319.87179999996,2729060.3504);
    }

    @Override
    public List<LayerInfo> getVisibleQueryableLayers() {
        List<LayerInfo> list = new ArrayList<LayerInfo>();
        LayerInfo layerInfo = new LayerInfo();
        layerInfo.setUrl("http://services.arcgis.com/P3ePLMYs2RVChkJx/ArcGIS/rest/services/Wildfire/FeatureServer/1");
        layerInfo.setType(LayerType.FeatureLayer);
        layerInfo.setLayerName("测试图层");
        layerInfo.setLayerId(5626);
        list.add(layerInfo);
        return list;
    }
}

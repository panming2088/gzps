package com.augurit.agmobile.mapengine.layermanage.service;


import com.augurit.agmobile.mapengine.layermanage.model.AgcomLayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.router.LayerRouter;
import com.esri.android.map.Layer;
import com.esri.core.geometry.Point;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * 包名：com.augur.agmobile.ammap.layer1.service
 * 文件描述：
 * 创建人：Augurit20160517
 * 创建时间：2016-11-25 11:22
 * 修改人：Augurit20160517
 * 修改时间：2016-11-25 11:22
 * 修改备注：
 */
public interface ILayersService {


    /**
     * 获取原始的LayerInfo
     * @return
     */
    Observable<List<LayerInfo>>  getLayerInfo();

    /**
     * 对图层进行排序
     * @param layerInfos
     * @return
     */
    List<LayerInfo> sortLayerInfos(List<LayerInfo> layerInfos);


    /**
     * 获取排序后的LayerInfo
     */
    Observable<List<LayerInfo>> getSortedLayerInfos();

    /**
     * 是否将该图层显示在图层列表中
     * @param amLayerInfo
     * @return
     */
    boolean ifActiveLayer(LayerInfo amLayerInfo);

    Observable<List<LayerInfo>> getCacheLayers(String projectId);

    void clearCacheDatas();

    /**
     * 更新所有添加到地图上的图层
     * @param layerId
     * @param amLayerInfo
     */
    void updateAllAddedToMapViewMap(int layerId, Layer amLayerInfo);

    /**
     * 刷新可见图层列表
     * @param layerId 图层的id
     * @param amLayerInfo 要添加（或者移除）的图层
     * @param ifAdd 是否将这个图层添加到可见图层中
     */
    void addOrRemoveFromVisibleMap(int layerId, LayerInfo amLayerInfo, boolean ifAdd);


    /**
     * 更新缓存的图层列表数据
     * @param layerInfos
     */
    void refreshCacheLayers(List<LayerInfo> layerInfos);

    /**
     * 更新缓存的图层列表数据
     * @param layerId
     * @param ifShow
     */
    void refreshCacheLayers(int layerId,boolean ifShow);

    /**
     * 更新某个图层的信息
     * @param layerInfo
     */
    void refreshChildLayerInfo(LayerInfo layerInfo);

   // void onAllLayerVisible();

   // void onAllLayerInVisible();

    Layer getLayerById(int layerId);

    void changeLayerVisibility(int layerId, boolean ifShow);
    /**
     * 根据activeInBs判断是否要显示在图层列表中
     * @param allLayers 排除掉奥格瓦片后的所有图层
     * @return 要显示的图层列表
     */
    //List<LayerInfo> excluedNotActiveLayer(List<LayerInfo> allLayers);

    String getCurrentProjectId();

    String getCurrentProjectName();


    double getProjectInitialResolution();

    Point getProjectInitialCenter();

    double getProjectMaxResolution();

    double getProjectMinResolution();

    String getProgramTableName();

    List<LayerInfo> getVisibleQueryableLayers();

    /**
     * 获取可见图层信息
     * @return 可见图层信息
     */
    List<LayerInfo> getVisibleLayerInfos();

    /**
     * 获取可见图层
     * @return 可见图层
     */
    List<Layer> getVisibleLayers();

    List<LayerInfo> getLayerInfosFromLocal();

    AgcomLayerInfo getAgcomLayerInfos();

    AgcomLayerInfo getAgcomLayerInfosFromLocal();

    double getBestResolutionForReadingIfItHas();

    LayerRouter getLayerRouter();

    void deleteProjectFolder();

    LayerInfo getLayerInfoByLayerId(int layerId);

    LinkedHashMap<Integer, Layer> getAllLayer();

    Map<Integer, LayerInfo> getVisibleLayer();

    Map<String, List<LayerInfo>> getCacheLayer();

    void clearAllLayer();

    void clearVisibleLayer();

    void clearCacheLayer();
}

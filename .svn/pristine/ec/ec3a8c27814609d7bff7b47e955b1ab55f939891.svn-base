package com.augurit.agmobile.mapengine.layermanage.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.layermanage.model.AgcomLayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.Group;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.router.LayerRouter;
import com.augurit.agmobile.mapengine.layermanage.util.LayerComparator;
import com.augurit.agmobile.mapengine.layermanage.util.LayerConstant;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.agmobile.mapengine.project.router.ProjectRouter;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.android.map.Layer;
import com.esri.core.geometry.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.layer.service
 * @createTime 创建时间 ：2016-12-16
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-16
 */

public class LayersService implements ILayersService {

    protected Context mContext;
    protected LayerRouter mLayerRouter;
    protected ProjectRouter mProjectRouter;

  //  BaseInfoManager baseInfoManager = null;

    /**
     * 显示在图层列表中的全部图层,key是专题id
     */
    public static Map<String, List<LayerInfo>> sCacheLayers = new HashMap<>();

    /**
     * 可见图层集合，与sAllLayer的区别在于：sAllLayer是全部加载到地图上的图层，而sVisibleLayer是加载到地图上并且状态可见的图层；
     */
    public static Map<Integer, LayerInfo> sVisibleLayers = new LinkedHashMap<>();

    /**
     * 添加到地图上的图层，与sCacheLayers的区别在于：sCacheLayers中的图层未必全部可已加载到地图上
     */
    public static LinkedHashMap<Integer, Layer> sAllLayer = new LinkedHashMap<>();

    /**
     * 通过表名查找到对应的图层Id
     */
    public static Map<String,Integer> sFindLayerIdByTableName = new HashMap<>();

    public LayersService(Context context) {
        this.mContext = context;
        this.mLayerRouter = new LayerRouter();
        this.mProjectRouter = new ProjectRouter();
       // baseInfoManager = new BaseInfoManager();
    }

    /**
     * 获取所有图层
     * @return
     */
    @Override
    public Observable<List<LayerInfo>>  getLayerInfo(){
        return Observable.fromCallable(new Callable<List<LayerInfo>>() {
            @Override
            public List<LayerInfo> call() throws Exception {
                String userId = BaseInfoManager.getUserId(mContext);
                return mLayerRouter.
                        getLayerInfos(mContext, mProjectRouter.getCurrentProjectId(mContext, userId),
                                BaseInfoManager.getUserId(mContext));
            }
        }).subscribeOn(Schedulers.io());
    }



    /**
     * 对图层进行排序
     * @return
     */
    @Override
    public List<LayerInfo> sortLayerInfos(List<LayerInfo> layerInfos){
        return sortedLayerInfos(layerInfos); //进行排序后返回
    }



    /**
     * 获取图层并进行排序后返回
     * @return
     */
    @Override
    public Observable<List<LayerInfo>> getSortedLayerInfos() {

        return getLayerInfo()
                .map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
                    @Override
                    public List<LayerInfo> call(List<LayerInfo> layerInfos) {
                        return sortedLayerInfos(layerInfos);
                    }
                }).subscribeOn(Schedulers.io());
       /* return Observable.create(new Observable.OnSubscribe<List<LayerInfo>>() {
            @Override
            public void call(Subscriber<? super List<LayerInfo>> subscriber) {
                try {
                    String userId = BaseInfoManager.getUserName(mContext);
                    List<LayerInfo> layerInfos = mLayerRouter.
                            getLayerInfos(mContext, mProjectRouter.getCurrentProjectId(mContext, userId),
                                    BaseInfoManager.getUserName(mContext));
                    if (ValidateUtil.isListNull(layerInfos)) {
                        subscriber.onNext(layerInfos);
                        subscriber.onCompleted();
                    } else {
                        List<LayerInfo> sortedLayerInfos = sortedLayerInfos(layerInfos); //进行排序后返回

                        for (LayerInfo layerInfo: layerInfos){
                            if (!TextUtils.isEmpty(layerInfo.getLayerTable())){
                                sFindLayerIdByTableName.put(layerInfo.getLayerTable(),layerInfo.getLayerId()); //保存表名对应的图层id
                            }

                        }
                        subscriber.onNext(sortedLayerInfos);
                        subscriber.onCompleted();
                    }
                } catch (IOException e) {
                    LogUtil.d("本地没有图层文件");
                }
            }
        }).subscribeOn(Schedulers.io());*/
    }

/*******************************************************************************************************************************************************************************
 * 图层排序文字描述：
 * dirorder为目录排序，
 * layerdirorder为图层排序。
 layerdirorder按数字从小到大排。
 dirorder：
 1.按数字从小到大排；
 2.有矢量图层的目录排在前面；
 3.数字相同时，按接口返回的顺序加入列表即可。

 具体步骤：
 1.排序矢量图层：先按dirorder从小到大排，再按layerdirorder从小到大排；
 2.排序瓦片图层：方法同1；
 3.生成总排序列表：从瓦片图层列表中逐一取出瓦片图层，先判断目录在矢量图层列表中是否存在，
 如存在，则把图层加入到相应的目录下，如不存在，
 则把目录加入到矢量图层列表的最后，
 然后再把图层加入到目录下。
 ****************************************************************************************************************************************************************************/
    /**
     * 对图层进行排序
     *
     * @param layerInfos
     */
    protected List<LayerInfo> sortedLayerInfos(List<LayerInfo> layerInfos) {

        //（1）分成矢量和瓦片两种
        List<LayerInfo> tileLayers = new ArrayList<>();
        List<LayerInfo> vectorLayers = new ArrayList<>();

        for (LayerInfo layerInfo : layerInfos) {
            if (layerInfo.isBelongToTile()) { //瓦片
                tileLayers.add(layerInfo);
            } else {
                vectorLayers.add(layerInfo);
            }
        }

        //（2）对矢量图层和瓦片图层进行组排序+组内排序
        LinkedHashMap<String, List<LayerInfo>> tileGroup = groupLayers(tileLayers);
        LinkedHashMap<String, List<LayerInfo>> vectorGroup = groupLayers(vectorLayers);

        //（3）遍历瓦片组，判断矢量组中是否有相同的组
        List<String> tobeDeletedGroupInTile = new ArrayList<>();//瓦片组中要删除的组
        Set<Map.Entry<String, List<LayerInfo>>> tileEntrySet = tileGroup.entrySet();
        for (Map.Entry<String, List<LayerInfo>> entry : tileEntrySet) {
            String tileGroupName = entry.getKey();
            //遍历矢量组
            Set<Map.Entry<String, List<LayerInfo>>> vectorSet = vectorGroup.entrySet();
            for (Map.Entry<String, List<LayerInfo>> vector : vectorSet) {
                boolean ifSame = vector.getKey().equals(tileGroupName);
                if (ifSame) {
                    //vectorGroup.put(tileGroupName,entry.getValue());
                    //如果相同，那么将瓦片组的图层加入到矢量组中，注意：原有的矢量图层还在
                    vector.getValue().addAll(entry.getValue());
                    tobeDeletedGroupInTile.add(tileGroupName);
                }
                //否则继续遍历
            }
        }
        //（4）删除掉瓦片图层中的组
        if (!ValidateUtil.isListNull(tobeDeletedGroupInTile)) {
            for (String groupName : tobeDeletedGroupInTile) {
                tileGroup.remove(groupName);
            }
        }
        //（5）将瓦片组加入到矢量组的最后
        LinkedHashMap<String, List<LayerInfo>> finalLayers = new LinkedHashMap<>();
        finalLayers.putAll(vectorGroup);
        finalLayers.putAll(tileGroup);

        //（6）遍历图层
        List<LayerInfo> finalList = new ArrayList<>();
        Set<Map.Entry<String, List<LayerInfo>>> entries = finalLayers.entrySet();
        for (Map.Entry<String, List<LayerInfo>> entry : entries) {
            finalList.addAll(entry.getValue());
        }
        return finalList;
    }

    /**
     * 进行组排序+组内排序
     *
     * @param layerInfos
     * @return
     */
    private LinkedHashMap<String, List<LayerInfo>> groupLayers(List<LayerInfo> layerInfos) {
        Map<Group, List<LayerInfo>> groups = new TreeMap<Group, List<LayerInfo>>(
                new Comparator<Group>() {
                    public int compare(Group obj1, Group obj2) {
                        // 对组进行降序排序
                        return obj1.getGroupOrder() - obj2.getGroupOrder();
                    }
                });

        for (LayerInfo info : layerInfos) {
            String typeName = info.getDirTypeName();
            Group group = new Group();
            group.setGroupName(typeName);
            group.setGroupOrder(info.getDirOrder());
            //如果存在就添加，不存在就创建
            if (groups.containsKey(group)) {
                groups.get(group).add(info);
            } else {
                List<LayerInfo> infoList = new ArrayList<>();
                infoList.add(info);
                groups.put(group, infoList);
            }
        }

        Set<Map.Entry<Group, List<LayerInfo>>> entries = groups.entrySet();
        LinkedHashMap<String, List<LayerInfo>> finalResult = new LinkedHashMap<>();
        //对组中的图层进行排序
        for (Map.Entry<Group, List<LayerInfo>> entry : entries) {
            Collections.sort(entry.getValue(), new LayerComparator());
            finalResult.put(entry.getKey().getGroupName(), entry.getValue());
        }
        return finalResult;
    }

    /**
     * 图层是否可以显示在图层列表中,条件（1）ifShowInList为true；（2）是普通图层
     * @param amLayerInfo
     * @return
     */
    @Override
    public boolean ifActiveLayer(LayerInfo amLayerInfo) {

        return amLayerInfo.isIfShowInLayerList() &&
                amLayerInfo.getRemarkFunc() == LayerConstant.NORMAL_LAYER;
        //return true;
    }

    @Override
    public Observable<List<LayerInfo>> getCacheLayers(final String projectId) {

        if (sCacheLayers == null || sCacheLayers.get(projectId) == null) {
            return null;
        }

        return Observable.fromCallable(new Callable<List<LayerInfo>>() {
            @Override
            public List<LayerInfo> call() throws Exception {
                return sCacheLayers.get(projectId);
            }
        });
    }

    @Override
    public void clearCacheDatas() {

        sAllLayer.clear();
        sVisibleLayers.clear();
    }

    @Override
    public void updateAllAddedToMapViewMap(int layerId, Layer amLayerInfo) {
        if (sAllLayer == null) {
            sAllLayer = new LinkedHashMap<>();
        }
        sAllLayer.put(layerId, amLayerInfo);
    }

    @Override
    public void addOrRemoveFromVisibleMap(int layerId, LayerInfo amLayerInfo, boolean ifAdd) {
        if (ifAdd) {
            sVisibleLayers.put(layerId, amLayerInfo);
        } else {
            sVisibleLayers.remove(layerId);
        }
        //sVisibleLayers.put(layerId,amLayerInfo);
    }

    @Override
    public String getCurrentProjectId() {
        return ProjectDataManager.getInstance().getCurrentProjectId(mContext);
    }

    @Override
    public String getCurrentProjectName() {
        return ProjectDataManager.getInstance().getCurrentProjectName(mContext);
    }

    @Override
    public double getProjectInitialResolution() {
        double initResolution = ProjectDataManager.getInstance().getInitialResolution(mContext);
        return initResolution;
    }

    @Override
    public Point getProjectInitialCenter() {
        Point point = ProjectDataManager.getInstance().getProjectCenter(mContext);
        return point;
    }

    @Override
    public double getProjectMaxResolution() {
        ProjectInfo currentProject = ProjectDataManager.getInstance().getCurrentProject(mContext);
        double[] resolution = currentProject.getProjectMapParam().getResolution();
        if (resolution != null && resolution.length >= 1){
            return resolution[0];
        }
        return 0;


    }

    @Override
    public double getProjectMinResolution() {
        ProjectInfo currentProject = ProjectDataManager.getInstance().getCurrentProject(mContext);
        double[] resolution = currentProject.getProjectMapParam().getResolution();
        if (resolution != null && resolution.length >= 1){
            return resolution[resolution.length - 1];
        }
        return 0;
    }

    @Override
    public String getProgramTableName() {
       /* Geodatabase geodatabase = GeodatabaseManager.getInstance(mContext).getGeodatabase();
        if (!ValidateUtil.isListNull(geodatabase.getGeodatabaseTables())){
            return geodatabase.getGeodatabaseTables().get(0).getTableName();
        }*/
        return "";
    }

    /**
     * 获取当前界面上可见的可查询的图层
     * @return
     */
    @Override
    public List<LayerInfo> getVisibleQueryableLayers() {
        List<LayerInfo> queryableLayers = new ArrayList<>();
        Set<Map.Entry<Integer, LayerInfo>> entries = sVisibleLayers.entrySet();
        for (Map.Entry<Integer, LayerInfo> entry : entries) {
            if (entry.getValue().isQueryable()) {
                queryableLayers.add(entry.getValue());
            }
        }
        return queryableLayers;
    }

    /**
     * 获取可见图层信息
     * @return
     */
    @Override
    public List<LayerInfo> getVisibleLayerInfos() {
        List<LayerInfo> visibleLayers = new ArrayList<>();
        Set<Map.Entry<Integer, LayerInfo>> entries = sVisibleLayers.entrySet();
        for (Map.Entry<Integer, LayerInfo> entry : entries) {
            visibleLayers.add(entry.getValue());
        }
        return visibleLayers;
    }

    /**
     * 获取可见图层
     * @return
     */
    @Override
    public List<Layer> getVisibleLayers() {

        List<Layer> visibleLayers = new ArrayList<>();
        Set<Map.Entry<Integer, LayerInfo>> entries = sVisibleLayers.entrySet();
        for (Map.Entry<Integer, LayerInfo> entry : entries) {
            Layer layer = sAllLayer.get(entry.getKey());
            visibleLayers.add(layer);
        }
        return visibleLayers;
    }

    @Override
    public List<LayerInfo> getLayerInfosFromLocal() {
        return mLayerRouter.getLayerInfoFromLocalFile(mContext, getCurrentProjectId());
    }

    @Override
    public AgcomLayerInfo getAgcomLayerInfos() {
        return mLayerRouter.getAgcomLayerInfo(mContext, getCurrentProjectId(), BaseInfoManager.getUserId(mContext));
    }

    @Override
    public AgcomLayerInfo getAgcomLayerInfosFromLocal() {
        return mLayerRouter.getAgcomLayerInfoFromLocal(mContext, getCurrentProjectId(), BaseInfoManager.getUserId(mContext));
    }

    @Override
    public double getBestResolutionForReadingIfItHas() {
        List<LayerInfo> localFile = mLayerRouter.getLayerInfoFromLocalFile(mContext, BaseInfoManager.getUserId(mContext));
        return 0;
    }

    @Override
    public LayerRouter getLayerRouter() {
        return mLayerRouter;
    }

    @Override
    public void deleteProjectFolder() {
        mProjectRouter.deleteAllProjects(mContext,BaseInfoManager.getUserId(mContext));
    }

    @Override
    public LayerInfo getLayerInfoByLayerId(int layerId) {
        List<LayerInfo> layerInfos = sCacheLayers.get(getCurrentProjectId());
        for (LayerInfo layerInfo : layerInfos){
            if (layerInfo.getLayerId() == layerId){
                return layerInfo;
            }
        }
        return null;
    }

    @Override
    public LinkedHashMap<Integer, Layer> getAllLayer() {
        return sAllLayer;
    }

    @Override
    public Map<Integer, LayerInfo> getVisibleLayer() {
        return sVisibleLayers;
    }

    @Override
    public Map<String, List<LayerInfo>> getCacheLayer() {
        return sCacheLayers;
    }

    @Override
    public void clearAllLayer() {
        sAllLayer.clear();
    }

    @Override
    public void clearVisibleLayer() {
        sVisibleLayers.clear();
    }

    @Override
    public void clearCacheLayer() {
        sCacheLayers.clear();
    }

    @Override
    public void refreshCacheLayers(List<LayerInfo> layerInfos) {
        String currentProjectId = getCurrentProjectId();
        sCacheLayers.put(currentProjectId, layerInfos);
    }

    @Override
    public void refreshCacheLayers(final int layerId, final boolean ifShow) {
        rx.Observable.create(new rx.Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                //更新全部的
                String currentProjectId = ProjectDataManager.getInstance().getCurrentProjectId(mContext);
                // List<LayerInfo> temp = new ArrayList<>();
                List<LayerInfo> layerInfos = sCacheLayers.get(currentProjectId);
                if (layerInfos == null) {
                    return;
                }
                synchronized (LayersService.class) {
                    for (LayerInfo layerInfo : layerInfos) {
                        if (layerInfo.getLayerId() == layerId) {
                            layerInfo.setDefaultVisibility(ifShow);
                        }
                       /* LayerInfo tempInfo = layerInfo;
                        if (tempInfo.getLayerId() == layerId){
                            tempInfo.setDefaultVisibility(ifShow);
                        }
                        temp.add(tempInfo);*/
                    }
                    // layerInfos.clear();
                    // layerInfos.addAll(temp);
                    sCacheLayers.put(currentProjectId, layerInfos);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void refreshChildLayerInfo(final LayerInfo layerInfo) {
        rx.Observable.create(new rx.Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                //更新全部的
                String currentProjectId = ProjectDataManager.getInstance().getCurrentProjectId(mContext);
                // List<LayerInfo> temp = new ArrayList<>();
                List<LayerInfo> cacheLayers = sCacheLayers.get(currentProjectId);
                if (cacheLayers == null) {
                    return;
                }
                synchronized (LayersService.class) {
                    for (LayerInfo cacheLayer : cacheLayers) {
                        if (cacheLayer.getLayerId() == layerInfo.getLayerId()) {
                            cacheLayer.setChildLayer(layerInfo.getChildLayer());
                        }
                       /* LayerInfo tempInfo = cacheLayer;
                        if (tempInfo.getLayerId() == layerId){
                            tempInfo.setDefaultVisibility(ifShow);
                        }
                        temp.add(tempInfo);*/
                    }
                    // cacheLayers.clear();
                    // cacheLayers.addAll(temp);
                    sCacheLayers.put(currentProjectId, cacheLayers);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public Layer getLayerById(int layerId) {
        return sAllLayer.get(layerId);
    }

    @Override
    public void changeLayerVisibility(int layerId, boolean ifShow) {
        sAllLayer.get(layerId).setVisible(ifShow);
    }


    public static void clearInstance() {
        sAllLayer.clear();
        sVisibleLayers.clear();
        sCacheLayers.clear();
        sFindLayerIdByTableName.clear();
    }

    public int getLayerIdByTableName(String tableName){
        return sFindLayerIdByTableName.get(tableName);
    }

}

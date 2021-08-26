package com.augurit.agmobile.patrolcore.layer.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.layermanage.dao.LocalLayerStorageDao;
import com.augurit.agmobile.mapengine.layermanage.model.AgcomLayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.router.LayerRouter;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.agmobile.mapengine.layermanage.util.LayerConstant;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.agmobile.mapengine.project.router.ProjectRouter;
import com.augurit.agmobile.patrolcore.layer.dao.RemoteLayerDao;
import com.augurit.agmobile.patrolcore.layer.model.LayerList;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.esri.android.map.Layer;
import com.esri.core.geometry.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 * 当前类与其他Service的区别在于：{@link #sAllLayer},{@link #sCacheLayers},{@link #sVisibleLayers}不再是静态变量，而是跟具体对象绑定在一起；
 * 这样做的原因是：如果采用静态变量，当一个界面的地图进行专题切换时，会导致其他界面的地图也发生变化。这样做在巡查项目中是不好的。
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.layer.service
 * @createTime 创建时间 ：17/8/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/1
 * @modifyMemo 修改备注：
 */

public class AgwebPatrolLayerService2 implements ILayersService,IPatrolLayerService{

    protected Context mContext;
    protected RemoteLayerDao mRemoteLayerDao;
    protected LocalLayerStorageDao mLocalLayerDao;
    protected LayerRouter mLayerRouter;
    protected ProjectRouter mProjectRouter;


    /**
     * 显示在图层列表中的全部图层,key是专题id
     */
    public  Map<String, List<LayerInfo>> sCacheLayers = new HashMap<>();

    /**
     * 可见图层集合，与sAllLayer的区别在于：sAllLayer是全部加载到地图上的图层，而sVisibleLayer是加载到地图上并且状态可见的图层；
     */
    public  Map<Integer, LayerInfo> sVisibleLayers = new LinkedHashMap<>();

    /**
     * 添加到地图上的图层，与sCacheLayers的区别在于：sCacheLayers中的图层未必全部可已加载到地图上
     */
    public  LinkedHashMap<Integer, Layer> sAllLayer = new LinkedHashMap<>();

    /**
     * 通过表名查找到对应的图层Id
     */
    public  Map<String,Integer> sFindLayerIdByTableName = new HashMap<>();

    public AgwebPatrolLayerService2(Context context) {
        this.mContext = context;
        this.mLayerRouter = new LayerRouter();
        this.mProjectRouter = new ProjectRouter();
        mRemoteLayerDao = new RemoteLayerDao(context);
        mLocalLayerDao = new LocalLayerStorageDao();
    }


    @Override
    public Observable<LayerList> getLayerList() {
        return mRemoteLayerDao.getLayerList(BaseInfoManager.getUserId(mContext));
    }


    @Override
    public Observable<List<LayerInfo>> getLayerInfo() {

        Observable<List<ProjectInfo>> projects = ProjectDataManager.getInstance().getIGetProjectInfoService().getProjects(mContext);

        return projects.map(new Func1<List<ProjectInfo>, ProjectInfo>() {
            @Override
            public ProjectInfo call(List<ProjectInfo> projectInfos) {
                ProjectDataManager.getInstance().setCurrentProjectById(mContext,projectInfos.get(0).getProjectId());
                return projectInfos.get(0);
            }
        }).map(new Func1<ProjectInfo, List<LayerInfo>>() {
            @Override
            public List<LayerInfo> call(ProjectInfo projectInfo) {
                return mLocalLayerDao.getLayerInfos(mContext,projectInfo.getProjectId(),BaseInfoManager.getUserId(mContext));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * agweb已经排序好了，不需要移动端排序
     * @param layerInfos
     * @return
     */
    @Override
    public List<LayerInfo> sortLayerInfos(List<LayerInfo> layerInfos) {
        return layerInfos;
    }


    @Override
    public Observable<List<LayerInfo>> getSortedLayerInfos() {

        Observable<List<ProjectInfo>> projects = ProjectDataManager.getInstance().getIGetProjectInfoService().getProjects(mContext);

        return projects.map(new Func1<List<ProjectInfo>, ProjectInfo>() {
            @Override
            public ProjectInfo call(List<ProjectInfo> projectInfos) {
                ProjectDataManager.getInstance().setCurrentProjectById(mContext,projectInfos.get(0).getProjectId());
                return projectInfos.get(0);
            }
        }).map(new Func1<ProjectInfo, List<LayerInfo>>() {
            @Override
            public List<LayerInfo> call(ProjectInfo projectInfo) {
                return mLocalLayerDao.getLayerInfos(mContext,projectInfo.getProjectId(),BaseInfoManager.getUserId(mContext));
            }
        }).subscribeOn(Schedulers.io());
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
        sCacheLayers.clear();
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
                    }
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
                List<LayerInfo> cacheLayers = sCacheLayers.get(currentProjectId);
                if (cacheLayers == null) {
                    return;
                }
                synchronized (LayersService.class) {
                    for (LayerInfo cacheLayer : cacheLayers) {
                        if (cacheLayer.getLayerId() == layerInfo.getLayerId()) {
                            cacheLayer.setChildLayer(layerInfo.getChildLayer());
                        }
                    }
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
}


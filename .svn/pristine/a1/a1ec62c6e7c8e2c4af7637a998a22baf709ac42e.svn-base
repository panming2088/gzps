package com.augurit.agmobile.patrolcore.layer.service;

import android.content.Context;

import com.augurit.agmobile.mapengine.layermanage.dao.LocalLayerStorageDao;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.LayersService;
import com.augurit.agmobile.mapengine.project.ProjectDataManager;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.agmobile.patrolcore.layer.dao.RemoteLayerDao;
import com.augurit.agmobile.patrolcore.layer.model.LayerList;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.esri.core.geometry.Point;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 *
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.layer.service
 * @createTime 创建时间 ：17/8/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/1
 * @modifyMemo 修改备注：
 */

public class AgwebPatrolLayerService extends LayersService implements IPatrolLayerService{

    protected RemoteLayerDao mRemoteLayerDao;
    protected LocalLayerStorageDao mLocalLayerDao;

    public static String MAP_EXTENT = ""; //地图范围

    public AgwebPatrolLayerService(Context context) {
        super(context);
        mRemoteLayerDao = new RemoteLayerDao(context);
        mLocalLayerDao = new LocalLayerStorageDao();
    }


    @Override
    public Observable<LayerList> getLayerList() {
        return mRemoteLayerDao.getLayerList(BaseInfoManager.getUserId(mContext));
    }

    /**
     * agweb已经排序好了，不需要移动端排序
     * @param layerInfos
     * @return
     */
    @Override
    protected List<LayerInfo> sortedLayerInfos(List<LayerInfo> layerInfos) {
        return layerInfos;
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

    @Override
    public double getProjectInitialResolution() {
        return 0;
    }

    @Override
    public Point getProjectInitialCenter() {
        return null;
    }

    /**
     * xcl 10.24 新增的部件图层需要不显示，所以不能一律返回true
     */
//    @Override
//    public boolean ifActiveLayer(LayerInfo amLayerInfo) {
//        return true;
//    }
}


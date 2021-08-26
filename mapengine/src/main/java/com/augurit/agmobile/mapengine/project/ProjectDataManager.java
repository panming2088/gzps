package com.augurit.agmobile.mapengine.project;

import android.content.Context;
import android.support.annotation.Keep;

import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.agmobile.mapengine.project.router.ProjectRouter;
import com.augurit.agmobile.mapengine.project.service.IProjectService;
import com.augurit.agmobile.mapengine.project.util.ProjectServiceFactory;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.core.geometry.Point;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 不带有默认UI视图的专题管理类，只提供跟数据有关的API，比如：获取当前专题等。注意：这个类是采用单例模式，当
 * 退出的时候要调用onDestory释放单例。
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.proj.mgr
 * @createTime 创建时间 ：2016-10-14 12:00
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-14 12:00
 */
@Keep
public class ProjectDataManager {

    private IProjectService mIGetProjectInfoService;

    private ProjectDataManager(){
        this.mIGetProjectInfoService = ProjectServiceFactory.provideProjectService();
    }

    /**
     * 这里采用单例模式的原因是：专题数据要在整个app运行中保持统一。因为用户会进行界面跳转，但是不可能只要用户一跳转页面，
     * 默认专题就变成第一个，这样是不合理的，也不能强制要求用户在每次界面跳转的时候将当前的专题ID传递过去。
     */
    private static ProjectDataManager sDataManager = null;

    public static ProjectDataManager getInstance(){
        if (ValidateUtil.isObjectNull(sDataManager)){
            synchronized (ProjectDataManager.class){
                if (ValidateUtil.isObjectNull(sDataManager)){
                    sDataManager = new ProjectDataManager();
                }
            }
        }
        return sDataManager;
    }

    /**
     * 更新专题信息
     * @param context 上下文
     * @param listener 更新过程监听
     */
    public void updateProjects(final Context context, final Callback2<List<ProjectInfo>> listener) {

       // listener.onLoading();
        mIGetProjectInfoService.forceUpdate(context,new Callback2<List<ProjectInfo>>() {
            @Override
            public void onSuccess(List<ProjectInfo> projectInfos) {
                mIGetProjectInfoService.setCurrentProject(context,projectInfos.get(0).getProjectId());
                //listener.onDataLoadedSuccess(projectInfos);
            }

            @Override
            public void onFail(Exception error) {
             //   listener.onDataLoadedError(error);
            }
        });

    }

    /**
     * 获取所有专题数据
     * @param context 上下文
     * @param callback 专题数据获取监听
     */
    public void getAllProjectsData(final Context context, final Callback2<List<ProjectInfo>> callback) {

        mIGetProjectInfoService.getProjects(context)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ProjectInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFail(new Exception(e));
                    }

                    @Override
                    public void onNext(List<ProjectInfo> projectInfos) {
                        callback.onSuccess(projectInfos);
                    }
                });
      //  listener.onLoading();
      /*  mIGetProjectInfoService.getProjects(context,new Callback<List<ProjectInfo>>() {
            @Override
            public void onSuccess(List<ProjectInfo> projectInfos) {


            }

            @Override
            public void onFail(Exception error) {

            }
        });*/
    }



    /**
     * 通过id获取专题数据
     * @param context
     * @param id
     * @return
     */
    public ProjectInfo getProjectDataById(Context context, String id) {

        ProjectRouter router = new ProjectRouter();
        BaseInfoManager manager = new BaseInfoManager();
        String userId = BaseInfoManager.getUserId(context);
        try {
           return router.getProjectDataById(context,userId,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取专题中心点
     * @param context
     * @return
     */
    public Point getProjectCenter(Context context){
        ProjectInfo currentProject = getCurrentProject(context);
        Point point = new Point(currentProject.getProjectMapParam().getMapCenterX(),
                currentProject.getProjectMapParam().getMapCenterY());
        return point;
    }

    /**
     * 获取初始缩放比例
     * @param context
     * @return
     */
    public double getInitialResolution(Context context){
        ProjectInfo projectInfo = getCurrentProject(context);
        int initZoomLevel = projectInfo.getProjectMapParam().getInitZoomLevel();
        //地图级别从1开始，但数组从0开始
        if(initZoomLevel>0){
            initZoomLevel--;
        }
        if (projectInfo.getProjectMapParam() != null && projectInfo.getProjectMapParam().getResolution() != null
                && projectInfo.getProjectMapParam().getResolution().length > initZoomLevel){
            return projectInfo.getProjectMapParam().getResolution()[initZoomLevel];
        }
        return 0;
    }


    public void setCurrentProjectById(Context context,String projectId) {

        mIGetProjectInfoService.setCurrentProject(context,projectId);

    }
    public void setCurrentProjectName(Context context,String projectId) {

        mIGetProjectInfoService.setCurrentProject(context,projectId);
    }


    public String getCurrentProjectId(Context context) {

        return mIGetProjectInfoService.getCurrentProjectId(context);
    }

    public ProjectInfo getCurrentProject(Context context){
        String currentProjectId = getCurrentProjectId(context);
        return getProjectDataById(context,currentProjectId);
    }

    /**
     * 获取当前专题ID
     *
     * @return
     */
    public String getCurrentProjectName(Context context) {
        ProjectInfo projectDataById = getProjectDataById(context, mIGetProjectInfoService.getCurrentProjectId(context));
        if (projectDataById == null){
            return null;
        }
        return projectDataById.getProjectName();
    }


    public void deleteAllProjects(Context context) {

        ProjectRouter router = new ProjectRouter();
        router.deleteAllProjects(context,null);
    }
    
    /**
     * 释放资源，由于这个是单例，所以记住要调用进行释放
     */
    public void clearInstance() {

        mIGetProjectInfoService.clearInstance();
        sDataManager = null;

    }

    public void setIGetProjectInfoService(IProjectService service){
        this.mIGetProjectInfoService = service;
    }


    public IProjectService getIGetProjectInfoService() {
        return mIGetProjectInfoService;
    }

}

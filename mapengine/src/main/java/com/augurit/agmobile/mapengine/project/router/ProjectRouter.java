package com.augurit.agmobile.mapengine.project.router;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.agmobile.mapengine.project.dao.CacheProjectDao;
import com.augurit.agmobile.mapengine.project.dao.LocalProjectStorageDao;
import com.augurit.agmobile.mapengine.project.dao.RemoteProjectRestDao;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.core.geometry.Point;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.proj.router
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public class ProjectRouter {

    private LocalProjectStorageDao mLocalProjectStorageDao;
    private RemoteProjectRestDao mRemoteProjectRestDao;

    public ProjectRouter(){
        mLocalProjectStorageDao = new LocalProjectStorageDao();
        mRemoteProjectRestDao = new RemoteProjectRestDao();
    }

    public void setmLocalProjectStorageDao(LocalProjectStorageDao mLocalProjectStorageDao) {
        this.mLocalProjectStorageDao = mLocalProjectStorageDao;
    }

    public void setRemoteProjectRestDao(RemoteProjectRestDao mRemoteProjectRestDao) {
        this.mRemoteProjectRestDao = mRemoteProjectRestDao;
    }

    /**
     * just for test
     */
    public ProjectRouter(LocalProjectStorageDao localProjectStorageDao,
                         RemoteProjectRestDao remoteProjectRestDao){
        mLocalProjectStorageDao = localProjectStorageDao;
        mRemoteProjectRestDao = remoteProjectRestDao;
    }

    public void saveProjectsToLocal(Context context,List<ProjectInfo> projectInfos,String userId){
        mLocalProjectStorageDao.saveProjectsToLocal(context,userId,projectInfos);
    }

    /**
     * 获取所有的专题,耗时操作，调用时请保证在子线程
     * @param context
     * @param userId
     * @return
     * @throws IOException
     */
    public List<ProjectInfo> getProjects(Context context, String userId) throws Exception {
        List<ProjectInfo> projects = mLocalProjectStorageDao.getAllProjects(context, userId);
        if (ValidateUtil.isListNull(projects)){
            List<ProjectInfo> allProjects = null;
            try {
                allProjects = mRemoteProjectRestDao.getAllProjects(context, userId);
            } catch (IOException e) {

                if (e instanceof SocketTimeoutException){
                   // ToastUtil.shortToast(context,"网络连接失败，请检查服务器地址是否可达...");
                  //  LogUtil.e("网络连接失败，请检查服务器地址是否可达...");
                    throw new SocketTimeoutException("网络连接失败，请检查服务器地址是否可达...");
                }
                throw e;
               // ToastUtil.shortToast(context,"专题更新失败");
               // LogUtil.e("专题更新失败");
            }
            mLocalProjectStorageDao.saveProjectsToLocal(context,userId,allProjects);
            return allProjects;
        }
        return projects;
    }


    /**
     * 从本地获取所有专题
     * @param context
     * @param userId
     * @return 本地获取的专题
     */
    public List<ProjectInfo> getProjectsFromLocal(Context context, String userId) {
        List<ProjectInfo> projects = mLocalProjectStorageDao.getAllProjects(context, userId);
        return projects;
    }


    public String getCurrentProjectId(Context context,String userId){
        if (CacheProjectDao.getCurrentProjectId(userId) == null){
            //如果此时为空，那么进行联网获取数据
            //String userId = new BaseInfoManager().getUserName(context);

            List<ProjectInfo> projects = mLocalProjectStorageDao.getAllProjects(context, userId);
            if (!ValidateUtil.isListNull(projects)){
                CacheProjectDao.setCurrentProjectId(userId,projects.get(0).getProjectId());
            }
        }
        return CacheProjectDao.getCurrentProjectId(userId);
    }


    public void setCurrentProjectId(String userId,Context context,String projectId){
        CacheProjectDao.setCurrentProjectId(userId,projectId);
    }


    /**
     * 通过id获取专题数据，,耗时操作，调用时请保证在子线程
     * @param context
     * @param id
     * @return
     */
    public ProjectInfo getProjectDataById(Context context,String userId, String id) throws Exception {
        List<ProjectInfo> allProjects = getProjects(context, userId);
        for (ProjectInfo projectInfo : allProjects){
            if (projectInfo.getProjectId().equals(id)){
                return projectInfo;
            }
        }
        return null;
    }

    /**
     * 获取当前专题ID，,耗时操作，调用时请保证在子线程
     * @return
     */
    public String getCurrentProjectName(Context context,String userId) throws Exception {
        ProjectInfo projectInfo = getCurrentProject(context,userId);
        return projectInfo.getProjectName();
    }

    /**
     * 获取当前专题，耗时操作，调用时请保证在子线程
     * @param context
     * @param userId
     * @return
     * @throws IOException
     */
    public ProjectInfo getCurrentProject(Context context, String userId) throws Exception {
        String currentProjectId = CacheProjectDao.getCurrentProjectId(userId);
        return getProjectDataById(context, userId, currentProjectId);
    }
    /**
     * 从本地获取当前专题
     * @param context
     * @param userId
     * @return
     * @throws IOException
     */
    public ProjectInfo getCurrentProjectFromLocal(Context context, String userId) throws IOException {
        String currentProjectId = CacheProjectDao.getCurrentProjectId(userId);
        if (!TextUtils.isEmpty(currentProjectId)) {
            return mLocalProjectStorageDao.getProjectById(context,userId,currentProjectId);
        }
       return null;
    }


    /**
     * 删除/project 文件夹
     * @param context
     * @param userId
     */
    public void deleteAllProjects(Context context,String userId) {
       // BaseSettingUtil.deleteAllFiles(context);
        mLocalProjectStorageDao.deleteAllProjects(context,userId);
    }


    public void clearInstance(){
        CacheProjectDao.clearInstance();
    }


    /**
     * 获取初始缩放比例
     * @param context
     * @return
     */
    public double getInitialResolution(Context context) throws IOException {
        String userId = BaseInfoManager.getUserId(context);
        ProjectInfo projectInfo = getCurrentProjectFromLocal(context,userId);
        int initZoomLevel = projectInfo.getProjectMapParam().getInitZoomLevel();
        //地图级别从1开始，但数组从0开始
        if(initZoomLevel>0){
            initZoomLevel--;
        }

        double[] resolution = projectInfo.getProjectMapParam().getResolution();
        if (resolution == null){
            throw new IOException("当前专题的分辨率为空！！请检查是否配置了分辨率");
        }
        double initResolution = resolution[initZoomLevel];
        return initResolution;
    }

    /**
     * 获取专题中心点
     * @param context
     * @return
     */
    public Point getProjectCenter(Context context) throws IOException {
        String userId = BaseInfoManager.getUserId(context);
        ProjectInfo currentProject = getCurrentProjectFromLocal(context,userId);
        Point point = new Point(currentProject.getProjectMapParam().getMapCenterX(),currentProject.getProjectMapParam().getMapCenterY());
        return point;
    }
}

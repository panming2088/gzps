package com.augurit.agmobile.mapengine.project.service;


import android.content.Context;


import com.augurit.agmobile.mapengine.project.dao.RemoteProjectRestDao;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;
import com.augurit.agmobile.mapengine.project.router.ProjectRouter;
import com.augurit.am.cmpt.common.Callback2;

import java.io.IOException;
import java.util.List;

import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.proj.service
 * @createTime 创建时间 ：2016-11-27
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-27
 */

public interface IProjectService {

    Observable<List<ProjectInfo>> getProjects(Context context);

    void getCurrentProjectName(Context context, Callback2<String> callback);

    String getCurrentProjectId(Context context);

    void getCurrentProject(Context context, Callback2<ProjectInfo> callback);

    void getProjectDataById(Context context, String projectId, Callback2<ProjectInfo> callback);

    void setCurrentProject(Context context, String projectId);

    void forceUpdate(Context context, Callback2<List<ProjectInfo>> callback);

    void clearInstance();

    /**
     * 获取阅读某个属性的最佳缩放比例
     * @return
     */
    double getBestResolutionForReadingIfItHas(Context context) throws IOException;

    ProjectRouter getProjectRouter();

    void setProjectRouter(ProjectRouter projectRouter);
}

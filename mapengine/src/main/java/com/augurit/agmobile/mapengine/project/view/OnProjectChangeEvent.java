package com.augurit.agmobile.mapengine.project.view;


import com.augurit.agmobile.mapengine.project.model.ProjectInfo;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.proj.newarc
 * @createTime 创建时间 ：2016-12-19
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-19
 */

public class OnProjectChangeEvent {

    private ProjectInfo mProjectInfo;

    public OnProjectChangeEvent(ProjectInfo projectInfo){
        this.mProjectInfo = projectInfo;
    }

    public ProjectInfo getProjectInfo() {
        return mProjectInfo;
    }
}

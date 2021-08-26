package com.augurit.agmobile.mapengine.project.model;


import android.support.annotation.Keep;

import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 将agcom中的实体类转成ProjectInfo
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.amlib.proj.impl
 * @createTime 创建时间 ：2016-11-01
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-01
 */
@Keep
public class AgcomProjectDataMapper implements IProjectDataMapper<List<UserProject>> {

    @Override
    public List<ProjectInfo> transformToList(List<UserProject> externalProjectInfos) {

        return changeToProject(externalProjectInfos);
    }


    public  List<ProjectInfo> changeToProject(List<UserProject> userProjects){
        List<ProjectInfo> projectList = new ArrayList<>();
        if (ListUtil.isEmpty(userProjects)){
            LogUtil.w("传入的专题数量为0");
        }else {
            ProjectInfo projectInfo= null;
            for (UserProject userProject : userProjects){
                projectInfo = new ProjectInfo();
                projectInfo.setProjectIconUrl(userProject.getProjectIcon());
                projectInfo.setProjectId(String.valueOf(userProject.getId()));
                ProjectInfo.MapParam  mapParam = new ProjectInfo.MapParam();
                mapParam.setInitZoomLevel(userProject.getParam().getZoom());
                mapParam.setReference(String.valueOf(userProject.getParam().getReference()));
                String[] center = userProject.getParam().getCenter().split(",");
                if (center.length == 2) {
                    mapParam.setMapCenterX(Double.valueOf(center[0]));
                    mapParam.setMapCenterY(Double.valueOf(center[1]));
                }
                String[] scales = userProject.getParam().getScales().split(",");
                if (scales.length > 0){
                    double[] scale = new double[scales.length];
                    for (int j=0; j< scales.length ;j++){
                        scale[j] = Double.valueOf(scales[j]);
                    }
                    mapParam.setResolution(scale);
                }
               /* String[] extents = userProjects.requestLocation(i).getParam().getExtent().split(",");
                double[] longExtents = new double[extents.length+1];
                for (int j =0; j< extents.length; j++){
                    longExtents[j] = Double.valueOf(extents[j]);
                }
                long initx = (long) ((longExtents[0] + longExtents[2]) ) / 2;
                long inity = (long) ((longExtents[1] + longExtents[3])) / 2;
                mapParam.setMapCenterX(initx);
                mapParam.setMapCenterY(inity);*/
                mapParam.setMapExtent(userProject.getParam().getExtent());
                mapParam.setDiscodeId(userProject.getParam().getDiscodeId());
                projectInfo.setProjectMapParam(mapParam);
                projectInfo.setProjectName(userProject.getName());
                projectInfo.setSortedId(userProject.getSortNo());
                projectList.add(projectInfo);
            }
        }
        return projectList;
    }
}

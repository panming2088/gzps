package com.augurit.agmobile.mapengine.project.model;

import android.text.TextUtils;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.util.AgwebLayerUtil;
import com.augurit.agmobile.mapengine.layermanage.util.LayerConstant;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.am.fw.utils.ListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将Agweb的专题图层实体类转成通用的专题实体类ProjectInfo以及图层实体类LayerInfo
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.layer.model
 * @createTime 创建时间 ：2017-07-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-07-11
 * @modifyMemo 修改备注：
 */

public class AgwebMapper {

    public AgwebMapperTransformResult transform(List<AgwebProjectLayer.AgwebProject> agwebProjects) {
        return transAGWebToAMobileLayer(agwebProjects);
    }


    protected AgwebMapperTransformResult transAGWebToAMobileLayer(List<AgwebProjectLayer.AgwebProject> agwebProjects) {

        List<ProjectInfo> projectInfos = new ArrayList<>();
        Map<String, List<LayerInfo>> layerMap = new HashMap<>();
        for (AgwebProjectLayer.AgwebProject agwebProject : agwebProjects) { //遍历专题

            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setProjectName(agwebProject.getProjectName());
            projectInfo.setProjectId(agwebProject.getId());

            ProjectInfo.MapParam mapParam = new ProjectInfo.MapParam();
            String[] split = agwebProject.getCenter().split(",");
            mapParam.setMapCenterX(Double.valueOf(split[0])); //中心点
            mapParam.setMapCenterY(Double.valueOf(split[1]));
            mapParam.setMapExtent(agwebProject.getExtent());
            mapParam.setReference(agwebProject.getReference());

            if (!TextUtils.isEmpty(agwebProject.getScale())) {
                String[] scales = agwebProject.getScale().split(","); //缩放比例
                if (scales.length > 0) {
                    double[] scale = new double[scales.length];
                    for (int j = 0; j < scales.length; j++) {
                        scale[j] = Double.valueOf(scales[j]);
                    }
                    mapParam.setResolution(scale);
                }
            }


            mapParam.setInitZoomLevel(agwebProject.getZoom()); //初始缩放级别
            projectInfo.setProjectMapParam(mapParam);

            projectInfos.add(projectInfo);

            //遍历该专题下的图层
            List<AgwebProjectLayer.AgwebProject.AgwebLayerInfo> layers = agwebProject.getLayers();
            if (!ListUtil.isEmpty(layers)) {
                List<LayerInfo> layerInfos = new ArrayList<>();
                for (AgwebProjectLayer.AgwebProject.AgwebLayerInfo agwebLayerInfo : layers) { //在这里暂时未填充子图层的信息

                    LayerInfo layerInfo = new LayerInfo();
                    layerInfo.setLayerId(agwebLayerInfo.getId());
                    layerInfo.setDefaultVisibility(LayerConstant.DEFAULT_SHOW.equals(agwebLayerInfo.getDefaultvisibility()));
                    layerInfo.setLayerName(agwebLayerInfo.getName());
                    layerInfo.setLayerTable(agwebLayerInfo.getLayerTable());
                    layerInfo.setType(AgwebLayerUtil.changeToLayerType(agwebLayerInfo.getLayerType())); //图层类型
                    layerInfo.setQueryable(LayerConstant.IF_QUERYABLE.equals(agwebLayerInfo.getIsQueryable()));
                    layerInfo.setBaseMap(LayerConstant.IS_BASE_MAP.equals(agwebLayerInfo.getIsBaseMap()));
                    layerInfo.setOpacity(1.0f);
                    //废弃
                   /* String initExtent = agwebLayerInfo.getInitExtent();  //图层初始范围
                    String[] extents = initExtent.split(",");
                    if (extents.length == 4){
                        double xmin = Double.valueOf(extents[0]);
                        double ymin = Double.valueOf(extents[1]);
                        double xmax = Double.valueOf(extents[2]);
                        double ymax = Double.valueOf(extents[3]);

                        Envelope envelope = new Envelope(xmin,ymin,xmax,ymax);
                        layerInfo.setInitialExtent(envelope);
                    }*/

                    layerInfo.setUrl(agwebLayerInfo.getUrl());
                    layerInfo.setIfShowInLayerList(LayerConstant.DEFAULT_SHOW.equals(agwebLayerInfo.getIfShowInList())); //是否在图层列表中显示
                    layerInfo.setRemarkFunc(agwebLayerInfo.getRemarkfunc()); //图层类型
                    layerInfo.setDirTypeName(agwebLayerInfo.getDirTypeName());
                    layerInfo.setLayerOrder(Integer.valueOf(agwebLayerInfo.getOrderNm())); //图层顺序

                    //图层中心点
                    if (!TextUtils.isEmpty(agwebLayerInfo.getCenter())){
                        String[] center = agwebLayerInfo.getCenter().split(",");
                        if (center.length == 2) {
                            double lon = Double.valueOf(center[0]);
                            double lat = Double.valueOf(center[1]);

                            LatLng latLng = new LatLng(lat, lon);
                            layerInfo.setCenter(latLng);
                        }
                    }

                    //图层的初始范围
                    if (!TextUtils.isEmpty(agwebLayerInfo.getInitScale())){

                        try {
                            Double scale = Double.valueOf(agwebLayerInfo.getInitScale());
                            layerInfo.setInitScale(scale);
                        } catch (Exception e) {

                        }
                    }

                    layerInfos.add(layerInfo);
                }

                layerMap.put(agwebProject.getId(), layerInfos);
            }
        }

        AgwebMapperTransformResult objects = new AgwebMapperTransformResult(projectInfos, layerMap);

        return objects;

    }

    public class AgwebMapperTransformResult {

        List<ProjectInfo> projectInfos;
        Map<String, List<LayerInfo>> layerMap;


        public AgwebMapperTransformResult(List<ProjectInfo> projectInfos, Map<String, List<LayerInfo>> layerMap) {
            this.projectInfos = projectInfos;
            this.layerMap = layerMap;
        }

        public List<ProjectInfo> getProjectInfos() {
            return projectInfos;
        }

        public void setProjectInfos(List<ProjectInfo> projectInfos) {
            this.projectInfos = projectInfos;
        }

        public Map<String, List<LayerInfo>> getLayerMap() {
            return layerMap;
        }

        public void setLayerMap(Map<String, List<LayerInfo>> layerMap) {
            this.layerMap = layerMap;
        }
    }

}

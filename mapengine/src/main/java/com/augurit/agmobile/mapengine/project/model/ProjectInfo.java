package com.augurit.agmobile.mapengine.project.model;


import android.support.annotation.Keep;

/**
 * AGMobile中的专题实体
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.proj.bean.agmobile
 * @createTime 创建时间 ：2016-10-14 13:37
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-14 13:37
 */
@Keep
public class ProjectInfo {
    /**
     * 专题的版本号
     */
    String versionId;
    /**
     * 专题ID
     */
    String projectId;
    /**
     * 专题名称
     */
    String projectName;
    /**
     * 专题所对应的地图参数
     */
    MapParam projectMapParam;
    /**
     * 专题的排序
     */
    String sortedId;
    /**
     * 获取专题图标对应的URL
     */
    String projectIconUrl;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public MapParam getProjectMapParam() {
        return projectMapParam;
    }

    public void setProjectMapParam(MapParam projectMapParam) {
        this.projectMapParam = projectMapParam;
    }

    public String getSortedId() {
        return sortedId;
    }

    public void setSortedId(String sortedId) {
        this.sortedId = sortedId;
    }

    public String getProjectIconUrl() {
        return projectIconUrl;
    }

    public void setProjectIconUrl(String projectIconUrl) {
        this.projectIconUrl = projectIconUrl;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public static class MapParam{
        /**
         * 专题的中心纬度，分开的原因是不想依赖于具体的esri类
         */
        double mapCenterX;
        /**
         * 专题的中心经度
         */
        double mapCenterY;
        /**
         * 专题的范围
         */
        String mapExtent;

        /**
         * 初始缩放比例
         */
        int initZoomLevel;
        /**
         * 所属坐标系
         */
        String reference;
        /**
         * 专题的对应的比例尺
         */
        double[] resolution;

        /**
         * 地图名称
         */
        String mapName;

        /**
         * 区域代码
         */
        private int discodeId;

        public String getMapName() {
            return mapName;
        }

        public void setMapName(String mapName) {
            this.mapName = mapName;
        }

        public double[] getResolution() {
            return resolution;
        }

        public void setResolution(double[] resolution) {
            this.resolution = resolution;
        }

        public double getMapCenterX() {
            return mapCenterX;
        }

        public void setMapCenterX(double mapCenterX) {
            this.mapCenterX = mapCenterX;
        }

        public double getMapCenterY() {
            return mapCenterY;
        }

        public void setMapCenterY(double mapCenterY) {
            this.mapCenterY = mapCenterY;
        }

        public String getMapExtent() {
            return mapExtent;
        }

        public void setMapExtent(String mapExtent) {
            this.mapExtent = mapExtent;
        }

        public int getInitZoomLevel() {
            return initZoomLevel;
        }

        public void setInitZoomLevel(int initZoomLevel) {
            this.initZoomLevel = initZoomLevel;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public int getDiscodeId() {
            return discodeId;
        }

        public void setDiscodeId(int discodeId) {
            this.discodeId = discodeId;
        }
    }
}


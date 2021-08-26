package com.augurit.agmobile.mapengine.statistics.model;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.tasks.query.OutStatistics;

/**
 * 统计参数的实体类
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.statistics.model
 * @createTime 创建时间 ：2017-02-23
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-23
 * @modifyMemo 修改备注：
 */
public class StatisticsParam {

    private String layerId;
    private String layerUrl;
    private Geometry geometry;
    private String statsField;
    private String groupField;
    private OutStatistics.Type statsType;
    private SpatialReference spatialReference;
    private String userId;
    private String projectId;

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getLayerUrl() {
        return layerUrl;
    }

    public void setLayerUrl(String layerUrl) {
        this.layerUrl = layerUrl;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getStatsField() {
        return statsField;
    }

    public void setStatsField(String statsField) {
        this.statsField = statsField;
    }

    public String getGroupField() {
        return groupField;
    }

    public void setGroupField(String groupField) {
        this.groupField = groupField;
    }

    public OutStatistics.Type getStatsType() {
        return statsType;
    }

    public void setStatsType(OutStatistics.Type statsType) {
        this.statsType = statsType;
    }

    public SpatialReference getSpatialReference() {
        return spatialReference;
    }

    public void setSpatialReference(SpatialReference spatialReference) {
        this.spatialReference = spatialReference;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}

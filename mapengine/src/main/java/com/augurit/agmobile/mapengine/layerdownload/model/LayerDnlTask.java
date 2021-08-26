package com.augurit.agmobile.mapengine.layerdownload.model;

import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.SpatialReference;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

/**
 * 描述：图层下载任务
 *
 * @author 创建人 ：liangsh
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerdownload.model
 * @createTime 创建时间 ：2016-09-26
 * @modifyBy 修改人 ：liangsh
 * @modifyTime 修改时间 ：2016-09-26
 * @modifyMemo 修改备注：
 */
public class LayerDnlTask {
    @PrimaryKey(AssignType.BY_MYSELF)
    private int id;
    private int userId;
    private int type; //0：瓦片；1：FeatureLayer
    private boolean tpk = false;  //瓦片图层下载是否为tpk包
    private int projectId;  //专题ID
    private int layerId;  //图层ID
    private String layerName;  //图层名称
    private String envelopeJson;   //下载的地图区域
    private String serviceURL;  //服务地址
    private String localPath;  //本地保存路径
    private boolean done = false;  //是否下载完成
    private double total;
    private double downed;
    private String startTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isTpk() {
        return tpk;
    }

    public void setTpk(boolean tpk) {
        this.tpk = tpk;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getLayerId() {
        return layerId;
    }

    public void setLayerId(int layerId) {
        this.layerId = layerId;
    }

    public String getEnvelopeJson() {
        return envelopeJson;
    }

    public void setEnvelopeJson(String envelopeJson) {
        this.envelopeJson = envelopeJson;
    }

    public Envelope getEnvelope() {
        try {
            Envelope envelope = new Envelope();
            JsonParser jsonParser = new JsonFactory().createJsonParser(this.envelopeJson);
            Geometry geometry = GeometryEngine.jsonToGeometry(jsonParser).getGeometry();
            geometry.queryEnvelope(envelope);
            return envelope;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setEnvelopeJson(Envelope envelope, SpatialReference spatialReference) {
        this.envelopeJson = GeometryEngine.geometryToJson(spatialReference, envelope);
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDowned() {
        return downed;
    }

    public void setDowned(double downed) {
        this.downed = downed;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}

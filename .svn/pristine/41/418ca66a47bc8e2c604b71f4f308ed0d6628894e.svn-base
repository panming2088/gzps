package com.augurit.agmobile.gzps.uploadevent.model;

import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 *  问题上报数据实体
 * @author : taoerxiang
 * @data : 2017-11-12  16:28
 * @des :
 */

public class ProblemUploadBean implements Serializable{
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private long dbid;
    @Expose
    private String id;
    @Expose
    private String JDMC;//街道名称
    @Expose
    private String SSLX;//设施类型
    //private String SSLX_NAME;//设施类型
    //private String BHLX_NAME;//病害类型
    @Expose
    private String BHLX;//病害类型
    @Expose
    private String BZ;//备注
    @Expose
    private String SZWZ;//所在位置
    @Expose
    private String JJCD;//紧急程度
    //private String JJCD_NAME;
    @Expose
    private String WTMS;//问题描述
    @Expose
    private String X;//经度
    @Expose
    private String Y;//纬度
    @Expose
    private String SBR;//上报人
    @Expose
    private String templateCode;
    @Expose
    private String layerurl;
    @Expose
    private String layer_id;
    @Expose
    private String layer_name;
    @Expose
    private String object_id;  //
    @Expose
    private String usid;      //部件标识码

    @Expose
    private String reportx;
    @Expose
    private String reporty;
    @Expose
    private String reportaddr;

    private long time;

    @Expose
    private List<Photo> photos;

    @Expose
    private String isbyself = "false";     //是否自行处理：true、false

    private String assignee;               //下一环节参与人编号

    private String assigneeName;           //下一环节参与人名字

    @Expose
    private String assigneeOrg ;         //下一环节所属于机构

    @Expose
    private String self_process_detail ; //自行处理

    @Expose
    private Long yjwcsj;                 //预计处理完成时间

    @Expose
    private String sfjb;                    //是否交办

    @Expose
    private String trackId;                    //巡检轨迹的trackId

    public String getSelf_process_detail() {
        return self_process_detail;
    }

    public void setSelf_process_detail(String self_process_detail) {
        this.self_process_detail = self_process_detail;
    }

    public String getAssigneeOrg() {
        return assigneeOrg;
    }

    public void setAssigneeOrg(String assigneeOrg) {
        this.assigneeOrg = assigneeOrg;
    }

    //    public String getSSLX_NAME() {
//        return SSLX_NAME;
//    }
//
//    public void setSSLX_NAME(String SSLX_NAME) {
//        this.SSLX_NAME = SSLX_NAME;
//    }
//
//    public String getBHLX_NAME() {
//        return BHLX_NAME;
//    }
//
//    public void setBHLX_NAME(String BHLX_NAME) {
//        this.BHLX_NAME = BHLX_NAME;
//    }
//
//    public String getJJCD_NAME() {
//        return JJCD_NAME;
//    }
//
//    public void setJJCD_NAME(String JJCD_NAME) {
//        this.JJCD_NAME = JJCD_NAME;
//    }

    public String getLayer_id() {
        return layer_id;
    }

    public void setLayer_id(String layer_id) {
        this.layer_id = layer_id;
    }

    public String getLayer_name() {
        return layer_name;
    }

    public void setLayer_name(String layer_name) {
        this.layer_name = layer_name;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getSBR() {
        return SBR;
    }

    public void setSBR(String SBR) {
        this.SBR = SBR;
    }

    public String getJDMC() {
        return JDMC;
    }

    public void setJDMC(String JDMC) {
        this.JDMC = JDMC;
    }

    public String getSSLX() {
        return SSLX;
    }

    public void setSSLX(String SSLX) {
        this.SSLX = SSLX;
    }

    public String getBHLX() {
        return BHLX;
    }

    public void setBHLX(String BHLX) {
        this.BHLX = BHLX;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getSZWZ() {
        return SZWZ;
    }

    public void setSZWZ(String SZWZ) {
        this.SZWZ = SZWZ;
    }

    public String getJJCD() {
        return JJCD;
    }

    public void setJJCD(String JJCD) {
        this.JJCD = JJCD;
    }

    public String getWTMS() {
        return WTMS;
    }

    public void setWTMS(String WTMS) {
        this.WTMS = WTMS;
    }

    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    public String getY() {
        return Y;
    }

    public void setY(String y) {
        Y = y;
    }

    public String getLayerurl() {
        return layerurl;
    }

    public void setLayerurl(String layerurl) {
        this.layerurl = layerurl;
    }

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }

    public String getReportx() {
        return reportx;
    }

    public void setReportx(String reportx) {
        this.reportx = reportx;
    }

    public String getReporty() {
        return reporty;
    }

    public void setReporty(String reporty) {
        this.reporty = reporty;
    }

    public String getReportaddr() {
        return reportaddr;
    }

    public void setReportaddr(String reportaddr) {
        this.reportaddr = reportaddr;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getIsbyself() {
        return isbyself;
    }

    public void setIsbyself(String isbyself) {
        this.isbyself = isbyself;
    }

    public long getDbid() {
        return dbid;
    }

    public void setDbid(long dbid) {
        this.dbid = dbid;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public Long getYjwcsj() {
        return yjwcsj;
    }

    public void setYjwcsj(Long yjwcsj) {
        this.yjwcsj = yjwcsj;
    }

    public String getSfjb() {
        return sfjb;
    }

    public void setSfjb(String sfjb) {
        this.sfjb = sfjb;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }
}

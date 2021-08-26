package com.augurit.agmobile.gzps.uploadfacility.model;

/**
 * com.augurit.agmobile.gzpssb.pshpublicaffair.view.condition
 * Created by sdb on 2018/4/11  20:04.
 * Desc：
 */

public class UploadFilterConditionEvent {

    private String distrct = null;
    private String bigType = null;
    private String smallType = null;

    private Long startTime;

    private Long endTime,uploadid;

    private String address, orgname;

    public UploadFilterConditionEvent(String distrct, String bigType, String smallType,
                                      Long startTime, Long endTime, String address, String orgname, Long uploadid) {
        this.distrct = distrct;
        this.bigType = bigType;
        this.smallType = smallType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        this.orgname = orgname;
        this.uploadid = uploadid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public Long getUploadid() {
        return uploadid;
    }

    public void setUploadid(Long uploadid) {
        this.uploadid = uploadid;
    }

    public String getDistrct() {
        return distrct;
    }

    public void setDistrct(String distrct) {
        this.distrct = distrct;
    }

    public String getBigType() {
        return bigType;
    }

    public void setBigType(String bigType) {
        this.bigType = bigType;
    }

    public String getSmallType() {
        return smallType;
    }

    public void setSmallType(String smallType) {
        this.smallType = smallType;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}

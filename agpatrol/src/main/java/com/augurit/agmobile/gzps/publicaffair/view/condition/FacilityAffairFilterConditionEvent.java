package com.augurit.agmobile.gzps.publicaffair.view.condition;

/**
 * 设施事务公开过滤条件
 * Created by xcl on 2017/11/17.
 */

public class FacilityAffairFilterConditionEvent {

    /**
     * 筛选的区域
     */
    private String distrct = null;
    /**
     * 设施类型：窨井，雨水口等
     */
    private String facilityType = null;
    /**
     * 上报类型：设施纠错还是设施上报
     */
    private String uploadType = null;

    private Long startTime;

    private Long endTime;

    public FacilityAffairFilterConditionEvent(String distrct, String uploadType, String facilityType,
                                              Long startTime,Long endTime) {
        this.distrct = distrct;
        this.uploadType = uploadType;
        this.facilityType = facilityType;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public String getUploadType() {
        return uploadType;
    }

    public String getQueryDistrict() {
        return distrct;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }
}

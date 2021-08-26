package com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist
 * @createTime 创建时间 ：18/1/25
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：18/1/25
 * @modifyMemo 修改备注：
 */

public class FacilityFilterCondition {

    /**
     * 设施类型：窨井，雨水口等
     */
    public String facilityType = null;
    public Long startTime;
    public Long endTime;
    public String attrFive;
    public String road;
    public String address;
    public String markId;
    public String filterListType;
    /**
     * 新增列表
     */
    public static final String NEW_ADDED_LIST = "0";
    /**
     * 校核列表
     */
    public static final String MODIFIED_LIST = "1";
    /**
     * 管线新增
     */
    public static final String NEW_PIPE_LIST = "2";
    /**
     * 管线校核
     */
    public static final String MODIFIED_PIPE_LIST = "3";

    /**
     * 存疑管井
     */
    public static final String DOUBT_WELL_LIST = "3";

    /**
     * 存疑区域
     */
    public static final String DOUBT_AREA_LIST = "4";

    /**
     * 接驳信息
     */
    public static final String HOOK_LIST = "5";
    /**
     * 接驳井监测
     */
    public static final String MONITOR_LIST = "6";
    /**
     * 关键点监测
     */
    public static final String KEY_NODE_MONITOR_LIST = "7";
    /**
     *
     * @param filterListType 过滤的列表类型 ， 0 表示过滤的是 新增列表，1 表示过滤的是校核列表
     * @param facilityType
     * @param startTime
     * @param endTime
     * @param attrFive
     * @param road
     * @param address
     * @param markId
     */
    public FacilityFilterCondition(String filterListType, String facilityType, Long startTime, Long endTime, String attrFive, String road, String address, String markId) {
        this.filterListType = filterListType;
        this.facilityType = facilityType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.attrFive = attrFive;
        this.road = road;
        this.address = address;
        this.markId = markId;
    }



}

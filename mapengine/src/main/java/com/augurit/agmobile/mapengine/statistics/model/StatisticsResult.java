package com.augurit.agmobile.mapengine.statistics.model;

/**
 * 通用的统计结果实体类
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.stats.module
 * @createTime 创建时间 ：2017-02-16
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-16
 * @modifyMemo 修改备注：
 */
public class StatisticsResult {
    private String group;
    private double value;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

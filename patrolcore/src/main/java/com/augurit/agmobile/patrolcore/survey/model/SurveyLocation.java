package com.augurit.agmobile.patrolcore.survey.model;

import com.google.gson.annotations.Expose;

/**
 * 描述：地址核查
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agsurvey.location.model
 * @createTime 创建时间 ：2017/9/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/9/7
 * @modifyMemo 修改备注：
 */

public class SurveyLocation {

    @Expose()
    private String id;

    @Expose()
    private String town;

    @Expose()
    private String village;

    @Expose()
    private String street;

    @Expose()
    private String mph;

    @Expose()
    private String grid_name;

    @Expose()
    private String x;

    @Expose()
    private String y;

    @Expose()
    private String projectId;

    @Expose(serialize = false, deserialize = false)
    private String state;   // 记录状态

    @Expose(serialize = false, deserialize = false)
    private double scale;   // 缩放等级

    @Expose(serialize = false, deserialize = false)
    private String address;     // 地址

    public SurveyLocation copy() {
        SurveyLocation surveyLocation = new SurveyLocation();
        surveyLocation.setId(id);
        surveyLocation.setTown(town);
        surveyLocation.setVillage(village);
        surveyLocation.setStreet(street);
        surveyLocation.setMph(mph);
        surveyLocation.setGrid_name(grid_name);
        surveyLocation.setX(x);
        surveyLocation.setY(y);
        surveyLocation.setState(state);
        surveyLocation.setScale(scale);
        surveyLocation.setAddress(address);
        return surveyLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getMph() {
        return mph;
    }

    public void setMph(String mph) {
        this.mph = mph;
    }

    public String getGrid_name() {
        return grid_name;
    }

    public void setGrid_name(String grid_name) {
        this.grid_name = grid_name;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

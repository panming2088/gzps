package com.augurit.agmobile.mapengine.common.model;

import android.support.annotation.Keep;

/**
 * Created by liangsh on 2016-12-24.
 */
@Keep
public class Area {
    private String centerPoint;
    private String discode;
    private DiscodeLocate discodeLocate;
    private String disname;
    private String extent;
    private int id;
    private String level;
    private String parentId;
    private String refValue;

    public String getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(String centerPoint) {
        this.centerPoint = centerPoint;
    }

    public String getDiscode() {
        return discode;
    }

    public void setDiscode(String discode) {
        this.discode = discode;
    }

    public DiscodeLocate getDiscodeLocate() {
        return discodeLocate;
    }

    public void setDiscodeLocate(DiscodeLocate discodeLocate) {
        this.discodeLocate = discodeLocate;
    }

    public String getDisname() {
        return disname;
    }

    public void setDisname(String disname) {
        this.disname = disname;
    }

    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRefValue() {
        return refValue;
    }

    public void setRefValue(String refValue) {
        this.refValue = refValue;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }
}

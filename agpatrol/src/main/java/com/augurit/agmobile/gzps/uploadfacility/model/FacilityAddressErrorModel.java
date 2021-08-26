package com.augurit.agmobile.gzps.uploadfacility.model;

/**
 * Created by xcl on 2017/11/30.
 */

public class FacilityAddressErrorModel {


    /**
     * 地址是否被修改过
     */
    private boolean hasModified;

    private String correctAddress;

    private String road;

    private double correctX;

    private double correctY;

    public boolean isHasModified() {
        return hasModified;
    }

    public void setHasModified(boolean hasModified) {
        this.hasModified = hasModified;
    }

    public String getCorrectAddress() {
        return correctAddress;
    }

    public void setCorrectAddress(String correctAddress) {
        this.correctAddress = correctAddress;
    }

    public double getCorrectX() {
        return correctX;
    }

    public void setCorrectX(double correctX) {
        this.correctX = correctX;
    }

    public double getCorrectY() {
        return correctY;
    }

    public void setCorrectY(double correctY) {
        this.correctY = correctY;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }
}

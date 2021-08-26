package com.augurit.agmobile.patrolcore.layer.model;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agcollection.layer.model
 * @createTime 创建时间 ：2017-05-25
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-05-25
 * @modifyMemo 修改备注：
 */

public class PatrolLayerInfo2 extends LayerInfo {

    /*************************巡查图层所需参数****************************************************/
    private double centerX;//图层中心x坐标

    private double centerY; //图层中心y坐标

    private int initZoomLevel; //初始缩放级别

   // private double initScale;//初始缩放比例

    private double maxScale; //最大缩放比例

    private double minScale; //最小缩放比例

    private String StringId;

    public String getStringId() {
        return StringId;
    }

    public void setStringId(String stringId) {
        StringId = stringId;
    }

    public double getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(double maxScale) {
        this.maxScale = maxScale;
    }

    public double getMinScale() {
        return minScale;
    }

    public void setMinScale(double minScale) {
        this.minScale = minScale;
    }

    private String reference;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    /*public double getInitScale() {
        return initScale;
    }*/

  /*  public void setInitScale(double initScale) {
        this.initScale = initScale;
    }*/

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public int getInitZoomLevel() {
        return initZoomLevel;
    }

    public void setInitZoomLevel(int initZoomLevel) {
        this.initZoomLevel = initZoomLevel;
    }

}

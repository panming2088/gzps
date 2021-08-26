package com.augurit.agmobile.mapengine.marker.model;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.mark.model
 * @createTime 创建时间 ：2017-01-06
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-06
 */

public class MarkStyle {
    private String markName;
    private String markMemo;
    private long   createTime;
    private int  lineColor;
    private int   inColor;
    private String pointStyle;
    private int lineWidth;

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public String getMarkMemo() {
        return markMemo;
    }

    public void setMarkMemo(String markMemo) {
        this.markMemo = markMemo;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getInColor() {
        return inColor;
    }

    public void setInColor(int inColor) {
        this.inColor = inColor;
    }

    public String getPointStyle() {
        return pointStyle;
    }

    public void setPointStyle(String pointStyle) {
        this.pointStyle = pointStyle;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }
}

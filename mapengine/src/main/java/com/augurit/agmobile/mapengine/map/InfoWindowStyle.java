package com.augurit.agmobile.mapengine.map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.map
 * @createTime 创建时间 ：2017-02-15
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-15
 * @modifyMemo 修改备注：
 */

public class InfoWindowStyle {

    private int position;

    private int backgroundColor;

    private int cornerCurve; //圆角大小

    private int maxHeightDp;

    private int maxWidthDp;

    public InfoWindowStyle build(){
        return this;
    }

    public InfoWindowStyle position(int position){
        this.position = position;
        return this;
    }

    public InfoWindowStyle backgroundColor(int backgroundColor){
        this.backgroundColor = backgroundColor;
        return this;
    }

    public InfoWindowStyle cornerCurve(int cornerCurve){
        this.cornerCurve = cornerCurve;
        return this;
    }

    public InfoWindowStyle maxHeightDp(int maxHeightDp){
        this.maxHeightDp = maxHeightDp;
        return this;
    }

    public InfoWindowStyle maxWidthDp(int maxWidthDp){
        this.maxWidthDp = maxWidthDp;
        return this;
    }

    public int getPosition() {
        return position;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getCornerCurve() {
        return cornerCurve;
    }

    public int getMaxHeightDp() {
        return maxHeightDp;
    }

    public int getMaxWidthDp() {
        return maxWidthDp;
    }
}

package com.augurit.agmobile.mapengine.map.graphic;

import android.os.Parcel;
import android.os.Parcelable;

import com.augurit.agmobile.mapengine.map.geometry.IPolygon;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;

/**
 * 面覆盖物
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.map.graphic
 * @createTime 创建时间 ：2017-02-15
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-15
 * @modifyMemo 修改备注：
 */

public class PolygonOverlay extends Overlay implements Parcelable {

    protected IPolygon position;
    protected int fillColor;
    protected int lineColor;
    protected int lineWidth;

    private PolygonOverlay(){}

    public PolygonOverlay geometry(IPolygon position){
        this.position = position;
        return this;
    }

    public PolygonOverlay fillColor(int fillColor){
        this.fillColor = fillColor;
        return this;
    }

    public PolygonOverlay lineColor(int lineColor){
        this.lineColor = lineColor;
        return this;
    }

    public PolygonOverlay lineWidth(int lineWidth){
        this.lineWidth = lineWidth;
        return this;
    }

    public PolygonOverlay getThis() {
        return this;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.position, flags);
        dest.writeInt(this.fillColor);
        dest.writeInt(this.lineColor);
    }

    protected PolygonOverlay(Parcel in) {
        this.position = in.readParcelable(LatLng.class.getClassLoader());
        this.fillColor = in.readInt();
        this.lineColor = in.readInt();
    }

    public static final Creator<PolygonOverlay> CREATOR = new Creator<PolygonOverlay>() {
        @Override
        public PolygonOverlay createFromParcel(Parcel source) {
            return new PolygonOverlay(source);
        }

        @Override
        public PolygonOverlay[] newArray(int size) {
            return new PolygonOverlay[size];
        }
    };
}

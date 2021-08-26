package com.augurit.agmobile.mapengine.marker.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 包名：com.augurit.am.map.arcgis.mark.model
 * 文件描述：
 * 创建人：xuciluan
 * 创建时间：2016-09-29 15:57
 * 修改人：xuciluan
 * 修改时间：2016-09-29 15:57
 * 修改备注：点的样式
 */

public class PointStyle implements Parcelable {

    String url;
    Boolean ifLocal;
    String name ;
    Bitmap bitmap;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIfLocal() {
        return ifLocal;
    }

    public void setIfLocal(Boolean ifLocal) {
        this.ifLocal = ifLocal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeValue(this.ifLocal);
        dest.writeString(this.name);
        dest.writeParcelable(this.bitmap, flags);
    }

    public PointStyle() {
    }

    protected PointStyle(Parcel in) {
        this.url = in.readString();
        this.ifLocal = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.name = in.readString();
        this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<PointStyle> CREATOR = new Creator<PointStyle>() {
        @Override
        public PointStyle createFromParcel(Parcel source) {
            return new PointStyle(source);
        }

        @Override
        public PointStyle[] newArray(int size) {
            return new PointStyle[size];
        }
    };
}

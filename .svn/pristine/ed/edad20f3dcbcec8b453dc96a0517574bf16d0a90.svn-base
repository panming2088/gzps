package com.augurit.agmobile.gzps.uploadfacility.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 包名：com.augurit.agmobile.gzps.uploadfacility.model
 * 类描述：
 * 创建人：luobiao
 * 创建时间：2020/3/22 16:24
 * 修改人：luobiao
 * 修改时间：2020/3/22 16:24
 * 修改备注：
 */
public class DoubtBean implements Parcelable {
    private String id;
    private String objectId;
    private int doubtType;
    private String status;
    private String markPerson;
    private String description;
    private String layerName;
    private String parentOrgName;
    private Long markTime;
    private Long updateTime;
    private String rings;//格式如下,第一个坐标和最后一个坐标要一样形成闭环 -97.06138,32.837;-97.06133,32.836;-97.06124,32.834;-97.06138,32.837

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getDoubtType() {
        return doubtType;
    }

    public void setDoubtType(int doubtType) {
        this.doubtType = doubtType;
    }

    public String getMarkPerson() {
        return markPerson;
    }

    public void setMarkPerson(String markPerson) {
        this.markPerson = markPerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getRings() {
        return rings;
    }

    public void setRings(String rings) {
        this.rings = rings;
    }

    public Long getMarkTime() {
        return markTime;
    }

    public void setMarkTime(Long markTime) {
        this.markTime = markTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.objectId);
        dest.writeInt(this.doubtType);
        dest.writeString(this.status);
        dest.writeString(this.markPerson);
        dest.writeString(this.description);
        dest.writeString(this.layerName);
        dest.writeString(this.parentOrgName);
        dest.writeValue(this.markTime);
        dest.writeValue(this.updateTime);
        dest.writeString(this.rings);
    }

    public DoubtBean() {
    }

    protected DoubtBean(Parcel in) {
        this.id = in.readString();
        this.objectId = in.readString();
        this.doubtType = in.readInt();
        this.status = in.readString();
        this.markPerson = in.readString();
        this.description = in.readString();
        this.layerName = in.readString();
        this.parentOrgName = in.readString();
        this.markTime = (Long) in.readValue(Long.class.getClassLoader());
        this.updateTime = (Long) in.readValue(Long.class.getClassLoader());
        this.rings = in.readString();
    }

    public static final Parcelable.Creator<DoubtBean> CREATOR = new Parcelable.Creator<DoubtBean>() {
        @Override
        public DoubtBean createFromParcel(Parcel source) {
            return new DoubtBean(source);
        }

        @Override
        public DoubtBean[] newArray(int size) {
            return new DoubtBean[size];
        }
    };
}

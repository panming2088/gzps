package com.augurit.agmobile.gzps.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;

import java.io.Serializable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzpssb.bean
 * @createTime 创建时间 ：2018-04-09
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-04-09
 * @modifyMemo 修改备注：
 */

public class DoorNOBean implements Serializable, Parcelable {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private transient long dbid;
    private Long id;
    private Long markId;
    private String objectId;
    private String mpObjectId;
    private String addr;
    private transient String dzdm;
    private String sGuid;//门牌id
    private String mph;
    private transient String PSDY_OID;
    private transient String PSDY_NAME;
    private transient String ISTATUE;
    private transient String isExist;
    private transient int type;//0,新增，1：替换、2：返回
    private transient String mph_id;
    private double y;
    private double x;
    private String s_guid;//门牌id
    private String stree;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMarkId() {
        return markId;
    }

    public void setMarkId(Long markId) {
        this.markId = markId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getAddr() {
        return addr;
    }

    public String getMph_id() {
        return mph_id;
    }

    public long getDbid() {
        return dbid;
    }

    public void setDbid(long dbid) {
        this.dbid = dbid;
    }

    public void setMph_id(String mph_id) {
        this.mph_id = mph_id;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getDzdm() {
        return dzdm;
    }

    public void setDzdm(String dzdm) {
        this.dzdm = dzdm;
    }

    public String getsGuid() {
        return sGuid;
    }

    public void setsGuid(String sGuid) {
        this.sGuid = sGuid;
    }

    public String getMph() {
        return mph;
    }

    public void setMph(String mph) {
        this.mph = mph;
    }

    public String getPSDY_OID() {
        return PSDY_OID;
    }

    public void setPSDY_OID(String PSDY_OID) {
        this.PSDY_OID = PSDY_OID;
    }

    public String getPSDY_NAME() {
        return PSDY_NAME;
    }

    public void setPSDY_NAME(String PSDY_NAME) {
        this.PSDY_NAME = PSDY_NAME;
    }

    public String getISTATUE() {
        return ISTATUE;
    }

    public void setISTATUE(String ISTATUE) {
        this.ISTATUE = ISTATUE;
    }

    public String getIsExist() {
        return isExist;
    }

    public void setIsExist(String isExist) {
        this.isExist = isExist;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public String getMpObjectId() {
        return mpObjectId;
    }

    public void setMpObjectId(String mpObjectId) {
        this.mpObjectId = mpObjectId;
    }

    public String getS_guid() {
        return s_guid;
    }

    public void setS_guid(String s_guid) {
        this.s_guid = s_guid;
    }

    public String getStree() {
        return stree;
    }

    public void setStree(String stree) {
        this.stree = stree;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.markId);
        dest.writeString(this.objectId);
        dest.writeString(this.mpObjectId);
        dest.writeString(this.addr);
        dest.writeString(this.sGuid);
        dest.writeString(this.mph);
        dest.writeDouble(this.y);
        dest.writeDouble(this.x);
        dest.writeString(this.s_guid);
        dest.writeString(this.stree);
    }

    public DoorNOBean() {
    }

    protected DoorNOBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.markId = (Long) in.readValue(Long.class.getClassLoader());
        this.objectId = in.readString();
        this.mpObjectId = in.readString();
        this.addr = in.readString();
        this.sGuid = in.readString();
        this.mph = in.readString();
        this.y = in.readDouble();
        this.x = in.readDouble();
        this.s_guid = in.readString();
        this.stree = in.readString();
    }

    public static final Parcelable.Creator<DoorNOBean> CREATOR = new Parcelable.Creator<DoorNOBean>() {
        @Override
        public DoorNOBean createFromParcel(Parcel source) {
            return new DoorNOBean(source);
        }

        @Override
        public DoorNOBean[] newArray(int size) {
            return new DoorNOBean[size];
        }
    };
}

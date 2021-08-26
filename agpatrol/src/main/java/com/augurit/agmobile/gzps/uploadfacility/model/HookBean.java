package com.augurit.agmobile.gzps.uploadfacility.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 包名：com.augurit.agmobile.gzps.uploadfacility.model
 * 类描述：
 * 创建人：luobiao
 * 创建时间：2020/8/31 16:28
 * 修改人：luobiao
 * 修改时间：2020/8/31 16:28
 * 修改备注：
 */
public class HookBean implements Parcelable {

    /**
     * markPerson : 技术支持2
     * sort : 雨水
     * rowno : 1
     * jbjAddr : 天河区瘦狗岭路569号华南理工大学(五山校区)内,华南理工大学五山校区-土木与交通学院西南66米附近
     * psdyAddr : 广州市天河区五山路373号之一五山科技广场A座
     * id : 207
     * jbjX : 113.33896279999999
     * psdyName : 五山科技广场A、B座
     * jbjY : 23.149842894000017
     * psdyLx : 商业企业
     * jbjObjectId : 869730
     * markTime : 1598701042000
     * psdyX : 113.34160594550002
     * psdyY : 23.151146390000036
     * usid : null
     */

    private String markPerson;
    private String sort;
    private int rowno;
    private String jbjAddr;
    private String psdyAddr;
    private int id;
    private double jbjX;
    private String psdyName;
    private double jbjY;
    private String psdyLx;
    private String jbjObjectId;
    private Long markTime;
    private double psdyX;
    private double psdyY;
    private String usid;

    public String getMarkPerson() {
        return markPerson;
    }

    public void setMarkPerson(String markPerson) {
        this.markPerson = markPerson;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getRowno() {
        return rowno;
    }

    public void setRowno(int rowno) {
        this.rowno = rowno;
    }

    public String getJbjAddr() {
        return jbjAddr;
    }

    public void setJbjAddr(String jbjAddr) {
        this.jbjAddr = jbjAddr;
    }

    public String getPsdyAddr() {
        return psdyAddr;
    }

    public void setPsdyAddr(String psdyAddr) {
        this.psdyAddr = psdyAddr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getJbjX() {
        return jbjX;
    }

    public void setJbjX(double jbjX) {
        this.jbjX = jbjX;
    }

    public String getPsdyName() {
        return psdyName;
    }

    public void setPsdyName(String psdyName) {
        this.psdyName = psdyName;
    }

    public double getJbjY() {
        return jbjY;
    }

    public void setJbjY(double jbjY) {
        this.jbjY = jbjY;
    }

    public String getPsdyLx() {
        return psdyLx;
    }

    public void setPsdyLx(String psdyLx) {
        this.psdyLx = psdyLx;
    }

    public String getJbjObjectId() {
        return jbjObjectId;
    }

    public void setJbjObjectId(String jbjObjectId) {
        this.jbjObjectId = jbjObjectId;
    }

    public Long getMarkTime() {
        return markTime;
    }

    public void setMarkTime(Long markTime) {
        this.markTime = markTime;
    }

    public double getPsdyX() {
        return psdyX;
    }

    public void setPsdyX(double psdyX) {
        this.psdyX = psdyX;
    }

    public double getPsdyY() {
        return psdyY;
    }

    public void setPsdyY(double psdyY) {
        this.psdyY = psdyY;
    }

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.markPerson);
        dest.writeString(this.sort);
        dest.writeInt(this.rowno);
        dest.writeString(this.jbjAddr);
        dest.writeString(this.psdyAddr);
        dest.writeInt(this.id);
        dest.writeDouble(this.jbjX);
        dest.writeString(this.psdyName);
        dest.writeDouble(this.jbjY);
        dest.writeString(this.psdyLx);
        dest.writeString(this.jbjObjectId);
        dest.writeValue(this.markTime);
        dest.writeDouble(this.psdyX);
        dest.writeDouble(this.psdyY);
        dest.writeString(this.usid);
    }

    public HookBean() {
    }

    protected HookBean(Parcel in) {
        this.markPerson = in.readString();
        this.sort = in.readString();
        this.rowno = in.readInt();
        this.jbjAddr = in.readString();
        this.psdyAddr = in.readString();
        this.id = in.readInt();
        this.jbjX = in.readDouble();
        this.psdyName = in.readString();
        this.jbjY = in.readDouble();
        this.psdyLx = in.readString();
        this.jbjObjectId = in.readString();
        this.markTime = (Long) in.readValue(Long.class.getClassLoader());
        this.psdyX = in.readDouble();
        this.psdyY = in.readDouble();
        this.usid = in.readString();
    }

    public static final Parcelable.Creator<HookBean> CREATOR = new Parcelable.Creator<HookBean>() {
        @Override
        public HookBean createFromParcel(Parcel source) {
            return new HookBean(source);
        }

        @Override
        public HookBean[] newArray(int size) {
            return new HookBean[size];
        }
    };
}

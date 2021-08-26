package com.augurit.agmobile.gzps.uploadfacility.model;

import android.os.Parcel;
import android.os.Parcelable;

import retrofit2.http.Query;
public class PsdyJbj implements Parcelable {
//              "id": 1,
//              "jbjX": null,
//              "jbjY": null,
//              "psdyObjectId": null,
//              "jbjObjectId": "1",
//              "psdyX": null,
//              "psdyY": null,
//              "usid": null
 private String id;
 private double jbjX;
 private double jbjY;
 private int psdyObjectId;
 private int jbjObjectId;
 private double psdyX;
 private double psdyY;
 private String usid;
 private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getJbjX() {
        return jbjX;
    }

    public void setJbjX(double jbjX) {
        this.jbjX = jbjX;
    }

    public double getJbjY() {
        return jbjY;
    }

    public void setJbjY(double jbjY) {
        this.jbjY = jbjY;
    }

    public int getPsdyObjectId() {
        return psdyObjectId;
    }

    public void setPsdyObjectId(int psdyObjectId) {
        this.psdyObjectId = psdyObjectId;
    }

    public int getJbjObjectId() {
        return jbjObjectId;
    }

    public void setJbjObjectId(int jbjObjectId) {
        this.jbjObjectId = jbjObjectId;
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
        dest.writeString(this.id);
        dest.writeDouble(this.jbjX);
        dest.writeDouble(this.jbjY);
        dest.writeInt(this.psdyObjectId);
        dest.writeInt(this.jbjObjectId);
        dest.writeDouble(this.psdyX);
        dest.writeDouble(this.psdyY);
        dest.writeString(this.usid);
    }

    public PsdyJbj() {
    }

    protected PsdyJbj(Parcel in) {
        this.id = in.readString();
        this.jbjX = in.readDouble();
        this.jbjY = in.readDouble();
        this.psdyObjectId = in.readInt();
        this.jbjObjectId = in.readInt();
        this.psdyX = in.readDouble();
        this.psdyY = in.readDouble();
        this.usid = in.readString();
    }

    public static final Parcelable.Creator<PsdyJbj> CREATOR = new Parcelable.Creator<PsdyJbj>() {
        @Override
        public PsdyJbj createFromParcel(Parcel source) {
            return new PsdyJbj(source);
        }

        @Override
        public PsdyJbj[] newArray(int size) {
            return new PsdyJbj[size];
        }
    };
}

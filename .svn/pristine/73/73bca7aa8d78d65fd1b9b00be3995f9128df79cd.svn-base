package com.augurit.agmobile.gzps.uploadfacility.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 包名：com.augurit.agmobile.gzps.uploadfacility.model
 * 类描述：
 * 创建人：luobiao
 * 创建时间：2019/10/24 16:10
 * 修改人：luobiao
 * 修改时间：2019/10/24 16:10
 * 修改备注：
 */
public class Plate implements Parcelable {
    private String sGuid;
    private String mph;
    private String addr;

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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sGuid);
        dest.writeString(this.mph);
        dest.writeString(this.addr);
    }

    public Plate() {
    }

    protected Plate(Parcel in) {
        this.sGuid = in.readString();
        this.mph = in.readString();
        this.addr = in.readString();
    }

    public static final Parcelable.Creator<Plate> CREATOR = new Parcelable.Creator<Plate>() {
        @Override
        public Plate createFromParcel(Parcel source) {
            return new Plate(source);
        }

        @Override
        public Plate[] newArray(int size) {
            return new Plate[size];
        }
    };
}

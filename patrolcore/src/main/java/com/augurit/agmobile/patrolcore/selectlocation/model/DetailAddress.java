package com.augurit.agmobile.patrolcore.selectlocation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xcl on 2017/10/26.
 */

public class DetailAddress implements Parcelable {
    /**
     * country : 中国
     * country_code : 0
     * province : 北京市
     * city : 北京市
     * district : 海淀区
     * adcode : 110108
     * street : 中关村大街
     * street_number : 27号1101-08室
     * direction : 附近
     * distance : 7
     */
    private String country;
    private int country_code;
    private String province;
    private String city;
    private String district;
    private String adcode;
    private String street;
    private String street_number;
    private String direction;
    private String distance;
    private String detailAddress;
    private double x;
    private double y;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCountry_code() {
        return country_code;
    }

    public void setCountry_code(int country_code) {
        this.country_code = country_code;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeInt(this.country_code);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.adcode);
        dest.writeString(this.street);
        dest.writeString(this.street_number);
        dest.writeString(this.direction);
        dest.writeString(this.distance);
        dest.writeString(this.detailAddress);
        dest.writeDouble(this.x);
        dest.writeDouble(this.y);
    }

    public DetailAddress() {
    }

    protected DetailAddress(Parcel in) {
        this.country = in.readString();
        this.country_code = in.readInt();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.adcode = in.readString();
        this.street = in.readString();
        this.street_number = in.readString();
        this.direction = in.readString();
        this.distance = in.readString();
        this.detailAddress = in.readString();
        this.x = in.readDouble();
        this.y = in.readDouble();
    }

    public static final Parcelable.Creator<DetailAddress> CREATOR = new Parcelable.Creator<DetailAddress>() {
        @Override
        public DetailAddress createFromParcel(Parcel source) {
            return new DetailAddress(source);
        }

        @Override
        public DetailAddress[] newArray(int size) {
            return new DetailAddress[size];
        }
    };
}

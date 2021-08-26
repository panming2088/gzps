package com.augurit.agmobile.gzps.jbjpsdy;

import com.esri.core.geometry.Point;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * 排水户实体类
 *
 * @PROJECT GZPS
 * @USER Augurit
 * @CREATE 2020/11/9 10:53
 */
public class SewerageUserEntity {

    public static final int TYPE_UNKNOW = -1;
    public static final int TYPE_CATERING_TRADE = 1;
    public static final int TYPE_PRECIPITATE = 2;
    public static final int TYPE_DANGER = 3;
    public static final int TYPE_LIFE = 4;

    public static final String TYPE_CATERING_TRADE_STR = "餐饮排污类";
    public static final String TYPE_PRECIPITATE_STR = "沉淀物排污类";
    public static final String TYPE_DANGER_STR = "有毒有害排污类";
    public static final String TYPE_LIFE_STR = "生活排污类";

    private String id = "";
    private String houseHolderName = "";
    private String belong = "";
    private String addrName = "";
    private String addr = "";
    private long time = 0;
    private int type = TYPE_UNKNOW;
    private String typeStr = "";
    private String type2Str = "";
    /**
     * 纬度
     */
    private double latitude = 0f;
    /**
     * 经度
     */
    private double longitude = 0.0;
    private String imgUrl = "";

    public SewerageUserEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getHouseHolderName() {
        return houseHolderName;
    }

    public void setHouseHolderName(String houseHolderName) {
        this.houseHolderName = houseHolderName;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getAddrName() {
        return addrName;
    }

    public void setAddrName(String addrName) {
        this.addrName = addrName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTimeStr() {
        String timePattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(timePattern, Locale.getDefault());
        return sdf.format(new Date(time));
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
        switch (typeStr) {
            case "生活排污类":
                setType(TYPE_LIFE);
                break;
            case "餐饮排污类":
                setType(TYPE_CATERING_TRADE);
                break;
            case "沉淀物排污类":
                setType(TYPE_PRECIPITATE);
                break;
            case "有毒有害排污类":
                setType(TYPE_DANGER);
                break;
            default:
                setType(TYPE_UNKNOW);
                break;
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType2Str() {
        return type2Str;
    }

    public void setType2Str(String type2Str) {
        this.type2Str = type2Str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SewerageUserEntity)) return false;
        SewerageUserEntity entity = (SewerageUserEntity) o;
        return time == entity.time &&
                type == entity.type &&
                Double.compare(entity.latitude, latitude) == 0 &&
                Double.compare(entity.longitude, longitude) == 0 &&
                id.equals(entity.id) &&
                Objects.equals(houseHolderName, entity.houseHolderName) &&
                Objects.equals(belong, entity.belong) &&
                Objects.equals(addrName, entity.addrName) &&
                Objects.equals(addr, entity.addr) &&
                Objects.equals(typeStr, entity.typeStr) &&
                Objects.equals(type2Str, entity.type2Str) &&
                Objects.equals(imgUrl, entity.imgUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, houseHolderName, belong, addrName, addr, time, type, typeStr, type2Str, latitude, longitude, imgUrl);
    }

    @Override
    public String toString() {
        return "SewerageUserEntity{" +
                "id='" + id + '\'' +
                ", houseHolderName='" + houseHolderName + '\'' +
                ", belong='" + belong + '\'' +
                ", addrName='" + addrName + '\'' +
                ", addr='" + addr + '\'' +
                ", time=" + time +
                ", type=" + type +
                ", typeStr='" + typeStr + '\'' +
                ", typeStr='" + type2Str + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}

package com.augurit.agmobile.gzps.maintain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.ListUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名：com.augurit.agmobile.gzps.maintain.model
 * 类描述：养护计划实体类
 * 创建人：luobiao
 * 创建时间：2018/10/18 16:58
 * 修改人：luobiao
 * 修改时间：2018/10/18 16:58
 * 修改备注：
 */
public class Conserve implements Parcelable {
    private Long id;
    private String addr;
    private String road;
    private String planId;
    private String loginName;
    private String userName;
    private String wtms;
    private String remark;
    private String state;
    @SerializedName("x")
    private double X;
    @SerializedName("y")
    private double Y;
    private String markPerson;
    private String parentOrgName;
    private String imgPath;
    private Long markTime;
    private List<Photo> photos;
    private List<Photo> thumbnailPhotos;
    private int order;
    public List<String> deletedPhotoIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWtms() {
        return wtms;
    }

    public void setWtms(String wtms) {
        this.wtms = wtms;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public String getMarkPerson() {
        return markPerson;
    }

    public void setMarkPerson(String markPerson) {
        this.markPerson = markPerson;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Long getMarkTime() {
        return markTime;
    }

    public void setMarkTime(Long markTime) {
        this.markTime = markTime;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Photo> getThumbnailPhotos() {
        return thumbnailPhotos;
    }

    public void setThumbnailPhotos(List<Photo> thumbnailPhotos) {
        this.thumbnailPhotos = thumbnailPhotos;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDeletedPhotoIdsStr(){

        if(ListUtil.isEmpty(deletedPhotoIds)){
            return null;
        }

        String ids = "";
        for (int i = 0; i < deletedPhotoIds.size() ; i ++){
            if (i == deletedPhotoIds.size() - 1){
                ids  += deletedPhotoIds.get(i);
            }else {
                ids  += deletedPhotoIds.get(i) + ",";
            }
        }
        return ids;

        //return Arrays.toString(deletedPhotoIds.toArray(new String[]{})).replace("[","").replace("]","");
    }

    public List<String> getDeletedPhotoIds() {
        return deletedPhotoIds;
    }

    public void setDeletedPhotoIds(List<String> deletedPhotoIds) {
        this.deletedPhotoIds = deletedPhotoIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.addr);
        dest.writeString(this.road);
        dest.writeString(this.planId);
        dest.writeString(this.loginName);
        dest.writeString(this.userName);
        dest.writeString(this.wtms);
        dest.writeString(this.remark);
        dest.writeString(this.state);
        dest.writeDouble(this.X);
        dest.writeDouble(this.Y);
        dest.writeString(this.markPerson);
        dest.writeString(this.parentOrgName);
        dest.writeString(this.imgPath);
        dest.writeValue(this.markTime);
        dest.writeList(this.photos);
        dest.writeList(this.thumbnailPhotos);
        dest.writeInt(this.order);
        dest.writeStringList(this.deletedPhotoIds);
    }

    public Conserve() {
    }

    protected Conserve(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.addr = in.readString();
        this.road = in.readString();
        this.planId = in.readString();
        this.loginName = in.readString();
        this.userName = in.readString();
        this.wtms = in.readString();
        this.remark = in.readString();
        this.state = in.readString();
        this.X = in.readDouble();
        this.Y = in.readDouble();
        this.markPerson = in.readString();
        this.parentOrgName = in.readString();
        this.imgPath = in.readString();
        this.markTime = (Long) in.readValue(Long.class.getClassLoader());
        this.photos = new ArrayList<Photo>();
        in.readList(this.photos, Photo.class.getClassLoader());
        this.thumbnailPhotos = new ArrayList<Photo>();
        in.readList(this.thumbnailPhotos, Photo.class.getClassLoader());
        this.order = in.readInt();
        this.deletedPhotoIds = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Conserve> CREATOR = new Parcelable.Creator<Conserve>() {
        @Override
        public Conserve createFromParcel(Parcel source) {
            return new Conserve(source);
        }

        @Override
        public Conserve[] newArray(int size) {
            return new Conserve[size];
        }
    };
}

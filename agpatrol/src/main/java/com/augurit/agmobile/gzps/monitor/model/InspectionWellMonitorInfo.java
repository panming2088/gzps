package com.augurit.agmobile.gzps.monitor.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 窨井监测信息实体类
 *
 * @PROJECT GZPS
 * @USER Augurit
 * @CREATE 2020/9/14 17:33
 */
public class InspectionWellMonitorInfo implements Serializable, Parcelable {

    private Long id;
    private String usid;//usid
    private String jbjObjectId;//objectid
    private String markPerson;//上报人
    private double userX;//上报人坐标
    private double userY;//上报人坐标
    private double jbjX;//坐标
    private double jbjY;//坐标
    private String ad;//氨氮浓度(mg/L)
    private String cod;//COD浓度(mg/L)
    private String jbjType;//类型雨水、污水、雨污合流
    private Long jcsj;//检测时间
    private Long markTime;//检测时间
    private Long updateTime;//更新时间
    private String deleteAttachment;
    private String loginName;
    private String imgPath;
    private String jbjAddr;
    private String description; // 备注
    private String sfgjjd = "1"; // 是否关键节点
    private List<Photo> photos;
    private List<Photo> thumbnailPhotos;
    private List<WellMonitorInfo.HasCertAttachmentBean> attachments;
    private String checkState;//审批状态

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }

    public String getJbjObjectId() {
        return jbjObjectId;
    }

    public void setJbjObjectId(String jbjObjectId) {
        this.jbjObjectId = jbjObjectId;
    }

    public String getMarkPerson() {
        return markPerson;
    }

    public void setMarkPerson(String markPerson) {
        this.markPerson = markPerson;
    }

    public double getUserX() {
        return userX;
    }

    public void setUserX(double userX) {
        this.userX = userX;
    }

    public double getUserY() {
        return userY;
    }

    public void setUserY(double userY) {
        this.userY = userY;
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

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getJbjType() {
        return jbjType;
    }

    public void setJbjType(String jbjType) {
        this.jbjType = jbjType;
    }

    public Long getJcsj() {
        return jcsj;
    }

    public void setJcsj(Long jcsj) {
        this.jcsj = jcsj;
    }

    public Long getMarkTime() {
        return markTime;
    }

    public void setMarkTime(Long markTime) {
        this.markTime = markTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getDeleteAttachment() {
        return deleteAttachment;
    }

    public void setDeleteAttachment(String deleteAttachment) {
        this.deleteAttachment = deleteAttachment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getJbjAddr() {
        return jbjAddr;
    }

    public void setJbjAddr(String jbjAddr) {
        this.jbjAddr = jbjAddr;
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

    public String getSfgjjd() {
        return sfgjjd;
    }

    public void setSfgjjd(String sfgjjd) {
        this.sfgjjd = sfgjjd;
    }

    public void setThumbnailPhotos(List<Photo> thumbnailPhotos) {
        this.thumbnailPhotos = thumbnailPhotos;
    }

    public List<WellMonitorInfo.HasCertAttachmentBean> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<WellMonitorInfo.HasCertAttachmentBean> attachments) {
        this.attachments = attachments;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InspectionWellMonitorInfo)) return false;
        InspectionWellMonitorInfo that = (InspectionWellMonitorInfo) o;
        return id.equals(that.id) &&
                usid.equals(that.usid) &&
                jbjObjectId.equals(that.jbjObjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usid, jbjObjectId);
    }

    @Override
    public String toString() {
        return "InspectionWellMonitorInfo{" +
                "id=" + id +
                ", usid='" + usid + '\'' +
                ", jbjObjectId='" + jbjObjectId + '\'' +
                ", markPerson='" + markPerson + '\'' +
                ", userX=" + userX +
                ", userY=" + userY +
                ", jbjX=" + jbjX +
                ", jbjY=" + jbjY +
                ", ad='" + ad + '\'' +
                ", cod='" + cod + '\'' +
                ", jbjType='" + jbjType + '\'' +
                ", jcsj=" + jcsj +
                ", markTime=" + markTime +
                ", updateTime=" + updateTime +
                ", deleteAttachment='" + deleteAttachment + '\'' +
                ", loginName='" + loginName + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", jbjAddr='" + jbjAddr + '\'' +
                ", description='" + description + '\'' +
                ", sfgjjd='" + sfgjjd + '\'' +
                ", photos=" + photos +
                ", thumbnailPhotos=" + thumbnailPhotos +
                ", attachments=" + attachments +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.usid);
        dest.writeString(this.jbjObjectId);
        dest.writeString(this.markPerson);
        dest.writeDouble(this.userX);
        dest.writeDouble(this.userY);
        dest.writeDouble(this.jbjX);
        dest.writeDouble(this.jbjY);
        dest.writeString(this.ad);
        dest.writeString(this.cod);
        dest.writeString(this.jbjType);
        dest.writeValue(this.jcsj);
        dest.writeValue(this.markTime);
        dest.writeValue(this.updateTime);
        dest.writeString(this.deleteAttachment);
        dest.writeString(this.loginName);
        dest.writeString(this.imgPath);
        dest.writeString(this.jbjAddr);
        dest.writeString(this.description);
        dest.writeString(this.sfgjjd);
        dest.writeList(this.photos);
        dest.writeList(this.thumbnailPhotos);
        dest.writeList(this.attachments);
        dest.writeString(this.checkState);
    }

    public InspectionWellMonitorInfo() {
    }

    protected InspectionWellMonitorInfo(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.usid = in.readString();
        this.jbjObjectId = in.readString();
        this.markPerson = in.readString();
        this.userX = in.readDouble();
        this.userY = in.readDouble();
        this.jbjX = in.readDouble();
        this.jbjY = in.readDouble();
        this.ad = in.readString();
        this.cod = in.readString();
        this.jbjType = in.readString();
        this.jcsj = (Long) in.readValue(Long.class.getClassLoader());
        this.markTime = (Long) in.readValue(Long.class.getClassLoader());
        this.updateTime = (Long) in.readValue(Long.class.getClassLoader());
        this.deleteAttachment = in.readString();
        this.loginName = in.readString();
        this.imgPath = in.readString();
        this.jbjAddr = in.readString();
        this.description = in.readString();
        this.sfgjjd = in.readString();
        this.photos = new ArrayList<Photo>();
        in.readList(this.photos, Photo.class.getClassLoader());
        this.thumbnailPhotos = new ArrayList<Photo>();
        in.readList(this.thumbnailPhotos, Photo.class.getClassLoader());
        this.attachments = new ArrayList<WellMonitorInfo.HasCertAttachmentBean>();
        in.readList(this.attachments, WellMonitorInfo.HasCertAttachmentBean.class.getClassLoader());
        this.checkState = in.readString();
    }

    public static final Parcelable.Creator<InspectionWellMonitorInfo> CREATOR = new Parcelable.Creator<InspectionWellMonitorInfo>() {
        @Override
        public InspectionWellMonitorInfo createFromParcel(Parcel source) {
            return new InspectionWellMonitorInfo(source);
        }

        @Override
        public InspectionWellMonitorInfo[] newArray(int size) {
            return new InspectionWellMonitorInfo[size];
        }
    };
}

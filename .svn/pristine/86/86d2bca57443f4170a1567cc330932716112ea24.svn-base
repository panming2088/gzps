package com.augurit.agmobile.gzps.uploadfacility.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.augurit.agmobile.gzps.journal.model.JournalAttachment;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsh on 2018/3/6.
 */

public class FeedbackInfo implements Parcelable {

    private long id;
    private String tableType;   //反馈的记录类型，0为数据新增，1为数据校核
    private long aid;             //反馈的记录ID
    private String describe;      //反馈描述信息
    private String situation;     //整改情况
    private String feedbackPerson;  //反馈人
    private String feedbackPersoncode;  //反馈人登录名
//    private long feedbackDate;           //反馈时间
    private long time;                   //反馈时间
    private String objectId;             //图层中的OBJECTID
    private List<Photo> files;
    private List<Photo> thumbPhoto;
    private List<JournalAttachment> sysList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getFeedbackPerson() {
        return feedbackPerson;
    }

    public void setFeedbackPerson(String feedbackPerson) {
        this.feedbackPerson = feedbackPerson;
    }

    public String getFeedbackPersoncode() {
        return feedbackPersoncode;
    }

    public void setFeedbackPersoncode(String feedbackPersoncode) {
        this.feedbackPersoncode = feedbackPersoncode;
    }

   /* public long getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(long feedbackDate) {
        this.feedbackDate = feedbackDate;
    }*/

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List<Photo> getFiles() {
        return files;
    }

    public void setFiles(List<Photo> files) {
        this.files = files;
    }

    public List<Photo> getThumbPhoto() {
        return thumbPhoto;
    }

    public void setThumbPhoto(List<Photo> thumbPhoto) {
        this.thumbPhoto = thumbPhoto;
    }

    public List<JournalAttachment> getSysList() {
        return sysList;
    }

    public void setSysList(List<JournalAttachment> sysList) {
        this.sysList = sysList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.tableType);
        dest.writeLong(this.aid);
        dest.writeString(this.describe);
        dest.writeString(this.situation);
        dest.writeString(this.feedbackPerson);
        dest.writeString(this.feedbackPersoncode);
        dest.writeLong(this.time);
        dest.writeString(this.objectId);
        dest.writeList(this.files);
        dest.writeList(this.thumbPhoto);
        dest.writeList(this.sysList);
    }

    public FeedbackInfo() {
    }

    protected FeedbackInfo(Parcel in) {
        this.id = in.readLong();
        this.tableType = in.readString();
        this.aid = in.readLong();
        this.describe = in.readString();
        this.situation = in.readString();
        this.feedbackPerson = in.readString();
        this.feedbackPersoncode = in.readString();
        this.time = in.readLong();
        this.objectId = in.readString();
        this.files = new ArrayList<Photo>();
        in.readList(this.files, Photo.class.getClassLoader());
        this.thumbPhoto = new ArrayList<Photo>();
        in.readList(this.thumbPhoto, Photo.class.getClassLoader());
        this.sysList = new ArrayList<JournalAttachment>();
        in.readList(this.sysList, JournalAttachment.class.getClassLoader());
    }

    public static final Parcelable.Creator<FeedbackInfo> CREATOR = new Parcelable.Creator<FeedbackInfo>() {
        @Override
        public FeedbackInfo createFromParcel(Parcel source) {
            return new FeedbackInfo(source);
        }

        @Override
        public FeedbackInfo[] newArray(int size) {
            return new FeedbackInfo[size];
        }
    };
}

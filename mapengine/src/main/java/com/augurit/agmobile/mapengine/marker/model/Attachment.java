package com.augurit.agmobile.mapengine.marker.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.mark.model
 * @createTime 创建时间 ：2017-01-03
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-03
 */

public class Attachment implements Parcelable {

    private int attachmentId;
    private String filePath;
    private String createDate;
    private String fileSize;
    private String fileName;
    private int fileType;

    /**
     * 文件类型
     */
    public static final int TEXT = 1;
    public static final int GIF = 2;
    public static final int JPG = 3;
    /**
     * 通过从AttachmentInfo获取到的contentType进行判断文件类型
     * @param contentType AttachmentInfo获取到的contentType
     * @return
     */
    public static int getFileType(String contentType){
        switch (contentType){
            case "text/plain":
               return TEXT;
            case "image/gif":
                return GIF;
            case "image/jpeg":
                return JPG;
            case "image/png":
                return JPG;
        }
        return TEXT; //默认返回文件
    }


    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.attachmentId);
        dest.writeString(this.filePath);
        dest.writeString(this.createDate);
        dest.writeString(this.fileSize);
        dest.writeString(this.fileName);
    }

    public Attachment() {
    }

    protected Attachment(Parcel in) {
        this.attachmentId = in.readInt();
        this.filePath = in.readString();
        this.createDate = in.readString();
        this.fileSize = in.readString();
        this.fileName = in.readString();
    }

    public static final Creator<Attachment> CREATOR = new Creator<Attachment>() {
        @Override
        public Attachment createFromParcel(Parcel source) {
            return new Attachment(source);
        }

        @Override
        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };
}

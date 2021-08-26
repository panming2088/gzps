package com.augurit.agmobile.gzps.common.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.augurit.am.cmpt.widget.filepicker.utils.FileUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 文件实体
 *
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.common.lib.model
 * @createTime 创建时间 ：2018-06-05
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2018-06-05
 * @modifyMemo 修改备注：
 */
public class FileBean implements Parcelable, Serializable {

    public static final String THUMB_SUFFIX = "_thumbnail";

    private int id;
    @SerializedName(value = "name", alternate = {"photoName","fileName"})
    private String name;     //文件名
    private String thumbName;   // 缩略图文件名
    @SerializedName(value = "path", alternate = {"att_path", "photoPath" ,"filePath"})
    private String path;     //文件路径
    @SerializedName(value = "thumbPath", alternate = {"thum_path"})
    private String thumbPath;      //图片或视频的缩略图
    private long size;      //文件大小，单位为byte
    private long timeInterval;   //视频或音频的时长，单位为毫秒
    @SerializedName(value = "mimeType", alternate = {"mime"})
    private String mimeType;   //图片的类型
    @SerializedName(value = "createTime", alternate = {"time", "att_time", "photoTime"})
    private long createTime;      //图片的创建时间
    @SerializedName(value = "uploaded", alternate = {"hasBeenUp"})
    private boolean uploaded;     //是否已上传到服务器
    private String uploadDate;    //专门用于记录String类型的时间
    private int icon;  //图标
    private boolean isOriginal;  //是否是原图
    private String problem_id; //上报内容ID
    private String sewerageUserId;
    private String preTag;
    public String getPreTag() {
        return preTag;
    }
    public void setPreTag(String preTag) {
        this.preTag = preTag;
    }
    public String getSewerageUserId() {
        return sewerageUserId;
    }

    public void setSewerageUserId(String sewerageUserId) {
        this.sewerageUserId = sewerageUserId;
    }

    public String getNameForUpload() {
        return getNameForUpload("_img");
    }

    public String getNameForUpload(String uploadSuffix) {
        String fileName = FileUtils.getFilenameWithoutSubffix(path);
        fileName = fileName.replace(THUMB_SUFFIX, "");
        fileName += uploadSuffix + "." + FileUtils.getFileSuffix(path);
        return fileName;
    }

    public String getThumbNameForUpload() {
        return getThumbNameForUpload("_thumbnail_img");
    }

    public String getThumbNameForUpload(String uploadThumbSuffix) {
        String fileName = FileUtils.getFilenameWithoutSubffix(thumbPath);
        fileName = fileName.replace(THUMB_SUFFIX, "");
        fileName += uploadThumbSuffix + "." + FileUtils.getFileSuffix(thumbPath);
        return fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbName() {
        return thumbName;
    }

    public void setThumbName(String thumbName) {
        this.thumbName = thumbName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getProblem_id() {
        return problem_id;
    }

    public void setProblem_id(String problem_id) {
        this.problem_id = problem_id;
    }
    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }


    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeString(this.thumbPath);
        dest.writeLong(this.size);
        dest.writeLong(this.timeInterval);
        dest.writeString(this.mimeType);
        dest.writeLong(this.createTime);
        dest.writeString(this.uploadDate);
        dest.writeByte(uploaded ? (byte) 1 : (byte) 0);
        dest.writeByte(isOriginal ? (byte) 1 : (byte) 0);
        dest.writeInt(this.icon);
        dest.writeString(this.preTag);
    }

    public FileBean() {
    }

    protected FileBean(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.path = in.readString();
        this.thumbPath = in.readString();
        this.size = in.readLong();
        this.timeInterval = in.readLong();
        this.mimeType = in.readString();
        this.createTime = in.readLong();
        this.uploadDate = in.readString();
        this.uploaded = in.readByte() != 0;
        this.isOriginal = in.readByte() != 0;
        this.icon = in.readInt();
        this.preTag = in.readString();
    }

    public static final Creator<FileBean> CREATOR = new Creator<FileBean>() {
        public FileBean createFromParcel(Parcel source) {
            return new FileBean(source);
        }

        public FileBean[] newArray(int size) {
            return new FileBean[size];
        }
    };

}

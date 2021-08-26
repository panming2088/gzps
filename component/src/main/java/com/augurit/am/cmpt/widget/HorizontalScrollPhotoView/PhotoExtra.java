package com.augurit.am.cmpt.widget.HorizontalScrollPhotoView;

import android.net.Uri;

import java.util.Date;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.widget.HorizontalScrollPhotoView
 * @createTime 创建时间 ：17/3/9
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/9
 * @modifyMemo 修改备注：
 */

public class PhotoExtra {
    private String filename, mFilePath;
    private Date photoTime;
    private Uri bitmapDataUri;
    private String key;

    public PhotoExtra(String filename, String mFilePath, Date photoTime,
                      Uri bitmapDataUri) {
        super();
        this.filename = filename;
        this.mFilePath = mFilePath;
        this.photoTime = photoTime;
        this.bitmapDataUri = bitmapDataUri;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public Date getPhotoTime() {
        return photoTime;
    }

    public void setPhotoTime(Date photoTime) {
        this.photoTime = photoTime;
    }

    public Uri getBitmapDataUri() {
        return bitmapDataUri;
    }

    public void setBitmapDataUri(Uri bitmapDataUri) {
        this.bitmapDataUri = bitmapDataUri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

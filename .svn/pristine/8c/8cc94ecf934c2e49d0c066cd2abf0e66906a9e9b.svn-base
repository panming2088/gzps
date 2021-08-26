package me.nereo.multi_image_selector.bean;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：me.nereo.multi_image_selector.bean
 * @createTime 创建时间 ：2018-06-19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-06-19
 * @modifyMemo 修改备注：
 */

import android.text.TextUtils;

import java.io.Serializable;
import java.util.Date;

import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 */
public class Image implements Serializable {

    private String filename, mFilePath;
    private Date photoTime;
    public long size;
    public Image(String filename, String filePath, Date photoTime ,long size) {
        this.filename = filename;
        mFilePath = filePath;
        this.photoTime = photoTime;
        this.size = size;
    }


//    public Image(String filename, String filePath, Date photoTime, Uri bitmapDataUri) {
//        this.filename = filename;
//        mFilePath = filePath;
//        this.photoTime = photoTime;
//        this.bitmapDataUri = bitmapDataUri;
//    }
    //    Date photoTime = new Date();
//    String dateString = TimeUtil
//            .getStringTimeYYMMDDSS(photoTime);
//    String filename = dateString + "_img.jpg";
//
//    String mFilePath = photoPath + "/" + filename;
//    File mImageFile = new File(mFilePath);
//
//    Uri bitmapDataUri ;
//    //Android 7.0采用了StrictMode API政策,私有目录被限制访问
//    //判断Android版本是否是Android7.0以上
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
//        bitmapDataUri= FileProvider.getUriForFile(activity,activity.getPackageName()+".FileProvider",mImageFile);
//    }else{
//        bitmapDataUri=Uri.fromFile(mImageFile);
//    }


    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return TextUtils.equals(this.filename, other.filename) && TextUtils.equals(this.mFilePath, other.mFilePath);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
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

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public Date getPhotoTime() {
        return photoTime;
    }

    public void setPhotoTime(Date photoTime) {
        this.photoTime = photoTime;
    }

//    public Uri getBitmapDataUri() {
//        return bitmapDataUri;
//    }
//
//    public void setBitmapDataUri(Uri bitmapDataUri) {
//        this.bitmapDataUri = bitmapDataUri;
//    }

}

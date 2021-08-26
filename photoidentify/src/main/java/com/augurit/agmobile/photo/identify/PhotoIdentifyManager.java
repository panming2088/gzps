package com.augurit.agmobile.photo.identify;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import java.io.File;

/**
 * 描述：身份证拍照识别出身份证号码
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.photo.identify
 * @createTime 创建时间 ：17/5/22
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/5/22
 * @modifyMemo 修改备注：
 */

public class PhotoIdentifyManager {
    private static PhotoIdentifyManager instance;
    private OnIdetifyCallback mCallback;
    private PhotoIdentifyManager(Context context){
        this.mContext = context;
    }
    private Context mContext;

    public static PhotoIdentifyManager getInstance(Context context){
        if(instance == null){
            synchronized (PhotoIdentifyManager.class){
                if(instance == null){
                    instance = new PhotoIdentifyManager(context);
                }
            }
        }

        return instance;
    }

    /**
     * 复制语料库到SD卡,只需要复制一次
     */
    public void copyDataFromAssetsToSD(FileUtils.FileOperateCallback callback){
        File file = new File(Environment.getExternalStorageDirectory(), "tessdata");
        //存在语料库则不再复制
        if(file.exists()){
            callback.onSuccess();
            return;
        }
        FileUtils.getInstance(mContext).copyAssetsToSD("tessdata","tessdata").setFileOperateCallback(callback);
    }

    /**
     * 下载语料库
     * @param callback
     */
    public void downloadDataFromNetToSD(FileUtils.FileOperateCallback callback,String url){
        File file = new File(Environment.getExternalStorageDirectory(), "tessdata");
        //存在语料库则不再下载
        if(file.exists()){
            callback.onSuccess();
            return;
        }
        IdentifyDataDownload.download(mContext,url,callback);
    }

    public interface OnIdetifyCallback{
        void onSuccess(String result);

        void onFail(String msg);
    }

    //拍照识别
    public void takePhotoForIdentify(OnIdetifyCallback callback){
        mCallback = callback;
        Intent ninten = new Intent(mContext, RectCameraActivity.class);
        ninten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(ninten);
    }

    public void onHandleIdentifySuccess(String result){
        if(mCallback != null){
            mCallback.onSuccess(result);
        }
    }

    public void onHandleIdentifyFail(String msg){
        if(mCallback != null){
            mCallback.onFail(msg);
        }
    }
}

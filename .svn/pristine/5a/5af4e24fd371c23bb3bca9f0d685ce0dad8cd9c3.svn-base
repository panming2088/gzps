package com.augurit.agmobile.patrolcore.common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

import static android.R.attr.path;

/**
 * 描述：删除图片的工具类
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.util
 * @createTime 创建时间 ：2017/9/19
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/9/19
 * @modifyMemo 修改备注：
 */

public class DelPhotoUtils {
    public static void delPhoto(List<String> pathList){
        for(String photoPath : pathList){
            if(!TextUtils.isEmpty(photoPath)){
                /*
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = context.getContentResolver();
                String where = MediaStore.Images.Media.DATA + "='" + photoPath + "'";
                //删除图片
                mContentResolver.delete(mImageUri, where, null);

                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File file = new File(photoPath);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
                */
                if(!TextUtils.isEmpty(photoPath)){
                    File file = new File(photoPath);
                    if(file.exists()){
                        file.delete();
                    }
                }

            }
        }
    }
}

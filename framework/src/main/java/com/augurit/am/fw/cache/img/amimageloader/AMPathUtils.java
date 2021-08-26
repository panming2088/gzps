package com.augurit.am.fw.cache.img.amimageloader;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016-07-13.
 */
public final class AMPathUtils {
    private AMPathUtils() {
    }

    /**
     * 获取sd缓存的目录，如果挂载了sd卡则使用sd卡缓存，否则使用应用的缓存目录
     * @param context
     * @param uniqueName 缓存目录命 例如：bitmap
     * @return
     */
    public static File getDiskCacheDir(Context context,String uniqueName){
        String cachePath;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            cachePath = context.getExternalCacheDir().getPath();
        }else{
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);

    }
}

package com.augurit.agmobile.mapengine.layermanage.util;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.EncryptFileCacheHelper;
import com.augurit.agmobile.mapengine.common.agmobilelayer.util.ITileCacheHelper;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.am.fw.utils.AMFileUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;

import java.io.File;

/**
 * AGMobile对应的瓦片图层加密缓存类，相当于原来的CustomTileCachePath和AMFileHelper2的合体
 *
 * @author 创建人 ：weiqiuyue
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.layer.util
 * @createTime 创建时间 ：2017-9-15 13:59
 * @modifyBy 修改人 ：weiqiuyue
 * @modifyTime 修改时间 ：2017-9-15 13:59
 */
public class EncryptTileCacheHelper implements ITileCacheHelper {
    protected String mLayerId;
    protected Context mContext;
    protected String mCacheFolderPath;
    protected EncryptFileCacheHelper mEncryptFileCacheHelper;

    public EncryptTileCacheHelper(Context context, String layerId) {
        this.mContext = context;
        this.mLayerId = layerId;
        this.mEncryptFileCacheHelper = new EncryptFileCacheHelper();
        this.initCacheFolderPath();
    }

    @Override
    public String getCacheFolderPath() {
        return mCacheFolderPath;
    }

    private void initCacheFolderPath() {
        FilePathUtil projectFileSavePathManager = new FilePathUtil(mContext);
        String topSavePath = projectFileSavePathManager.getSavePath();
        //只有当顶层路径不为空时进行保存
        if (!ValidateUtil.isObjectNull(topSavePath)) {
            mCacheFolderPath = topSavePath + "/layer/layer" + mLayerId + "/tile/Layers/_alllayers";
        }
    }

    protected String buildOffLineCachePath(int level, int col, int rol) {
        if (!TextUtils.isEmpty(mCacheFolderPath)) {
            String LLevel;
            if (level <= 9) {
                LLevel = "L0" + level;
            } else {
                LLevel = "L" + level;
            }
            String RRow = "R" + getHexWith8bit(rol);
            String CCol = "C" + getHexWith8bit(col);
            return mCacheFolderPath + "/" + LLevel + "/" + RRow + "/" + CCol + ".png";
        }
        return null;
    }

    /**
     * 删除layerId为mLayerId的图层
     */
    @Override
    public void deleteAllCache() {
        FilePathUtil projectFileSavePathManager = new FilePathUtil(mContext);
        String topSavePath = projectFileSavePathManager.getSavePath();
        //只有当顶层路径不为空时进行保存
        if (!ValidateUtil.isObjectNull(topSavePath)) {
            String cachePath = topSavePath + "/" + "layer" + mLayerId + "/tile";
            File file = new File(cachePath);
            AMFileUtil.deleteFile(file);
        }
       /* if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //说明有SD卡,那么有保存文件
            String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String cachePath = absolutePath+"/"+"AGMobile/layer"+mLayerId+"/tile";
            File file = new File(cachePath);
            AMFileUtil.deleteFile(file);
        }*/
    }

    /**
     * 10进制数转换为16进制，不足8位在前面补0
     *
     * @param i 要转换的数
     * @return 转换结果
     */
    private String getHexWith8bit(int i) {
        //        String hexInt = Integer.toHexString(i);
        String hexInt = String.format("%08x", i);
        return hexInt;
    }

    @Override
    public byte[] getOfflineCacheFile(int level, int col, int rol) {
        String cacheFileName = buildOffLineCachePath(level, col, rol);
        return mEncryptFileCacheHelper.getOfflineCacheFile(cacheFileName);
    }

    @Override
    public boolean addOfflineCacheFile(byte[] bytes, int level, int col, int rol) {
        String cacheFileName = buildOffLineCachePath(level, col, rol);
        return mEncryptFileCacheHelper.addOfflineCacheFile(bytes, cacheFileName);
    }
}

package com.augurit.agmobile.mapengine.layermanage.util;

import android.content.Context;
import android.os.storage.StorageManager;

import com.augurit.am.fw.utils.AMFileUtil;
import com.augurit.am.fw.utils.ExternalSDCardFileUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 获取外置SD卡中瓦片路径
 * Created by liangsh on 2017-03-06.
 */
public class ExternalSDCardTileCachePath{

    private static final String PREFIX = "VECTOR_";  //瓦片图层目录的前缀

    private boolean havePrefix = false;

    private String mLayerId;
    private String mLayerTable;
    private Context mContext;
    public ExternalSDCardTileCachePath(Context context, String layerId, String mLayerTable){
        this.mContext = context;
        this.mLayerTable = mLayerTable;
//        this.mLayerTable = "ZG_SYLX_G";
        this.mLayerId = layerId;
    }

    public String getFilePath(int level, int col, int rol) {
//        String topSavePath = getExternalStoragePath(mContext, true);
        String cachePath = mLayerTable+"/Layers/_alllayers";
        if(!ExternalSDCardFileUtil.isDirectory(mContext, cachePath)){
            cachePath = PREFIX + cachePath;
            havePrefix = true;
        }
        return getFullCachePath(cachePath,level,col,rol);
    }

    public String getEncryptFilePath(int level, int col, int rol){
//        String topSavePath = getExternalStoragePath(mContext, true);
        String cachePath = mLayerTable+"_encrypt/Layers/_alllayers";
        if(!havePrefix){
            cachePath = PREFIX + cachePath;
        }
        return getFullCachePath(cachePath,level,col,rol);
    }

    /**
     * 删除layerId为mLayerId的图层
     */
    public void deleteAllLayer() {
        if(true){
            return;
        }
        String topSavePath = getExternalStoragePath(mContext, true);
        //只有当顶层路径不为空时进行保存
        if (!ValidateUtil.isObjectNull(topSavePath)) {
            String cachePath = topSavePath+"/"+mLayerTable;
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
     * 生成瓦片文件路径，形式是"{cachepath}/level/col/row.png"
     * @param cachepath 存放的目录名称，接在sd路径后，所以不用加sd卡路径
     * @param level 缩放的级别
     * @param col 列
     * @param row 行
     * @return 返回完整的存放图片的文件路径，比如：“/顶层路径/3/2/0.png”
     */
    public static String getFullCachePath(String cachepath, int level, int col, int row) {
        String LLevel;
        if(level<=9){
            LLevel = "L0" + level;
        } else {
            LLevel = "L" + level;
        }
        String RRow = "R" + getHexWith8bit(row);
        String CCol = "C" + getHexWith8bit(col);

        return cachepath + "/" + LLevel + "/" + RRow + "/" + CCol + ".png";
    }

    /**
     * 10进制数转换为16进制，不足8位在前面补0
     * @param i 要转换的数
     * @return 转换结果
     */
    private static String getHexWith8bit(int i){
//        String hexInt = Integer.toHexString(i);
        String hexInt = String.format("%08x", i);
        return hexInt;
    }

    /**
     * 获取SD卡的路径
     * @param mContext
     * @param is_removale true为外置SD卡，false为内置SD卡
     * @return SD卡的路径
     */
    public static String getExternalStoragePath(Context mContext, boolean is_removale) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.augurit.agmobile.mapengine.layermanage.util;

import android.content.Context;

import com.augurit.agmobile.mapengine.common.agmobilelayer.util.NonEncryptFileCacheHelper;

/**
 * AGMobile对应的瓦片图层不加密缓存类
 *
 * @author 创建人 ：weiqiuyue
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.layer.util
 * @createTime 创建时间 ：2017-9-15 13:59
 * @modifyBy 修改人 ：weiqiuyue
 * @modifyTime 修改时间 ：2017-9-15 13:59
 */
public class NonEncryptTileCacheHelper extends EncryptTileCacheHelper {
    protected NonEncryptFileCacheHelper mNonEncryptFileCacheHelper;

    public NonEncryptTileCacheHelper(Context context, String layerId) {
        super(context, layerId);
        this.mNonEncryptFileCacheHelper = new NonEncryptFileCacheHelper();
    }

    @Override
    public byte[] getOfflineCacheFile(int level, int col, int rol) {
        String cacheFileName = buildOffLineCachePath(level, col, rol);
        return mNonEncryptFileCacheHelper.getOfflineCacheFile(cacheFileName);
    }


    @Override
    public boolean addOfflineCacheFile(byte[] bytes, int level, int col, int rol) {
        String cacheFileName = buildOffLineCachePath(level, col, rol);
        return mNonEncryptFileCacheHelper.addOfflineCacheFile(bytes, cacheFileName);
    }
}

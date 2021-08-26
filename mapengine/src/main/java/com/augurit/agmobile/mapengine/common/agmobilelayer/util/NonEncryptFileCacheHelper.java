package com.augurit.agmobile.mapengine.common.agmobilelayer.util;

import com.augurit.am.fw.cache.cache.Cache;

import java.io.File;

/**
 * 普通缓存文件工具，取代AMFileHelper
 *
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.agmobile.mapengine.common.agmobilelayer.util
 * @createTime 创建时间 ：2017-09-15
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-09-15 11:53
 */
public class NonEncryptFileCacheHelper implements IFileCacheHelper {
    @Override
    public byte[] getOfflineCacheFile(String cachePath) {
        try{
            Cache cache = Cache.get(new File(cachePath));
            return cache.getAsBinary(cachePath);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean addOfflineCacheFile(byte[] bytes, String cachePath) {
        try {
            File file = new File(cachePath);
            Cache cache = Cache.get(file);
            cache.put(cachePath, bytes);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

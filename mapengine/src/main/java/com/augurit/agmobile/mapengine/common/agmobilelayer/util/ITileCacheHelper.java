package com.augurit.agmobile.mapengine.common.agmobilelayer.util;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.agmobile.mapengine.common.agmobilelayer.util
 * @createTime 创建时间 ：2017-09-15
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-09-15 13:39
 */
public interface ITileCacheHelper {
    void deleteAllCache();

    String getCacheFolderPath();

    /**
     * 从cachePath中读取文件
     *
     * @return 文件的字节流
     */
    byte[] getOfflineCacheFile(int level, int col, int rol);

    /**
     * 将bytes保存到文件中
     *
     * @param bytes 要保存的字节流
     * @return 保存后的字节流
     */
    boolean addOfflineCacheFile(byte[] bytes, int level, int col, int rol);
}

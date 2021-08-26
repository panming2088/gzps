package com.augurit.agmobile.mapengine.common.agmobilelayer.util;

import com.augurit.am.fw.scy.CryptoUtil;
import com.augurit.am.fw.utils.SDCardUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import java.io.File;

/**
 * 加解密缓存文件工具，取代AMFileHelper2
 *
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.agmobile.mapengine.common.agmobilelayer.util
 * @createTime 创建时间 ：2017-09-15
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-09-15 11:50
 */
public class EncryptFileCacheHelper implements IFileCacheHelper {

    @Override
    public byte[] getOfflineCacheFile(String cachePath) {
        byte[] data = null;
        try {
            data = CryptoUtil.decryptFileToBytes(cachePath);
        } catch (Exception e) {
            return null;
        }
        return data;
    }

    @Override
    public boolean addOfflineCacheFile(byte[] bytes, String cachePath) {
        if (ValidateUtil.isObjectNull(cachePath)) {
            LogUtil.e("EncryptFileCacheHelper-addOfflineCacheFile", "cachePath is null");
            return false;
        }
        if (SDCardUtil.isSdCardExit()) {
            File file = new File(cachePath);
            if (!file.exists()) {
                try {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                } catch (Exception e) {
                    return false;
                }
            }
        } else {
            LogUtil.e("EncryptFileCacheHelper-addOfflineCacheFile", "请先插入sd卡");
            return false;
        }
        try {
            CryptoUtil.encryptFile(bytes, cachePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

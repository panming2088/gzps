package com.augurit.am.fw.utils;

import android.os.Environment;

/**
 * 创建时间:2016-08-23
 * 创建人：xuciluan
 * 功能描述： sd卡工具类
 */

public final class SDCardUtil {
    private SDCardUtil() {
    }

    /**
     * 检查手机上是否有sd卡
     *
     * @return 返回true 或者false
     */
    public static boolean isSdCardExit() {

        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        return sdCardExist;
    }
}

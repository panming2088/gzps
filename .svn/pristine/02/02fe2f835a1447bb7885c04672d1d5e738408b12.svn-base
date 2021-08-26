package com.augurit.am.fw.log.util;


import com.augurit.am.fw.log.AMLogReport;

import java.io.File;

public final class CheckCacheUtil {
    private CheckCacheUtil() {
    }

    /**
     * 检查文件夹是否超出缓存大小
     *
     * @param dir 需要检查大小的文件夹
     * @return 返回是否超过大小，true为是，false为否
     */
    public static boolean checkCacheSize(File dir) {
        long dirSize = FileUtil.folderSize(dir);
        return dirSize >= AMLogReport.getInstance().getCacheSize() && FileUtil.deleteDir(dir);
    }
}

package com.augurit.am.cmpt.maintenance.cache.util;

/**
 * 包名：com.augurit.am.cmpt.storagemgr.cnst
 * 文件描述：存放存储位置常量
 * 创建人：xiejiexin
 * 创建时间：2016-09-22 14:22
 * 修改人：xiejiexin
 * 修改时间：2016-09-22 14:22
 * 修改备注：
 */
public final class StorageDirConstant {
    /**
     * 在此加入应用额外的存储路径(即除cache、data、code之外的路径)
     * 在存储空间计算及自动清理时会将其包含
     */
    public static String[] DIR_PATHS = {
            "/AGMobile/layer"
    };

    private StorageDirConstant() {
    }
}

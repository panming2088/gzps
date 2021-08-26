package com.augurit.am.fw.cache.img.amimageloader;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * 硬盘缓存策略
 * Created by GuoKunhu on 2016-07-12.
 */
public enum AMDiskCache {
    //跳过硬盘缓存
    NONE(DiskCacheStrategy.NONE),

    //仅仅保存原始分辨率的图片
    SOURCE(DiskCacheStrategy.SOURCE),

    //仅仅缓存最终分辨率的图像(降低分辨率后的或者转换后的)
    RESULT(DiskCacheStrategy.RESULT),

    //缓存所有版本的图片,默认行为
    ALL(DiskCacheStrategy.ALL);

    private DiskCacheStrategy strategy;

    AMDiskCache(DiskCacheStrategy strategy) {
        this.strategy = strategy;
    }

    public DiskCacheStrategy getStrategy() {
        return strategy;
    }
}

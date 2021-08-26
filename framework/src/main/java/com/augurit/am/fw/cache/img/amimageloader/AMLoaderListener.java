package com.augurit.am.fw.cache.img.amimageloader;

import com.bumptech.glide.request.target.Target;

/**
 * Created by GuoKunhu on 2016-07-12.
 */
public interface AMLoaderListener {
    /**
     * 加载成功时回调
     * @param resource
     * @param model
     * @param target
     * @param isFromMemoryCache
     * @param isFirstResource
     */
    void onSuccess(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource);

    /**
     * 加载失败时回调
     * @param e
     * @param model
     * @param target
     * @param isFirstResource
     */
    void onError(Exception e, Object model, Target target, boolean isFirstResource);
}

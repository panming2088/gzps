package com.augurit.am.fw.cache.img.amimageloader;


import android.graphics.Bitmap;
import android.view.View;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;

/**
 * Created by Administrator on 2016-07-12.
 */
public class AMLoaderBuilder {
    public static final int CENTER_CROP = 0;

    public Integer placeHolderResId;
    public Integer errorResId;
    public boolean crossFade;
    public int crossDuration;
    public AMOverrideSize size;
    public int CropType = CENTER_CROP;
    public boolean asGif;
    public boolean asBitmap;
    public boolean skipMemoryCache;
    public AMDiskCache diskCacheStrategy = AMDiskCache.ALL;
    public AMLoadPriority prioriy = AMLoadPriority.HIGH;
    public float thumbnail;
    public String thumbnailUrl;
    public SimpleTarget<Bitmap> simpleTarget;
    public ViewTarget<? extends View, GlideDrawable> viewTarget;
    public NotificationTarget notificationTarget;
    public AppWidgetTarget appWidgetTarget;
    public Integer animResId;
    public ViewPropertyAnimation.Animator animator;
    public boolean cropCircle;
    public boolean roundedCorners;
    public boolean grayscale;
    public boolean blur;
    public boolean rotate;
    public int rotateDegree =90;
    public String tag;

    public AMLoaderBuilder setPlaceHolderResId(Integer placeHolderResId) {
        this.placeHolderResId = placeHolderResId;
        return this;
    }

    public AMLoaderBuilder setErrorResId(Integer errorResId) {
        this.errorResId = errorResId;
        return this;
    }

    public AMLoaderBuilder setCrossFade(boolean crossFade) {
        this.crossFade = crossFade;
        return this;
    }

    public AMLoaderBuilder setCrossDuration(int crossDuration) {
        this.crossDuration = crossDuration;
        return this;
    }

    public AMLoaderBuilder setSize(AMOverrideSize size) {
        this.size = size;
        return this;
    }

    public AMLoaderBuilder setCropType(int cropType) {
        CropType = cropType;
        return this;
    }

    public AMLoaderBuilder setAsGif(boolean asGif) {
        this.asGif = asGif;
        return this;
    }

    public AMLoaderBuilder setAsBitmap(boolean asBitmap) {
        this.asBitmap = asBitmap;
        return this;
    }

    public AMLoaderBuilder setSkipMemoryCache(boolean skipMemoryCache) {
        this.skipMemoryCache = skipMemoryCache;
        return this;
    }

    public AMLoaderBuilder setDiskCacheStrategy(AMDiskCache diskCacheStrategy) {
        this.diskCacheStrategy = diskCacheStrategy;
        return this;
    }

    public AMLoaderBuilder setPrioriy(AMLoadPriority prioriy) {
        this.prioriy = prioriy;
        return this;
    }

    public AMLoaderBuilder setThumbnail(float thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public AMLoaderBuilder setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    public AMLoaderBuilder setSimpleTarget(SimpleTarget<Bitmap> simpleTarget) {
        this.simpleTarget = simpleTarget;
        return this;
    }

    public AMLoaderBuilder setViewTarget(ViewTarget<? extends View, GlideDrawable> viewTarget) {
        this.viewTarget = viewTarget;
        return this;
    }

    public AMLoaderBuilder setNotificationTarget(NotificationTarget notificationTarget) {
        this.notificationTarget = notificationTarget;
        return this;
    }

    public AMLoaderBuilder setAppWidgetTarget(AppWidgetTarget appWidgetTarget) {
        this.appWidgetTarget = appWidgetTarget;
        return this;
    }

    public AMLoaderBuilder setAnimResId(Integer animResId) {
        this.animResId = animResId;
        return this;
    }

    public AMLoaderBuilder setAnimator(ViewPropertyAnimation.Animator animator) {
        this.animator = animator;
        return this;
    }

    public AMLoaderBuilder setCropCircle(boolean cropCircle) {
        this.cropCircle = cropCircle;
        return this;
    }

    public AMLoaderBuilder setRoundedCorners(boolean roundedCorners) {
        this.roundedCorners = roundedCorners;
        return this;
    }

    public AMLoaderBuilder setGrayscale(boolean grayscale) {
        this.grayscale = grayscale;
        return this;
    }

    public AMLoaderBuilder setBlur(boolean blur) {
        this.blur = blur;
        return this;
    }

    public AMLoaderBuilder setRotate(boolean rotate) {
        this.rotate = rotate;
        return this;
    }

    public AMLoaderBuilder setRotateDegree(int rotateDegree) {
        this.rotateDegree = rotateDegree;
        return this;
    }

    public AMLoaderBuilder setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public AMImageloaderConfig build() {
        return new AMImageloaderConfig(this);
    }



}

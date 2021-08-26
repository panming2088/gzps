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
 * 图片加载配置
 * Created by GuoKunHu on 2016-07-12.
 */
public class AMImageloaderConfig {
    //从中部裁剪来显示（只放大不缩小，通过裁剪来适应）
    public static final int CENTER_CROP = 0;
    //按比例缩放来适应，居中显示全图
    public static final int FIT_CENTER = 1;

    //默认占位资源
    public Integer placeHolderResId;

    //错误时显示的资源
    public Integer errorResId;

    //是否淡入淡出动画
    public boolean crossFade;

    //淡入淡出动画持续的时间
    public int crossDuration;

    //图片最终显示在ImageView上的宽高度像素
    public AMOverrideSize size;

    //裁剪类型,默认为中部裁剪
    public int CropType = CENTER_CROP;

    //true,强制显示的是gif动画,如果url不是gif的资源,那么会回调error()
    public boolean asGif;

    //true,强制显示为常规图片,如果是gif资源,则加载第一帧作为图片
    public boolean asBitmap;

    //true,跳过内存缓存,默认false
    public boolean skipMemoryCache;

    //硬盘缓存,默认为all类型
    public AMDiskCache diskCacheStrategy;

    public AMLoadPriority prioriy;

    //设置缩略图的缩放比例0.0f-1.0f,如果缩略图比全尺寸图先加载完，就显示缩略图，否则就不显示
    public float thumbnail;

    //设置缩略图的url,如果缩略图比全尺寸图先加载完，就显示缩略图，否则就不显示
    public String thumbnailUrl;

    //指定simpleTarget对象,可以在Target回调方法中处理bitmap,同时该构造方法中还可以指定size
    public SimpleTarget<Bitmap> simpleTarget;

    //指定viewTarget对象,可以是自定义View,该构造方法传入的view和通配符的view是同一个
    public ViewTarget<? extends View, GlideDrawable> viewTarget;

    //设置通知栏加载大图片的target;
    public NotificationTarget notificationTarget;

    //设置加载小部件图片的target
    public AppWidgetTarget appWidgetTarget;

    //图片加载完后的动画效果,在异步加载资源完成时会执行该动画。
    public Integer animResId;

    //在异步加载资源完成时会执行该动画。可以接受一个Animator对象
    public ViewPropertyAnimation.Animator animator;

    //圆形裁剪
    public boolean cropCircle;

    //圆角处理
    public boolean roundedCorners;

    //灰度处理
    public boolean grayscale;

    //高斯模糊处理
    public boolean blur;

    //旋转图片
    public boolean rotate;

    //默认旋转°
    public int rotateDegree;

    //唯一标识
    public String tag;

    public AMImageloaderConfig(AMLoaderBuilder builder) {
        this.placeHolderResId = builder.placeHolderResId;
        this.errorResId = builder.errorResId;
        this.crossFade = builder.crossFade;
        this.crossDuration = builder.crossDuration;
        this.size = builder.size;
        this.CropType = builder.CropType;
        this.asGif = builder.asGif;
        this.asBitmap = builder.asBitmap;
        this.skipMemoryCache = builder.skipMemoryCache;
        this.diskCacheStrategy = builder.diskCacheStrategy;
        this.thumbnail = builder.thumbnail;
        this.thumbnailUrl = builder.thumbnailUrl;
        this.simpleTarget = builder.simpleTarget;
        this.viewTarget = builder.viewTarget;
        this.notificationTarget = builder.notificationTarget;
        this.appWidgetTarget = builder.appWidgetTarget;
        this.animResId = builder.animResId;
        this.animator = builder.animator;
        this.prioriy = builder.prioriy;
        this.blur = builder.blur;
        this.cropCircle = builder.cropCircle;
        this.roundedCorners = builder.roundedCorners;
        this.grayscale = builder.grayscale;
        this.rotate =builder.rotate;
        this.rotateDegree = builder.rotateDegree;
        this.tag = tag;
    }

    /**
     * 将配置翻译到Glide构造器对象上
     * @param config
     * @return
     */
    public static AMLoaderBuilder parseBuilder(AMImageloaderConfig config) {
        AMLoaderBuilder builder = new AMLoaderBuilder();
        builder.placeHolderResId = config.placeHolderResId;
        builder.errorResId = config.errorResId;
        builder.crossFade = config.crossFade;
        builder.crossDuration = config.crossDuration;
        builder.size = config.size;
        builder.CropType = config.CropType;
        builder.asGif = config.asGif;
        builder.asBitmap = config.asBitmap;
        builder.skipMemoryCache = config.skipMemoryCache;
        builder.diskCacheStrategy = config.diskCacheStrategy;
        builder.thumbnail = config.thumbnail;
        builder.thumbnailUrl = config.thumbnailUrl;
        builder.simpleTarget = config.simpleTarget;
        builder.viewTarget = config.viewTarget;
        builder.notificationTarget = config.notificationTarget;
        builder.appWidgetTarget = config.appWidgetTarget;
        builder.animResId = config.animResId;
        builder.animator = config.animator;
        builder.prioriy = config.prioriy;
        builder.cropCircle = config.cropCircle;
        builder.roundedCorners = config.roundedCorners;
        builder.grayscale = config.grayscale;
        builder.blur = config.blur;
        builder.rotate = config.rotate;
        builder.rotateDegree =config.rotateDegree;
        builder.tag = config.tag;
        return builder;
    }

    public Integer getPlaceHolderResId() {
        return placeHolderResId;
    }

    public Integer getErrorResId() {
        return errorResId;
    }

    public boolean isCrossFade() {
        return crossFade;
    }

    public int getCrossDuration() {
        return crossDuration;
    }

    public AMOverrideSize getSize() {
        return size;
    }

    public int getCropType() {
        return CropType;
    }

    public boolean isAsGif() {
        return asGif;
    }

    public boolean isAsBitmap() {
        return asBitmap;
    }

    public boolean isSkipMemoryCache() {
        return skipMemoryCache;
    }

    public AMDiskCache getDiskCacheStrategy() {
        return diskCacheStrategy;
    }

    public AMLoadPriority getPrioriy() {
        return prioriy;
    }

    public float getThumbnail() {
        return thumbnail;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public SimpleTarget<Bitmap> getSimpleTarget() {
        return simpleTarget;
    }

    public ViewTarget<? extends View, GlideDrawable> getViewTarget() {
        return viewTarget;
    }

    public NotificationTarget getNotificationTarget() {
        return notificationTarget;
    }

    public AppWidgetTarget getAppWidgetTarget() {
        return appWidgetTarget;
    }

    public Integer getAnimResId() {
        return animResId;
    }

    public ViewPropertyAnimation.Animator getAnimator() {
        return animator;
    }

    public boolean isCropCircle() {
        return cropCircle;
    }

    public boolean isRoundedCorners() {
        return roundedCorners;
    }

    public boolean isGrayscale() {
        return grayscale;
    }

    public boolean isBlur() {
        return blur;
    }

    public boolean isRotate() {
        return rotate;
    }

    public int getRotateDegree() {
        return rotateDegree;
    }

    public String getTag() {
        return tag;
    }


}

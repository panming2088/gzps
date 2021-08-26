package com.augurit.am.fw.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 设备相关工具类
 *
 * @author 创建人 ：taoerxiang
 * @version 1.0
 * @package 包名 ：com.augurit.am.fw.utils
 * @createTime 创建时间 ：2016-09-06 17:17
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2016-10-28 09:06
 */
public final class DeviceUtil {
    public static final String ONLY_PHONE = "1"; //强制手机

    public static final String ONLY_PAD = "2"; //强制平板

    public static final String DYNAMIC_CHANGE = "3"; //自适应

    public static String MODE = DYNAMIC_CHANGE;

    private DeviceUtil() {
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {

        switch (MODE){

            case ONLY_PAD:
                return true;

            case ONLY_PHONE:
                return false;

            case DYNAMIC_CHANGE:
            default:
                return (context.getResources().getConfiguration().screenLayout & Configuration
                        .SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        }

        // xjx 下面的方法在某些机型上判断有问题
        //        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //        Display display = wm.getDefaultDisplay();
        //        // 屏幕宽度
        //        float screenWidth = display.getWidth();
        //        // 屏幕高度
        //        float screenHeight = display.getHeight();
        //        DisplayMetrics dm = new DisplayMetrics();
        //        display.getMetrics(dm);
        //        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        //        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        //        // 屏幕尺寸
        //        double screenInches = Math.sqrt(x + y);
        //        // 大于8尺寸则为Pad
        //        if (screenInches >= 7.0) {  // 这里暂时改为7.0
        //            return true;
        //        }
        //
        //        return false;

    }


    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

}

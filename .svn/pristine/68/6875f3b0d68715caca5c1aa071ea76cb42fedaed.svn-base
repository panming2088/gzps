package com.augurit.am.fw.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.DisplayMetrics;


/**
 * Created by ac on 2016-07-26.
 */
public final class DisplayUtil {
    private DisplayUtil() {
    }

    /**
     * Gets the heigh of PhoneScreen.
     *
     * @param context
     * @return
     */
    public static int getWindowHeight(Context context) {
        // return contextActivity.getWindowManager().getDefaultDisplay()
        // .getHeight();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * Gets the width of PhoneScreen.
     *
     * @param context
     * @return
     */
    public static int getWindowWidth(Context context) {
        // return contextActivity.getWindow().getWindowManager()
        // .getDefaultDisplay().getWidth();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getDensityDpi(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    /**
     * Gets the height of the status bar.It has to be invoked in such as
     * onWindowFocusChanged if you want to calculate when activity creates.
     *
     * @param contextActivity
     * @return
     */
    public static int getStatusBarHeight(Activity contextActivity) {
        Rect frame = new Rect();
        /**
         * Sets frame to application field.PhoneScreen consists of a statusBar
         * and an application field
         */
        contextActivity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(frame);
        /**
         * The distance from the top of the application field to the top of
         * PhoneScreen is the heigh of statusBar.
         */
        return frame.top;
    }

    /**
     * Strongly suggestion:Uses {@link android.support.v7.widget.Toolbar}
     * instead of {@link android.support.v7.app.ActionBar}, and uses
     * actionBarSize to custom the heigh of Toolbar
     *
     * @param activity
     * @return
     */
    public static int getToolBarHeight(Activity activity) {
        TypedArray a = activity.getTheme().obtainStyledAttributes(
                ResourceUtil.getIdsByName(activity, "styleable", "Theme"));
        int height = a.getDimensionPixelSize(ResourceUtil.getIdByName(activity,
                "styleable", "Theme_actionBarSize"), 0);
        a.recycle();
        return height;
    }
}

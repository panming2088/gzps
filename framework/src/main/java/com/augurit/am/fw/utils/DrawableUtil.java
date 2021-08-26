package com.augurit.am.fw.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.fw.utils
 * @createTime 创建时间 ：2016-12-27
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-27
 */

public final class DrawableUtil {
    private DrawableUtil() {
    }

    /**
     * 可以动态改变图标的颜色
     *
     * @param drawable
     * @param colors
     * @return
     */
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }
}

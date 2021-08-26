package com.augurit.am.fw.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;

import java.lang.reflect.Method;

/**
 * Created by ac on 2016-07-26.
 */
public final class OptionMenuUtil {
    private OptionMenuUtil() {
    }

    // enable为true时，菜单添加图标有效，enable为false时无效。4.0以上系统默认无效
    public static void setIconEnable(Menu menu, boolean enable) {
        try {
            Class<?> clazz = Class
                    .forName("android.support.v7.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible",
                    boolean.class);
            m.setAccessible(true);

            // MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使当前Action的onCreateOptionsMenu重新被调用，用于有自己专属OptionsMenu的功能模块
     *
     * @param context
     */
    public static void invalidateOptionsMenu(Context context) {
        Activity activity = (Activity) context;
        activity.invalidateOptionsMenu();
    }
}

package com.augurit.am.fw.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by ac on 2016-07-26.
 */
public final class PackageUtil {
    private PackageUtil() {
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static PackageInfo getPackageInfo(Context context)
            throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(
                getPackageName(context), 0);
    }

    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = getPackageInfo(context).versionCode;
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            nameNotFoundException.printStackTrace();
        }
        return verCode;
    }

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = getPackageInfo(context).versionName;
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
            nameNotFoundException.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取包名最后一个字母，如com.android.example的example
     *
     * @param context
     * @return
     */
    public static String getPackageLastName(Context context) {
        String[] str = context.getPackageName().split("\\.");
        String path = context.getPackageName();
        if (!ListUtil.isEmpty(str)) {
            path = str[str.length - 1];
        }
        return path;
    }
}

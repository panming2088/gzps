package com.augurit.am.fw.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ac on 2016-07-26.
 */
public final class ContextUtil {
    private ContextUtil() {
    }

    public static Activity getActivityFromConext(Context context) {
        if (context != null) {
            try {
                return (Activity) context;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static AppCompatActivity getAppCompatActivityFromConext(
            Context context) {
        if (context != null) {
            try {
                return (AppCompatActivity) context;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public static Context getApplicationContext(Context context) {
        if (context != null) {
            return context.getApplicationContext();
        }
        return null;

    }
}

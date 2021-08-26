package com.augurit.am.fw.net.util;

/**
 * Created by Guokunhu on 2016-07-09.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreference工具类
 */
public class SharedPreferencesUtil {
    private Context mContext;
    private SharedPreferences sharedPreferences;

    public SharedPreferencesUtil(Context context) {
        this.mContext = context;
        this.sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
    }

    public void refresh() {
        this.sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public String getString(String key, String defaultValue) {
        return this.sharedPreferences.getString(key, defaultValue);
    }

    public boolean setString(String key, String value) {
        return this.sharedPreferences.edit().putString(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.sharedPreferences.getBoolean(key, defaultValue);
    }

    public boolean setBoolean(String key, boolean value) {
        return this.sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public int getInt(String key, int defaultValue) {
        return this.sharedPreferences.getInt(key, defaultValue);
    }

    public boolean setInt(String key, int value) {
        return this.sharedPreferences.edit().putInt(key, value).commit();
    }

    public Long getLong(String key, Long defaultValue) {
        return this.sharedPreferences.getLong(key, defaultValue);
    }

    public boolean setLong(String key, Long value) {
        return this.sharedPreferences.edit().putLong(key, value).commit();
    }
}



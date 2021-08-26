package com.augurit.agmobile.gzps.common.util;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Json辅助类
 *
 * @PROJECT GZPS
 * @USER Augurit
 * @CREATE 2020/11/10 11:29
 */
public class JSONHelper {

    public static int getInt(JSONObject jsob, String key) {
        return getInt(jsob, key, -1);
    }

    public static int getInt(JSONObject jsob, String key, int defValue) {
        if (jsob == null || TextUtils.isEmpty(key)) {
            return defValue;
        }
        if (jsob.has(key) && !jsob.isNull(key)) {
            try {
                return jsob.getInt(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return defValue;
    }

    public static String getString(JSONObject jsob, String key) {
        return getString(jsob, key, "");
    }

    public static String getString(JSONObject jsob, String key, String defValue) {
        if (jsob == null || TextUtils.isEmpty(key)) {
            return defValue;
        }
        if (jsob.has(key) && !jsob.isNull(key)) {
            try {
                return jsob.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return defValue;
    }

    public static long getLong(JSONObject jsob, String key) {
        return getLong(jsob, key, -1L);
    }

    public static long getLong(JSONObject jsob, String key, long defValue) {
        if (jsob == null || TextUtils.isEmpty(key)) {
            return defValue;
        }
        if (jsob.has(key) && !jsob.isNull(key)) {
            try {
                return jsob.getLong(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return defValue;
    }

    public static double getDouble(JSONObject jsob, String key) {
        return getDouble(jsob, key, -1.0);
    }

    public static double getDouble(JSONObject jsob, String key, double defValue) {
        if (jsob == null || TextUtils.isEmpty(key)) {
            return defValue;
        }
        if (jsob.has(key) && !jsob.isNull(key)) {
            try {
                return jsob.getDouble(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return defValue;
    }

    public static Object getObject(JSONObject jsob, String key) {
        return getObject(jsob, key, null);
    }

    public static Object getObject(JSONObject jsob, String key, Object defValue) {
        if (jsob == null || TextUtils.isEmpty(key)) {
            return defValue;
        }
        if (jsob.has(key) && !jsob.isNull(key)) {
            try {
                return jsob.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return defValue;
    }
}

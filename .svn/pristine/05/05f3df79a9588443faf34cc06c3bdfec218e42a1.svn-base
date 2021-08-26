package com.augurit.am.fw.utils;

import android.os.Bundle;

import java.util.List;

/**
 * Created by ac on 2016-07-26.
 */
public final class BundleUtil {
    private BundleUtil() {
    }

    public static Bundle getParamBundle(Object... bundleObjects) {
        List<Object> bundleObjectList = ListUtil.getList(bundleObjects);
        if (ListUtil.isEmpty(bundleObjectList)) {
            throw new IllegalArgumentException(
                    "Param must not be empty or null");
        }
        Bundle bundle = new Bundle();
        for (int i = 0; i + 1 < bundleObjectList.size(); i = i + 2) {
            Object keyObject = bundleObjectList.get(i);
            Object valueObject = bundleObjectList.get(i + 1);
            try {
                bundle.putString(keyObject.toString(), String.valueOf(valueObject));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bundle;
    }
}

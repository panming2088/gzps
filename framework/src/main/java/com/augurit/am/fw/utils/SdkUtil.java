package com.augurit.am.fw.utils;

/**
 * Created by ac on 2016-07-26.
 */
public final class SdkUtil {
    private SdkUtil() {
    }

    public static boolean isHighSdkVerson(int targetSDK) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        return sdk >= targetSDK;
    }
}

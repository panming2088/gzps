package com.augurit.am.cmpt.loc.cnst;

/**
 * Created by Yahya Bayramoglu on 19/02/16.
 */
public final class FailType {
    public static final int PERMISSION_DENIED = 1; //未授权
    public static final int GP_SERVICES_NOT_AVAILABLE = 2;
    public static final int GP_SERVICES_CONNECTION_FAIL = 3;
    public static final int NETWORK_NOT_AVAILABLE = 4;
    public static final int NETWORK_PERMISSON_ISNOT_GRANTED = 9; //网络定位权限未开启
    public static final int TIMEOUT = 5;
    public static final int GP_SERVICES_SETTINGS_DIALOG = 6;
    public static final int GP_SERVICES_SETTINGS_DENIED = 7;
    public static final int LOCATION_TRANSFORM_FAILED = 8 ;//坐标转换失败
    private FailType() {
    }

}
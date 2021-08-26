package com.augurit.agmobile.patrolcore.selectlocation.util;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.map.util
 * @createTime 创建时间 ：2017-04-21
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-21
 * @modifyMemo 修改备注：
 */

public class SelectLocationConstant {

    //public static boolean ifRequestLocationByBaidu = false;
    public static boolean ifLocationAddressEditable = true;
    public static boolean ifLocateByBlueTooth = false; //是否采用蓝牙定位

    public static final String IF_READ_ONLY = "if_read_only";//是否只允许查阅位置
    public static final String DESTINATION_OR_LASTTIME_SELECT_LOCATION = "destination_location";//目的地坐标或者上次选择点的坐标
    public static final String DESTINATION_OR_LASTTIME_SELECT_ADDRESS = "destination_address";//上次选择的地址
    public static final String INITIAL_SCALE = "initial_scale";//初始缩放比例
}

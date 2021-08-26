package com.augurit.agmobile.gzps.doorno.model;


/**
 * Created by xiaoyu on 2018/4/9.
 */

public class MyApplication {
    public static double X ;
    public static double Y ;
//    public static String ROOMTYPE="";
    public static String ID="";
    public static String ENTRANCE="";
    public static String GUID="";
    public static String DZDM="";
    public static String buildId="";
    //待关联排水户是否选择了门牌
//    public static boolean isChooseDoorNo = false;

//    public  static String HOUSE_ID = "";


    public static SewerageRoomClickItemBean SEWERAGEROOMCLICKITEMBEAN;
    public static SewerageItemBean SEWERAGEITEMBEAN;
    public static int refreshListType = 0;
    public static DoorNOBean doorBean;

    public static void clearData() {
        refreshListType = 0;
        buildId = null;
        doorBean = null;
        X = 0;
        Y = 0;
        GUID = null;
        DZDM = null;
    }
}

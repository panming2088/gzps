package com.augurit.agmobile.gzps.doorno.view;


/**
 * Created by xiaoyu on 2017/4/7.
 * 所有I开头的JAVA文件皆是接口
 */

public class IBizFactory {

    public static ISewerageItemBiz getSewerageItemBiz() {
        return BizSewerageItem.getInstanse();
    }

    public static ISewerageRoomClickBiz getSewerageRoomClickBiz() {
        return BizSewerageRoomClick.getInstanse();
    }
}

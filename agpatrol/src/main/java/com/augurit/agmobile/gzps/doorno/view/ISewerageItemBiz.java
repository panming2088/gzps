package com.augurit.agmobile.gzps.doorno.view;



/**
 * Created by xiaoyu on 2018/4/9.
 */

public interface ISewerageItemBiz {
    void   getdata(SewerageItemListener sewerageItemListener, String id, String dzdm, int page, int count, int refresh_item, int refresh_list_type, String add_type);
    void   investEnd(SewerageItemListener sewerageItemListener, String sGuid, String loginName, String add_type);
    void   getdata(SewerageItemListener sewerageItemListener, String id, String dzdm, int page, int count, int refresh_item, int refresh_list_type, String name, String add_type);
    void   getBuildInfBySGuid(SewerageItemListener sewerageItemListener, String sGuid);
}

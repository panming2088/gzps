package com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist;

/**
 * 包名：com.augurit.agmobile.gzps.uploadfacility.view.myuploadlist
 * 类描述：
 * 创建人：luobiao
 * 创建时间：2019/6/28 11:20
 * 修改人：luobiao
 * 修改时间：2019/6/28 11:20
 * 修改备注：
 */
public class RefreshPipeListEvent {
    private int type;//1:新增，2：校核

    public RefreshPipeListEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

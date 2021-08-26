package com.augurit.agmobile.gzps.doorno.model;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadevent.event
 * @createTime 创建时间 ：2017/12/25
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/12/25
 * @modifyMemo 修改备注：
 */

public class RefreshDataListEvent {

    public static final int UPDATE_MIAN_LIST =1;//更新主页面，在主界面的单位列表点击添加按钮
//    public static final int UPDATE_MAIN_UNIT_LIST =3;//在主界面的单位列表，点击item进入
    public static final int UPDATE_ROOM_UNIT_LIST1 =2;//进入房屋列表下面的单位列表，直接点击添加按钮
//    public static final int UPDATE_ROOM_UNIT_LIST2 =4;//进入房屋列表下面的单位列表,点进单位item之后进入

    private int updateType;

    public RefreshDataListEvent(int updateState) {
        this.updateType = updateType;
    }

    public int getUpdateState() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }
}

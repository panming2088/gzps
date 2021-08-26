package com.augurit.agmobile.gzps.uploadevent.event;

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

public class UpdateAfterEditEvent {

    public static final String UPDATE_DETAIL ="0";//更新任务详情页面
    public static final String UPDATE_LIST="1";//跳出详情页面然后更新列表

    private String updateState;

    public UpdateAfterEditEvent(String updateState) {
        this.updateState = updateState;
    }

    public String getUpdateState() {
        return updateState;
    }

    public void setUpdateState(String updateState) {
        this.updateState = updateState;
    }
}

package com.augurit.agmobile.mapengine.edit.view;

/**
 * 编辑后返回接口,留做处理编辑后原始现场UI的恢复
 *
 * Created by guokunhu on 16/9/27.
 */

public interface IBackAfterEditListener {

    /**
     * 退出编辑功能后恢复现场
     */
    void resetViewAfterEdited();
}

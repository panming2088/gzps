package com.augurit.agmobile.patrolcore.common.device.event;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.device.event
 * @createTime 创建时间 ：2017/8/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/8/7
 * @modifyMemo 修改备注：
 */

public class CheckDeviceRegisterEvent {
    private boolean isRegister;

    public CheckDeviceRegisterEvent(boolean isRegister) {
        this.isRegister = isRegister;
    }

    public boolean isRegister() {
        return isRegister;
    }

    public void setRegister(boolean register) {
        isRegister = register;
    }
}

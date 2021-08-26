package com.augurit.am.cmpt.media.speech.intfc;

/**
 * @author 创建人: xiejiexin
 * @date 创建时间: 2016-08-08
 * @description 功能描述: 唤醒状态接口
 */
public interface WakeUpListener {

    /**
     * 被唤醒
     * @param word 唤醒词
     */
    void onWakeUp(String word);

    /**
     * 唤醒服务停止
     */
    void onExit();
}

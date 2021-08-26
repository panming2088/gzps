package com.augurit.am.cmpt.media.speech.intfc;

/**
 * @author 创建人: xiejiexin
 * @date 创建时间: 2016-08-25
 * @description 功能描述: 语音识别状态监听
 */
public interface VoiceRecognizeListener {

    /**
     * 返回识别结果
     * @param result 识别结果
     */
    void onResult(String result);
}

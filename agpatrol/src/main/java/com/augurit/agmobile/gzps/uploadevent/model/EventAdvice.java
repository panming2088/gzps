package com.augurit.agmobile.gzps.uploadevent.model;

/**
 * 事件意见
 *
 * Created by xcl on 2017/11/11.
 */

public class EventAdvice {

    private String advisor;
    /**
     * 建议内容
     */
    private String content;

    public String getAdvisor() {
        return advisor;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

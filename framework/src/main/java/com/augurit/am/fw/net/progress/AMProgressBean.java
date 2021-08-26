package com.augurit.am.fw.net.progress;

/**
 * 下载进度 bean 类
 * Created by GuoKunhu on 2016-07-22.
 */
public class AMProgressBean {
    //已读取字节数
    private long bytesRead;

    //总下载长度
    private long contentLength;

    //是否完成下载
    private boolean done;


    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}

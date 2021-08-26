package com.augurit.am.fw.net.progress;

/**
 * 下载进度监听器
 * Created by GuoKunhu on 2016-07-22.
 */
public interface AMProgressListener {
    /**
     *
     * @param progress 已经下载或者上传字节数
     * @param total   总字节数
     * @param done    是否完成
     */
    void onProgress(long progress, long total, boolean done);
}

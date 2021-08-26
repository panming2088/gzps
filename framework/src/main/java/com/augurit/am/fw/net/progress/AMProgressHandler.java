package com.augurit.am.fw.net.progress;

import android.os.Looper;
import android.os.Message;
import android.os.Handler;


/**
 * 通过 Handler 将子线程中的 ProgressListener的数据发送到UI线程中进行处理
 * Created by GuoKunhu on 2016-07-22.
 */
public abstract class AMProgressHandler {
    /**
     * 向UI线程发送消息
     * @param AMProgressBean
     */
    public abstract void sendMessage(AMProgressBean AMProgressBean);

    /**
     * 处理消息
     * @param message
     */
    public abstract void handleMessage(Message message);

    /**
     * 在UI线程中进行调用，显示下载进度
     * @param progress
     * @param total
     * @param done
     */
    public abstract void onProgress(long progress,long total,boolean done);

    //为了避免内存泄漏，使用static关键字
    public static class ResponseHandler extends Handler{
        private AMProgressHandler mAMProgressHandler;
        public ResponseHandler(AMProgressHandler mAMProgressHandler,Looper looper){
            super(looper);
            this.mAMProgressHandler = mAMProgressHandler;
        }

        @Override
        public void handleMessage(Message msg) {
            mAMProgressHandler.handleMessage(msg);
        }
    }

}

package com.augurit.am.fw.net.progress;

import android.os.Looper;
import android.os.Message;

/**
 * Created by GuoKunhu on 2016-07-22.
 */
public abstract class AMDownloadProgressHandler extends AMProgressHandler {

    private static final int DOWNLOAD_PROGRESS =1;
    public ResponseHandler mHandler = new ResponseHandler(this, Looper.getMainLooper());

    @Override
    public void handleMessage(Message message) {
        switch (message.what){
            case DOWNLOAD_PROGRESS:
                AMProgressBean AMProgressBean = (AMProgressBean)message.obj;
                onProgress(AMProgressBean.getBytesRead(), AMProgressBean.getContentLength(), AMProgressBean.isDone());
        }
    }

    @Override
    public void sendMessage(AMProgressBean AMProgressBean) {
        mHandler.obtainMessage(DOWNLOAD_PROGRESS, AMProgressBean).sendToTarget();
    }
}

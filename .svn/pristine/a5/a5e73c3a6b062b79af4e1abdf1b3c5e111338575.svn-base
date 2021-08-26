package com.augurit.am.fw.net.progress;

import android.os.Looper;
import android.os.Message;

/**
 * Created by GuoKunhu on 2016-07-24.
 */
public abstract class AMUploadProgressHandler extends AMProgressHandler {
    private static final int UPLOAD_PROGRESS = 0;
    protected ResponseHandler mHandler = new ResponseHandler(this, Looper.getMainLooper());

    @Override
    public void sendMessage(AMProgressBean AMProgressBean) {
        mHandler.obtainMessage(UPLOAD_PROGRESS, AMProgressBean).sendToTarget();

    }

    @Override
    public void handleMessage(Message message){
        switch (message.what){
            case UPLOAD_PROGRESS:
                AMProgressBean AMProgressBean = (AMProgressBean)message.obj;
                onProgress(AMProgressBean.getBytesRead(), AMProgressBean.getContentLength(), AMProgressBean.isDone());
        }
    }
}

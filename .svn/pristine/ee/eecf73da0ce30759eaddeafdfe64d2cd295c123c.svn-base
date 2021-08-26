package com.augurit.am.fw.net.progress;

import java.io.IOException;

import okio.Buffer;
import okio.ForwardingSource;
import okio.Source;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;

/**
 * 对于文件下载的进度监听，需要重写ResponseBody 的一些方法
 * Created by GuoKunhu on 2016-07-22.
 */
public class AMProgressResponseBody extends ResponseBody{
    private  ResponseBody responseBody;
    private AMProgressListener AMProgressListener;
    private BufferedSource bufferedSource;

    public AMProgressResponseBody(ResponseBody responseBody, AMProgressListener AMProgressListener) {
        this.AMProgressListener = AMProgressListener;
        this.responseBody = responseBody;
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public BufferedSource source() {
        if(bufferedSource == null)
            bufferedSource = Okio.buffer(source(responseBody.source()));
        return bufferedSource;
    }

    private Source source(Source source){
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink,byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                AMProgressListener.onProgress(totalBytesRead,responseBody.contentLength(),bytesRead== -1);
                return bytesRead;
            }
        };

    }
}

package com.augurit.am.fw.net.progress;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 *  对于文件上传的进度监听
 * Created by GuoKunhu on 2016-07-24.
 */
public class AMProgressRequestBody extends RequestBody {

    private final RequestBody requestBody;
    private final AMProgressListener AMProgressListener;
    private BufferedSink bufferedSink;

    public AMProgressRequestBody(RequestBody requestBody, AMProgressListener AMProgressListener){
        this.requestBody = requestBody;
        this.AMProgressListener = AMProgressListener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {

            bufferedSink = Okio.buffer(sink(sink));
        }

        requestBody.writeTo(bufferedSink);

        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {

            long bytesWritten = 0L;
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }

                bytesWritten += byteCount;
                AMProgressListener.onProgress(bytesWritten, contentLength, bytesWritten == contentLength);
            }
        };
    }
}

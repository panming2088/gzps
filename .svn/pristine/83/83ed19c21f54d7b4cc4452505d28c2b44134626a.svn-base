package com.augurit.agmobile.mapengine.common.agmobilelayer.service;


import android.support.annotation.Keep;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by ac on 2016-07-07.
 */
@Keep
public class TiandiTuDownloader implements ILayerDownloader {
    @Override
    public void loadMessageFromUrl(String url, final OnLoadDataListenr listenr) {

        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback()   {
            @Override
            public void onFailure(Call call, IOException e) {
                listenr.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String htmlStr = response.body().string();
                listenr.onSuccess(htmlStr);
            }
        });
    }
}

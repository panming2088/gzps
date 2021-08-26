/*
 * @(#)CrashHttpReport.java		       Project: CrashHandler
 * Date: 2014-7-1
 *
 * Copyright (c) 2014 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.augurit.am.fw.log.upload.http;

import android.content.Context;
import android.util.Log;



import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.augurit.am.fw.log.upload.BaseUpload;
import com.augurit.am.fw.net.AMNetwork;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;


/**
 * HTTP的post请求方式发送。
 */
public class AMHttpReporter extends BaseUpload {
    HttpClient httpclient = new DefaultHttpClient();
    private String url;
    private Map<String, String> otherParams;
    private String titleParam;
    private String bodyParam;
    private String fileParam;
    private String to;
    private String toParam;
    private HttpReportCallback callback;
    private   AMNetwork mAMNetwork;
    private String serverUrl = "http://192.168.30.27:8088/";


    public AMHttpReporter(Context context) {
        super(context);


        mAMNetwork = new AMNetwork(serverUrl);
        mAMNetwork.addLogPrint();
        //  mAMNetwork.addRxjavaConverterFactory();
        mAMNetwork.build();
        mAMNetwork.registerApi(UploadLogApi.class);
    }

    @Override
    protected void sendReport(String title, String body, File file, final OnUploadFinishedListener onUploadFinishedListener) {

        /*
       UploadLogApi logApi = (UploadLogApi) mAMNetwork.getServiceApi(UploadLogApi.class);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("file"+"\"; filename=\""+file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"),file));

        Observable<ResponseBody> observable = logApi.uploadFiles(url, map);
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(onUploadFinishedListener != null){
                            onUploadFinishedListener.onError(e.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void onNext(ResponseBody response) {
                      if(onUploadFinishedListener != null){
                          onUploadFinishedListener.onSuceess();
                      }
                    }
                });
                */

        SimpleMultipartEntity entity = new SimpleMultipartEntity();
        entity.addPart(titleParam, title);
        entity.addPart(bodyParam, body);
        entity.addPart(toParam, to);
        if (otherParams != null) {
            for (Map.Entry<String, String> param : otherParams.entrySet()) {
                entity.addPart(param.getKey(), param.getValue());
            }
        }
        if(file!=null) {
            entity.addPart(fileParam, file, true);
        }
        try {
            HttpPost req = new HttpPost(url);
            req.setEntity(entity);
            HttpResponse resp = httpclient.execute(req);
            int statusCode = resp.getStatusLine().getStatusCode();
            String responseString = EntityUtils.toString(resp.getEntity());
            if(onUploadFinishedListener!=null){
                onUploadFinishedListener.onSuceess();
            }

        } catch (Exception e) {
            if(onUploadFinishedListener!=null){
                onUploadFinishedListener.onError("Http send fail!!!!!!");
            }

            e.printStackTrace();
        }

    }

    private boolean deleteLog(File file) {
        Log.d("AMHttpReporter", "delete: " + file.getName());
        return file.delete();
    }

    public String getUrl() {
        return url;
    }

    /**
     * @param url 发送请求的地址。
     */
    public AMHttpReporter setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitleParam() {
        return titleParam;
    }

    /**
     * @param titleParam 标题的参数名
     */
    public AMHttpReporter setTitleParam(String titleParam) {
        this.titleParam = titleParam;
        return this;
    }

    public String getBodyParam() {
        return bodyParam;
    }

    /**
     * @param bodyParam 内容的参数名
     */
    public AMHttpReporter setBodyParam(String bodyParam) {
        this.bodyParam = bodyParam;
        return this;
    }

    public String getFileParam() {
        return fileParam;
    }

    /**
     * @param fileParam 文件的参数名
     */
    public AMHttpReporter setFileParam(String fileParam) {
        this.fileParam = fileParam;
        return this;
    }

    public Map<String, String> getOtherParams() {
        return otherParams;
    }

    /**
     * 。
     *
     * @param otherParams 其他自定义的参数对（可不设置）
     */
    public void setOtherParams(Map<String, String> otherParams) {
        this.otherParams = otherParams;
    }

    public String getTo() {
        return to;
    }

    /**
     * @param to 收件人
     */
    public AMHttpReporter setTo(String to) {
        this.to = to;
        return this;
    }

    public HttpReportCallback getCallback() {
        return callback;
    }

    /**
     * @param callback 设置发送请求之后的回调接口。
     */
    public AMHttpReporter setCallback(HttpReportCallback callback) {
        this.callback = callback;
        return this;
    }

    public String getToParam() {
        return toParam;
    }

    /**
     * @param toParam 收件人参数名。
     */
    public AMHttpReporter setToParam(String toParam) {
        this.toParam = toParam;
        return this;
    }


    public interface HttpReportCallback {
        /**
         * 判断是否发送成功。它在发送日志的方法中被调用，如果成功，则日志文件会被删除。
         *
         * @param status  状态码
         * @param content 返回的内容。
         * @return 如果成功，则日志文件会被删除,返回true
         */
        boolean isSuccess(int status, String content);
    }
}

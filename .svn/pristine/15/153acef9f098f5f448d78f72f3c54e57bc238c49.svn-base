package com.augurit.am.fw.net;

import android.content.Context;

import com.augurit.am.fw.net.api.ApiManager;
import com.augurit.am.fw.net.api.CommonApi;
import com.augurit.am.fw.net.progress.AMProgressHandler;
import com.augurit.am.fw.net.soap.AMSoapCallback;
import com.augurit.am.fw.net.soap.AMSoapRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * 网络接口使用类
 *
 * 基于Retorfit 2.0(OkHttp)封装的类库
 * Created by GuoKunHu on 2016-07-04.
 */
public class AMNetwork {

    /**
     * retrofit 客户端
     */
    public Retrofit retrofit;

    /**
     * retrofit 构建对象
     */
    public Retrofit.Builder retrofitBuilder;

    /**
     * okHttpClient 构建对象
     */
    public OkHttpClient.Builder okHttpBuilder;

    /**
     *  soap网络请求对象实例
     */
    public AMSoapRequest amSoapRequest;

    /**
     * 构造函数 http相关请求实例构造函数
     * @param baseUrl 网络请求基本URL
     */
    public AMNetwork(String baseUrl) {
        this.retrofitBuilder = AMRetrofitManager.getInstance(baseUrl).getBuilder();
        this.okHttpBuilder = new AMOKHttpManager().getBuilder();
    }

    /**
     * 构造函数 soap相关网络请求实例构造函数
     * @param context
     * @param url
     * @param paramObject
     * @param nameSpace
     */
    public AMNetwork(Context context,String url,Object paramObject,String nameSpace,String methodName){
        this.amSoapRequest = new AMSoapRequest(context,url,paramObject,nameSpace,null,methodName);
    }

    /**
     * 执行soap网络请求函数
     * @param amSoapCallback
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public void execute(AMSoapCallback amSoapCallback) throws IOException {
        if(this.amSoapRequest != null){
            this.amSoapRequest.execute(amSoapCallback);
        }
    }

    /**
     * 添加请求日志打印
     */
    public OkHttpClient.Builder addLogPrint(){
        this.okHttpBuilder=AMOKHttpManager.getInstance().addLogPrint();
        return this.okHttpBuilder;
    }

    /**
     * 设置网络读时间
     * @param readTime
     */
    public OkHttpClient.Builder setReadTime(int readTime){
        this.okHttpBuilder = AMOKHttpManager.getInstance().setReadTime(readTime);
        return this.okHttpBuilder;
    }

    /**
     * 设置网络写时间
     * @param writeTime
     */
    public OkHttpClient.Builder setWriteTime(int writeTime){
        this.okHttpBuilder = AMOKHttpManager.getInstance().setWriteTime(writeTime);
        return this.okHttpBuilder;
    }

    /**
     * 设置网络连接时间
     * @param connectTime
     */
    public OkHttpClient.Builder setConnectTime(int connectTime){
        this.okHttpBuilder = AMOKHttpManager.getInstance().setConnectTime(connectTime);
        return this.okHttpBuilder;
    }



    /**
     * 设置是否连接失败重连
     * @param isSet
     */
    public OkHttpClient.Builder setRetryOnConnectionFail(boolean isSet){
        this.okHttpBuilder = AMOKHttpManager.getInstance().setRetryOnConnectionFailure(isSet);
        return this.okHttpBuilder;
    }

    /**
     * 设置cookie缓存管理和 SharedPreference 持久化存储管理
     * @param context
     * @return
     */
    public OkHttpClient.Builder setCookie(Context context){
        this.okHttpBuilder = AMOKHttpManager.getInstance().setCookie(context);
        return this.okHttpBuilder;
    }


    /**
     * 设置带时间限制的缓存
     * @param context
     * @param cacheSize
     * @param cachePath
     * @param cacheDir
     * @return
     */
    public OkHttpClient.Builder setTimeCache(Context context,int cacheSize,String cachePath,String cacheDir){
        this.okHttpBuilder = AMOKHttpManager.getInstance().setTimeCache(context, cacheSize, cachePath, cacheDir);
        return this.okHttpBuilder;
    }

    public OkHttpClient.Builder setTimeCache(Context context,int cacheSize){
        this.okHttpBuilder = AMOKHttpManager.getInstance().setTimeCache(context, cacheSize);
        return this.okHttpBuilder;
    }

    public OkHttpClient.Builder setTimeCache(Context context){
        this.okHttpBuilder = AMOKHttpManager.getInstance().setTimeCache(context);
        return this.okHttpBuilder;
    }

    /**
     * 设置不带时间限制的简单缓存
     * @param context
     * @param cacheSize
     * @param cachePath
     * @param cacheDir
     * @return
     */
    public OkHttpClient.Builder setSimpleCache(Context context,int cacheSize,String cachePath,String cacheDir){
        this.okHttpBuilder = AMOKHttpManager.getInstance().setSimpleCache(context, cacheSize, cachePath, cachePath);
        return this.okHttpBuilder;
    }

    public OkHttpClient.Builder setSimpleCache(Context context,int cacheSize){
        this.okHttpBuilder = AMOKHttpManager.getInstance().setSimpleCache(context,cacheSize);
        return this.okHttpBuilder;
    }

    public OkHttpClient.Builder setSimpleCache(Context context){
        this.okHttpBuilder = AMOKHttpManager.getInstance().setSimpleCache(context);
        return this.okHttpBuilder;
    }


    public OkHttpClient.Builder addProgress(AMProgressHandler amProgressHandler){
        this.okHttpBuilder = AMOKHttpManager.getInstance().addProgress(amProgressHandler);
        return this.okHttpBuilder;
    }


    /**
     * 开启https支持
     * 信任所有服务器证书：参数为 null, null, null；此模式下，通信过程中的数据仍然加密，但有中间人攻击风险
     * 单向认证模式：第一个参数不为空，第二、三个参数为null即可
     * 双向认证模式：前三个参数都不能为空
     * @param certificates  服务器证书，.cer后缀
     * @param bksFile       客户端证书，BKS格式证书文件
     * @param password      客户端证书密码
     * @return
     */
    public OkHttpClient.Builder supportHttps(InputStream[] certificates, InputStream bksFile, String password){
        return this.supportHttps(certificates, bksFile, password, true);
    }

    /**
     * 开启https支持
     * 信任所有服务器证书：参数为 null, null, null；此模式下，通信过程中的数据仍然加密，但有中间人攻击风险
     * 单向认证模式：第一个参数不为空，第二、三个参数为null即可
     * 双向认证模式：前三个参数都不能为空
     * @param certificates  服务器证书，.cer后缀
     * @param bksFile       客户端证书，BKS格式证书文件
     * @param password      客户端证书密码
     * @param verifyHostname     是否校验服务器域名
     * @return
     */
    public OkHttpClient.Builder supportHttps(InputStream[] certificates, InputStream bksFile, String password, boolean verifyHostname){
        this.okHttpBuilder = AMOKHttpManager.getInstance().supportHttps(certificates, bksFile, password, verifyHostname);
        return this.okHttpBuilder;
    }

    /**
     * 支持Rxjava
     */
    public void addRxjavaConverterFactory(){
        this.retrofitBuilder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    /**
     * 构建 retrofit 对象，在设置完后构建
     */
    public void build(){
        OkHttpClient okHttpClient = AMOKHttpManager.getInstance().getBuilder().build();
        AMRetrofitManager.getInstance().setHttpClient(okHttpClient);
        this.retrofit =  AMRetrofitManager.getInstance().buildRetrofit();
        registerApi(CommonApi.class);
    }

    /**
     * 获取 retrofit 客户端
     * @return
     */
    public Retrofit getRetrofit(){
        if(retrofit != null)
            return retrofit;
        throw new RuntimeException("Retrofit was not be inited!");
    }


    /**
     * 重置Retrofit客户端(当更改到Retrofit客户端配置时需要重置)
     * @param retrofit
     */
    public void resetRetrofit(Retrofit retrofit){
        AMRetrofitManager.getInstance().setRetrofit(retrofit);
        this.retrofit = retrofit;

    }

    /**
     * 重置Retrofit客户端
     * @param baseUrl
     */
    public void resetRetrofit(String baseUrl){
        AMRetrofitManager.getInstance().setRetrofit(baseUrl);
    }


    /**
     * 使用前注册服务API接口类
     * @param clazz
     */
    public void registerApi(Class clazz){
    //    if(!ApiManager.getInstance().isContainsApi(clazz)) {
            ApiManager.getInstance().registerApi(clazz);
      //  }
    }

    /**
     * 检测是否已经注册该接口服务了
     * @param clazz
     * @return
     */
    public boolean isContainsApi(Class clazz){
        return ApiManager.getInstance().isContainsApi(clazz);
    }

    /**
     * 获取已经注册的服务接口类
     * @param clazz
     * @return
     */
    public Object getServiceApi(Class clazz){
        return ApiManager.getInstance().getServiceApi(clazz);
    }

    /**
     * 去掉注册API接口
     * @param clazz
     */
    public void unregisterApi(Class clazz){
        ApiManager.getInstance().unregisterApi(clazz);
    }

    /**
     * 清空API集合
     */
    public void clearApi(){
        ApiManager.getInstance().clearApi();
    }

    /**
     * 销毁当前网络管理对象
     */
    public void destory(){
        clearApi();
        //okhttp网络请求管理对象
        AMOKHttpManager.getInstance().clearInstance();
        //retrofit客户端对象
        AMRetrofitManager.getInstance().clearInstance();
        //api管理对象
        ApiManager.getInstance().clearInstance();
    }

}

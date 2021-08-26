package com.augurit.am.fw.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Retrofit管理类
 * Created by GuoKunhu on 2016-07-04.
 */
public class AMRetrofitManager {
    //单例对象
    private static AMRetrofitManager instance;

    //Retrofit对象
    private Retrofit retrofit;

    //Retrofit构建对象
    private Retrofit.Builder builder;

    //初始化提供默认服务器URL前缀，后续操作可覆盖其值
    private static String baseUrl;

    /**
     * 构造函数
     * @param baseUrl restful请求基本URL
     */
    private AMRetrofitManager(String baseUrl){
        AMRetrofitManager.baseUrl = baseUrl;
        builder = new Retrofit.Builder();

    }

    /**
     * 获取Retrofit构造对象
     * @return
     */
    public Retrofit.Builder getBuilder(){
        if(builder != null)
            return builder;
        return null;
    }

    /**
     * 重置Retrofit构建对象
     * @param builder
     */
    public void setBuilder(Retrofit.Builder builder){
        this.builder =builder;
    }

    /**
     * 设置Retrofit网络传输引擎OKHttpClient对象
     * 由OKHttpClient完成网络数据交换的过程
     * @param client
     * @return
     */
    public Retrofit.Builder setHttpClient(OkHttpClient client){
        return builder.client(client);
    }

    /**
     * 完成Retrofit对象的构建
     * @return
     */
    public Retrofit buildRetrofit(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        this.retrofit = builder.baseUrl(AMRetrofitManager.baseUrl)
                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                               .addConverterFactory(GsonConverterFactory.create(gson))
                               .build();

        return this.retrofit;
    }
    /**
     * 获取当前对象
     * @param baseUrl 第一次获取需要传参数 baseUrl restful请求基本URL
     * @return
     */
    public static AMRetrofitManager getInstance(String baseUrl){
        if(instance == null || !AMRetrofitManager.baseUrl.equals(baseUrl) ){
            synchronized (AMRetrofitManager.class){
                if(instance == null || !AMRetrofitManager.baseUrl.equals(baseUrl) ){
                    instance = new AMRetrofitManager(baseUrl);
                }
            }
        }
        return instance;
    }

    /**
     * 获取当前 AMRetrofitManager 对象
     * @return
     */
    public static AMRetrofitManager getInstance(){
        if(instance != null){
            return instance;
        }
        throw new RuntimeException("retrofit was not inited before!");
    }

    /**
     * 获取当前 retrofit客户端
     * @return
     */
    public Retrofit getRetrofit(){
        return retrofit;
    }

    /**
     * 重置当前 retrofit客户端
     * @param retrofit
     */
    public void setRetrofit(Retrofit retrofit){
        this.retrofit = retrofit;
    }

    /**
     * 重置当前 retrofit客户端
     * @param baseUrl
     */
    public void setRetrofit(String baseUrl){
        instance = new AMRetrofitManager(baseUrl);
    }

    /**
     * 销毁当前对象
     */
    public void clearInstance(){
        instance = null;
    }

}

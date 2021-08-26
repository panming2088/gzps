package com.augurit.agmobile.gzps.doorno.utils;


import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xiaoyu on 2017/5/27.
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 */


public class ReHttpUtils {
//    private static String baseUrl = "http://139.159.243.185:8080/";

    private static ReHttpUtils reHttpUtils;

    private ReHttpUtils() {
    }

//    public static String getBaseUrl() {
//        return baseUrl;
//    }

    public synchronized static ReHttpUtils instans() {
        if (reHttpUtils == null)
            reHttpUtils = new ReHttpUtils();
        return reHttpUtils;
    }

    /**
     * 也可以这里全局重新指定
     *
     * @param baseUrl
     */
//    public static void initRetro(String baseUrl) {
//        ReHttpUtils.baseUrl = baseUrl;
//    }
    public Retrofit creatRetrofit(String baseUrl, OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //增加返回值为String的支持
//                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    public OkHttpClient getMyclient(Interceptor interceptor) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor)
                .build();
        return client;
    }

//    public Subscription httpRequestMain(HttpSubCribe frext) {
//        Retrofit retrofit = creatRetrofit(RequestConstant.HTTP_REQUEST + LoginConstant.GZPS_AGSUPPORT + RequestConstant.URL_DIVIDER, getMyclient(FactoryInterceptor.getCYTDinterc()));
//        return frext.getObservable(retrofit.create(MyRetroService.class)).subscribe(frext);
//    }

//    public Subscription httpRequestLife(LifecycleProvider lifecycleProvider, HttpSubCribe frext) {
//        Retrofit retrofit = creatRetrofit(RequestConstant.HTTP_REQUEST + LoginConstant.GZPSH_AGSUPPORT + RequestConstant.URL_DIVIDER, getMyclient(FactoryInterceptor.getCYTDinterc()));
//        return frext.getObservable(retrofit.create(MyRetroService.class))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(lifecycleProvider.bindToLifecycle())
//                .subscribe(frext);
//    }

//    public Subscription httpRequest(HttpSubCribe frext) {
////        Retrofit retrofit = creatRetrofit(PortSelectUtil.getAgSupportPortBaseURL(BaseApplication.application), getMyclient(Interceptor.Chain()));
////        return frext.getObservable(retrofit.create(MyRetroService.class))
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(frext);
//    }

   /* Class<MyRetroService> service = MyRetroService.class;
    private <T> T create(Retrofit retrofit, Class<T> service) {
        T t = service.newInstance();
        return retrofit.create(service);
    }*/
}

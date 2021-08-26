package com.augurit.am.fw.net.interceptor;

import android.text.TextUtils;


import com.augurit.am.fw.net.util.NetworkUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络拦截器
 *
 * 无网读缓存，有网根据过期时间重新请求
 * Created by GuoKunHu on 2016-07-14.
 */
public class AMTimeCacheInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        LogUtil.i("request:"+request);

       // Response response = chain.proceed(request);
        //System.out.println("response:"+response);

       //无网的时候强制读缓存
        LogUtil.i("111111111111111111111111 network!!");
        if(!NetworkUtil.isNetworkAvailable()){
            LogUtil.w("no network !!!");
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);

        if(NetworkUtil.isNetworkAvailable()){
            //有网的时候读接口上的@Headers里的配置,或者默认配置为120秒
            String cacheControl = request.cacheControl().toString();
            int maxAge = 120;
            if(TextUtils.isEmpty(cacheControl)){
                cacheControl = "public,max-age="+maxAge;
            }
            ///覆盖服务器响应头的Cache-Control,用我们自己的,因为服务器响应回来的可能不支持缓存
            return response.newBuilder().
                    header("Cache-Control", cacheControl).
                    removeHeader("Pragma").
                    build();
        }else {
            LogUtil.w("no network22222 !!!");
            //无网
            int maxStale = 60 * 60 * 24 * 28;
            return response.newBuilder()
                    .header("Cache-Control","public,only-if-cached,max-stale="+maxStale)
                    .removeHeader("Pragma")
                    .build();
        }



/*
        String cacheControl = request.cacheControl().toString();
        int maxAge = 120;
        if(TextUtils.isEmpty(cacheControl)){
            cacheControl = "public,max-age="+maxAge;
        }

        return response.newBuilder().
                header("Cache-Control", cacheControl).
                removeHeader("Pragma").
                build();
*/
        /*
        Request request = chain.request();
        //获取retrofit @headers里面的参数，参数可以自己定义，在本例我自己定义的是cache，跟@headers里面对应就可以了
        String cache = chain.request().header("cache");
        Response originalResponsen= chain.proceed(request);
        String cacheControl = originalResponsen.header("Cache-Control");
        //如果cacheControl为空，就让他TIMEOUT_CONNECT秒的缓存，本例是5秒，方便观察。注意这里的cacheControl是服务器返回的
        if(cacheControl == null){
            //如果cache没值，缓存时间为TIMEOUT_CONNECT，有的话就为cache的值
            if(cache == null || "".equals(cache)){
                cache = 60 +"";
            }
            originalResponsen = originalResponsen.newBuilder()
                    .header("Cache-Control","public,max-age="+cache)
                    .build();
            return originalResponsen;
        }else{
            return originalResponsen;
        }
        */
/*
        String uid = "0";

        Request request = chain.request();
        Response originalResponse = chain.proceed(request);

        String serverCache = originalResponse.header("Cache-Control");
        if (TextUtils.isEmpty(serverCache)) {
            // 如果服务端设置相应的缓存策略那么遵从服务端的不做修改

            String cacheControl = request.cacheControl().toString();
            Response res = originalResponse.newBuilder()
                    .addHeader("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
            return res;
        } else {
            return originalResponse;
        }
        */
    }

}

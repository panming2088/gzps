package com.augurit.am.fw.net.interceptor;



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
 * 设置离线则从缓存上获取数据，在线就从网上获取数据
 * Created by GuoKunHu on 2016-07-14.
 */
public class AMSimpleCacheInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        //Request 拦截操作
        Request request = chain.request();
        //网络出问题，则强制从缓存中读取
        if(!NetworkUtil.isNetworkAvailable()){
            request =request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            LogUtil.w("no network!");
        }

        //设置Response
        Response response = chain.proceed(request);
        //有网络时
        if(NetworkUtil.isNetworkAvailable()){
            //有网络时设置缓存过期时间为 0
            int maxAge = 0*60;
            response.newBuilder()
                    .header("Cache-Control","public,max-age="+maxAge)
                    //清除头信息，因为服务器如果不支持缓存设置，会返回一些干扰信息，不清除的话下面无法生效
                    .removeHeader("Pragma")
                    .build();
        }else {
            //无网络时,设置过期时间为4周
            int maxStale = 60 * 60 *24 *28;
            response.newBuilder()
                    .header("Cache-Control","public,only-if-cached,max-stale="+maxStale)
                    .removeHeader("Pragma")
                    .build();

        }
        return response;
    }
}

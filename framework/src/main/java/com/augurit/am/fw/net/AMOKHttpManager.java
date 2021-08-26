package com.augurit.am.fw.net;

import android.content.Context;


import com.augurit.am.fw.net.cookie.ClearableCookieJar;
import com.augurit.am.fw.net.cookie.PersistentCookieJar;
import com.augurit.am.fw.net.cookie.cache.SetCookieCache;
import com.augurit.am.fw.net.cookie.persistence.SharedPrefsCookiePersistor;
import com.augurit.am.fw.net.interceptor.AMSimpleCacheInterceptor;
import com.augurit.am.fw.net.interceptor.AMTimeCacheInterceptor;
import com.augurit.am.fw.net.progress.AMProgressBean;
import com.augurit.am.fw.net.progress.AMProgressHandler;
import com.augurit.am.fw.net.progress.AMProgressListener;
import com.augurit.am.fw.net.progress.AMProgressResponseBody;
import com.augurit.am.fw.net.util.HttpsUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * OKHttp管理类
 * Created by GuoKunHu on 2016-07-04.
 */
public class AMOKHttpManager {
    //单例对象
    private static AMOKHttpManager instance;
    //OkHttpClient对象
    private OkHttpClient okHttpClient;
    //OkHttpClient构建器
    private OkHttpClient.Builder builder;

    //默认读数据时间
    private static final int TIMEOUT_READ = 25;

    //默认连接时间
    private static final int TIMEOUT_CONNECTION = 25;

    //默认缓存文件夹
    private static final String DEFAULT_CACHE_DIR = "response";

    //默认缓存大小 10M
    private static final int DEFAULT_CACHE_SIZE = 10;


    public AMOKHttpManager(){
        builder = new OkHttpClient.Builder();
        instance = this;
    }

    /**
     * 获取单例对象
     * @return
     */
    public static AMOKHttpManager getInstance(){
        if(instance == null){
            synchronized (AMOKHttpManager.class){
                if(instance == null){
                    instance = new AMOKHttpManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取OkHttpClient 构建者
     * @return
     */
    public OkHttpClient.Builder getBuilder(){
        return builder;
    }

    /**
     * 开启日志打印
     * @return
     */
    public OkHttpClient.Builder addLogPrint(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //设置dubug Log 模式
        builder.addInterceptor(httpLoggingInterceptor);
        return builder;
    }

    /**
     * 设置网络读时间
     * @param readTime
     * @return
     */
    public OkHttpClient.Builder setReadTime(int readTime){
        builder.readTimeout(readTime, TimeUnit.SECONDS);
        return builder;
    }


    /**
     * 设置网络写时间
     * @param writeTime
     * @return
     */
    public OkHttpClient.Builder setWriteTime(int writeTime){
        builder.writeTimeout(writeTime, TimeUnit.SECONDS);
        return builder;
    }

    /**
     * 设置网络连接时间
     * @param connectTime
     * @return
     */
    public OkHttpClient.Builder setConnectTime(int connectTime){
        builder.connectTimeout(connectTime, TimeUnit.SECONDS);
        return builder;
    }

    /**
     * 设置是否失败重连
     * @param isRetry
     * @return
     */
    public OkHttpClient.Builder setRetryOnConnectionFailure(boolean isRetry){
        builder.retryOnConnectionFailure(isRetry);
        return builder;
    }

    /**
     * 设置可以带时间限制的缓存
     * 在规定的时间内，有网没网都是先从缓存中获取数据
     * @param context
     * @param cacheSize
     * @param cachePath
     * @param cacheDir
     * @return
     */
    public OkHttpClient.Builder setTimeCache(Context context,int cacheSize,String cachePath,String cacheDir){
        if(cachePath == null){
           cachePath = context.getCacheDir().getPath();
        }
        if(cacheDir == null){
            cacheDir = DEFAULT_CACHE_DIR;
        }
        //设置缓存路径
        File httpCacheDirectory = new File(cachePath,cacheDir);
        //设置缓存大小
        Cache cache = new Cache(httpCacheDirectory,cacheSize*1024*1024);

     //   builder.addNetworkInterceptor(new AMTimeCacheInterceptor()).cache(cache);
     //   builder.addInterceptor(new AMTimeCacheInterceptor()).cache(cache);
        builder.addInterceptor(new AMTimeCacheInterceptor())
                .addNetworkInterceptor(new AMTimeCacheInterceptor())
                .cache(cache);


        return builder;
    }

    public OkHttpClient.Builder setTimeCache(Context context,int cacheSize){
       return setTimeCache(context, cacheSize, null, null);
    }

    public OkHttpClient.Builder setTimeCache(Context context){
        return setTimeCache(context, DEFAULT_CACHE_SIZE, null, null);
    }

    /**
     * 设置简单缓存，不带时间限制的，有网络则直接从网络上获取数据，
     * 没网络则尝试从缓存中获取数据
     * @param context
     * @param cacheSize
     * @param cachePath
     * @param cacheDir
     * @return
     */
    public OkHttpClient.Builder setSimpleCache(Context context,int cacheSize,String cachePath,String cacheDir){
        if(cachePath == null){
            cachePath = context.getCacheDir().getPath();
        }
        if(cacheDir == null){
            cacheDir = DEFAULT_CACHE_DIR;
        }
        //设置缓存路径
        File httpCacheDirectory = new File(cachePath,cacheDir);
        //设置缓存大小
        Cache cache = new Cache(httpCacheDirectory,cacheSize*1024*1024);

        builder.addInterceptor(new AMSimpleCacheInterceptor()).cache(cache);
        return builder;
    }

    public OkHttpClient.Builder setSimpleCache(Context context){
        return setSimpleCache(context,DEFAULT_CACHE_SIZE,null,null);
    }

    public OkHttpClient.Builder setSimpleCache(Context context,int cacheSize){
        return setSimpleCache(context,cacheSize,null,null);
    }

    /**
     * 设置cookie缓存管理和 SharedPreference 持久化存储管理
     * @param context
     * @return
     */
    public OkHttpClient.Builder setCookie(Context context){
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        builder.cookieJar(cookieJar);
        return builder;
    }

    /**
     * 证书锁定 使用它来约束哪些认证机构被信任
     * @param params ("YOU API.com", "sha1/DmxUShsZuNiqPQsX2Oi9uv2sCnw=")
     * @return
     */
    public OkHttpClient.Builder setCertificatePinner(Map<String,String> params){
        CertificatePinner.Builder certBuilder = new CertificatePinner.Builder();
        for(Map.Entry<String,String> entry : params.entrySet()){
            certBuilder = certBuilder.add(entry.getKey(),entry.getValue());
        }
        builder.certificatePinner(certBuilder.build());
        return builder;
    }

    /**
     * 添加进度
     * @param AMProgressHandler
     * @return
     */
    public OkHttpClient.Builder addProgress(final AMProgressHandler AMProgressHandler){
        final AMProgressBean AMProgressBean = new AMProgressBean();
        final AMProgressListener AMProgressListener = new AMProgressListener() {
            //该方法在子线程中运行
            @Override
            public void onProgress(long progress, long total, boolean done) {
                AMProgressBean.setBytesRead(progress);
                AMProgressBean.setContentLength(total);
                AMProgressBean.setDone(done);
                AMProgressHandler.sendMessage(AMProgressBean);

            }
        };

        //添加拦截器，自定义ResponseBody 添加下载进度
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(new AMProgressResponseBody(originalResponse.body(), AMProgressListener)).build();
            }
        });

        return builder;
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
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(certificates, bksFile, password);
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        if(!verifyHostname){
            //不校验服务器域名
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }
        return builder;
    }


    /**
     * 支持https (加密和普通http客户端请求支持https一样)
     * 调用时把服务器生成的.cer证书放到raw目录下，然后调用 getSSLSocketFactory_Certificate(context,"BKS", R.raw.XXX);
     */
    @Deprecated
    public void supportHttps(Context context,int resId){
        try {
            SSLSocketFactory sslSocketFactory = getSSLSocketFactory_Certificate(context, "BKS", resId);
            if(okHttpClient != null){
            //    okHttpClient.setSslSocketFactory(sslSocketFactory);


            }
        }catch (Exception e){

        }

    }

    @Deprecated
    private static SSLSocketFactory getSSLSocketFactory_Certificate(Context context, String keyStoreType,int keystoreResId)
    throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = context.getResources().openRawResource(keystoreResId);
        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        if(keyStoreType ==null|| keyStoreType.length() ==0) {
            keyStoreType = KeyStore.getDefaultType();
        }
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null,null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        TrustManager[] wrappedTrustManagers =getWrappedTrustManagers(tmf.getTrustManagers());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext.getSocketFactory();

    }

    private static TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return originalTrustManager.getAcceptedIssuers();
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    try{
                        originalTrustManager.checkClientTrusted(certs, authType);
                    }catch(CertificateException e) {
                        e.printStackTrace();
                    }
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    try{
                        originalTrustManager.checkServerTrusted(certs, authType);
                    }catch(CertificateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }


    /**
     * 构建OkHttpClient的终点，返回构建好的OkHttpclient对象
     * @return
     */
    public OkHttpClient build(){
        this.okHttpClient = builder.build();
        return this.okHttpClient;
    }



    /**
     * 获取当前okHttpClient
     * @return
     */
    public OkHttpClient getOkHttpClient(){
        return okHttpClient;
    }

    /**
     * 设置当前okHttpClient
     * @param client
     */
    public void setOkHttpClient(OkHttpClient client){
        okHttpClient = client;
    }

    /**
     * 销毁当前对象
     */
    public void clearInstance(){
        instance = null;
    }
}

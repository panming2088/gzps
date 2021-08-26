package com.augurit.am.fw.net.cookie;

import com.augurit.am.fw.net.cookie.cache.CookieCache;
import com.augurit.am.fw.net.cookie.persistence.CookiePersistor;
import com.augurit.am.fw.utils.log.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Cookie存储类，通过内存存储接口和持久化接口来提供服务
 */
public class PersistentCookieJar implements ClearableCookieJar {

    private CookieCache cache;
    private CookiePersistor persistor;

    public PersistentCookieJar(CookieCache cache, CookiePersistor persistor) {
        this.cache = cache;
        this.persistor = persistor;

        this.cache.addAll(persistor.loadAll());
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    //从网络请求响应中获取 Cookie
    @Override
    synchronized public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        LogUtil.i("0000000000 cookies:" + cookies.toString());

        cache.addAll(cookies);
        persistor.saveAll(cookies);
    }

    //为网络请求加载上 cookie
    @Override
    synchronized public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> removedCookies = new ArrayList<>();
        List<Cookie> validCookies = new ArrayList<>();

        for (Iterator<Cookie> it = cache.iterator(); it.hasNext(); ) {
            Cookie currentCookie = it.next();

            if (isCookieExpired(currentCookie)) {
                removedCookies.add(currentCookie);
                it.remove();

            } else if (currentCookie.matches(url)) {
                validCookies.add(currentCookie);
            }
        }

        persistor.removeAll(removedCookies);

        return validCookies;
    }

    synchronized public void clear() {
        cache.clear();
        persistor.clear();
    }
}



package com.augurit.am.fw.net.cookie.persistence;

import java.util.Collection;
import java.util.List;

import okhttp3.Cookie;

/**
 * cookie 持久化接口 ,提供cookie 持久化接口
 */
public interface CookiePersistor {

    List<Cookie> loadAll();

    /**
     * Persist all cookies, existing cookies will be overwritten.
     *
     * @param cookies cookies persist
     */
    void saveAll(Collection<Cookie> cookies);

    /**
     * Removes indicated cookies from persistence.
     *
     * @param cookies cookies to remove from persistence
     */
    void removeAll(Collection<Cookie> cookies);

    /**
     * Clear all cookies from persistence.
     */
    void clear();

}

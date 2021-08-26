

package com.augurit.am.fw.net.cookie.cache;

import java.util.Collection;

import okhttp3.Cookie;

/**
 * cookie内存存储接口
 */
public interface CookieCache extends Iterable<Cookie> {

    /**
     * Add all the new cookies to the session, existing cookies will be overwritten.
     *
     * @param cookies
     */
    void addAll(Collection<Cookie> cookies);

    /**
     * Clear all the cookies from the session.
     */
    void clear();
}

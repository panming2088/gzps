package com.augurit.am.fw.utils;

/**
 * Utilities for formating url
 * Created by ac on 2016-07-26.
 */
public final class UrlUtil {
    private UrlUtil() {
    }

    /**
     * To judge whether there is "/" between two urls before combining them .
     *
     * @param url     behind urlBase
     * @param urlBase In front of url
     * @return
     */
    public static String buildURL(String url, String urlBase) {
        url = url.trim();
        if (url.indexOf("://") == -1) {
            url = urlBase
                    + (urlBase.endsWith("/") || url.startsWith("/") ? url : "/"
                    + url);
        } else {
            throw new IllegalArgumentException("Url '" + url
                    + "' should not contain '://'");
        }

        return url;
    }
}

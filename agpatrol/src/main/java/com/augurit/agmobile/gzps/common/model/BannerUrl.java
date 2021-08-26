package com.augurit.agmobile.gzps.common.model;

/**
 * Created by long on 2018/1/11.
 */

public class BannerUrl {


    public static final String LOGIN_BG = "0";

    private String url;
    /**
     * 0 是app启动图片  1是顶部图片
     */
    private String type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

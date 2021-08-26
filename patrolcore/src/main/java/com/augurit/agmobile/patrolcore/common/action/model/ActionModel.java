package com.augurit.agmobile.patrolcore.common.action.model;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.realm.RealmObject;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmobile.common
 * @createTime 创建时间 ：2017-06-12
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-06-12
 * @modifyMemo 修改备注：
 */

public class ActionModel extends RealmObject {

    @StringDef({ONLINE, OFFLINE, WEBVIEW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MenuType {
    }

    public static final String ONLINE = "1"; //服务
    public static final String OFFLINE = "2";  //单机
    public static final String WEBVIEW = "3";  //网页


    private String featurecode;// 功能类型编码
    private String actionno; //功能编号
    private String actionname; //功能名称
    private String ipadurl;
    private String iconurl; // 功能图标url
    private int priority; //排序优先级，app在排序时分两步，先根据是否折叠到更多将功能菜单分为两部分，再根据priority分别对它们进行排序
                            //越小越排前
    private String userId;
    @MenuType
    private String menuproxy; //菜单类型
    private String proxyurl; //当菜单类型为使用“网页”加载时的URL

    public String getMenuproxy() {
        return menuproxy;
    }

    public void setMenuproxy(String menuproxy) {
        this.menuproxy = menuproxy;
    }

    public String getProxyurl() {
        return proxyurl;
    }

    public void setProxyurl(String proxyurl) {
        this.proxyurl = proxyurl;
    }

    public String getActionNo() {
        return actionno;
    }

    public void setActionNo(String actionNo) {
        this.actionno = actionNo;
    }

    public String getFeaturecode() {
        return featurecode;
    }

    public void setFeaturecode(String featurecode) {
        this.featurecode = featurecode;
    }

    public String getActionname() {
        return actionname;
    }

    public void setActionname(String actionname) {
        this.actionname = actionname;
    }

    public String getIpadurl() {
        return ipadurl;
    }

    public void setIpadurl(String ipadurl) {
        this.ipadurl = ipadurl;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

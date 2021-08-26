package com.augurit.agmobile.gzps.common.model;

/**
 * Created by liangsh on 2017/11/10.
 */

public class OUser {

    private long userId;
    private String loginName;
    private String userName;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

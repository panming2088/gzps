package com.augurit.am.cmpt.login.model;

import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.annotation.Table;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;

import java.util.Date;

/**
 * 保存至本地的User对象
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.login.model
 * @createTime 创建时间 ：2016-11-18 16:48
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2016-11-18 16:48
 */
@Table("LocalUser")
public class LocalUser {

    @PrimaryKey(AssignType.BY_MYSELF)
    private String id;     // 用户ID
    private User user;  // 用户
    private boolean showInHistory;  // 是否显示在历史中（自动完成提示列表）
    private boolean isFingerprint;  // 是否可指纹登录
    private Date loginTime;     // 登录时间
    private boolean isFirstLoginTodayOnline;  // 今日是否第一次在线登录
    private boolean isLoginOffline;     // 是否离线登录

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isShowInHistory() {
        return showInHistory;
    }

    public void setShowInHistory(boolean showInHistory) {
        this.showInHistory = showInHistory;
    }

    public boolean isFingerprint() {
        return isFingerprint;
    }

    public void setFingerprint(boolean fingerprint) {
        isFingerprint = fingerprint;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public boolean isFirstLoginTodayOnline() {
        return isFirstLoginTodayOnline;
    }

    public void setFirstLoginTodayOnline(boolean firstLoginToday) {
        isFirstLoginTodayOnline = firstLoginToday;
    }

    public boolean isLoginOffline() {
        return isLoginOffline;
    }

    public void setLoginOffline(boolean loginOffline) {
        isLoginOffline = loginOffline;
    }
}

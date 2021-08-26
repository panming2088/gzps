package com.augurit.agmobile.gzps.im.bean;

import java.io.Serializable;

/**
 * @author : taoerxiang
 * @data : 2017-11-12  18:03
 * @des :
 */

public class FriendItem implements Serializable {
    private String id;
    private String isActive;
    private String isActiveCn;
    private String loginName;
    private String orgCode;
    private String orgId;
    private String orgName;
    private String orgPath;
    private String orgUserId;
    private String orgUserIds;
    private String password;
    private String phone;
    private String portraitUri;
    private String StringroleId;
    private String title;
    private String token;
    private String userName;
    private String userRoleId;

    public FriendItem(String id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsActiveCn() {
        return isActiveCn;
    }

    public void setIsActiveCn(String isActiveCn) {
        this.isActiveCn = isActiveCn;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgPath() {
        return orgPath;
    }

    public void setOrgPath(String orgPath) {
        this.orgPath = orgPath;
    }

    public String getOrgUserId() {
        return orgUserId;
    }

    public void setOrgUserId(String orgUserId) {
        this.orgUserId = orgUserId;
    }

    public String getOrgUserIds() {
        return orgUserIds;
    }

    public void setOrgUserIds(String orgUserIds) {
        this.orgUserIds = orgUserIds;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public String getStringroleId() {
        return StringroleId;
    }

    public void setStringroleId(String stringroleId) {
        StringroleId = stringroleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }
}

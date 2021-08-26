package com.augurit.am.cmpt.login.model;

import java.io.Serializable;

/**
 * 包名：com.augurit.am.cmpt.login.model
 * 文件描述：用户类
 * 创建人：luobiao
 * 创建时间：2016-09-02 17:18
 * 修改人：luobiao
 * 修改时间：2016-09-02 17:18
 * 修改备注：
 */
public class User implements Cloneable, Serializable {
    //部门
    private Department department;
    //区域
    private String district;
    //email
    private String email;
    //用户id
    private String id;
    //是否激活状态
    private String isActive;
    //登录名
    private String loginName;
    //职务
    private String title;
    //电话号码
    private String phone;
    //办公号码
    private String officeTel;
    //密码
    private String password;
    //是否标记
    private String remark;
    //性别
    private String sex;
    //用户code
    private String userCode;
    //用户名
    private String userName;
    //token 融云im身份令牌
    private String token;
    //org
    private String orgName;

    //此字段只有在用户是“花都净水公司”时才返回值“花都净水”，其余情况皆为空字符串
    private String orgPath;

    //多角色名称（逗号分隔）
    private String roleName;
    //角色编码
    private String roleCode;
    //部门编码
    private String orgCode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User() {
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOfficeTel() {
        return officeTel;
    }

    public void setOfficeTel(String officeTel) {
        this.officeTel = officeTel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

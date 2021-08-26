package com.augurit.agmobile.gzps.uploadfacility.model;

import java.io.Serializable;

/**
 * @author 创建人 //xuciluan
 * @version 1.0
 * @package 包名 //com.augurit.agmobile.gzpssb.pshdoorno.doorlist.mode
 * @createTime 创建时间 //2018-04-23
 * @modifyBy 修改人 //xuciluan
 * @modifyTime 修改时间 //2018-04-23
 * @modifyMemo 修改备注//
 */

public class UploadDoorNoDetailBean implements Serializable{

    private String sGuid;//主键
    private String ssxzqh;//行政区划（eg:天河区）
    private String ssxzjd;//所属镇街（eg:五山街）
    private String sssqcjwh;//所属社区、村（居）委会代码（eg:瘦狗岭社区）
    private String ssjlx;//所属街路巷代码（eg:瘦狗岭路）
    private String  mpdzmc;//门牌地址名称(eg:555号)
    private String mpwzhm;//门牌完整号码（eg:瘦狗岭路555号）后台自己拼凑
    private String dzdm;//地址代码（自己生成）
    private String dzqc;//DZQC:地址全称（eg: 广东省广州市越秀区北京路200号）后台自己拼凑
    private double zxjd;//中心经度
    private double zxwd;//中心纬度
    private int markPersonId;//上报人id
    private String markPerson;//上报人名称
    private String markTime;//上报时间
    private String updateTime;//更新时间
    private String description;//上报描述
    private int state;//审核状态（1新增；2；经审核；3存在疑问；）
    private String checkPersonId;//审批人id
    private String checkPerson;//审批人姓名
    private String checkTime;//审批时间
    private String checkDescription;//审批备注
    private String loginName;//登录名
    private String directOrgId;//维管单位id
    private String directOrgName;//维管单位名称
    private String teamOrgId;//街镇id
    private String teamOrgName;//街镇名称
    private String parentOrgId;//区级机构id
    private String parentOrgName;//区级机构名称
    private int istatue;//:调查状态（（1:未调查、2部分调查、3全部调查））
    private int isExsit;//（0实现存在；1自己新增） 默认为1
    private String objectId;//上报编号

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    private String checkState;//（1新增；2；经审核；3存在疑问；） 默认为1
    public String getsGuid() {
        return sGuid;
    }

    public void setsGuid(String sGuid) {
        this.sGuid = sGuid;
    }


    public String getSsxzqh() {
        return ssxzqh;
    }

    public void setSsxzqh(String ssxzqh) {
        this.ssxzqh = ssxzqh;
    }

    public String getSsxzjd() {
        return ssxzjd;
    }

    public void setSsxzjd(String ssxzjd) {
        this.ssxzjd = ssxzjd;
    }

    public String getSssqcjwh() {
        return sssqcjwh;
    }

    public void setSssqcjwh(String sssqcjwh) {
        this.sssqcjwh = sssqcjwh;
    }

    public String getSsjlx() {
        return ssjlx;
    }

    public void setSsjlx(String ssjlx) {
        this.ssjlx = ssjlx;
    }

    public String getMpdzmc() {
        return mpdzmc;
    }

    public void setMpdzmc(String mpdzmc) {
        this.mpdzmc = mpdzmc;
    }

    public String getMpwzhm() {
        return mpwzhm;
    }

    public void setMpwzhm(String mpwzhm) {
        this.mpwzhm = mpwzhm;
    }

    public String getDzdm() {
        return dzdm;
    }

    public void setDzdm(String dzdm) {
        this.dzdm = dzdm;
    }

    public String getDzqc() {
        return dzqc;
    }

    public void setDzqc(String dzqc) {
        this.dzqc = dzqc;
    }

    public double getZxjd() {
        return zxjd;
    }

    public void setZxjd(double zxjd) {
        this.zxjd = zxjd;
    }

    public double getZxwd() {
        return zxwd;
    }

    public void setZxwd(double zxwd) {
        this.zxwd = zxwd;
    }

    public int getMarkPersonId() {
        return markPersonId;
    }

    public void setMarkPersonId(int markPersonId) {
        this.markPersonId = markPersonId;
    }

    public String getMarkPerson() {
        return markPerson;
    }

    public void setMarkPerson(String markPerson) {
        this.markPerson = markPerson;
    }

    public String getMarkTime() {
        return markTime;
    }

    public void setMarkTime(String markTime) {
        this.markTime = markTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCheckPersonId() {
        return checkPersonId;
    }

    public void setCheckPersonId(String checkPersonId) {
        this.checkPersonId = checkPersonId;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckDescription() {
        return checkDescription;
    }

    public void setCheckDescription(String checkDescription) {
        this.checkDescription = checkDescription;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getDirectOrgId() {
        return directOrgId;
    }

    public void setDirectOrgId(String directOrgId) {
        this.directOrgId = directOrgId;
    }

    public String getDirectOrgName() {
        return directOrgName;
    }

    public void setDirectOrgName(String directOrgName) {
        this.directOrgName = directOrgName;
    }

    public String getTeamOrgId() {
        return teamOrgId;
    }

    public void setTeamOrgId(String teamOrgId) {
        this.teamOrgId = teamOrgId;
    }

    public String getTeamOrgName() {
        return teamOrgName;
    }

    public void setTeamOrgName(String teamOrgName) {
        this.teamOrgName = teamOrgName;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }

    public int getIstatue() {
        return istatue;
    }

    public void setIstatue(int istatue) {
        this.istatue = istatue;
    }

    public int getIsExsit() {
        return isExsit;
    }

    public void setIsExsit(int isExsit) {
        this.isExsit = isExsit;
    }
}

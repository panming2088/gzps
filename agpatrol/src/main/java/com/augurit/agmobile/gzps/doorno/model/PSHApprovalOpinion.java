package com.augurit.agmobile.gzps.doorno.model;

import java.io.Serializable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzpssb.bean
 * @createTime 创建时间 ：2018-05-10
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-05-10
 * @modifyMemo 修改备注：排水户信息审核列表
 */

public class PSHApprovalOpinion implements Serializable{

    public String getCheckDesription() {
        return checkDesription;
    }

    public void setCheckDesription(String checkDesription) {
        this.checkDesription = checkDesription;
    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public String getCheckPersonPhone() {
        return checkPersonPhone;
    }

    public void setCheckPersonPhone(String checkPersonPhone) {
        this.checkPersonPhone = checkPersonPhone;
    }

    public long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(long checkTime) {
        this.checkTime = checkTime;
    }

    private String checkDesription;
    private String checkPerson;
    private String checkState;
    private String checkPersonPhone;
    private long checkTime;
}

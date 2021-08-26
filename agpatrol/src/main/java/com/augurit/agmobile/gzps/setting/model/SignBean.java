package com.augurit.agmobile.gzps.setting.model;

import java.util.List;

/**
 * Created by augur on 17/12/25.
 */

public class SignBean {

    /**
     * monthlySignDate : ["20171121","20171122","20171123"]
     * orgName : 小米
     * orgSeq : 10232.232
     * signerId : adsf
     */

    private String orgName;
    private String orgSeq;
    private String signerId;
    private List<String> monthlySignDate;

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setOrgSeq(String orgSeq) {
        this.orgSeq = orgSeq;
    }

    public void setSignerId(String signerId) {
        this.signerId = signerId;
    }

    public void setMonthlySignDate(List<String> monthlySignDate) {
        this.monthlySignDate = monthlySignDate;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getOrgSeq() {
        return orgSeq;
    }

    public String getSignerId() {
        return signerId;
    }

    public List<String> getMonthlySignDate() {
        return monthlySignDate;
    }
}

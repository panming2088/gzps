package com.augurit.agmobile.gzps.statistic.model;

import java.util.List;

/**
 * Created by luob on 2017/12/26.
 */

public class SignStatisticInfoBean {

    /**
     * childOrgs : [{"orgName":"黄埔","signNumber":0,"signPercentage":0,"total":43},{"orgName":"荔湾","signNumber":0,"signPercentage":0,"total":48},{"orgName":"花都","signNumber":0,"signPercentage":0,"total":104},{"orgName":"越秀","signNumber":0,"signPercentage":0,"total":74},{"orgName":"增城","signNumber":0,"signPercentage":0,"total":44},{"orgName":"从化","signNumber":0,"signPercentage":0,"total":57},{"orgName":"净水公司","signNumber":0,"signPercentage":0,"total":106},{"orgName":"天河","signNumber":0,"signPercentage":0,"total":49},{"orgName":"番禺","signNumber":0,"signPercentage":0,"total":98},{"orgName":"南沙","signNumber":0,"signPercentage":0,"total":36},{"orgName":"白云","signNumber":0,"signPercentage":0,"total":56},{"orgName":"海珠","signNumber":0,"signPercentage":0,"total":62},{"orgName":"市水务局","signNumber":1,"signPercentage":1.2987012987012987,"total":77}]
     * orgName : 全市
     * signDate : 20171227
     * signNumber : 1
     * signPercentage : 0.11723329425556857
     * total : 853
     */

    private String orgName;
    private String signDate;
    private int signNumber;
    private double signPercentage;
    private int total;
    private List<ChildOrgsEntity> childOrgs;

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public void setSignNumber(int signNumber) {
        this.signNumber = signNumber;
    }

    public void setSignPercentage(double signPercentage) {
        this.signPercentage = signPercentage;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setChildOrgs(List<ChildOrgsEntity> childOrgs) {
        this.childOrgs = childOrgs;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getSignDate() {
        return signDate;
    }

    public int getSignNumber() {
        return signNumber;
    }

    public double getSignPercentage() {
        return signPercentage;
    }

    public int getTotal() {
        return total;
    }

    public List<ChildOrgsEntity> getChildOrgs() {
        return childOrgs;
    }

    public static class ChildOrgsEntity {
        /**
         * orgName : 黄埔
         * signNumber : 0
         * signPercentage : 0
         * total : 43
         */

        private String orgName;
        private int signNumber;
        private double signPercentage;
        private int total;

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public void setSignNumber(int signNumber) {
            this.signNumber = signNumber;
        }

        public void setSignPercentage(double signPercentage) {
            this.signPercentage = signPercentage;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getOrgName() {
            return orgName;
        }

        public int getSignNumber() {
            return signNumber;
        }

        public double getSignPercentage() {
            return signPercentage;
        }

        public int getTotal() {
            return total;
        }
    }
}

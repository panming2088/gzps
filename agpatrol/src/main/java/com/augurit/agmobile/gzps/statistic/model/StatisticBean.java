package com.augurit.agmobile.gzps.statistic.model;

import java.util.ArrayList;

/**
 * @author : taoerxiang
 * @data : 2017-11-16  14:26
 * @des :
 */

public class StatisticBean {
    private int code;
    private StatisticInfo data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public StatisticInfo getData() {
        return data;
    }

    public void setData(StatisticInfo data) {
        this.data = data;
    }

    public class StatisticInfo {
        private int total;
        private int install;
        private ArrayList<ChildOrg> child_orgs;

        public ArrayList<ChildOrg> getChild_orgs() {
            return child_orgs;
        }

        public void setChild_orgs(ArrayList<ChildOrg> child_orgs) {
            this.child_orgs = child_orgs;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getInstall() {
            return install;
        }

        public void setInstall(int install) {
            this.install = install;
        }
    }
    public class ChildOrg{
        private String install_percent;
        private String org_name;

        public String getInstall_percent() {
            return install_percent;
        }

        public void setInstall_percent(String install_percent) {
            this.install_percent = install_percent;
        }

        public String getOrg_name() {
            return org_name;
        }

        public void setOrg_name(String org_name) {
            this.org_name = org_name;
        }
    }
}

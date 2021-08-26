package com.augurit.agmobile.gzps.statistic.model;

import java.util.ArrayList;

import static com.augurit.agmobile.gzps.R.id.user_name;

/**
 * @author : taoerxiang
 * @data : 2017-11-20  23:45
 * @des :
 */

public class InstallInfo {
    private InstallData data;
    private int code;

    public InstallData getData() {
        return data;
    }

    public void setData(InstallData data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public class InstallData {
        private ArrayList<InstallUser> users;

        public ArrayList<InstallUser> getUsers() {
            return users;
        }

        public void setUsers(ArrayList<InstallUser> users) {
            this.users = users;
        }
    }
    public class InstallUser{
        private String user_name;
        private String phone;
        private String job;
        private boolean isInstalled;
        private String direc_org;

        public String getDirec_org() {
            return direc_org;
        }

        public void setDirec_org(String direc_org) {
            this.direc_org = direc_org;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public boolean isInstalled() {
            return isInstalled;
        }

        public void setInstalled(boolean installed) {
            isInstalled = installed;
        }
    }

}

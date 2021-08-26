package com.augurit.agmobile.gzps.uploadevent.model;

import java.util.List;

/**
 * Created by long on 2017/11/23.
 */

public class Assigneers {


    /**
     * assigneers : [{"userCode":"USER#1593","userName":"邱俊宇"},{"userCode":"USER#1594","userName":"覃学森"},{"userCode":"USER#1595","userName":"刘植良"},{"userCode":"USER#1596","userName":"陈金生"},{"userCode":"USER#1597","userName":"刘志成"}]
     * activityChineseName : 任务处置
     * activityName : getTask
     */

    private String activityChineseName;   //环节中文名
    private String activityName;          //环节英文名
    private List<Assigneer> assigneers;    //环节参与者

    public String getActivityChineseName() {
        return activityChineseName;
    }

    public void setActivityChineseName(String activityChineseName) {
        this.activityChineseName = activityChineseName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public List<Assigneer> getAssigneers() {
        return assigneers;
    }

    public void setAssigneers(List<Assigneer> assigneers) {
        this.assigneers = assigneers;
    }

    public static class Assigneer {
        /**
         * userCode : USER#1593
         * userName : 邱俊宇
         */

        private String userCode;
        private String userName;
        private boolean isDefault = false;

        public Assigneer() {
        }

        public Assigneer(String userCode, String userName) {
            this.userCode = userCode;
            this.userName = userName;
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

        public boolean isDefault() {
            return isDefault;
        }

        public void setDefault(boolean aDefault) {
            isDefault = aDefault;
        }
    }
}

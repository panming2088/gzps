package com.augurit.agmobile.gzps.im.bean;

import java.util.ArrayList;

/**
 * @author : taoerxiang
 * @data : 2017-11-15  0:42
 * @des :
 */

public class GroupUserInfo {
    private GroupUserBean result;

    public GroupUserBean getResult() {
        return result;
    }

    public void setResult(GroupUserBean result) {
        this.result = result;
    }

    public class GroupUserBean {
        private String groupId;
        private String groupName;
        private ArrayList<FriendItem> userList;

        public ArrayList<FriendItem> getUserList() {
            return userList;
        }

        public void setUserList(ArrayList<FriendItem> userList) {
            this.userList = userList;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }


    }

}

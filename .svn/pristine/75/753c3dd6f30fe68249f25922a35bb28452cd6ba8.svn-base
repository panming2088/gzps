package com.augurit.agmobile.gzps.im.bean;


import java.util.ArrayList;
import java.util.List;

/**
 * 群组信息。
 */
public class GroupInfo {
    private GroupData result;

    public class GroupData {
        private ArrayList<GroupItem> groupList;

        public ArrayList<GroupItem> getGroupList() {
            return groupList;
        }

        public void setGroupList(ArrayList<GroupItem> groupList) {
            this.groupList = groupList;
        }
    }

    public class GroupItem {
        private String groupId;
        private String groupName;
        private int size;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        private List<FriendItem> userList;

        public List<FriendItem> getUserList() {
            return userList;
        }

        public void setUserList(List<FriendItem> userList) {
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

    public GroupData getResult() {
        return result;
    }

    public void setResult(GroupData result) {
        this.result = result;
    }
}

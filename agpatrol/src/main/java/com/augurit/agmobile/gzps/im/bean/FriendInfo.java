package com.augurit.agmobile.gzps.im.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 创建人 ：taoerxiang
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.im.bean
 * @createTime 创建时间 ：2017-11-09
 * @modifyBy 修改人 ：taoerxiang
 * @modifyTime 修改时间 ：2017-11-09
 * @modifyMemo 修改备注：
 */

public class FriendInfo implements Serializable {
    private FriendData result;

    public FriendData getResult() {
        return result;
    }

    public void setResult(FriendData result) {
        this.result = result;
    }

    public class FriendData implements Serializable {
        private int size;
        private ArrayList<FriendItem> userList;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public ArrayList<FriendItem> getUserList() {
            return userList;
        }

        public void setUserList(ArrayList<FriendItem> userList) {
            this.userList = userList;
        }
    }
}

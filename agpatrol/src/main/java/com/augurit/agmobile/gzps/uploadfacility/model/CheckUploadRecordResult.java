package com.augurit.agmobile.gzps.uploadfacility.model;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.model
 * @createTime 创建时间 ：18/1/29
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：18/1/29
 * @modifyMemo 修改备注：
 */

public class CheckUploadRecordResult {

    //电话号码
    private String phone;

    //用户名
    private String userName;

    /**
     * 总共有多少人上报过这个设施
     */
    private int total;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

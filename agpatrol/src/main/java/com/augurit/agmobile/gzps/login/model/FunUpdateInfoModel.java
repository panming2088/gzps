package com.augurit.agmobile.gzps.login.model;

/**
 * 描述：
 *
 * @author 创建人 ：luobiao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.login.model
 * @createTime 创建时间 ：2017/8/10
 * @modifyBy 修改人 ：luobiao
 * @modifyTime 修改时间 ：2017/8/10
 * @modifyMemo 修改备注：
 */

public class FunUpdateInfoModel {
    private String funName; //模块名称
    private String type;//对应唯一id
    private String userId;//用户id
    private String date;//时间

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

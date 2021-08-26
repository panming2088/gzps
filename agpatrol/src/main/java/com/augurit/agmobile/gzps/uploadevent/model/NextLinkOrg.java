package com.augurit.agmobile.gzps.uploadevent.model;

import java.util.List;

/**
 * 下一环节处理人
 *
 * Created by liangsh on 2017/11/14.
 */

public class NextLinkOrg {

    private String code;  //机构编码
    private String name;  //机构名
    private List<Assigneers.Assigneer> userFormList;   //机构下用户

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Assigneers.Assigneer> getUserFormList() {
        return userFormList;
    }

    public void setUserFormList(List<Assigneers.Assigneer> userFormList) {
        this.userFormList = userFormList;
    }
}

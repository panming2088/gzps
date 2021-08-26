package com.augurit.agmobile.gzps.uploadevent.dao;

import com.augurit.agmobile.gzps.uploadevent.model.Person;

import java.util.List;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadevent.dao
 * @createTime 创建时间 ：2017/12/7
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/12/7
 * @modifyMemo 修改备注：
 */

public class GetPersonByOrgApiData {
    private String code;
    private String name;

    private List<Person> userFormList;




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

    public List<Person> getUserFormList() {
        return userFormList;
    }

    public void setUserFormList(List<Person> userFormList) {
        this.userFormList = userFormList;
    }

    /*
    public static class Content{
        private List<Person> userFormList;

        public List<Person> getUserFormList() {
            return userFormList;
        }

        public void setUserFormList(List<Person> userFormList) {
            this.userFormList = userFormList;
        }
    }
    */
}

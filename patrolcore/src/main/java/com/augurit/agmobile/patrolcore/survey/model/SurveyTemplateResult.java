package com.augurit.agmobile.patrolcore.survey.model;


import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 项目列表
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.module
 * @createTime 创建时间 ：17/8/22
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/22
 * @modifyMemo 修改备注：
 */

public class SurveyTemplateResult {

    /**
     * message : 200
     * result : [{"hasProject":"","id":"3a5eec6a-b3a4-465c-9356-fc599cfd35ef","isLast":"1","name":"市政","parentId":"1","xpath":"/1/"},{"hasProject":"","id":"5726d269-c8f9-4492-9fe1-71ff5e7bf6d5","isLast":"1","name":"农业源","parentId":"1","xpath":"/1/"},{"hasProject":"","id":"bd8204d9-5cd6-4263-8739-f43ea71f458a","isLast":"1","name":"工业源","parentId":"1","xpath":"/1/"}]
     * success : true
     */
    private String message;
    private boolean success;
    private RealmList<Template> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Template> getResult() {
        return result;
    }

    public void setResult(RealmList<Template> result) {
        this.result = result;
    }

}



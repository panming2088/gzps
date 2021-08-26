package com.augurit.agmobile.patrolcore.survey.model;

/**
 * 签收任务返回结果
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agsurvey.tasks.module
 * @createTime 创建时间 ：17/8/24
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/24
 * @modifyMemo 修改备注：
 */

public class SignTaskResult {

    private boolean success;

    private String id;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return id;
    }

    public void setMessage(String message) {
        this.id = message;
    }
}

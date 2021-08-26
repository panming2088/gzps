package com.augurit.agmobile.patrolcore.common.table.dao.remote;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model.manager.remote
 * @createTime 创建时间 ：17/3/15
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/15
 * @modifyMemo 修改备注：
 */

public class UploadTableItemResult {
    private String success;
    private String message;
    private String patrolCode;
    private String id; //为多表任务增加字段

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPatrolId() {
        return patrolCode;
    }

    public void setPatrolId(String patrolId) {
        this.patrolCode = patrolId;
    }
}

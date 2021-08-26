package com.augurit.agmobile.patrolcore.common.table.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table.model
 * @createTime 创建时间 ：2017/8/26
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/8/26
 * @modifyMemo 修改备注：
 */

public class ClientTaskRecord extends RealmObject{
    @PrimaryKey
    private String id;
    private String projectId;
    private String projectName;

    private String taskId;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}

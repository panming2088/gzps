package com.augurit.agmobile.patrolcore.survey.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * 多表单模板
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.model
 * @createTime 创建时间 ：17/8/30
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/30
 * @modifyMemo 修改备注：
 */
@Deprecated
public class MultiTableTemplate extends RealmObject {

    private String projectId; //项目id

    private String projectName;//项目名称

    //private List<String> tableIds; //项目下的表集合


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

   /* public List<String> getTableIds() {
        return tableIds;
    }

    public void setTableIds(List<String> tableIds) {
        this.tableIds = tableIds;
    }*/
}

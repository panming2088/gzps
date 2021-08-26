package com.augurit.agmobile.patrolcore.common.table.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 描述：项目实体
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model
 * @createTime 创建时间 ：17/3/21
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/21
 * @modifyMemo 修改备注：
 */

public class Project extends RealmObject{
    @PrimaryKey
    private String id; //projectId
    private String name;//

    /********xcl 2017-08-14 新增字段****************/
    private String dirId;

    private String dirName;

    private String patrolCode;

    private String patrolName;

    private String patrolType;

    private String projectVersion;

    @SerializedName("tableName")
    private String industryTableName; //行业表名

    //2017.8.29 gkh
   /// private List<String>  tableItemList;//该项目表格模板所包含表格项的ID



    /***********************************************/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getPatrolCode() {
        return patrolCode;
    }

    public void setPatrolCode(String patrolCode) {
        this.patrolCode = patrolCode;
    }

    public String getPatrolName() {
        return patrolName;
    }

    public void setPatrolName(String patrolName) {
        this.patrolName = patrolName;
    }

    public String getPatrolType() {
        return patrolType;
    }

    public void setPatrolType(String patrolType) {
        this.patrolType = patrolType;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public String getIndustryTableName() {
        return industryTableName;
    }

    public void setIndustryTableName(String industryTableName) {
        this.industryTableName = industryTableName;
    }


   /* public List<String> getTableItemList() {
        return tableItemList;
    }

    public void setTableItemList(List<String> tableItemList) {
        this.tableItemList = tableItemList;
    }*/
}

package com.augurit.agmobile.patrolcore.survey.model;

import com.augurit.am.fw.db.liteorm.db.annotation.Table;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 单个项目
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.model
 * @createTime 创建时间 ：17/8/30
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/30
 * @modifyMemo 修改备注：
 */
@Table("Template")
public class Template extends RealmObject {
    /**
     * hasProject :
     * id : 3a5eec6a-b3a4-465c-9356-fc599cfd35ef
     * isLast : 1
     * name : 市政
     * parentId : 1
     * xpath : /1/
     */
    private String hasProject;
    @PrimaryKey
    private String id; //dirId
    private String isLast;
    private String name;
    private String parentId;
    private String xpath;


    public String getHasProject() {
        return hasProject;
    }

    public void setHasProject(String hasProject) {
        this.hasProject = hasProject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsLast() {
        return isLast;
    }

    public void setIsLast(String isLast) {
        this.isLast = isLast;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }
}
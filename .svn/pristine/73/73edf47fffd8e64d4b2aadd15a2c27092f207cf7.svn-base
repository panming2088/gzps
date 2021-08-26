package com.augurit.agmobile.patrolcore.common.table.dao.local;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * 描述：本地保存用的编辑上报的表格项
 * 本地保存的为提交的上报实体
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model
 * @createTime 创建时间 ：17/3/17
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/17
 * @modifyMemo 修改备注：
 */

public class LocalTable extends RealmObject {

    @PrimaryKey
    private String key;

    private String id; //projectId

    private long time ;//时间

    private String industryTableName; //xcl 2017-08-14 加入行业表名字段

    private String name; //xcl 8.31 加入表格项名称

    private String projectName;//xcl  8.31

    private String recordId ;// 记录id

    private RealmList<LocalTableItem> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<LocalTableItem> getList() {
        return list;
    }

    public void setList(RealmList<LocalTableItem> list) {
        this.list = list;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustryTableName() {
        return industryTableName;
    }

    public void setIndustryTableName(String industryTableName) {
        this.industryTableName = industryTableName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof LocalTable)){
            return false;
        }
        return this.id == ((LocalTable) obj).getId();
    }
}

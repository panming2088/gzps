package com.augurit.agmobile.gzps.common.search.util.model;

import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;

import java.io.Serializable;
/**
 * 服务端传递过来的任务实体类
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agsurvey.tasks.model
 * @createTime 创建时间 ：17/8/22
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/8/22
 * @modifyMemo 修改备注：
 */

public class SearchHistory implements Serializable {
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id; //任务id  taskId
    private String userId;

    private String key;

    private String typeName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}

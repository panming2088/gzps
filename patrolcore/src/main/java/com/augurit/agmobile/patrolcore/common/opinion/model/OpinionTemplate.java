package com.augurit.agmobile.patrolcore.common.opinion.model;

import android.support.annotation.StringDef;

import com.augurit.agmobile.patrolcore.common.opinion.util.OpinionConstant;
import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 意见模板
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.opinion.model
 * @createTime 创建时间 ：2017-07-07
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-07
 * @modifyMemo 修改备注：
 */

public class OpinionTemplate implements Serializable{

    @PrimaryKey(AssignType.BY_MYSELF)
    private String id;
    private String name;
    private String content;    //模板内容
    private String userId;
    private String projectId;
    private String link;       //流程环节名称
    private @OPINIONAUTH String authorization = OpinionConstant.PRIVATE;   //访问授权：私有、机构、公开
    private long createTime;
    private String addFlag;
    private boolean isUpload = true;

    @StringDef({OpinionConstant.PRIVATE, OpinionConstant.PUBLIC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OPINIONAUTH {}

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(@OPINIONAUTH String authorization) {
        this.authorization = authorization;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getAddFlag() {
        return addFlag;
    }

    public void setAddFlag(String addFlag) {
        this.addFlag = addFlag;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }
}

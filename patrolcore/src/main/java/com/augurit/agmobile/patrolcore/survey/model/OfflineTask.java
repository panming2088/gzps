package com.augurit.agmobile.patrolcore.survey.model;

import com.augurit.am.fw.db.liteorm.db.annotation.PrimaryKey;
import com.augurit.am.fw.db.liteorm.db.annotation.Table;
import com.augurit.am.fw.db.liteorm.db.enums.AssignType;


/**
 * 离线下载过的任务
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agsurvey.tasks.model
 * @createTime 创建时间 ：17/9/22
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/9/22
 * @modifyMemo 修改备注：
 */
@Table("OfflineTask")
public class OfflineTask{

    @PrimaryKey(AssignType.BY_MYSELF)
    private String taskId;

    private Long saveTime ;//离线下载的时间

    public Long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Long saveTime) {
        this.saveTime = saveTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}

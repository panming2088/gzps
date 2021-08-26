package com.augurit.agmobile.patrolcore.survey.view;

import com.augurit.agmobile.patrolcore.survey.model.ServerTableRecord;

/**
 *
 * 新增记录
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.view
 * @createTime 创建时间 ：17/9/5
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/9/5
 * @modifyMemo 修改备注：
 */

public class AddRecordEvent {

    String recordName;
    String recordId;
    String tableId;

    public AddRecordEvent(String recordName, String recordId, String tableId) {
        this.recordName = recordName;
        this.recordId = recordId;
        this.tableId = tableId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}

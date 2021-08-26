package com.augurit.agmobile.patrolcore.common.table.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table.model
 * @createTime 创建时间 ：2017/9/4
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/9/4
 * @modifyMemo 修改备注：
 */

public class TaoFinishedRecord extends RealmObject {
    @PrimaryKey
    private String recordId;

    private String nextStepDirId; //下一个界面的dirid

    public String getNextStepDirId() {
        return nextStepDirId;
    }

    public void setNextStepDirId(String nextStepDirId) {
        this.nextStepDirId = nextStepDirId;
    }

    public String getRecord() {
        return recordId;
    }

    public void setRecord(String record) {
        this.recordId = record;
    }
}

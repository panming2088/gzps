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

public class DongFinishedRecord extends RealmObject{
    @PrimaryKey
    private String recordId;


    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}

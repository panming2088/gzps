package com.augurit.agmobile.patrolcore.common.table.dao.local;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 描述：本地存储的Task
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table.dao.local
 * @createTime 创建时间 ：2017/8/25
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/8/25
 * @modifyMemo 修改备注：
 */

public class LocalTask extends RealmObject {
    @PrimaryKey
    private String key; //唯一键 时间戳

    private String name;

    //该任务相关的本地存储表项内容(每一行)
    private RealmList<LocalTaskRecord> localTaskRecords;

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

    public RealmList<LocalTaskRecord> getLocalTaskRecords() {
        return localTaskRecords;
    }

    public void setLocalTaskRecords(RealmList<LocalTaskRecord> localTaskRecords) {
        this.localTaskRecords = localTaskRecords;
    }
}

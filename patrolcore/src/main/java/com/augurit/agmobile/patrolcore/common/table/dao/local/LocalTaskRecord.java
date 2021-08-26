package com.augurit.agmobile.patrolcore.common.table.dao.local;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 描述：本地存储表项内容(每一行)
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common.table.dao.local
 * @createTime 创建时间 ：2017/8/25
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/8/25
 * @modifyMemo 修改备注：
 */

public class LocalTaskRecord extends RealmObject {
    @PrimaryKey
    private String key; //LocalTable中的key

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

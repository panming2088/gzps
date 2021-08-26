package com.augurit.agmobile.patrolcore.common.table.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model.manager
 * @createTime 创建时间 ：17/3/22
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/22
 * @modifyMemo 修改备注：
 */

public class ProjectTable extends RealmObject{
    @PrimaryKey
    private String id; //projectId

    private RealmList<TableItem> tableItems;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<TableItem> getTableItems() {
        return tableItems;
    }

    public void setTableItems(RealmList<TableItem> tableItems) {
        this.tableItems = tableItems;
    }
}

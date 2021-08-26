package com.augurit.agmobile.patrolcore.common.action.model;


import com.augurit.agmobile.patrolcore.common.table.model.TableItem;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * 功能配置
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.common.action.model
 * @createTime 创建时间 ：2017-06-13
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-06-13
 * @modifyMemo 修改备注：
 */

public class ActionConfigureModel extends RealmObject{

    private String action_code ; //功能类型编码

    private String action_no; //功能编号

    private RealmList<TableItem> operationItems; // 操作项

    public String getAction_no() {
        return action_no;
    }

    public void setAction_no(String action_no) {
        this.action_no = action_no;
    }

    public String getAction_code() {
        return action_code;
    }

    public void setAction_code(String action_code) {
        this.action_code = action_code;
    }

    public List<TableItem> getOperationItems() {
        return operationItems;
    }

    public void setOperationItems(RealmList<TableItem> operationItems) {
        this.operationItems = operationItems;
    }
}

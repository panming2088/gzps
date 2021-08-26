package com.augurit.agmobile.patrolcore.common.action.dao.local;

import com.augurit.agmobile.patrolcore.common.action.model.ActionModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 本地菜单文件管理
 *
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.ammap.proj.dao
 * @createTime 创建时间 ：2016-11-26
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-26
 */

public class ActionDBLogic {

    /**
     * 将表模板数据存入数据库
     *
     * @param layerDnlTask
     */
    /**
     * 将表模板数据存入数据库
     *
     * @param list
     */
    public void setTableItemsToDB(List<ActionModel> list,String userId) {
        deleteItemFromDB(userId);
        Realm realm = Realm.getDefaultInstance();
        for (ActionModel tableItem : list) {
            realm.beginTransaction();
            realm.copyToRealm(tableItem);
            realm.commitTransaction();
        }
    }


    /**
     * 从数据库获取全部菜单项
     *
     * @return
     */
    public List<ActionModel> getTableItemsByDB() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ActionModel> tableItems = realm.where(ActionModel.class).findAll();
        return realm.copyFromRealm(tableItems);
    }

    /**
     * 清除所有菜单
     */
    @Deprecated
    public void clearTableItemsInDB() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ActionModel> tableItems = realm.where(ActionModel.class).findAll();

        realm.beginTransaction();
        tableItems.deleteAllFromRealm();
        realm.commitTransaction();
    }

    /**
     * 删除对应用户下的菜单
     *
     * @param userId
     */
    public void deleteItemFromDB(String userId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ActionModel> table = realm.where(ActionModel.class).equalTo("userId", userId)
                .findAll();

        if (table != null) {
            realm.beginTransaction();
            table.deleteAllFromRealm();
            realm.commitTransaction();
        }
    }

    public List<ActionModel> getMenuItemsForUserId(String userId){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ActionModel> table = realm.where(ActionModel.class).equalTo("userId", userId)
                .findAll();
        return realm.copyFromRealm(table);
    }
}

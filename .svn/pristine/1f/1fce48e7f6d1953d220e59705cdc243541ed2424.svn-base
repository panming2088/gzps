package com.augurit.agmobile.patrolcore.layerdownload.service;

import android.content.Context;

import com.augurit.agmobile.patrolcore.layerdownload.model.LayerDnlTask;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * 描述：数据库逻辑
 *
 * @author 创建人 ：luobiao
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.layerdownload.service
 * @createTime 创建时间 ：17/7/27
 * @modifyBy 修改人 ：luobiao
 * @modifyTime 修改时间 ：17/7/27
 * @modifyMemo 修改备注：
 */

public class DownloadTableDBService {
    private Context mContext;


    public DownloadTableDBService(Context context) {
        this.mContext = context;
   //     AMDatabase.init(context);
    }

    public DownloadTableDBService() {
    }

    /**
     * 将表模板数据存入数据库
     *
     * @param list
     */
    public void setTableItemsToDB(List<LayerDnlTask> list) {
        Realm realm = Realm.getDefaultInstance();
        for (LayerDnlTask tableItem : list) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(tableItem);
            realm.commitTransaction();
        }
    }

    /**
     * 将表模板数据存入数据库
     *
     * @param layerDnlTask
     */
    public void setTableItemToDB(LayerDnlTask layerDnlTask) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(layerDnlTask);
        realm.commitTransaction();
    }


    /**
     * 从数据库获取表模板数据
     *
     * @return
     */
    public List<LayerDnlTask> getTableItemsByDB() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LayerDnlTask> tableItems = realm.where(LayerDnlTask.class).findAll();
        return realm.copyFromRealm(tableItems);
    }

    /**
     * 清除数据库里面的表模板数据
     */
    @Deprecated
    public void clearTableItemsInDB() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LayerDnlTask> tableItems = realm.where(LayerDnlTask.class).findAll();

        realm.beginTransaction();
        tableItems.deleteAllFromRealm();
        realm.commitTransaction();
    }

    /**
     * 删除对应的下载任务
     *
     * @param id
     */
    public void deleteItemFromDB(int id) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LayerDnlTask> table = realm.where(LayerDnlTask.class).equalTo("id", id)
                .findAll();

        if (table != null) {
            realm.beginTransaction();
            table.deleteAllFromRealm();
            realm.commitTransaction();
        }
    }

}

package com.augurit.agmobile.patrolcore.common.table.dao.local;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.event.AddSaveRecordEvent;
import com.augurit.agmobile.patrolcore.common.table.model.ClientTaskRecord;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.model.DongFinishedRecord;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.common.table.model.ProjectTable;
import com.augurit.agmobile.patrolcore.common.table.model.TableChildItem;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.common.table.model.TaoFinishedRecord;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTable;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTableRecord;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTableRecord2;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.ListUtil;

import org.apache.commons.collections4.MapUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 描述：数据库逻辑
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model.manager.local
 * @createTime 创建时间 ：17/3/6
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/6
 * @modifyMemo 修改备注：
 */

public class TableDBService {
    private Context mContext;


    public TableDBService(Context context) {
        this.mContext = context;
        //     AMDatabase.init(context);
    }

    public TableDBService() {
    }

    /**
     * 将表模板数据存入数据库
     * 主要用于同步模板数据
     *
     * @param list
     */
    public void setTableItemsToDB(List<TableItem> list) {
        //先删掉旧模板数据
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(TableItem.class);
        realm.commitTransaction();
        //再更新模板数据
        for (TableItem tableItem : list) {
            realm.beginTransaction();
            realm.copyToRealm(tableItem);
            realm.commitTransaction();
        }
        realm.close();
    }


    /**
     * 从数据库获取表模板数据
     *
     * @return
     */
    public List<TableItem> getTableItemsByDB() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableItem> tableItems = realm.where(TableItem.class).findAll();
        List<TableItem> results = realm.copyFromRealm(tableItems);
        realm.close();
        return results;
    }

    /**
     * 根据 projectId 获取该项目所属的 TableItem 集合
     *
     * @param projectId
     * @return
     */
    public List<TableItem> getTableItemsByProjectIdFromDB(String projectId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableItem> tableItems = realm.where(TableItem.class).equalTo("device_id", projectId).findAll();
        List<TableItem> results = realm.copyFromRealm(tableItems);
        realm.close();
        return results;
    }

    /**
     * 清除数据库里面的表模板数据
     */
    @Deprecated
    public void clearTableItemsInDB() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableItem> tableItems = realm.where(TableItem.class).findAll();

        realm.beginTransaction();
        tableItems.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }


    /**
     * 更新表模板子项数据
     *
     * @param list
     * @param typeCode
     */
    @Deprecated
    public void updateTableChildItemsInDB(List<TableChildItem> list, String typeCode) {
        clearTableChildItemsInDB(typeCode);
        setTableChildItemsToDB(list, typeCode);
    }

    /**
     * 存储表模板子项数据
     *
     * @param list
     * @param typeCode
     */
    @Deprecated
    public void setTableChildItemsToDB(List<TableChildItem> list, String typeCode) {
        clearTableChildItemsInDB(typeCode);
        Realm realm = Realm.getDefaultInstance();
        for (TableChildItem tableChildItem : list) {
            realm.beginTransaction();
            realm.copyToRealm(tableChildItem);
            realm.commitTransaction();
        }
        realm.close();

    }

    /**
     * 更新表模板子项数据
     *
     * @param typeCode
     */
    @Deprecated
    public void clearTableChildItemsInDB(String typeCode) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableChildItem> tableChildItems = realm.where(TableChildItem.class).equalTo("typeCode", typeCode)
                .findAll();
        realm.beginTransaction();
        tableChildItems.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    /**
     * 从字典中获取表模板子项数据
     *
     * @param typeCode
     * @return
     */
    public List<TableChildItem> getTableChildItemsByDB(String typeCode) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableChildItem> tableChildItems = realm.where(TableChildItem.class).equalTo("typeCode", typeCode)
                .findAll();
        List<TableChildItem> results = realm.copyFromRealm(tableChildItems);
        realm.close();
        return results;
    }




    /*
    public List<TableChildItem> getTableChildItemsByDBCode(String code) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableChildItem> tableChildItems = realm.where(TableChildItem.class).equalTo("code", code)
                .findAll();
        return realm.copyFromRealm(tableChildItems);
    }
    */

    /**
     * 从数据库获取表模板数据
     *
     * @return
     */
    public List<TableItem> getTableItemsByDB(String featureCode, String actionId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableItem> tableItems = realm.where(TableItem.class).equalTo("featureCode", featureCode)
                .equalTo("device_id", actionId).findAll();
        List<TableItem> result = realm.copyFromRealm(tableItems);
        realm.close();
        return result;
    }

    /**
     * 从数据库获取功能对应的配置项
     *
     * @param featureCode
     * @return
     */
    public Map<String, String> getConfigureItemsFromDB(String featureCode, String actionId) {
        List<TableItem> tableItems = getTableItemsByDB(featureCode, actionId);
        Map<String, String> configureItems = new HashMap<>();
        for (TableItem tableItem : tableItems) {
            configureItems.put(tableItem.getField1().toLowerCase(), tableItem.getValue()); //统一转成小写
        }
        return configureItems;
    }


    /**
     * 从字典中获取表模板子项数据
     *
     * @param code
     * @return
     */
    public List<TableChildItem> getTableChildItemsByCode(String code) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TableChildItem> tableChildItems = realm.where(TableChildItem.class).equalTo("code", code)
                .findAll();
        List<TableChildItem> result = realm.copyFromRealm(tableChildItems);
        realm.close();
        return result;
    }


    /**
     * 将已编辑的表数据储存进数据库
     *
     * @param tableItems
     * @param photos
     * @param valueMap
     */
    @Deprecated
    public void setEditedTableItemToDB(String projectId, List<TableItem> tableItems, List<Photo> photos, Map<String, String> valueMap) {
        RealmList<LocalTableItem> localTableItems = new RealmList<>();
        long time = System.currentTimeMillis();

        //将表模板子项转换为本地存储的类型
        long i = 0;
        for (TableItem tableItem : tableItems) {
            i++;
            LocalTableItem localTableItem = new LocalTableItem();
            localTableItem.setKey(String.valueOf(time + i)); //key值得保证唯一 否则会被覆盖掉已有数据
            localTableItem.setProjectId(projectId);
            localTableItem.setDic_code(tableItem.getDic_code());
            localTableItem.setBase_table(tableItem.getBase_table());
            localTableItem.setColum_num(tableItem.getColum_num());
            localTableItem.setControl_type(tableItem.getControl_type());
            localTableItem.setDevice_id(tableItem.getDevice_id());
            localTableItem.setField1(tableItem.getField1());
            localTableItem.setField2(tableItem.getField2());
            localTableItem.setValue(tableItem.getValue());
            localTableItem.setHtml_name(tableItem.getHtml_name());
            localTableItem.setId(tableItem.getId());
            localTableItem.setId_edit(tableItem.getIs_edit());
            localTableItem.setIf_hidden(tableItem.getIf_hidden());
            localTableItem.setIndustry_code(tableItem.getIndustry_code());
            localTableItem.setIndustry_table(tableItem.getIndustry_table());
            localTableItem.setRow_num(tableItem.getRow_num());
            localTableItem.setIs_form_field(tableItem.getIs_form_field());
            localTableItem.setRegex(tableItem.getRegex());
            localTableItem.setValidate_type(tableItem.getValidate_type());
            localTableItem.setChildren_code(tableItem.getChildren_code());
            localTableItem.setDisplay_order(tableItem.getDisplay_order());
            localTableItem.setFirst_correlation(tableItem.getFirst_correlation());
            localTableItem.setThree_correlation(tableItem.getThree_correlation());
            localTableItem.setIf_required(tableItem.getIf_required());
            localTableItems.add(localTableItem);
        }

        //创建本地存储的表类实体
        LocalTable localTable = new LocalTable();
        localTable.setList(localTableItems);
        localTable.setId(projectId);
        localTable.setTime(time);
        localTable.setKey(String.valueOf(time));

        //存进数据库
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(localTable);
        realm.commitTransaction();

        //为图片绑定表文字内容
        if (photos != null) {
            for (Photo photo : photos) {
                photo.setProblem_id(String.valueOf(time));
                /////photo.setTag(id); //标志该图片是属于该表单工单下面的那一个图片配置项的
            }
            setEditedTablePhotoToDB(photos);
        }
        realm.close();
    }

    /**
     * 将已编辑的表数据储存进数据库
     *
     * @param tableItems
     * @param valueMap
     */
    public void setEditedTableItemToDB(String projectId, List<TableItem> tableItems, Map<String, List<Photo>> photosMap, Map<String, String> valueMap) {
        RealmList<LocalTableItem> localTableItems = new RealmList<>();
        long time = System.currentTimeMillis();

        //将表模板子项转换为本地存储的类型
        long i = 0;
        for (TableItem tableItem : tableItems) {
            i++;
            LocalTableItem localTableItem = new LocalTableItem();
            localTableItem.setKey(String.valueOf(time + i)); //key值得保证唯一 否则会被覆盖掉已有数据
            localTableItem.setProjectId(projectId);
            localTableItem.setDic_code(tableItem.getDic_code());
            localTableItem.setBase_table(tableItem.getBase_table());
            localTableItem.setColum_num(tableItem.getColum_num());
            localTableItem.setControl_type(tableItem.getControl_type());
            localTableItem.setDevice_id(tableItem.getDevice_id());
            localTableItem.setField1(tableItem.getField1());
            localTableItem.setField2(tableItem.getField2());
            localTableItem.setValue(tableItem.getValue());
            localTableItem.setHtml_name(tableItem.getHtml_name());
            localTableItem.setId(tableItem.getId());
            localTableItem.setId_edit(tableItem.getIs_edit());
            localTableItem.setIf_hidden(tableItem.getIf_hidden());
            localTableItem.setIndustry_code(tableItem.getIndustry_code());
            localTableItem.setIndustry_table(tableItem.getIndustry_table());
            localTableItem.setRow_num(tableItem.getRow_num());
            localTableItem.setIs_form_field(tableItem.getIs_form_field());
            localTableItem.setRegex(tableItem.getRegex());
            localTableItem.setValidate_type(tableItem.getValidate_type());
            localTableItem.setChildren_code(tableItem.getChildren_code());
            localTableItem.setDisplay_order(tableItem.getDisplay_order());
            localTableItem.setFirst_correlation(tableItem.getFirst_correlation());
            localTableItem.setThree_correlation(tableItem.getThree_correlation());
            localTableItem.setIf_required(tableItem.getIf_required());
            localTableItems.add(localTableItem);
        }

        //创建本地存储的表类实体
        LocalTable localTable = new LocalTable();
        localTable.setList(localTableItems);
        localTable.setId(projectId);
        // localTable.setUserId(userId);
        localTable.setTime(time);
        String tablekey = String.valueOf(time);
        localTable.setKey(tablekey);


        //存进数据库
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(localTable);
        realm.commitTransaction();

        //为图片绑定表文字内容
        if (photosMap != null && !MapUtils.isEmpty(photosMap)) {
            List<Photo> photos = new ArrayList<>(); // 全部存入一个List
            for (Map.Entry<String, List<Photo>> entry : photosMap.entrySet()) {
                String key = entry.getKey();
                for (Photo photo : entry.getValue()) {

                    photo.setProblem_id(String.valueOf(time));
                    photo.setField1(key);
                    photos.add(photo);
                }
            }
            if (!ListUtil.isEmpty(photos)) {
                setEditedTablePhotoToDB(photos);
            }

        }


        /****************************** 8.30 gkh **********************************/
        //发送消息将该记录绑定到任务
        LocalTaskRecord localTaskRecord = new LocalTaskRecord();
        localTaskRecord.setKey(tablekey);
        AddSaveRecordEvent addSaveRecordEvent = new AddSaveRecordEvent(localTaskRecord);
        EventBus.getDefault().post(addSaveRecordEvent);
        /****************************** 8.30 gkh **********************************/

        realm.close();
    }


    /**
     * 将已编辑的表数据储存进数据库(四标四实采用的保存)
     *
     * @param tableItems
     * @param recordId
     * @param valueMap
     */
    public void setEditedTableItemToDB2(String projectId, String recordId, List<TableItem> tableItems, Map<String, List<Photo>> photosMap, Map<String, String> valueMap) {
        RealmList<LocalTableItem> localTableItems = new RealmList<>();
        long time = System.currentTimeMillis();

        //将表模板子项转换为本地存储的类型
        long i = 0;
        for (TableItem tableItem : tableItems) {
            i++;
            LocalTableItem localTableItem = new LocalTableItem();
            localTableItem.setKey(String.valueOf(time + i)); //key值得保证唯一 否则会被覆盖掉已有数据
            localTableItem.setProjectId(projectId);
            localTableItem.setDic_code(tableItem.getDic_code());
            localTableItem.setBase_table(tableItem.getBase_table());
            localTableItem.setColum_num(tableItem.getColum_num());
            localTableItem.setControl_type(tableItem.getControl_type());
            localTableItem.setDevice_id(tableItem.getDevice_id());
            localTableItem.setField1(tableItem.getField1());
            localTableItem.setField2(tableItem.getField2());
            localTableItem.setValue(tableItem.getValue());
            localTableItem.setHtml_name(tableItem.getHtml_name());
            localTableItem.setId(tableItem.getId());
            localTableItem.setId_edit(tableItem.getIs_edit());
            localTableItem.setIf_hidden(tableItem.getIf_hidden());
            localTableItem.setIndustry_code(tableItem.getIndustry_code());
            localTableItem.setIndustry_table(tableItem.getIndustry_table());
            localTableItem.setRow_num(tableItem.getRow_num());
            localTableItem.setIs_form_field(tableItem.getIs_form_field());
            localTableItem.setRegex(tableItem.getRegex());
            localTableItem.setValidate_type(tableItem.getValidate_type());
            localTableItem.setChildren_code(tableItem.getChildren_code());
            localTableItem.setDisplay_order(tableItem.getDisplay_order());
            localTableItem.setFirst_correlation(tableItem.getFirst_correlation());
            localTableItem.setThree_correlation(tableItem.getThree_correlation());
            localTableItem.setIf_required(tableItem.getIf_required());
            localTableItems.add(localTableItem);
        }

        //创建本地存储的表类实体
        LocalTable localTable = new LocalTable();
        localTable.setList(localTableItems);
        localTable.setId(projectId);
        if (!TextUtils.isEmpty(recordId)) {
            localTable.setRecordId(recordId);
        }

        // localTable.setUserId(userId);
        localTable.setTime(time);
        String tablekey = String.valueOf(time);
        localTable.setKey(tablekey);


        //存进数据库
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(localTable);
        realm.commitTransaction();

        //为图片绑定表文字内容
        if (photosMap != null && !MapUtils.isEmpty(photosMap)) {
            List<Photo> photos = new ArrayList<>(); // 全部存入一个List
            for (Map.Entry<String, List<Photo>> entry : photosMap.entrySet()) {
                String key = entry.getKey();
                for (Photo photo : entry.getValue()) {

                    photo.setProblem_id(String.valueOf(time));
                    photo.setField1(key);
                    photos.add(photo);
                }
            }
            if (!ListUtil.isEmpty(photos)) {
                setEditedTablePhotoToDB(photos);
            }

        }


        /****************************** 8.30 gkh **********************************/
        //发送消息将该记录绑定到任务
        LocalTaskRecord localTaskRecord = new LocalTaskRecord();
        localTaskRecord.setKey(tablekey);
        AddSaveRecordEvent addSaveRecordEvent = new AddSaveRecordEvent(localTaskRecord);
        EventBus.getDefault().post(addSaveRecordEvent);
        /****************************** 8.30 gkh **********************************/

        realm.close();
    }


    /**
     * 将已编辑的表数据储存进数据库
     *
     * @param tableItems
     * @param valueMap
     */
    public void setEditedTableItemToDB(String tablekey, String projectId, List<TableItem> tableItems, Map<String, List<Photo>> photosMap, Map<String, String> valueMap) {
        RealmList<LocalTableItem> localTableItems = new RealmList<>();
        long time = System.currentTimeMillis();

        //将表模板子项转换为本地存储的类型
        long i = 0;
        for (TableItem tableItem : tableItems) {
            i++;
            LocalTableItem localTableItem = new LocalTableItem();
            localTableItem.setKey(String.valueOf(time + i)); //key值得保证唯一 否则会被覆盖掉已有数据
            localTableItem.setProjectId(projectId);
            localTableItem.setDic_code(tableItem.getDic_code());
            localTableItem.setBase_table(tableItem.getBase_table());
            localTableItem.setColum_num(tableItem.getColum_num());
            localTableItem.setControl_type(tableItem.getControl_type());
            localTableItem.setDevice_id(tableItem.getDevice_id());
            localTableItem.setField1(tableItem.getField1());
            localTableItem.setField2(tableItem.getField2());
            localTableItem.setValue(tableItem.getValue());
            localTableItem.setHtml_name(tableItem.getHtml_name());
            localTableItem.setId(tableItem.getId());
            localTableItem.setId_edit(tableItem.getIs_edit());
            localTableItem.setIf_hidden(tableItem.getIf_hidden());
            localTableItem.setIndustry_code(tableItem.getIndustry_code());
            localTableItem.setIndustry_table(tableItem.getIndustry_table());
            localTableItem.setRow_num(tableItem.getRow_num());
            localTableItem.setIs_form_field(tableItem.getIs_form_field());
            localTableItem.setRegex(tableItem.getRegex());
            localTableItem.setValidate_type(tableItem.getValidate_type());
            localTableItem.setChildren_code(tableItem.getChildren_code());
            localTableItem.setDisplay_order(tableItem.getDisplay_order());
            localTableItem.setFirst_correlation(tableItem.getFirst_correlation());
            localTableItem.setThree_correlation(tableItem.getThree_correlation());
            localTableItem.setIf_required(tableItem.getIf_required());
            localTableItems.add(localTableItem);
        }

        //创建本地存储的表类实体
        LocalTable localTable = new LocalTable();
        localTable.setList(localTableItems);
        localTable.setId(projectId);
        // localTable.setUserId(userId);
        localTable.setTime(time);
        if (tablekey != null) {
            localTable.setKey(tablekey);
        }


        //存进数据库
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(localTable);
        realm.commitTransaction();


        //为图片绑定表文字内容
        if (photosMap != null && !MapUtils.isEmpty(photosMap)) {
            List<Photo> photos = new ArrayList<>(); // 全部存入一个List
            for (Map.Entry<String, List<Photo>> entry : photosMap.entrySet()) {
                String key = entry.getKey();
                for (Photo photo : entry.getValue()) {
                    photo.setProblem_id(tablekey);
                    photo.setField1(key);
                    photos.add(photo);
                }
            }

            if (!ListUtil.isEmpty(photos)) {
                setEditedTablePhotoToDB(photos);
            }

        }

        /****************************** 8.30 gkh **********************************/
        //发送消息将该记录绑定到任务
        LocalTaskRecord localTaskRecord = new LocalTaskRecord();
        localTaskRecord.setKey(tablekey);
        AddSaveRecordEvent addSaveRecordEvent = new AddSaveRecordEvent(localTaskRecord);
        EventBus.getDefault().post(addSaveRecordEvent);
        /****************************** 8.30 gkh **********************************/

        realm.close();
    }

    /**
     * 将已编辑的表数据储存进数据库
     *
     * @param tableItems
     * @param valueMap
     */
    public void setEditedTableItemToDB(String tableName, String tablekey, String projectId, List<TableItem> tableItems, Map<String, List<Photo>> photosMap, Map<String, String> valueMap) {
        RealmList<LocalTableItem> localTableItems = new RealmList<>();
        long time = System.currentTimeMillis();

        //将表模板子项转换为本地存储的类型
        long i = 0;
        for (TableItem tableItem : tableItems) {
            i++;
            LocalTableItem localTableItem = new LocalTableItem();
            localTableItem.setKey(String.valueOf(time + i)); //key值得保证唯一 否则会被覆盖掉已有数据
            localTableItem.setProjectId(projectId);
            localTableItem.setDic_code(tableItem.getDic_code());
            localTableItem.setBase_table(tableItem.getBase_table());
            localTableItem.setColum_num(tableItem.getColum_num());
            localTableItem.setControl_type(tableItem.getControl_type());
            localTableItem.setDevice_id(tableItem.getDevice_id());
            localTableItem.setField1(tableItem.getField1());
            localTableItem.setField2(tableItem.getField2());
            localTableItem.setValue(tableItem.getValue());
            localTableItem.setHtml_name(tableItem.getHtml_name());
            localTableItem.setId(tableItem.getId());
            localTableItem.setId_edit(tableItem.getIs_edit());
            localTableItem.setIf_hidden(tableItem.getIf_hidden());
            localTableItem.setIndustry_code(tableItem.getIndustry_code());
            localTableItem.setIndustry_table(tableItem.getIndustry_table());
            localTableItem.setRow_num(tableItem.getRow_num());
            localTableItem.setIs_form_field(tableItem.getIs_form_field());
            localTableItem.setRegex(tableItem.getRegex());
            localTableItem.setValidate_type(tableItem.getValidate_type());
            localTableItem.setChildren_code(tableItem.getChildren_code());
            localTableItem.setDisplay_order(tableItem.getDisplay_order());
            localTableItem.setFirst_correlation(tableItem.getFirst_correlation());
            localTableItem.setThree_correlation(tableItem.getThree_correlation());
            localTableItem.setIf_required(tableItem.getIf_required());
            localTableItems.add(localTableItem);
        }

        //创建本地存储的表类实体
        LocalTable localTable = new LocalTable();
        localTable.setList(localTableItems);
        localTable.setId(projectId);
        localTable.setIndustryTableName(tableName);
        // localTable.setUserId(userId);
        localTable.setTime(time);
        if (tablekey != null) {
            localTable.setKey(tablekey);
        }

        //存进数据库
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(localTable);
        realm.commitTransaction();

        //为图片绑定表文字内容
        if (photosMap != null) {
            List<Photo> photos = new ArrayList<>(); // 全部存入一个List
            for (Map.Entry<String, List<Photo>> entry : photosMap.entrySet()) {
                String key = entry.getKey();
                for (Photo photo : entry.getValue()) {
                    photo.setProblem_id(tablekey);
                    photo.setField1(key);
                    photos.add(photo);
                }
            }
            setEditedTablePhotoToDB(photos);
        }

        realm.close();
    }

    /**
     * 将图片存入数据
     *
     * @param photos
     */
    public void setEditedTablePhotoToDB(List<Photo> photos) {
        //TODO Photo在cmp层 无法存在Realm数据库中
        AMDatabase.getInstance().saveAll(photos);
    }

    /**
     * 删除照片
     *
     * @param photo
     */
    public void deleteEditTablePhotoInDB(final Photo photo) {
        if (photo.getProblem_id() == null) return;

        List<String> pathList = new ArrayList<>();
        if (photo.getLocalPath() != null) {
            pathList.add(photo.getLocalPath());
            //   DelPhotoUtils.delPhoto(pathList);
        }


        List<Photo> list = AMDatabase.getInstance().getQueryByWhere(Photo.class, "problem_id", photo.getProblem_id());
        for (Photo photo1 : list) {
            AMDatabase.getInstance().delete(photo1);
        }

    }

    /**
     * 获取存储的已编辑的表实体集合
     * 每个表实体里面包含配置的已经被编辑过的表格项集
     *
     * @return
     */
    public List<LocalTable> getEditedTableItemsFromDB() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalTable> localTables = realm.where(LocalTable.class)
                .findAll();
        List<LocalTable> results = realm.copyFromRealm(localTables);
        realm.close();
        return results;
    }

    /**
     * 根据表格键ID获取具体的某表实体记录
     *
     * @param tableKey
     * @return
     */
    public LocalTable getEditedTableItemsByTableKey(String tableKey) {
        Realm realm = Realm.getDefaultInstance();
        LocalTable localTable = realm.where(LocalTable.class).equalTo("key", tableKey)
                .findFirst();
        LocalTable result = null;
        if (localTable != null) {
            result = realm.copyFromRealm(localTable);
        }
        realm.close();
        return result;
    }

    /**
     * 根据项目ID获取存储的已编辑的表实体
     *
     * @return
     */
    public List<LocalTable> getEditedTableItemsFromDB(String projjectId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalTable> localTables = realm.where(LocalTable.class).equalTo("id", projjectId)
                .findAll();
        List<LocalTable> result = realm.copyFromRealm(localTables);
        realm.close();
        return result;
    }

    /**
     * 删除已经编辑的保存到数据中的表格实例数据
     *
     * @param key
     * @param callback
     */
    public void deleteEditedTableItemsFromBD(String key, TableDBCallback callback) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalTable> localTables = realm.where(LocalTable.class).equalTo("key", key)
                .findAll();
        realm.beginTransaction();
        localTables.deleteAllFromRealm();
        realm.commitTransaction();
        if (callback != null) {
            callback.onSuccess(null);
        }
        realm.close();
    }

    public void deleteEditedTableItemsFromBD(List<String> keyList, TableDBCallback callback) {
        Realm realm = Realm.getDefaultInstance();
        for (String key : keyList) {
            RealmResults<LocalTable> localTables = realm.where(LocalTable.class).equalTo("key", key)
                    .findAll();
            realm.beginTransaction();
            localTables.deleteAllFromRealm();
            realm.commitTransaction();
        }
        if (callback != null) {
            callback.onSuccess(null);
        }
        realm.close();
    }

    /**
     * 获取存储在数据中的图片
     *
     * @param problem_id
     * @return
     */
    public List<Photo> getPhotosFromDB(String problem_id) {
        List<Photo> list = AMDatabase.getInstance().getQueryByWhere(Photo.class, "problem_id", problem_id);
        //  List<Photo> list = new ArrayList<>();
        return list;
    }

    /**
     * 将字典数据存入数据库
     *
     * @param dictionaryItems
     */
    public void setDictionaryToDB(List<DictionaryItem> dictionaryItems) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(DictionaryItem.class);
        realm.commitTransaction();
        for (DictionaryItem dictionaryItem : dictionaryItems) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(dictionaryItem);
            realm.commitTransaction();
        }
        realm.close();
    }

    public List<DictionaryItem> getDictionaryByCode(String code) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DictionaryItem> dictionaryItems = realm.where(DictionaryItem.class).equalTo("code", code)
                .findAll();
        List<DictionaryItem> result = realm.copyFromRealm(dictionaryItems);
        realm.close();
        return result;
    }

    /**
     * 根据 typecode 字段获取字典数据
     *
     * @param typeCode
     */
    public List<DictionaryItem> getDictionaryByTypecodeInDB(String typeCode) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DictionaryItem> dictionaryItems = realm.where(DictionaryItem.class).equalTo("type_code", typeCode)
                .findAll();
        List<DictionaryItem> result = realm.copyFromRealm(dictionaryItems);
        realm.close();
        return result;
    }

    /**
     * 根据 code 字段获取填充数据列表
     *
     * @param code
     * @return
     */
    public List<DictionaryItem> getChildDictionaryByCodeInDB(String code) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DictionaryItem> dictionaryItems = realm.where(DictionaryItem.class).equalTo("code", code)
                .findAll();
        List<DictionaryItem> result = realm.copyFromRealm(dictionaryItems);
        realm.close();
        return result;
    }

    /**
     * 根据 pcode 字段获取级联子项数据列表
     *
     * @param pcode
     * @return
     */
    public List<DictionaryItem> getChildDictionaryByPCodeInDB(String pcode) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DictionaryItem> dictionaryItems = realm.where(DictionaryItem.class).equalTo("pcode", pcode)
                .findAll();
        List<DictionaryItem> result = realm.copyFromRealm(dictionaryItems);
        realm.close();
        return result;
    }

    /**
     * 根据 pcode 字段获取级联子项数据列表(可见列表)
     *
     * @param pcode
     * @return
     */
    public List<DictionaryItem> getChildInVisableDictionaryByPCodeInDB(String pcode) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DictionaryItem> dictionaryItems = realm.where(DictionaryItem.class).equalTo("pcode", pcode).equalTo("note","1")
                .findAll();
        List<DictionaryItem> result = realm.copyFromRealm(dictionaryItems);
        realm.close();
        return result;
    }

    /**
     * 获取本地所有的数据字典
     */
    public List<DictionaryItem> getAllDictionaries() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DictionaryItem> dictionaryItems = realm.where(DictionaryItem.class).findAll();
        List<DictionaryItem> result = realm.copyFromRealm(dictionaryItems);
        realm.close();
        return result;
    }

    /**
     * 保存对应项目的上报对应表格模板实体到数据库中
     *
     * @param table
     */
    public void setProjectTableToDB(ProjectTable table) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(table);
        realm.commitTransaction();
        realm.close();
    }

    /**
     * 根据项目ID获取对应的保存的表格模板
     *
     * @param projetId
     * @return
     */
    public List<ProjectTable> getProjectTableFormDB(String projetId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ProjectTable> table = realm.where(ProjectTable.class).equalTo("id", projetId)
                .findAll();
        List<ProjectTable> result = realm.copyFromRealm(table);
        realm.close();
        return result;
    }

    /**
     * 删除对应的项目表格模板
     *
     * @param projectId
     */
    public void deleteProjectTableFromDB(String projectId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ProjectTable> table = realm.where(ProjectTable.class).equalTo("id", projectId)
                .findAll();

        if (table != null) {
            realm.beginTransaction();
            table.deleteAllFromRealm();
            realm.commitTransaction();
        }

        realm.close();
    }

    /**
     * 清空项目表格模板
     */
    public void deleteAllProjectTableFromDB() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ProjectTable> tables = realm.where(ProjectTable.class).findAll();
        realm.beginTransaction();
        tables.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    /**
     * 保存项目数据到数据库
     *
     * @param projects
     */
    public void setProjectToDB(List<Project> projects) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(projects);
        realm.commitTransaction();
        realm.close();
    }

    /**
     * 从数据库中获取项目数据
     *
     * @return
     */
    public List<Project> getProjectFromDB() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Project> projects = realm.where(Project.class).findAll();
        List<Project> result = realm.copyFromRealm(projects);
        realm.close();
        return result;
    }

    /**
     * 删除数据库中的项目数据
     */
    public void deleteProjectInDB() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Project> projects = realm.where(Project.class).findAll();
        realm.beginTransaction();
        projects.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    /**************************************任务(多表联合处理)****************************************/
    /**
     * 保存新增上报任务到本地
     * 一个任务包含若干个表实体,也即是若干行 (多表任务里面的表记录即是行记录)
     *
     * @param localTask
     */
    public void setLocalTaskToDB(LocalTask localTask) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(localTask);
        realm.commitTransaction();
        realm.close();
    }

    /**
     * 根据主键获取之前保存的任务状态
     *
     * @param key
     * @return
     */
    public LocalTask getLocalTaskByKey(String key) {
        Realm realm = Realm.getDefaultInstance();
        LocalTask localTask = realm.where(LocalTask.class).equalTo("key", key).findFirst();
        LocalTask result = null;
        if (localTask != null) {
            result = realm.copyFromRealm(localTask);
        }
        realm.close();
        return result;

    }

    /**
     * 获取保存到本地的新增上报任务
     *
     * @return
     */
    public List<LocalTask> getLocalTaskFromDB() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalTask> localTasks = realm.where(LocalTask.class).findAll();
        List<LocalTask> result = realm.copyFromRealm(localTasks);
        realm.close();
        return result;
    }

    /**
     * 删除保存在本地的任务
     *
     * @param localTask
     */
    public void deleteLocalTaskFromDB(LocalTask localTask) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalTask> localTasks = realm.where(LocalTask.class).equalTo("key", localTask.getKey()).findAll();
        realm.beginTransaction();
        localTasks.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    /**
     * 保存已经上传记录的ID
     * 每个记录ID对应已经上传到服务器的行记录(表记录)
     *
     * @param list
     */
    public void setClientTaskRecordToDB(List<ClientTaskRecord> list) {
        Realm realm = Realm.getDefaultInstance();
        for (ClientTaskRecord item : list) {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(item);
            realm.commitTransaction();
        }
        realm.close();
    }

    /**
     * 根据任务ID获取已经上传记录的ID
     * 每个记录ID对应已经上传到服务器的行记录(表记录)
     *
     * @param taskId
     * @return
     */
    public List<ClientTaskRecord> getClientTaskRecordFromDB(String taskId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ClientTaskRecord> clientTaskRecords = realm.where(ClientTaskRecord.class).equalTo("taskId", taskId)
                .findAll();
        List<ClientTaskRecord> result = realm.copyFromRealm(clientTaskRecords);
        realm.close();
        return result;
    }

    /**************************************修改任务(多表联合处理)****************************************/
    //2017.9.2 by gkh

    /**
     * 保存或者更新任务数据
     * @param serverTasks
     * */
    /*public void saveServerTaskToDB(List<ServerTask> serverTasks){
        Realm realm = Realm.getDefaultInstance();
        for(ServerTask serverTask : serverTasks){
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(serverTask);
            realm.commitTransaction();
        }
        realm.close();
    }*/

    /**
     * 根据任务ID查询任务
     * @param taskId
     * @return
     */
    /*public ServerTask getServerTaskByTaskIdInDB(String taskId){
        Realm realm = Realm.getDefaultInstance();
        ServerTask serverTask = realm.where(ServerTask.class).equalTo("id",taskId).findFirst();
        ServerTask result = null;
        if(serverTask != null) {
             result = realm.copyFromRealm(serverTask);
        }
        realm.close();
        return result;
    }*/


    /**
     * 保存ServerTable(列表项对应的表)里面的记录项
     *
     * @param serverTableRecord
     */
    public void saveLocalServerTableRecord(final LocalServerTableRecord2 serverTableRecord, final TableDBCallback callback) {
        //先删掉之前已有的记录
        deleteLocalServerTableRecord2(serverTableRecord.getRecordId());

        //再保存新的记录
        Realm realm = Realm.getDefaultInstance();
        RealmAsyncTask transaction = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(serverTableRecord);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                callback.onSuccess(null);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                callback.onError(error.getLocalizedMessage());
            }
        });

    }


    /**
     * 保存ServerTable(列表项对应的表)里面的记录项
     *
     * @param serverTableRecord
     */
    public void saveLocalServerTableRecord(final LocalServerTableRecord2 serverTableRecord, String oldTableKey, final TableDBCallback callback) { //xcl 2017.09.21 存在recordId一样的情况，所以需要根据tableKey和recordId删除记录
        //先删掉之前已有的记录
        deleteLocalServerTableRecord2(serverTableRecord.getRecordId(), oldTableKey);

        //再保存新的记录
        Realm realm = Realm.getDefaultInstance();
        RealmAsyncTask transaction = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(serverTableRecord);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                callback.onSuccess(null);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                callback.onError(error.getLocalizedMessage());
            }
        });

    }

    /**
     * 根据recordId和tableKey，更新旧数据
     *
     * @param
     */
    public void saveLocalServerTableRecord(final String recordId, String oldTableKey, final LocalServerTableRecord2 newData, final TableDBCallback callback) { //xcl 2017.09.21 存在recordId一样的情况，所以需要根据tableKey和recordId删除记录
        //先删掉之前已有的记录
        deleteLocalServerTableRecord2(recordId, oldTableKey);

        //再保存新的记录
        Realm realm = Realm.getDefaultInstance();
        RealmAsyncTask transaction = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(newData);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                callback.onSuccess(null);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                callback.onError(error.getLocalizedMessage());
            }
        });

    }

    public void deleteLocalServerTableRecord2(String recordId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalServerTableRecord2> localServerTableRecord2s = realm.where(LocalServerTableRecord2.class)
                .equalTo("recordId", recordId)
                .findAll();
        realm.beginTransaction();
        localServerTableRecord2s.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public void deleteLocalServerTableRecord2(String recordId, String oldTableKey) { //xcl 2017.09.21 存在recordId一样的情况，所以需要根据tableKey和recordId删除记录

        if (oldTableKey == null) {
            return; //没有本地保存过，不需要删除
        }

        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalServerTableRecord2> localServerTableRecord2s = realm.where(LocalServerTableRecord2.class)
                .equalTo("recordId", recordId)
                .equalTo("tableKey", oldTableKey)
                .findAll();

        realm.beginTransaction();
        localServerTableRecord2s.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public void deleteLocalServerTableRecord(final LocalServerTableRecord2 serverTableRecord) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalServerTableRecord2> localServerTableRecord2s = realm.where(LocalServerTableRecord2.class)
                .equalTo("recordId", serverTableRecord.getRecordId())
                .findAll();
        realm.beginTransaction();
        localServerTableRecord2s.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();

    }


    public void deleteLocalServerTableRecordByRecordId(final String recordId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalServerTableRecord2> localServerTableRecord2s = realm.where(LocalServerTableRecord2.class)
                .equalTo("recordId", recordId)
                .findAll();
        realm.beginTransaction();
        localServerTableRecord2s.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();

    }

    /**
     * 根据
     *
     * @param recordId
     * @param tableKey
     */
    public void deleteLocalServerTableRecordByRecordIdAndTableKey(final String recordId, String tableKey) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalServerTableRecord2> localServerTableRecord2s = realm.where(LocalServerTableRecord2.class)
                .equalTo("recordId", recordId)
                .equalTo("tableKey", tableKey)
                .findAll();
        realm.beginTransaction();
        localServerTableRecord2s.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();

    }

    /**
     * @param
     * @return
     */
    public List<LocalServerTableRecord2> getAllLocalServerTableRecord() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalServerTableRecord2> serverTableRecord = realm.where(LocalServerTableRecord2.class).findAll();
        List<LocalServerTableRecord2> results = realm.copyFromRealm(serverTableRecord);
        realm.close();
        return results;
    }

    /**
     * @param
     * @return
     */
    public List<LocalServerTableRecord2> getAllLocalServerTableRecord(String taskId, String tableId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalServerTableRecord2> serverTableRecord = realm.where(LocalServerTableRecord2.class)
                .equalTo("taskId", taskId)
                .equalTo("tableId", tableId).findAll();


        List<LocalServerTableRecord2> result = realm.copyFromRealm(serverTableRecord);
        realm.close();
        return result;
    }

    /**
     * 根据记录ID获取第一条记录数据
     *
     * @param recordId
     * @return
     */
    public LocalServerTableRecord2 getLocalServerTableRecordByRecordId(String recordId) {
        Realm realm = Realm.getDefaultInstance();
        LocalServerTableRecord2 localServerTableRecord2 = realm.where(LocalServerTableRecord2.class).equalTo("recordId", recordId).findFirst();

        LocalServerTableRecord2 result = null;
        if (localServerTableRecord2 != null) {
            result = realm.copyFromRealm(localServerTableRecord2);
        }
        realm.close();
        return result;
    }

    /**
     * 根据记录ID获取所有的记录数据
     *
     * @param recordId
     * @return
     */
    public List<LocalServerTableRecord2> getAllLocalServerTableRecordByRecordId(String recordId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalServerTableRecord2> records = realm.where(LocalServerTableRecord2.class).equalTo("recordId", recordId).findAll();

        List<LocalServerTableRecord2> result = null;
        if (records != null) {
            result = realm.copyFromRealm(records);
        }
        realm.close();
        return result;
    }

    /**
     * 根据记录ID，tableKey 获取记录数据，
     *
     * @param recordId
     * @return
     */
    public LocalServerTableRecord2 getLocalServerTableRecordByRecordIdAndTableKey(String recordId, String tableKey) { //xcl 2017.09.21 此时recordId已经不是主键了，需要增加tableKey作为筛选条件
        Realm realm = Realm.getDefaultInstance();
        LocalServerTableRecord2 localServerTableRecord2 = realm.where(LocalServerTableRecord2.class)
                .equalTo("recordId", recordId)
                .equalTo("tableKey", tableKey)
                .findFirst();

        LocalServerTableRecord2 result = null;
        if (localServerTableRecord2 != null) {
            result = realm.copyFromRealm(localServerTableRecord2);
        }
        realm.close();
        return result;
    }

    public List<LocalServerTableRecord2> getAllLocalServerRecord2() {
        Realm realm = Realm.getDefaultInstance();
        List<LocalServerTableRecord2> list = realm.where(LocalServerTableRecord2.class).findAll();

        List<LocalServerTableRecord2> result = null;
        if (list != null) {
            result = realm.copyFromRealm(list);
        }
        realm.close();
        return result;
    }

    /**************************************修改任务(多表联合处理)****************************************/


    public void setDongFinishedRecordToDB(DongFinishedRecord dongFinishedRecordToDB) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(dongFinishedRecordToDB);
        realm.commitTransaction();
        realm.close();
    }

    public void setTaoFinishedRecordToDB(TaoFinishedRecord taoFinishedRecordToDB) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(taoFinishedRecordToDB);
        realm.commitTransaction();
        realm.close();
    }

    public DongFinishedRecord getDongFinishedRecordByRecord(String recordId) {
        Realm realm = Realm.getDefaultInstance();
        DongFinishedRecord dongFinishedRecord = realm.where(DongFinishedRecord.class).equalTo("recordId", recordId).findFirst();
        DongFinishedRecord result = null;
        if (dongFinishedRecord != null) {
            result = realm.copyFromRealm(dongFinishedRecord);
        }
        realm.close();
        return result;
    }

    public TaoFinishedRecord getTaoFinishedRecord(String recordId) {
        Realm realm = Realm.getDefaultInstance();
        TaoFinishedRecord taoFinishedRecord = realm.where(TaoFinishedRecord.class).equalTo("recordId", recordId).findFirst();
        TaoFinishedRecord result = null;
        if (taoFinishedRecord != null) {
            result = realm.copyFromRealm(taoFinishedRecord);
        }
        realm.close();
        return result;
    }


    /**************************************本地数据同步处理  9.13  by gkh ****************************************/
    /**
     * 保存所有的LocalServerTable (本地数据同步)
     *
     * @param list
     * @param callback
     */
    public void saveLocalServerTable(final List<LocalServerTable> list, final TableNetCallback callback) {
        Observable.create(new Observable.OnSubscribe<List<LocalServerTable>>() {
            @Override
            public void call(Subscriber<? super List<LocalServerTable>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    for (LocalServerTable item : list) {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(item);
                        realm.commitTransaction();
                    }
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalServerTable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<LocalServerTable> result) {
                        callback.onSuccess(null);
                    }
                });
    }

    /**
     * 获取所有的 LocalServerTable
     *
     * @param callback
     */
    public void getAllLocalServerTable(final TableNetCallback<List<LocalServerTable>> callback) {
        Observable.create(new Observable.OnSubscribe<List<LocalServerTable>>() {
            @Override
            public void call(Subscriber<? super List<LocalServerTable>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<LocalServerTable> results = realm.where(LocalServerTable.class).findAll();
                    List<LocalServerTable> list = realm.copyFromRealm(results);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalServerTable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<LocalServerTable> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    /**
     * 根据taskId获取所有的 LocalServerTable
     *
     * @param callback
     */
    public void getLocalServerTableByTaskId(final String taskId, final TableNetCallback callback) {
        Observable.create(new Observable.OnSubscribe<List<LocalServerTable>>() {
            @Override
            public void call(Subscriber<? super List<LocalServerTable>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<LocalServerTable> results = realm.
                            where(LocalServerTable.class).
                            equalTo("taskId", taskId).
                            findAll();
                    List<LocalServerTable> list = realm.copyFromRealm(results);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalServerTable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<LocalServerTable> result) {
                        callback.onSuccess(result);
                    }
                });
    }


    /**
     * 保存所有的LocalServerTableRecord (本地数据同步)
     *
     * @param list
     * @param callback
     */
    public void saveLocalServerTableRecord(final List<LocalServerTableRecord> list, final TableNetCallback callback) {
        Observable.create(new Observable.OnSubscribe<List<LocalServerTableRecord>>() {
            @Override
            public void call(Subscriber<? super List<LocalServerTableRecord>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    for (LocalServerTableRecord item : list) {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(item);
                        realm.commitTransaction();
                    }
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalServerTableRecord>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<LocalServerTableRecord> result) {
                        callback.onSuccess(null);
                    }
                });
    }

    /**
     * 保存所有的LocalServerTableRecord2 (本地数据同步)
     *
     * @param list
     * @param callback
     */
    public void saveLocalServerTableRecord2(final List<LocalServerTableRecord2> list, final TableNetCallback callback) {
        Observable.create(new Observable.OnSubscribe<List<LocalServerTableRecord2>>() {
            @Override
            public void call(Subscriber<? super List<LocalServerTableRecord2>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    for (LocalServerTableRecord2 item : list) {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(item);
                        realm.commitTransaction();
                    }
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalServerTableRecord2>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<LocalServerTableRecord2> result) {
                        callback.onSuccess(null);
                    }
                });
    }

    /**
     * 获取所有的 LocalServerTable
     *
     * @param callback
     */
    public void getAllLocalServerTableRecord(final TableNetCallback<List<LocalServerTableRecord>> callback) {
        Observable.create(new Observable.OnSubscribe<List<LocalServerTableRecord>>() {
            @Override
            public void call(Subscriber<? super List<LocalServerTableRecord>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<LocalServerTableRecord> results = realm.where(LocalServerTableRecord.class).findAll();
                    List<LocalServerTableRecord> list = realm.copyFromRealm(results);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalServerTableRecord>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<LocalServerTableRecord> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    /**
     * 获取所有的 LocalServerTable
     *
     * @param callback
     */
    public void getAllLocalServerTableRecordByTaskType(final String taskType, final TableNetCallback<List<LocalServerTableRecord>> callback) {
        Observable.create(new Observable.OnSubscribe<List<LocalServerTableRecord>>() {
            @Override
            public void call(Subscriber<? super List<LocalServerTableRecord>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<LocalServerTableRecord> results = realm.where(LocalServerTableRecord.class)
                            .equalTo("taskType", taskType).findAll();
                    List<LocalServerTableRecord> list = realm.copyFromRealm(results);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalServerTableRecord>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<LocalServerTableRecord> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    /**
     * 获取所有的 LocalServerTable
     *
     * @param callback
     */
    public void getAllLocalServerTableRecord2(final TableNetCallback<List<LocalServerTableRecord2>> callback) {
        Observable.create(new Observable.OnSubscribe<List<LocalServerTableRecord2>>() {
            @Override
            public void call(Subscriber<? super List<LocalServerTableRecord2>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<LocalServerTableRecord2> results = realm.where(LocalServerTableRecord2.class).findAll();
                    List<LocalServerTableRecord2> list = realm.copyFromRealm(results);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalServerTableRecord2>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<LocalServerTableRecord2> result) {
                        callback.onSuccess(result);
                    }
                });
    }


    /**
     * 根据recordId获取所有的 LocalServerTable
     *
     * @param callback
     */
    public void getLocalServerTableRecordByRecordId(final String recordId, final TableNetCallback<List<LocalServerTableRecord>> callback) {
        Observable.create(new Observable.OnSubscribe<List<LocalServerTableRecord>>() {
            @Override
            public void call(Subscriber<? super List<LocalServerTableRecord>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<LocalServerTableRecord> results =
                            realm.where(LocalServerTableRecord.class).
                                    equalTo("recordId", recordId).
                                    findAll();
                    List<LocalServerTableRecord> list = realm.copyFromRealm(results);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalServerTableRecord>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<LocalServerTableRecord> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    /**
     * 根据taskId获取所有的 LocalServerTable
     *
     * @param callback
     */
    public void getLocalServerTableRecordByTaskId(final String taskId, final TableNetCallback<List<LocalServerTableRecord>> callback) {
        Observable.create(new Observable.OnSubscribe<List<LocalServerTableRecord>>() {
            @Override
            public void call(Subscriber<? super List<LocalServerTableRecord>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<LocalServerTableRecord> results =
                            realm.where(LocalServerTableRecord.class)
                                    .equalTo("taskId", taskId)
                                    .findAll();
                    List<LocalServerTableRecord> list = realm.copyFromRealm(results);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalServerTableRecord>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<LocalServerTableRecord> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    /**
     * 根据taskId获取所有的 LocalServerTable2
     *
     * @param callback
     */
    public void getLocalServerTableRecordByTaskId2(final String taskId, final TableNetCallback<List<LocalServerTableRecord2>> callback) {
        Observable.create(new Observable.OnSubscribe<List<LocalServerTableRecord2>>() {
            @Override
            public void call(Subscriber<? super List<LocalServerTableRecord2>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<LocalServerTableRecord2> results =
                            realm.where(LocalServerTableRecord2.class)
                                    .equalTo("taskId", taskId)
                                    .findAll();
                    List<LocalServerTableRecord2> list = realm.copyFromRealm(results);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalServerTableRecord2>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<LocalServerTableRecord2> result) {
                        callback.onSuccess(result);
                    }
                });
    }


    /**
     * 根据 设定LocalServerTable中某些字段的值 获取所有的 LocalServerTable
     *
     * @param callback
     */
    public void getLocalServerTableRecordByField(final Map<String,String> filterCondition,
                                                 final TableNetCallback<List<LocalServerTableRecord>> callback) {

        Observable.create(new Observable.OnSubscribe<List<LocalServerTableRecord>>() {
            @Override
            public void call(Subscriber<? super List<LocalServerTableRecord>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    RealmQuery<LocalServerTableRecord> where = realm.where(LocalServerTableRecord.class);
                    Set<Map.Entry<String, String>> entries = filterCondition.entrySet();

                    for (Map.Entry<String, String> entry : entries){
                        where = where.equalTo(entry.getKey(), entry.getValue());
                    }
                    RealmResults<LocalServerTableRecord> results = where.findAll();
                    List<LocalServerTableRecord> list = realm.copyFromRealm(results);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalServerTableRecord>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<LocalServerTableRecord> result) {
                        callback.onSuccess(result);
                    }
                });



       /* Observable.create(new Observable.OnSubscribe<List<LocalServerTableRecord>>() {
                @Override
                public void call(Subscriber<? super List<LocalServerTableRecord>> subscriber) {
                    try {
                        Realm realm = Realm.getDefaultInstance();
                        RealmResults<LocalServerTableRecord> results =
                                realm.where(LocalServerTableRecord.class)
                                        .equalTo(fieldName, fieldValue)
                                        .findAll();
                        List<LocalServerTableRecord> list = realm.copyFromRealm(results);
                        subscriber.onNext(list);
                        subscriber.onCompleted();
                        realm.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<LocalServerTableRecord>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            callback.onError(e.getLocalizedMessage());
                        }

                        @Override
                        public void onNext(List<LocalServerTableRecord> result) {
                            callback.onSuccess(result);
                        }
                    });*/

    }

    /**
     * 根据 设定LocalServerTable中某个字段的值 获取所有的 LocalServerTable
     *
     * @param fieldName  LocalServerTable中的字段名称
     * @param fieldValue 字段的值
     * @param callback
     */
    public void getLocalServerTableRecordByField2(final String fieldName, final String fieldValue,
                                                  final TableNetCallback<List<LocalServerTableRecord2>> callback) {

        Observable.create(new Observable.OnSubscribe<List<LocalServerTableRecord2>>() {
            @Override
            public void call(Subscriber<? super List<LocalServerTableRecord2>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<LocalServerTableRecord2> results =
                            realm.where(LocalServerTableRecord2.class)
                                    .equalTo(fieldName, fieldValue)
                                    .findAll();
                    List<LocalServerTableRecord2> list = realm.copyFromRealm(results);
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LocalServerTableRecord2>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<LocalServerTableRecord2> result) {
                        callback.onSuccess(result);
                    }
                });
    }


    /**
     * 删除所有本地保存的记录
     */
    public void deleteAllLocalServerTableRecord() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalServerTableRecord> localServerTableRecord2s = realm.where(LocalServerTableRecord.class)
                .findAll();
        realm.beginTransaction();
        localServerTableRecord2s.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();

    }

    /**
     * 删除所有本地保存的表单
     */
    public void deleteAllLocalServerTable() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalServerTable> localServerTableRecord2s = realm.where(LocalServerTable.class)
                .findAll();
        realm.beginTransaction();
        localServerTableRecord2s.deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();

    }
}

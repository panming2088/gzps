package com.augurit.agmobile.patrolcore.common.table.dao;

import android.content.Context;


import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTable;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBCallback;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.AllFormTableItems;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetCallback;
import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableNetService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.model.Project;
import com.augurit.agmobile.patrolcore.common.table.model.ProjectTable;
import com.augurit.agmobile.patrolcore.common.table.model.TableChildItem;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;
import com.augurit.agmobile.patrolcore.survey.model.LocalServerTableRecord2;
import com.augurit.am.cmpt.common.base.BaseInfoManager;
import com.augurit.am.cmpt.login.service.LoginService;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.annotations.PrimaryKey;

/**
 * 描述：暴露接口以供使用字典数据
 * 通过  数据库 和网络服务进行数据同步更新
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model.manager
 * @createTime 创建时间 ：17/3/6
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/6
 * @modifyMemo 修改备注：
 */

public class TableDataManager {
    private TableDBService tableDbService;

    private TableNetService tableNetService;

    private LoginService mLoginService;

    private String serverUrl = "http://192.168.30.27:8088/";

    private Context mContext;

    public TableDataManager(Context context) {
        tableNetService = new TableNetService(context, serverUrl);
        tableDbService = new TableDBService(context);
        mLoginService = new LoginService(context, AMDatabase.getInstance());
        this.mContext = context;
    }

    /**
     * 从服务器获取字典数据
     *
     * @param url
     * @param callback
     */
    public void getDictionaryByNet(String url, TableNetCallback callback) {
        tableNetService.getDictionaryByNet(url, callback);
    }

    /**
     * 从的数据库获取所有字典数据
     *
     * @modify xjx 2017-08-16
     */
    public List<DictionaryItem> getAllDictionariesFromDB() {
        return tableDbService.getAllDictionaries();
    }

    /**
     * 从服务器获取项目数据
     *
     * @param url
     * @param callback
     */
    public void getAllProjectByNet(String url, TableNetCallback callback) {
        tableNetService.getProjectDataByNet(url, callback);
    }

    /**
     * 将字典数据存入数据库
     *
     * @param dictionaryItems
     */
    public void setDictionaryToDB(List<DictionaryItem> dictionaryItems) {
        tableDbService.setDictionaryToDB(dictionaryItems);
    }

    @Deprecated
    public void setTableItemsToDB(List<TableItem> list) {
        tableDbService.setTableItemsToDB(list);
    }

    /**
     * 从服务器获取表格模板子项数据
     *
     * @param projectId
     * @param url
     * @param callback
     */
    public void getTableItemsFromNet(String projectId, String url, TableNetCallback callback) {
        tableNetService.getTableItemsNetData(projectId, url, callback);
    }


    /**
     * 从服务器获取表格模板子项中需要的下拉数据
     *
     * @param url
     * @param callback
     */
    public void getTableChildItemsFromNet(String url, TableNetCallback callback) {
        tableNetService.getTableChildItemsByNet(url, callback);
    }


    @Deprecated
    public List<TableChildItem> getTableChildItemsFromDB(String typeCode) {
        List<TableChildItem> list = new ArrayList<>();
        list = tableDbService.getTableChildItemsByDB(typeCode);
        return list;
    }

    @Deprecated
    public void setTableChildItemsToDB(List<TableChildItem> list, String typeCode) {
        tableDbService.setTableChildItemsToDB(list, typeCode);
    }


    /**
     * 通过code从字典数据中获取下拉数据
     *
     * @param code
     * @return
     */
    public List<TableChildItem> getTableChildItemsByTypeCode(String code) {
        List<TableChildItem> list = new ArrayList<>();
        List<DictionaryItem> dictionaryItems = tableDbService.getDictionaryByTypecodeInDB(code);
        convertDictionaryToTableChild(list, dictionaryItems);
        return list;
    }

    /**
     * 把DictionaryItem转换成TableChildItem
     *
     * @param list
     * @param dictionaryItems
     */
    private void convertDictionaryToTableChild(List<TableChildItem> list, List<DictionaryItem> dictionaryItems) {
        for (DictionaryItem dictionaryItem : dictionaryItems) {
            TableChildItem tableChildItem = new TableChildItem();
            tableChildItem.setName(dictionaryItem.getName());
            tableChildItem.setTypeCode(dictionaryItem.getType_code());
            tableChildItem.setCode(dictionaryItem.getCode());
            tableChildItem.setId(dictionaryItem.getId());
            tableChildItem.setTypeName(dictionaryItem.getType_name());
            tableChildItem.setValue(dictionaryItem.getValue()); // 2017-7-24 加入value字段
            if(dictionaryItem.getNote() != null) {
                tableChildItem.setNote(dictionaryItem.getNote());
            }
            list.add(tableChildItem);
        }
    }

    /**
     * 通过pcode筛选联动的子下拉数据
     *
     * @param pcode
     * @return
     */
    public List<TableChildItem> getTableChildItemsByPCode(String pcode) {
        List<TableChildItem> list = new ArrayList<>();
        List<DictionaryItem> dictionaryItems = tableDbService.getChildDictionaryByPCodeInDB(pcode);
        convertDictionaryToTableChild(list, dictionaryItems);
        return list;
    }

    /**
     * 将已编辑的表格实体存进数据库
     *
     * @param item
     * @param photos
     * @param valueMap
     */
    @Deprecated
    public void setEditedTableItemToDB(String project_id, List<TableItem> item, List<Photo> photos, Map<String, String> valueMap) {
        tableDbService.setEditedTableItemToDB(project_id, item, photos, valueMap);
    }

    /**
     * 将已编辑的表格实体存进数据库
     *
     * @param item
     * @param photos
     * @param valueMap
     */
    public void setEditedTableItemToDB(String project_id, List<TableItem> item, Map<String, List<Photo>> photos, Map<String, String> valueMap) {
        tableDbService.setEditedTableItemToDB(project_id, item, photos, valueMap);
    }
   /**
     * 将已编辑的表格实体存进数据库(四标四实项目)
     *
     * @param item
     * @param photos
     * @param valueMap
     */
    public void setEditedTableItemToDB2(String project_id,String recordId, List<TableItem> item, Map<String, List<Photo>> photos, Map<String, String> valueMap) {
        tableDbService.setEditedTableItemToDB2(project_id,recordId, item, photos, valueMap);
    }

    /**
     * 将已编辑的表格实体存进数据库
     *
     * @param item
     * @param photos
     * @param valueMap
     */
    public void setEditedTableItemToDB(String tableKey, String project_id, List<TableItem> item, Map<String, List<Photo>> photos, Map<String, String> valueMap) {
        tableDbService.setEditedTableItemToDB(tableKey, project_id, item, photos, valueMap);
    }

    /**
     * 将已编辑的表格实体存进数据库
     *
     * @param item
     * @param photos
     * @param valueMap
     */
    @Deprecated
    public void setEditedTableItemToDB(String tableName, String tableKey, String project_id, List<TableItem> item, Map<String, List<Photo>> photos, Map<String, String> valueMap) {
        tableDbService.setEditedTableItemToDB(tableKey, project_id, item, photos, valueMap);
    }


    /**
     * 从数据库中获取已编辑的表格实体
     *
     * @return
     */
    public List<LocalTable> getEditedTableItemsFromDB() {
        return tableDbService.getEditedTableItemsFromDB();
    }


    /**
     * 根据项目id从数据库中获取已编辑的表格实体
     *
     * @return
     */
    public List<LocalTable> getEditedTableItemsFromDB(String project) {
        return tableDbService.getEditedTableItemsFromDB(project);
    }

    /**
     * 上传编辑的表格文字内容
     *
     * @param url
     * @param project_id
     * @param valueMap
     * @param list
     * @param callback
     */
    public void uploadTableItems(String url, String project_id, Map<String, String> valueMap, List<TableItem> list, TableNetCallback callback) {
        String loginName = mLoginService.getUser().getLoginName();
        tableNetService.uploadTableItems(url, project_id, loginName, list, valueMap, callback);
    }

    /**
     * 上传编辑的表格文字内容
     *
     * @param url
     * @param project_id
     * @param valueMap
     * @param list
     * @param callback
     * @param recordId   服务器给的记录ID
     */
    public void uploadTableItems(String url, String recordId, String project_id, Map<String, String> valueMap, List<TableItem> list, TableNetCallback callback) {
        String loginName = mLoginService.getUser().getLoginName();
        tableNetService.uploadTableItems(url, recordId, project_id, loginName, list, valueMap, callback);
    }

    /**
     * 上传编辑的表格文字内容
     *
     * @param url
     * @param project_id
     * @param valueMap
     * @param list
     * @param callback
     * @param taskId     该上报内容所在的任务ID
     * @param recordId   服务器给的记录ID
     */
    public void uploadTableItems(String url, String taskId, String recordId, String project_id, Map<String, String> valueMap, List<TableItem> list, TableNetCallback callback) {
        String loginName = mLoginService.getUser().getLoginName();
        tableNetService.uploadTableItems(url, taskId, recordId, project_id, loginName, list, valueMap, callback);
    }

    /**
     * 上传编辑的表格文字内容,传递的参数加多一个username xcl 8.31
     *
     * @param url
     * @param project_id
     * @param valueMap
     * @param list
     * @param callback
     * @param taskId     该上报内容所在的任务ID
     * @param recordId   服务器给的记录ID
     * @param userName
     */
    public void uploadTableItemsWithUserName(String url, String userName, String taskId, String recordId, String project_id, Map<String, String> valueMap, List<TableItem> list,Map<String,String> photoNameMap, TableNetCallback callback) {
        String loginName = mLoginService.getUser().getLoginName();
        tableNetService.uploadTableItems(url,userName, taskId, recordId, project_id, loginName, list, valueMap,photoNameMap, callback);
    }

    /**
     * 上传编辑的图片数据
     *
     * @param url
     * @param patrolCode
     * @param photos
     * @param callback
     */
    public void uploadPhotos(String url, String patrolCode, List<Photo> photos, TableNetCallback callback) {
        tableNetService.uploadPhotos(url, patrolCode, photos, callback);
    }

    /**
     * 上传编辑的图片数据
     */
    public void uploadPhotos(String url, Map<String, List<Photo>> photosMap, String prefix, TableNetCallback callback) {
        tableNetService.uploadPhotos(url, photosMap, prefix, callback);
    }

    /**
     * 从数据库中获取编辑表格中保存的图片数据
     *
     * @param problem_id
     * @return
     */
    public List<Photo> getPhotoFormDB(String problem_id) {
        return tableDbService.getPhotosFromDB(problem_id);
    }

    public void deletePhotoInDB(Photo photo){
        tableDbService.deleteEditTablePhotoInDB(photo);
    }
    /**
     * 将项目表格模板实体类型存进数据库
     *
     * @param tables
     */
    public void setProjectTableToDB(ProjectTable tables) {
        tableDbService.setProjectTableToDB(tables);
    }

    /**
     * 获取项目表格模板实体类型数据
     *
     * @param project_id
     * @return
     */
    public List<ProjectTable> getProjectTableFromDB(String project_id) {
        return tableDbService.getProjectTableFormDB(project_id);
    }

    /**
     * 删除项目表格模板实体类型
     *
     * @param project_id
     */
    public void deleteProjectTableFromDB(String project_id) {
        tableDbService.deleteProjectTableFromDB(project_id);
    }

    /**
     * 删除所有的项目表格模板实体类型
     */
    public void deleteAllProjectTableFromDB() {
        tableDbService.deleteAllProjectTableFromDB();
    }


    /**
     * 保存项目数据到数据库
     *
     * @param projects
     */
    public void setProjectToDB(List<Project> projects) {
        tableDbService.setProjectToDB(projects);
    }

    /**
     * 从数据库中获取项目数据
     *
     * @return
     */
    public List<Project> getProjectFromDB() {
        return tableDbService.getProjectFromDB();
    }

    /**
     * 删除数据库中的项目数据
     */
    public void deleteProjectInDB() {
        tableDbService.deleteProjectInDB();
    }


    /**
     * 删除已经编辑的保存到数据中的表格实例数据
     *
     * @param key
     * @param callback
     */
    public void deleteEditedTableItemsFromBD(String key, TableDBCallback callback) {
        tableDbService.deleteEditedTableItemsFromBD(key, callback);
    }

    /**
     * 删除已经编辑的保存到数据中的表格实例数据
     *
     * @param key
     * @param callback
     */
    public void deleteEditedTableItemsFromBD(List<String> key, TableDBCallback callback) {
        tableDbService.deleteEditedTableItemsFromBD(key, callback);
    }


    /**
     * 同步更新所有项目的表格项(TableItem)
     *
     * @param callBack
     */
    public void syncAllFormsTableItem(final CallBack callBack) {
        String serverUrl = BaseInfoManager.getBaseServerUrl(mContext);
        String url = serverUrl + "rest/report/allForms";
        tableNetService.getAllFormTableItemsByNet(url, new TableNetCallback() {
            @Override
            public void onSuccess(Object data) {
                AllFormTableItems allFormTableItems = (AllFormTableItems) data;
                if (allFormTableItems.getSuccess().equals("true")) {
                    List<AllFormTableItems.ProjectItem> projectItemList = allFormTableItems.getResult();
                    List<TableItem> allTableItems = new ArrayList<TableItem>();
                    for (AllFormTableItems.ProjectItem item : projectItemList) {
                        allTableItems.addAll(item.getColumns());
                    }
                    tableDbService.setTableItemsToDB(allTableItems);
                    callBack.onSuccess();
                } else {
                    callBack.onFail("同步数据失败:" + allFormTableItems.getMessage());
                }
            }

            @Override
            public void onError(String msg) {
                callBack.onFail(msg);
            }
        });
    }


    public List<LocalServerTableRecord2> getAllLocalServerTableRecord2(){
        return tableDbService.getAllLocalServerTableRecord();
    }

    public interface CallBack {
        void onSuccess();

        void onFail(String msg);
    }

}

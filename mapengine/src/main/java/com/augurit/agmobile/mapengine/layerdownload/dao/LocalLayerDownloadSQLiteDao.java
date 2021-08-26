package com.augurit.agmobile.mapengine.layerdownload.dao;

import com.augurit.agmobile.mapengine.layerdownload.model.LayerDnlTask;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.db.AMQueryBuilder;
import com.augurit.am.fw.utils.ListUtil;

import java.util.List;

/**
 * 图层下载任务的本地数据库管理
 * @author 创建人 ：liangshenghong
 * @package 包名 ：com.augurit.agmobile.mapengine.layerdownload.dao
 * @createTime 创建时间 ：2017-02-13
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-13
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public class LocalLayerDownloadSQLiteDao {

    AMDatabase database;

    public LocalLayerDownloadSQLiteDao(){
        database = AMDatabase.getInstance();
    }

    /**
     * 获取所有下载任务
     * @return
     */
    public List<LayerDnlTask> getTasks(){
        return database.getQueryAll(LayerDnlTask.class);
    }

    /**
     * 获取专题下所有下载任务
     * @param projectId 专题ID
     * @return
     */
    public List<LayerDnlTask> getTasks(String projectId){
        return database.query(
                new AMQueryBuilder<LayerDnlTask>(LayerDnlTask.class)
                        .where("projedtId=?", projectId));
    }

    /**
     * 获取下载任务
     * @param id 任务ID
     * @return 下载任务
     */
    public LayerDnlTask getTaskById(int id){
        return database.getQueryById(LayerDnlTask.class, id);
    }

    /**
     * 获取下载任务
     * @param layerId 图层ID
     * @param layerName 图层名称
     * @return 下载任务
     */
    public LayerDnlTask getTaskByLayerIdAndLayerName(int layerId, String layerName){
        List<LayerDnlTask> results = database.query(
                new AMQueryBuilder<LayerDnlTask>(LayerDnlTask.class)
                        .where("layerId=? and layerName=?", ""+layerId, layerName));
        if(ListUtil.isEmpty(results)){
            return null;
        }
        return results.get(0);
    }

    /**
     * 保存下载任务
     * @param task 待保存任务
     */
    public void saveTask(LayerDnlTask task){
        database.save(task);
    }
}

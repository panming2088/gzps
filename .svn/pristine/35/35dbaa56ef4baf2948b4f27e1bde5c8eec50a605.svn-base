package com.augurit.agmobile.mapengine.layerdownload.view;

import android.view.View;

import com.augurit.agmobile.mapengine.layerdownload.model.LayerDnlTask;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述：图层下载视图接口
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerdownload.view
 * @createTime 创建时间 ：2017-02-14
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-14
 * @modifyMemo 修改备注：
 */
public interface ILayerDownloadView {

    /**
     * 初始化下载任务列表
     * @param tasks 下载任务
     */
    void initTaskList(ArrayList<LayerDnlTask> tasks);

    /**
     * 添加任务
     * @param task 下载任务
     */
    void addTaskList(LayerDnlTask task);

    /**
     * 更新下载任务
     * @param taskId id
     * @param total  需下载总量
     * @param downed 已下载
     * @param done   是否已完成
     * @param e      异常
     */
    void updateTask(int taskId, double total, double downed, boolean done, Throwable e);

    /**
     * 设置图层数据
     * @param projectInfos 专题信息
     * @param layerInfosMap 键为专题Id,值为图层信息的Map
     */
    void setLayerData(List<ProjectInfo> projectInfos,
                      Map<String, List<LayerInfo>> layerInfosMap);

    /**
     * 设置当前专题名称
     * @param projectName 专题名称
     */
    void setProjectName(String projectName);

    /**
     * 获取图层下载视图
     * @return
     */
    View getDnlMgrView();
}

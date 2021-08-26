package com.augurit.agmobile.mapengine.project.view;


import com.augurit.am.fw.common.IPresenter;
import com.augurit.am.fw.common.IView;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.amlib.proj.prstr
 * @createTime 创建时间 ：2016-11-01
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-01
 */
public interface IProjectPresenter extends IPresenter {
    /**
     * 加载列表
     */
    void loadProjects();

    void setCurrentProjectById(String id);

    /**
     * 获取专题视图
     * @return
     */
    IView getView();

    void attachView(IView view);

    void detachView(boolean shouldRetainInstance);

    void setOnProjectChangeListener(OnProjectChangeListener onProjectChangeListener);

    interface OnProjectChangeListener{
        void onChanged(ProjectInfo projectInfo);
    }
}

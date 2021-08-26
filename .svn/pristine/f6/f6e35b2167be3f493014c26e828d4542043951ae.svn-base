package com.augurit.agmobile.mapengine.project.view;

import com.augurit.am.fw.common.IView;
import com.augurit.agmobile.mapengine.project.model.ProjectInfo;

import java.util.List;


/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augur.agmobile.amlib.proj.view
 * @createTime 创建时间 ：2016-11-01
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-01
 */
public interface IProjectView extends IView {

    void addProjectViewToContainer();

    void initProjectView(List<ProjectInfo> projectInfos,int selectedPosition);

    void refreshProjectView(List<ProjectInfo> projectInfos);

    void showLoadingView();

    void hideLoadingView();

    void hideProjectView();

    void showProjectView();

    void removeProjectView();

    void hideContainer();

    void showLoadProjectErrorView();

    void setOnProjectChangeListener(IProjectPresenter.OnProjectChangeListener listener);
}

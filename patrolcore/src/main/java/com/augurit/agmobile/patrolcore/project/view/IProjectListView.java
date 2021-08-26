package com.augurit.agmobile.patrolcore.project.view;

import android.view.ViewGroup;


import com.augurit.agmobile.patrolcore.common.table.model.Project;

import java.util.List;

/**
 * 描述：项目列表
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.setting.problem.view
 * @createTime 创建时间 ：17/3/21
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/21
 * @modifyMemo 修改备注：
 */

public interface IProjectListView {
    void onShowProjectListView(List<Project> projects, ViewGroup container);
}

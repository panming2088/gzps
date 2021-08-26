package com.augurit.agmobile.patrolcore.localhistory.view;

import android.view.ViewGroup;


import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTable;

import java.util.List;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.setting.problem.view
 * @createTime 创建时间 ：17/3/21
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/21
 * @modifyMemo 修改备注：
 */

public interface ILocalProblemView {
    void onShowLocalProblemListView(List<LocalTable> problems, ViewGroup container, ViewGroup menuContainer, ViewGroup multiContainer);

    void onDeleteLocalProbelm();

    /**
     * 批量提交过程中更新列表
     */
    void refreshListView(LocalTable position);
}

package com.augurit.agmobile.patrolcore.localhistory.view;



import com.augurit.agmobile.patrolcore.common.table.dao.local.LocalTable;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBCallback;

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

public interface ILocalProblemPresenter {
    void showLocalProblemListView();

    void deleteLocalTable(String projectId, TableDBCallback callback);

    /**
     * 批量上传所有本地保存,上传过程中可中断
     */
    void uploadAllTable(List<LocalTable> list);

}

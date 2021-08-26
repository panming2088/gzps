package com.augurit.agmobile.gzps.maintain.view;

import com.augurit.agmobile.gzps.maintain.model.MaintainBatchInfo;

import java.util.Set;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.feedback
 * @createTime 创建时间 ：2018-06-01
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2018-06-01
 * @modifyMemo 修改备注：
 */

public interface MaintainFilterGroupListener {
    void filterByIds(Set<MaintainBatchInfo.RowsBean> selectedList);

    void getPrePage();

    void getNextPage();

    void searchFor(String s);
}

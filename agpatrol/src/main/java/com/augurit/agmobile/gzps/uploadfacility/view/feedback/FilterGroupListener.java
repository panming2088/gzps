package com.augurit.agmobile.gzps.uploadfacility.view.feedback;

import com.augurit.agmobile.gzps.uploadfacility.model.BatchInfo;

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

public interface FilterGroupListener {
    void filterByIds(Set<BatchInfo.Info> selectedList);

    void getPrePage();

    void getNextPage();

    void searchFor(String s);
}

package com.augurit.agmobile.gzps.trackmonitor.view;

import com.augurit.agmobile.gzps.trackmonitor.model.UserOrg;
import com.augurit.agmobile.gzps.trackmonitor.view.presenter.ISubordinateListPresenter;

import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.trackmonitor.view
 * @createTime 创建时间 ：2017-08-21
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-08-21
 * @modifyMemo 修改备注：
 */

public interface ISubordinateListView {

    void setPresenter(ISubordinateListPresenter subordinateListPresenter);
    void showSubordinateList(List<UserOrg> subordinateList);
}

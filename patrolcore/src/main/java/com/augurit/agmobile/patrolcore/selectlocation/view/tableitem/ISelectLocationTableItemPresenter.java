package com.augurit.agmobile.patrolcore.selectlocation.view.tableitem;

import android.content.Intent;
import android.view.ViewGroup;



import com.augurit.agmobile.patrolcore.selectlocation.view.IMapTableItemPresenter;
import com.augurit.am.fw.common.IPresenter;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view.tableitem
 * @createTime 创建时间 ：17/11/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/1
 * @modifyMemo 修改备注：
 */

public interface ISelectLocationTableItemPresenter extends IPresenter {

    void setView(ISelectLocationTableItemView tableItem);

    ISelectLocationTableItemView getView();

    void loadMap();

    void addTo(ViewGroup container);

    void setAddress(String address);

    void setGeometry(Geometry geometry);

    void startLocate();

    void setOnReceivedSelectedLocationListener(IReceivedSelectLocationListener onReceivedSelectedLocationListener);

    void exit();

    void addGeometry(Geometry geometry);

    /**
     * 填充到传递给下一个界面的数据
     * @return
     */
    void completeIntent(Intent intent);

    /**
     * 设置编辑模式，有两种值：{@link com.augurit.agmobile.patrolcore.editmap.util.EditModeConstant#EDIT_LINE}或者
     * {@link com.augurit.agmobile.patrolcore.editmap.util.EditModeConstant#EDIT_POINT}
     * @param editMode
     */
    void setEditMode(String editMode);

}

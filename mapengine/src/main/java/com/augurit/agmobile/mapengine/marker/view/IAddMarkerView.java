package com.augurit.agmobile.mapengine.marker.view;

import android.content.Context;

import com.augurit.am.fw.common.IView;
import com.augurit.agmobile.mapengine.marker.model.Mark;
import com.augurit.agmobile.mapengine.marker.model.PointStyle;
import com.esri.android.map.ags.ArcGISFeatureLayer;

import java.util.List;

/**
 * 添加标注的视图接口
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.marker
 * @createTime 创建时间 ：2017-02-25
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-25
 * @modifyMemo 修改备注：
 */

public interface IAddMarkerView extends IView{

    void showView();

    void hideView();

    void showControllerView();

    void hideControllerView();

    void hideTopBarView();

    void showAddPointDialog(ArcGISFeatureLayer layer, Mark graphic, List<PointStyle> pointStyles);

    void showAddLineDialog(ArcGISFeatureLayer layer, Mark graphic);

    void showAddPolygonDialog(ArcGISFeatureLayer layer, Mark graphic);

    void showLoadingDialog();

    void hideLoadingDialog();

    void showAddButton();

    void showWarningView();

    void hideWarningView();

    Context getActivity();

    void  performAddButtonClick();

    void performCloseButtonClick();

    /**
     * 退出添加标注的总视图
     */
    void exitAddOperationView();

    void clearMapThenRestoreMapOnTouchListener();

}

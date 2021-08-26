package com.augurit.agmobile.mapengine.marker.view;

import android.content.Context;

import com.augurit.am.fw.common.IView;
import com.augurit.agmobile.mapengine.marker.model.Mark;
import com.augurit.agmobile.mapengine.marker.model.PointStyle;
import com.augurit.agmobile.mapengine.marker.view.OnMarkListClickListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Geometry;

import java.util.List;

/**
 * 标注结果视图接口
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.marker
 * @createTime 创建时间 ：2017-02-25
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-25
 * @modifyMemo 修改备注：
 */

public interface IMarkerResultView extends IView {

    void showView();

    void hideView();

   // void notifyForDataChanged(int position);

    void showLoading();

    void hideLoading();

    void clearInstance();

    int drawSingleMark(Mark mark);

    void removeAllMarksOnMap();

    //void onApplyDeleteError();

    Context getApplicationContext();

    void showPopupView(Mark mark);

    void highligthMark(int[] id);

    MapView getMapView();

    void refreshMarkList(List<Mark> marks);

    void showEditPointView(ArcGISFeatureLayer featureLayer, List<PointStyle> pointStyles, Mark mark);

    void showEditLineView(ArcGISFeatureLayer featureLayer,Mark mark);

    void showEditPolygonView(ArcGISFeatureLayer featureLayer, Mark mark);

    void initListener();

  //  void showErrorView();

    void hideCallout();

    void showEmptyResultView();

    Context getActivity();

    void setOnMarkListClickListener(OnMarkListClickListener onMarkListClickListener);

    void removeGraphicLayer();

   // void addGraphicLayer();

   // void showResultList();

    void hideResultList();

    void setSelectedPosition(int positon);

    void clearSelectedPosition();

    void restoreMapOnTouchListener();

    void drawMarksOnMap(List<Mark> marks);

    void onDeleteMarksSuccess(Mark mark);

    Mark getMarkByGraphicId(int graphicId);

    int getPositionInMarkList(int graphicId, Geometry geometry);

    void onAddedSuccess(Mark mark);

    void onEditSuccess(Mark mark);

    List<Mark> getCurrentCacheMarks();

    /**
     * 展示搜索结果
     * @param marks
     */
    void showSearchResult(List<Mark> marks);

    /**
     * 重新显示完整的标注列表
     */
    void returnToMarkList();
}

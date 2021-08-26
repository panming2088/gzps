package com.augurit.agmobile.mapengine.layerquery.view;

import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.common.IPresenter;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.esri.core.geometry.Geometry;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerquery.view
 * @createTime 创建时间 ：2017-02-07
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-07
 * @modifyMemo 修改备注：
 */

public interface ILayerQueryResultPresenter extends IPresenter {

    /**
     * 进行图层查询，如果geometry传入Null，默认查询当前范围
     * @param geometry 要查询的范围
     */
    void queryLayers(Geometry geometry);


    void onBackButtonClick();

    void clearInstance();

    void clearMap();

    void loadMore();

    AMFindResult getFindResult(int positonInList);

    void setOnFinishQueryCallback(Callback2<List<AMFindResult>> callback);
 //   double getBestResolutionForReading();

}

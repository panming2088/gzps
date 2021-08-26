package com.augurit.agmobile.mapengine.layerquery.view;

import android.app.Activity;
import android.view.View;

import com.augurit.agmobile.mapengine.common.ILoadingView;
import com.augurit.am.fw.common.IView;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;

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

public interface ILayerQueryConditionView extends IView,ILoadingView {

    void initConditionView(List<LayerInfo> queryableLayers ,
                           List<String> searchHistories);

    void showConditionView();

    void hideConditionView();

    Activity getActivity();

    View getRootView();

    void addConditionViewToContainer();

    boolean ifRootViewNull();



}

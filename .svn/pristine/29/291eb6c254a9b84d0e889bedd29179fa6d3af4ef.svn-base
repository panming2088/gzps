package com.augurit.agmobile.mapengine.layermanage.view;

import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.common.IPresenter;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.layermanage.view
 * @createTime 创建时间 ：2017-01-18
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-18
 */

public interface ILayerPresenter extends IPresenter {

    void loadLayer();

    void loadLayer(Callback2<Integer> callback);

    void showLayerList();

   // void onAllSelectedButtonClicked();

   // void onAllUnSelectedButtonClick();

    void onClickCheckbox(String groupName,  int layerId, LayerInfo currentLayer, boolean ifShow);

    void onClickOpacityButton(int layerId, LayerInfo currentLayer, float opacity);

    void clearInstance();

    ILayersService getService();
}

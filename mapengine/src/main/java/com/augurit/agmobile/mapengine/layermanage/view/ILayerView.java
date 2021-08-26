package com.augurit.agmobile.mapengine.layermanage.view;

import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.am.fw.common.IView;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;

import java.util.List;

/**
 * 图层列表视图
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.layermanage.view
 * @createTime 创建时间 ：2017-01-18
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-18
 */

public interface ILayerView extends IView {

      interface OnLayerItemClickListener {
            void onClickCheckbox(int layerId, String groupName, LayerInfo currentLayer, boolean ifShow);
            void onOpacityChange(int layerId, String groupName, LayerInfo currentLayer, float opacity);
      }

      interface  OnHolderItemClickListener{
          void onItemClick(Object item);
      }


    void initView();
    /**
     * 显示正在加载地图中
     */
    void showLoadingMap();

    /**
     * 隐藏正在加载地图视图
     */
    void hideLoadingMap();

    /**
     * 显示正在加载图层列表中
     */
    void showLoadingView();

    void hideLoadingView();

    void showLoadLayerFailMessage(Exception e);

    void addLayerToMapView(Layer layer);

    View createLayerView(List<LayerInfo> layerInfoList);

    void addLayerViewToTreeViewContainer(View view);

    void removeAllLayers();

    MapView getMapView();

    View getRootView();

    void addToContainer();

    void toggle(LayerInfo layerInfo,boolean checked);

    void setContainer(ViewGroup viewGroup);
}

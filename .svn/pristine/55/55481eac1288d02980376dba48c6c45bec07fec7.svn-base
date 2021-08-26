package com.augurit.agmobile.mapengine.layerquery.view;

import com.augurit.am.fw.common.IPresenter;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.esri.core.map.Field;

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

public interface ILayerQueryConditionPresenter extends IPresenter {

    void showConditionView();

    void saveQueryKey(String history);

    void onSelectConditionFinish(String key,List<LayerInfo> selectedLayers);

    void onSelectConditionFinish(String key,List<LayerInfo> selectedLayers,String queryFields);

    List<String> suggest(String key);

    void clearInstance();

    void retryWhenLoadedFail();

    List<Field> queryAllFileds(LayerInfo layer);
}

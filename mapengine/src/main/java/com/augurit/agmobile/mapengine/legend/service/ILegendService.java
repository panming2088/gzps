package com.augurit.agmobile.mapengine.legend.service;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.legend.model.LayerLegend;
import com.augurit.agmobile.mapengine.legend.model.Legend;

import java.util.List;

import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.legend.service
 * @createTime 创建时间 ：17/7/19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/19
 * @modifyMemo 修改备注：
 */

public interface ILegendService {

    Observable<List<Legend>> getCurrentVisibleLayerLegends();

    Observable<List<LayerLegend>> getLegendsFromLayers(List<LayerInfo> visibleLayer);
}

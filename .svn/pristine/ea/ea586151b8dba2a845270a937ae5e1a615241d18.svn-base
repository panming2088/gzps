package com.augurit.agmobile.patrolcore.common.legend;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.am.fw.common.IPresenter;

import java.util.List;

/**
 * Created by xcl on 2017/10/30.
 */

public interface ILegendPresenter extends IPresenter {

    /**
     * 当父类是{@link com.augurit.agmobile.mapengine.layermanage.service.LayersService}时才调用该方法，否则请调用{@link #showLegends(List)}
     */
    void showLegends();

    void showLegends(List<LayerInfo> visibleLayerInfos);
}

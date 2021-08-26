package com.augurit.agmobile.gzps.layer;

import android.support.annotation.NonNull;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;

import java.util.Iterator;
import java.util.List;

/**
 * 加载部件图层_新的Presenter
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.layer
 * @createTime 创建时间 ：17/12/19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/19
 * @modifyMemo 修改备注：
 */

public class IncludeNewComponentLayerPresenter extends PatrolLayerPresenter {

    public IncludeNewComponentLayerPresenter(ILayerView layerView, ILayersService layersService) {
        super(layerView, layersService);
    }

    public IncludeNewComponentLayerPresenter(ILayerView layerView) {
        super(layerView);
    }

    /**
     * 过滤掉不需要显示的图层（但是不过滤掉部件图层_新）
     *
     * @param layerInfos
     * @return
     */
    @Override
    @NonNull
    protected List<LayerInfo> filterLayerNoNeedToAdd(List<LayerInfo> layerInfos) {
        Iterator<LayerInfo> iterator = layerInfos.iterator();
        while (iterator.hasNext()) {
            LayerInfo next = iterator.next();
            if (!mILayersService.ifActiveLayer(next)) {
                iterator.remove();
            }
        }
        return layerInfos;
    }
}

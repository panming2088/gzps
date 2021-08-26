package com.augurit.agmobile.patrolcore.common.legend;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.legend.model.LayerLegend;
import com.augurit.agmobile.mapengine.legend.service.ILegendService;
import com.augurit.agmobile.mapengine.legend.view.ILegendView;
import com.augurit.am.fw.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 注意：如果不是当前图层的Service不是继承自{@link com.augurit.agmobile.mapengine.layermanage.service.LayersService}，请调用{@link #showLegends(List)}，
 * 不要调用{@link #showLegends()}，原因是：showLegends（）最终会引用到{@link com.augurit.agmobile.mapengine.layermanage.service.LayersService}里面的
 * 静态变量，如果父类不是{@link com.augurit.agmobile.mapengine.layermanage.service.LayersService}，此时将无法正确初始化这些静态变量的值，
 * 从而导致『当前地图没有地图显示』的问题；
 * <p>
 * Created by xcl on 2017/10/30.
 */

public class LegendPresenter implements ILegendPresenter {

    protected ILegendView legendView;
    protected ILegendService legendService;
    //private MapView mMapView;

    public LegendPresenter(ILegendView legendView, ILegendService legendService) {
        // this.mMapView = mapView;
        this.legendView = legendView;
        this.legendView.setPresenter(this);
        this.legendService = legendService;

    }

    /**
     * 当当前图层的Service继承自{@link com.augurit.agmobile.mapengine.layermanage.service.LayersService}，时调用
     */
    @Override
    public void showLegends() {
        List<LayerInfo> visibleLayerInfos = LayerServiceFactory.provideLayerService(legendView.getApplicationContext()).getVisibleLayerInfos();
        List<LayerInfo> tempInfos = new ArrayList<>();
        for (LayerInfo layerInfo : visibleLayerInfos) {
            if (layerInfo.getType() != LayerType.TianDiTu && !layerInfo.isBaseMap()) {
                tempInfos.add(layerInfo);
            }
        }
//        if (ListUtil.isEmpty(tempInfos)) {
//            legendView.showToast("当前地图没有图层显示，请至少选择一个图层进行显示");
//            return;
//        }
        legendView.clearData();
        legendView.showLoadingLegend();
        legendService.getLegendsFromLayers(visibleLayerInfos)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LayerLegend>>() {
                    @Override
                    public void onCompleted() {
                        legendView.hideLoadingLegend();
                    }

                    @Override
                    public void onError(Throwable e) {
                        legendView.showLoadingLegendError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<LayerLegend> legends) {
                        legendView.showLayerLegends(legends);
                    }
                });
    }

    /**
     * 当当前图层的Service不是继承自{@link com.augurit.agmobile.mapengine.layermanage.service.LayersService}，时调用
     *
     * @param visibleLayerInfos
     */
    @Override
    public void showLegends(List<LayerInfo> visibleLayerInfos) {
        List<LayerInfo> tempInfos = new ArrayList<>();
        for (LayerInfo layerInfo : visibleLayerInfos) {
            if (layerInfo.getType() != LayerType.TianDiTu && !layerInfo.isBaseMap()) {
                tempInfos.add(layerInfo);
            }
        }
//        if (ListUtil.isEmpty(tempInfos)) {
//            legendView.showToast("当前地图没有图层显示，请至少选择一个图层进行显示");
//            return;
//        }
        legendView.clearData();
        legendView.showLoadingLegend();
        legendService.getLegendsFromLayers(visibleLayerInfos)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LayerLegend>>() {
                    @Override
                    public void onCompleted() {
                        legendView.hideLoadingLegend();
                    }

                    @Override
                    public void onError(Throwable e) {
                        legendView.showLoadingLegendError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<LayerLegend> legends) {
                        legendView.showLayerLegends(legends);
                    }
                });
    }
}

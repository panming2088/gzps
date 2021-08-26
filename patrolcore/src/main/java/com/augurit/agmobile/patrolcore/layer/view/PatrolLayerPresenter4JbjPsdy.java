package com.augurit.agmobile.patrolcore.layer.view;


import android.support.annotation.NonNull;
import android.util.ArrayMap;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.mapengine.layermanage.view.LayerPresenter;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.android.map.Layer;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agcollection.selectlocation.presenter
 * @createTime 创建时间 ：2017-05-19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-05-19
 * @modifyMemo 修改备注：
 */

public class PatrolLayerPresenter4JbjPsdy extends LayerPresenter {


    protected boolean ifWGS84 = false;
    // public static final double initScale = 9288.327490917793;
    // public static final double initScale = 12484.591278733007;
    public static final double initScale = 4914.154569615676;
    public static final double longitude = 113.33835968915169;
    public static final double latitude = 23.14901648232401;

    /**
     * 上报图层名称
     */
    public static final String UPLOAD_LAYER_NAME = "数据上报";

    public static final String FEEDBACK_LAYER_NAME = "交办反馈";

    public static final String MAINTAIN_LAYER_NAME = "养护计划";

    public static final String MY_UPLOAD_LAYER_NAME = "我的上报";

    public static final String RIVER_FLOW_LAYER_NAME = "河涌";

    public static final String JOURNAL_LAYER_NAME = "巡检日志";

    public static final String LOCAL_UPLOAD_LAYER_NAME = "本区数据";

    public static final String NW_UPLOAD_LAYER_NAME = "农污上报";

    public static final String COMPONENT_LAYER = "部件";
    public static final String DRAINAGE_PIPELINE = "排水管道";
    public static final String DRAINAGE_WELL = "排水管井";
    public static final String DOOR_NO_LAYER = "门牌号";

    public static final String RIVER_DOUBT_LAYER_NAME = "存疑区域";
    public static final String DRAINAGE_UNIT_LAYER = "排水单元";
    public static final String DRAINAGE_UNIT_LAYER2 = "广州排水单元";
    public static final String DRAINAGE_PSHMP = "排水户门牌";

    public PatrolLayerPresenter4JbjPsdy(ILayerView layerView, ILayersService layersService) {
        super(layerView, layersService);
    }

    public PatrolLayerPresenter4JbjPsdy(ILayerView layerView) {
        super(layerView, LayerServiceFactory.provideLayerService(layerView.getApplicationContext()));
    }

    /**
     * agmobile默认全局只有一个mapview，所以同一个专题下，当加载过之后就不允许再次添加相同的图层，在移动巡查中需要去掉
     *
     * @param layers
     * @param map
     */
    @Override
    protected void addLayerToMapView(List<Layer> layers, Map<Layer, Integer> map) {
        for (Layer entry : layers) {
            mILayerView.addLayerToMapView(entry);
            mILayersService.updateAllAddedToMapViewMap(map.get(entry), entry);
        }

    }

    /**
     * 过滤掉不需要显示的图层 以及 部件图层_新
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
            if (!mILayersService.ifActiveLayer(next) || next.getLayerName().contains("_新")|| next.getLayerName().equals("接驳井")
                    || next.getLayerName().equals("河涌")
                    || next.getLayerName().equals("兴趣点")
                    || next.getLayerName().equals("道路")
                    || next.getLayerName().equals("房屋面")
                    || next.getLayerName().equals("巡查人员分布")
                    || next.getLayerName().equals("地上式消防栓")
//                    || next.getLayerName().equals("门牌号")
                    || next.getLayerName().equals("石井街管线")
                    || next.getLayerName().equals("石井街排水单元")
                    || next.getLayerName().equals("接驳井连线")
                    || next.getLayerName().equals("接户井")
                    || next.getLayerName().equals("接驳连线")
                    || next.getLayerName().equals("门牌展图")
                    || next.getLayerName().equals("典型排水户")
                    || next.getLayerName().equals("排水单元")
                    || next.getLayerName().equals("我的上报")
                    || next.getLayerName().equals("标高、管径、流向")
                    || next.getLayerName().equals("关键节点")
//                    || next.getLayerName().equals("排水管道")
//                    || next.getLayerName().equals("排水管井(中心城区)")
//                    || next.getLayerName().equals("排水管道(中心城区)")
//                    || next.getLayerName().equals("排水管井(黄埔管井)")
//                    || next.getLayerName().equals("排水管井")
//                    || next.getLayerName().equals("排水管道(黄埔管道)")
                    || next.getLayerName().equals("存疑区域")
                    || next.getLayerName().equals("部件图层")
            ) {
                iterator.remove();
            }
        }
        return layerInfos;
    }

    @NonNull
    @Override
    protected List<LayerInfo> changeLayerOrder(List<LayerInfo> layerInfos) {
        return layerInfos;
    }

    @Override
    protected void setMapInitState() {

    }

    @Override
    public void onClickCheckbox(String groupName, int layerId, LayerInfo currentLayer, boolean ifShow) {

        if (onLayerVisibilityChangedListener != null) {
            onLayerVisibilityChangedListener.changed(ifShow, currentLayer);
        }

        /**
         *  首先判断是否是{@link LayerType.DynamicLayer_SubLayer},
         *  如果是DaynamicLayer的子图层，那么此时要通过DynamicLayer才能控制它的显示与隐藏。
         */
        if (currentLayer.getType() == LayerType.DynamicLayer_SubLayer) {
            String parentLayerId = currentLayer.getParentLayerId();
            Layer layer = mILayersService.getLayerById(Integer.valueOf(parentLayerId));
            if (layer instanceof ArcGISDynamicMapServiceLayer) {
                ArcGISDynamicMapServiceLayer parentLayer = (ArcGISDynamicMapServiceLayer) layer;
                if (parentLayer.getLayers() != null && parentLayer.getAllLayers().length > layerId) {
                    ArcGISLayerInfo arcGISLayerInfo = parentLayer.getAllLayers()[layerId];
                    arcGISLayerInfo.setVisible(ifShow);
                    parentLayer.refresh();

                    //修改子图层可见性
                    LayerInfo layerInfo = mILayersService.getLayerInfoByLayerId(Integer.valueOf(parentLayerId));
                    List<LayerInfo> childLayer = layerInfo.getChildLayer();
                    if (!ListUtil.isEmpty(childLayer)) {
                        for (LayerInfo layerInfo1 : childLayer) {
                            if (layerInfo1.getLayerId() == layerId) {
                                layerInfo1.setDefaultVisibility(ifShow);
                            }
                        }
                    }

                    //更新缓存中图层数据的子图层数据
                    mILayersService.refreshChildLayerInfo(layerInfo);
                }
            }
            return;
        }

        Layer layer = mILayersService.getLayerById(layerId);

        if (layer != null) {
            //如果allLayer集合中包含这个layer，说明是叠加到地图上的，否则说明并没有叠加到地图上，那么就不进行操作
            mILayersService.changeLayerVisibility(layerId, ifShow);
            //更新缓存中图层数据
            mILayersService.refreshCacheLayers(layerId, ifShow);

            mILayersService.addOrRemoveFromVisibleMap(layerId, currentLayer, ifShow);

            layer.setVisible(ifShow);
        }
    }

    @Override
    public void onClickOpacityButton(int layerId, LayerInfo currentLayer, float opacity) {
        Layer targetLayer = mILayersService.getLayerById(layerId);
        if (targetLayer != null) {
            targetLayer.setOpacity(opacity);
        }

    }


//    /**
//     * 通过图层名称（可以只是名称的一部分）进行操作图层
//     *
//     * @param partlayerName 图层名称（可以只是名称的一部分）
//     * @param ifVisible     是否显示该图层
//     */
//    public void setVisible(final String partlayerName, final boolean ifVisible) {
//        getLayerObservable()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<LayerInfo>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<LayerInfo> layerInfos) {
//                        for (LayerInfo layerInfo : layerInfos) {
//                            if (layerInfo.getLayerName().contains(partlayerName) && layerInfo.isDefaultVisible() != ifVisible) {
//                                onClickCheckbox(layerInfo.getDirTypeName(),
//                                        layerInfo.getLayerId(), layerInfo, ifVisible);
//                            }
//                        }
//                    }
//                });
//    }

    /**
     * 改变图层列表中的图层Toggle
     *
     * @param partlayerName 图层名称（可以只是名称的一部分）
     * @param visible       是否显示该图层
     */
    public void changeLayerVisibility(final String partlayerName, final boolean visible) {
        getLayerObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LayerInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<LayerInfo> layerInfos) {
                        for (LayerInfo layerInfo : layerInfos) {
                            if (layerInfo.getLayerName().equals(partlayerName) && layerInfo.isDefaultVisible() != visible) {
                                /**
                                 * 在这里创建的原因是：当图层列表视图未初始化时，
                                 * 无法回调{@link PatrolLayerPresenter4JbjPsdy#onClickCheckbox(String, int, LayerInfo, boolean)}方法
                                 */
                                mILayerView.createLayerView(layerInfos);
                                mILayerView.toggle(layerInfo, visible);
                                break;
                            }
                        }
                    }
                });
    }

    /**
     * 不要加载到地图上的图层
     *
     * @param excludeLayerName         不进行添加的图层名称
     * @param onAddLayerFinishCallback 当添加图层完毕时的回调，记住：此时可以继续添加图层，但是不可以添加GraphicLayer，因为此时并不代表地图已经初始化完成
     */
    public void loadLayer(final String excludeLayerName, final Callback1 onAddLayerFinishCallback) {
        mILayerView.showLoadingMap();
        mILayerView.removeAllLayers();

        Observable<List<LayerInfo>> layerInfoObservable = getLayerObservable();
        layerInfoObservable
                .map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
                    @Override
                    public List<LayerInfo> call(List<LayerInfo> layerInfos) {

                        Iterator<LayerInfo> iterator = layerInfos.iterator();
                        while (iterator.hasNext()) {
                            LayerInfo next = iterator.next();
                            if (next.getLayerName().contains(excludeLayerName)) {
                                iterator.remove();
                            }
                        }
                        return layerInfos;
                    }
                })
                .map(new Func1<List<LayerInfo>, Object[]>() {
                    @Override
                    public Object[] call(List<LayerInfo> layerInfos) {

                        Object[] objects = new Object[2];
                        Map<Layer, Integer> map = new ArrayMap<Layer, Integer>();

                        List<LayerInfo> temp = changeLayerOrder(layerInfos);
                        List<Layer> layers = getLayers(map, temp);

                        objects[0] = layers;
                        objects[1] = map;
                        return objects;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object[]>() {
                    @Override
                    public void onCompleted() {
                        mILayerView.hideLoadingMap();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mILayerView.hideLoadingMap();
                        mILayerView.showLoadLayerFailMessage(new Exception(e));
                    }

                    @Override
                    public void onNext(Object[] objects) {

                        mCurrentProjectId = mILayersService.getCurrentProjectId();

                        List<Layer> layers = (List<Layer>) objects[0];
                        Map<Layer, Integer> map = (Map<Layer, Integer>) objects[1];

                        addLayerToMapView(layers, map);
                        //修改地图的初始状态（移动到中心点并且缩放到初始级别）
                        setMapInitState();

                        if (onAddLayerFinishCallback != null) {
                            onAddLayerFinishCallback.onCallback(null);
                        }
                    }
                });
    }

    /**
     * 通过图层名称获取LayerInfo
     *
     * @param partLayerName
     * @return
     */
    public LayerInfo getLayerInfoByPartLayerName(String partLayerName) {
        List<LayerInfo> layerInfosFromLocal = mILayersService.getLayerInfosFromLocal();
        for (LayerInfo layerInfo : layerInfosFromLocal) {
            if (layerInfo.getLayerName().contains(partLayerName)) {
                return layerInfo;
            }
        }
        return null;
    }

    public void destroy() {
        if (mILayerView != null) {
            mILayerView.hideLoadingMap();
        }
    }

    private OnLayerVisibilityChangedListener onLayerVisibilityChangedListener;

    public void registerLayerVisibilityChangedListener(OnLayerVisibilityChangedListener onLayerVisibilityChangedListener) {
        this.onLayerVisibilityChangedListener = onLayerVisibilityChangedListener;
    }

    public interface OnLayerVisibilityChangedListener {
        void changed(boolean visible, LayerInfo layerInfo);
    }
}

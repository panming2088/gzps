package com.augurit.agmobile.mapengine.layermanage.view;

import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.view.View;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.util.LayerFactoryProvider;
import com.augurit.agmobile.mapengine.project.view.OnProjectChangeEvent;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.Layer;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.layermanage.view
 * @createTime 创建时间 ：2017-01-18
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-18
 */

public class LayerPresenter implements ILayerPresenter {

    public static final String TAG = "图层模块";

    protected ILayerView mILayerView;

    protected ILayersService mILayersService;

    public String mCurrentProjectId = "";

    private boolean isLoading = false;


    public LayerPresenter(ILayerView layerView, ILayersService layersService) {
        mILayersService = layersService;
        mILayerView = layerView;
        mILayerView.setPresenter(this);
         EventBus.getDefault().register(this);
    }

    /**
     * 获取图层数据并加载到地图上
     */
    @Override
    public void loadLayer() {
        loadLayer(null);
    }

    @Override
    public void loadLayer(final Callback2<Integer> callback) {
        if(isLoading){
            return;
        }
        isLoading = true;
        mILayerView.showLoadingMap();
        mILayerView.removeAllLayers();

        Observable<List<LayerInfo>> layerInfoObservable = getLayerObservable();
        layerInfoObservable
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
                        isLoading = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mILayerView.hideLoadingMap();
                        mILayerView.showLoadLayerFailMessage(new Exception(e));
                        if(callback != null){
                            callback.onFail(new Exception(e));
                        }
                    }

                    @Override
                    public void onNext(Object[] objects) {

                        mCurrentProjectId = mILayersService.getCurrentProjectId();

                        List<Layer> layers = (List<Layer>) objects[0];
                        Map<Layer, Integer> map = (Map<Layer, Integer>) objects[1];

                        addLayerToMapView(layers, map);
                        //修改地图的初始状态（移动到中心点并且缩放到初始级别）
                        setMapInitState();
                        if(ListUtil.isEmpty(layers)){
                            if(callback != null){
                                callback.onFail(new Exception("图层加载失败"));
                            }
                        } else {
                            if(callback != null){
                                callback.onSuccess(1);
                            }
                        }
                    }
                });
    }

    /**
     * 修改地图的初始状态（移动到中心点并且缩放到初始级别）
     */
    protected void setMapInitState() {
        if (mILayersService.getProjectInitialCenter() != null &&
                mILayersService.getProjectInitialResolution() != 0) {

            mILayerView.getMapView().setResolution(mILayersService.getProjectInitialResolution());
            mILayerView.getMapView().centerAt(mILayersService.getProjectInitialCenter(), true);
            mILayerView.getMapView().setMaxResolution(mILayersService.getProjectMaxResolution());
            mILayerView.getMapView().setMinResolution(mILayersService.getProjectMinResolution());
        }
    }


    /**
     * 添加地图
     *
     * @param layers
     * @param map
     */
    protected void addLayerToMapView(List<Layer> layers, Map<Layer, Integer> map) {
        for (Layer entry : layers) {
            //判断现在界面上是否有存在，防止EventBus多次发送加载图层事件而导致多次添加
            if (mILayersService.getLayerById(map.get(entry)) != null) {
                continue;
            }
            mILayerView.addLayerToMapView(entry);
            mILayersService.updateAllAddedToMapViewMap(map.get(entry), entry);
        }
    }

    @NonNull
    protected List<Layer> getLayers(Map<Layer, Integer> map, List<LayerInfo> temp) {
        List<Layer> layers = new ArrayList<Layer>();
        for (LayerInfo layer : temp) {
            Layer arcgisLayer = getSingleLayer(layer);
            if (arcgisLayer != null) {
                arcgisLayer.setVisible(layer.isDefaultVisible());
                layers.add(arcgisLayer);
                map.put(arcgisLayer, layer.getLayerId());
                if (layer.isDefaultVisible()) {
                    mILayersService.addOrRemoveFromVisibleMap(layer.getLayerId(), layer, true);
                }
            } else {
                LogUtil.e(TAG, "名称为：" + layer.getLayerName() + "的图层不支持加载或者无法连接,该图层类型为：" +
                        layer.getType().toString() + "请询问服务端配置是否正确或者服务是否可连接");
            }
        }
        return layers;
    }


    /**
     * 得到图层
     *
     * @param layer
     * @return
     */
    protected Layer getSingleLayer(LayerInfo layer) {
        return LayerFactoryProvider.provideLayerFactory().getLayer(mILayerView.getApplicationContext(), layer);
    }

    @NonNull
    protected List<LayerInfo> changeLayerOrder(List<LayerInfo> layerInfos) {
        List<LayerInfo> temp = new ArrayList<>();
        temp.addAll(layerInfos);
        //将图层顺序逆序
        Collections.reverse(temp);
        return temp;
    }


    /**
     * 获取到图层
     *
     * @return
     */
    protected Observable<List<LayerInfo>> getLayerObservable() {
        Observable<List<LayerInfo>> layerInfoObservable = null;

        layerInfoObservable = mILayersService.getCacheLayers(mILayersService.getCurrentProjectId()); //读取缓存

        if (layerInfoObservable == null) {
            layerInfoObservable = mILayersService.getLayerInfo()
                    //过滤掉不需要显示的图层
                    .map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
                        @Override
                        public List<LayerInfo> call(List<LayerInfo> layerInfos) {
                            return filterLayerNoNeedToAdd(layerInfos);
                        }
                    })
                    //过滤掉相同的图层（agweb的角色配置bug）
                    .map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
                        @Override
                        public List<LayerInfo> call(List<LayerInfo> layerInfos) {
                            return filterSameLayerInfo(layerInfos);
                        }
                    })
                    //进行排序
                    .map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
                        @Override
                        public List<LayerInfo> call(List<LayerInfo> layerInfos) {
                            return mILayersService.sortLayerInfos(layerInfos);
                        }
                    })
                    .map(new Func1<List<LayerInfo>, List<LayerInfo>>() {
                        @Override
                        public List<LayerInfo> call(List<LayerInfo> layerInfos) {
                            //保存到缓存
                            mILayersService.refreshCacheLayers(layerInfos);
                            return layerInfos;
                        }
                    });
        }
        return layerInfoObservable;
    }

    /**
     * 过滤掉不需要显示的图层
     *
     * @param layerInfos
     * @return
     */
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

    /**
     * 过滤掉相同的图层
     *
     * @param layerInfos
     * @return
     */
    @NonNull
    protected List<LayerInfo> filterSameLayerInfo(List<LayerInfo> layerInfos) {
        Set<LayerInfo> layerInfos1 = new TreeSet<LayerInfo>();
        for (LayerInfo layerInfo : layerInfos) {
            layerInfos1.add(layerInfo);
        }
        LayerInfo[] layerInfos2 = layerInfos1.toArray(new LayerInfo[0]);
        return Arrays.asList(layerInfos2);
    }

    @Override
    public void showLayerList() {

        mILayerView.addToContainer();
        mILayerView.showLoadingView();

        Observable<List<LayerInfo>> layerObservable = getLayerObservable();
        layerObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LayerInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mILayerView.hideLoadingView();
                        mILayerView.showLoadLayerFailMessage(new Exception(e));
                    }

                    @Override
                    public void onNext(List<LayerInfo> layerInfos) {
                        mILayerView.hideLoadingView();
                        View layerView = mILayerView.createLayerView(layerInfos);
                        mILayerView.addLayerViewToTreeViewContainer(layerView);
                    }
                });
    }

    @Override
    public void onClickCheckbox(String groupName,int layerId, LayerInfo currentLayer, boolean ifShow) {
        /**
         *  首先判断是否是{@link com.augurit.agmobile.mapengine.layermanage.model.LayerType.DynamicLayer_SubLayer},
         *  如果是DaynamicLayer的子图层，那么此时要通过DynamicLayer才能控制它的显示与隐藏。
         */
        if(currentLayer.getType() == LayerType.DynamicLayer_SubLayer){
            String parentLayerId = currentLayer.getParentLayerId();
            Layer layer = mILayersService.getLayerById(Integer.valueOf(parentLayerId));
            if (layer instanceof ArcGISDynamicMapServiceLayer){
                ArcGISDynamicMapServiceLayer parentLayer = (ArcGISDynamicMapServiceLayer) layer;
                if (parentLayer.getLayers() != null && parentLayer.getLayers().length > layerId){
                    ArcGISLayerInfo arcGISLayerInfo = parentLayer.getLayers()[layerId];
                    arcGISLayerInfo.setVisible(ifShow);
                    parentLayer.refresh();

                    //修改子图层可见性
                    LayerInfo layerInfo = mILayersService.getLayerInfoByLayerId(Integer.valueOf(parentLayerId));
                    List<LayerInfo> childLayer = layerInfo.getChildLayer();
                    if (!ListUtil.isEmpty(childLayer)){
                        for (LayerInfo layerInfo1 : childLayer){
                            if (layerInfo1.getLayerId() == layerId){
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
        }
    }

    @Override
    public void onClickOpacityButton(int layerId, LayerInfo currentLayer, float opacity) {

        /**
         *  首先判断是否是{@link com.augurit.agmobile.mapengine.layermanage.model.LayerType.DynamicLayer_SubLayer},
         *  如果是DaynamicLayer的子图层,那么直接控制DynamicLayer的透明度，因为子图层无法单独控制透明度
         */
        if(currentLayer.getType() == LayerType.DynamicLayer_SubLayer){
            String parentLayerId = currentLayer.getParentLayerId();
            Layer layer = mILayersService.getLayerById(Integer.valueOf(parentLayerId));
            if (layer instanceof ArcGISDynamicMapServiceLayer){
                ArcGISDynamicMapServiceLayer parentLayer = (ArcGISDynamicMapServiceLayer) layer;
                parentLayer.setOpacity(opacity);
            }
            return;
        }


        Layer targetLayer = mILayersService.getLayerById(layerId);
        if (targetLayer != null) {
            targetLayer.setOpacity(opacity);
        }

    }

    @Override
    public void clearInstance() {
        EventBus.getDefault().unregister(this);
        if (mILayerView != null) {
            mILayerView = null;
        }
    }

    @Override
    public ILayersService getService() {
        return mILayersService;
    }

    @Subscribe
    public void onReceivedProjectChangeEvent(OnProjectChangeEvent onProjectChangeEvent) {
        if (!mCurrentProjectId.equals(onProjectChangeEvent.getProjectInfo().getProjectId())){
            mCurrentProjectId = onProjectChangeEvent.getProjectInfo().getProjectId();
            mILayersService.clearAllLayer();
            mILayersService.clearVisibleLayer();
            loadLayer();
        }
    }
}

package com.augurit.agmobile.mapengine.map.arcgis;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;

import com.augurit.agmobile.mapengine.edit.service.MoveAllEditService;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.util.LayerFactory;
import com.augurit.agmobile.mapengine.map.IMapContainer;
import com.augurit.agmobile.mapengine.map.IMapDelegate;
import com.augurit.agmobile.mapengine.map.InfoWindow;
import com.augurit.agmobile.mapengine.map.Map;
import com.augurit.agmobile.mapengine.map.OnMapClickListener;
import com.augurit.agmobile.mapengine.map.OnMapDoubleClickListener;
import com.augurit.agmobile.mapengine.map.OnMapLongClickListener;
import com.augurit.agmobile.mapengine.map.OnMapMoveListener;
import com.augurit.agmobile.mapengine.map.OnMapReadyCallback;
import com.augurit.agmobile.mapengine.map.graphic.Overlay;
import com.augurit.agmobile.mapengine.map.graphic.PictureMarkerOverlay;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.am.fw.utils.view.ToastUtil;

import com.esri.android.map.Callout;
import com.esri.android.map.CalloutStyle;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.am.intro.maps.arcgis
 * @createTime 创建时间 ：2017-01-13
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-13 0:00
 */
public class ArcGISMapDelegate implements IMapDelegate {
    private MapView mMapView;
    private IMapContainer mIMapContainer;
    private GraphicsLayer mGraphicsLayer;
    private AtomicBoolean isGraphicAdded;
    private OnMapReadyCallback mOnMapReadyCallback;
    private DefaultMapClickListener mDefaultMapClickListener;

    //xcl 2017-02-28
    /**
     * 当地图加载完成时的回调集合
     */
    private List<OnMapReadyCallback> mOnMapReadyCallbacks = new ArrayList<>();

    /*public ArcGISMapDelegate(MapView mapView) {
        this.mMapView = mapView;
    }*/
    public ArcGISMapDelegate(IMapContainer iMapContainer) {
        this.mIMapContainer = iMapContainer;
        this.isGraphicAdded = new AtomicBoolean(false);
        if (this.mIMapContainer != null) {
            this.mMapView = (MapView) iMapContainer.getMapView();
            this.mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
                private static final long serialVersionUID = 1L;

                public void onStatusChanged(Object source, STATUS status) {
                    if (STATUS.INITIALIZED == status && source == mMapView) {
                        Map map = new Map(ArcGISMapDelegate.this);
                        if (ArcGISMapDelegate.this.mIMapContainer != null) {
                            ArcGISMapDelegate.this.mIMapContainer.setMap(map);
                        }
                        if (mOnMapReadyCallback != null) {
                            mOnMapReadyCallback.onMapReady(map);
                        }
                        //遍历回调
                        if (mOnMapReadyCallbacks.size() > 0){ //xcl 2017-02-28 加入对回调的遍历，建议以后不采用直接setOnStatusChangedListener的方法
                            for (OnMapReadyCallback onMapReadyCallback : mOnMapReadyCallbacks){
                                mOnMapReadyCallback.onMapReady(map);
                            }
                        }
                    }
                }
            });
            mGraphicsLayer = new GraphicsLayer();
            mDefaultMapClickListener = new DefaultMapClickListener(mMapView.getContext(),mMapView);
            mMapView.setOnTouchListener(mDefaultMapClickListener);
        }
    }


    @Override
    public void setMapClickListener(final OnMapClickListener mapClickListener) {
        //xcl 2017-02-21 去掉mMapView.setOnSingleTapListener
        // 原因：因为设置double click事件的时候采用的是setOnTouchListener，如果在这里调用setOnSingleTapListener，刚刚的
        //double click事件依然存在，即便调用getMap().setOnDoubleClickListener(null)也无法屏蔽，如果改成setOnTouchListener，又会发生
        //多个TouchListener相互覆盖的问题。

        if (this.mDefaultMapClickListener != null) {
            mDefaultMapClickListener.setMapClickListener(mapClickListener);

          /*  if (mMapView != null) {
                this.mMapView.setOnSingleTapListener(new OnSingleTapListener() {
                    @Override
                    public void onSingleTap(float screenX, float screenY) {
                        Point arcgisPoint = mMapView.toMapPoint(screenX, screenY);
                        LatLng point = new LatLng(arcgisPoint.getY(),
                                arcgisPoint.getX());
                        if (mapClickListener != null) {
                            if (!mapClickListener.onMapClick(point)) {//false时执行默认点击
                                mDefaultMapClickListener.onMapClick(point);
                            }
                        } else {
                            mDefaultMapClickListener.onMapClick(point);
                        }
                    }
                });
            }*/
        }
    }

    @Override
    public void setMapLongClickListener(final OnMapLongClickListener mapLongClickListener) {
        //xcl 2017-02-21 修改原因同setMapOnClickListener
        if (this.mDefaultMapClickListener != null){
            mDefaultMapClickListener.setMapLongClickListener(mapLongClickListener);
        }
       /* if (this.mMapView != null) {
            //xcl 2017-02-21
            this.mMapView.setOnLongPressListener(new OnLongPressListener() {
                @Override
                public boolean onLongPress(float screenX, float screenY) {
                    if (mapLongClickListener != null) {
                        mapLongClickListener.onMapLongClick(screenX, screenY);
                    }
                    return true;
                }
            });
        }*/
    }

    @Override
    public void setMapMoveListener(OnMapMoveListener mapMoveListener) {
        //xcl 2017-02-21 修改原因同setMapOnClickListener
        if (this.mDefaultMapClickListener != null){
            mDefaultMapClickListener.setMapMoveListener(mapMoveListener);
        }
    }

    @Override
    public void registerMapReadyCallback(OnMapReadyCallback callback) {
        mOnMapReadyCallbacks.add(callback);
    }

    @Override
    public void unRegisterMapReadyCallback(OnMapReadyCallback callback) {
        mOnMapReadyCallbacks.remove(callback);
    }

    @Override
    public void setMapDoubleClickListener(final OnMapDoubleClickListener mapDoubleClickListener) {
        //xcl 2017-02-21 修改原因同setMapOnClickListener
        if (mDefaultMapClickListener != null){
            mDefaultMapClickListener.setMapDoubleClickListener(mapDoubleClickListener);
        }
       /* if (this.mMapView != null) {
            if (mapDoubleClickListener != null) {
                this.mMapView.setOnTouchListener(
                        new DefaultMapClickListener(this.mMapView.getContext(), this.mMapView) {
                            @Override
                            public boolean onDoubleTap(MotionEvent point) {
                                mapDoubleClickListener.onMapMove(point.getX(), point.getY());
                                return super.onDoubleTap(point);
                            }
                        }
                );
            }
        }*/
    }

    @Override
    public void zoomIn() {
        if (this.mMapView != null) {
            this.mMapView.zoomin();
        }
    }

    @Override
    public void zoomOut() {
        if (this.mMapView != null) {
            this.mMapView.zoomout();
        }
    }

    private void addGraphicLayer() {
        if (this.isGraphicAdded.compareAndSet(false, true)) {
            mMapView.addLayer(mGraphicsLayer);
        }
    }

    public void removeGraphicLayer() {
        if (this.isGraphicAdded.compareAndSet(true, false)) {
            mMapView.removeLayer(mGraphicsLayer);
        }
    }

    @Override
    public Graphic addMarker(PictureMarkerOverlay markerOptions) {
        if (markerOptions == null || mMapView == null) {
            return null;
        }
        Point point = new Point(markerOptions.getPosition().getLongitude(), markerOptions.getPosition().getLatitude());
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(
                new BitmapDrawable(mMapView.getContext().getResources(), markerOptions.getIcon()));
        Graphic graphic = new Graphic(point, pictureMarkerSymbol);
        this.mGraphicsLayer.addGraphic(graphic);
        addGraphicLayer();
        return graphic;
    }

    @Override
    public float getMaxZoom() {
        if (mMapView != null) {
            return (float) this.mMapView.getMaxScale();
        }
        return 0f;
    }

    @Override
    public void setMaxZoom(float maxZoom) {
    }

    @Override
    public float getMinZoom() {
        return 0;
    }

    @Override
    public void setMinZoom(float minZoom) {

    }

    @Override
    public void getMapAsync(OnMapReadyCallback callback) {
        this.mOnMapReadyCallback = callback;
    }

    @Override
    public void clear() {
        if (this.mGraphicsLayer != null) {
            this.mGraphicsLayer.removeAll();
        }
    }

    @Override
    public void setDefaultMapListener(MapOnTouchListener mapListener) {
        this.mDefaultMapClickListener.setDefaultMapListener(mapListener);
    }

    //xcl 2017-02-15 增加以下方法
    /*@Override
    public void addLayer(LayerInfo layerInfo) {
        Layer layer = LayerFactory.getLayer(mMapView.getContext().getApplicationContext(), layerInfo);
        mAddedLayers.put(layerInfo.getLayerId(), layer);
        mMapView.addLayer(layer);
    }

    @Override
    public void showLayerByLayerId(int layerId) {
        Layer layer = mAddedLayers.get(layerId);
        if (layer != null) {
            layer.setVisible(true);
        }
    }

    @Override
    public void hideLayerByLayerId(int layerId) {
        Layer layer = mAddedLayers.get(layerId);
        if (layer != null) {
            layer.setVisible(false);
        }
    }

    @Override
    public void centerAt(LatLng latLng, boolean ifAnimated) {
        if (mMapView != null) {
            mMapView.centerAt(new Point(latLng.getLongitude(), latLng.getLatitude()), ifAnimated);
        }
    }

    @Override
    public void setResolution(double resolution) {
        if (mMapView != null) {
            mMapView.setResolution(resolution);
        }
    }

    @Override
    public void removeAllLayer() {
        if (mMapView != null) {
            mMapView.removeAll();
        }
    }


    @Override
    public void setCachePath(String cachepath) {

    }

    @Override
    public void hideInfoWindow() {
        if (mMapView != null) {
            mMapView.getCallout().hide();
        }
    }

    @Override
    public void showInfoWindow(InfoWindow infoWindow) {
        Callout callout = mMapView.getCallout();
        callout.setContent(infoWindow.getView());
        CalloutStyle calloutStyle = new CalloutStyle();
        calloutStyle.setBackgroundColor(infoWindow.getInfoWindowStyle().getBackgroundColor());
        calloutStyle.setCornerCurve(infoWindow.getInfoWindowStyle().getCornerCurve());
        calloutStyle.setMaxHeightDp(infoWindow.getInfoWindowStyle().getMaxHeightDp(), mMapView.getContext());
        calloutStyle.setMaxWidthDp(infoWindow.getInfoWindowStyle().getMaxHeightDp(), mMapView.getContext());

        callout.setStyle(calloutStyle);
        callout.show(new Point(infoWindow.getAnchor().getLongitude(), infoWindow.getAnchor().getLatitude()));
    }

    @Override
    public int addOverlayCanvas() {
        return 0;
    }

    @Override
    public void addOverlay(int overlayCanvasId, Overlay overlay) {

    }*/


    class DefaultMapClickListener extends MapOnTouchListener {

        protected Context mContext;
        protected OnMapClickListener mOnMapClickListener;
        protected OnMapDoubleClickListener mOnMapDoubleClickListener;
        protected MapOnTouchListener mDefaultMapOnTouchListener; //默认点击事件
        protected OnMapMoveListener mOnMapMoveListener;
        protected OnMapLongClickListener mOnMapLongClickListener;


        public DefaultMapClickListener(Context context, MapView view) {
            super(context, view);
            this.mContext = context;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            Point arcgisPoint = mMapView.toMapPoint(motionEvent.getX(), motionEvent.getY());
            LatLng point = new LatLng(arcgisPoint.getY(),
                    arcgisPoint.getX());
            if (mOnMapDoubleClickListener != null) {
                if (!mOnMapDoubleClickListener.onMapDoubleClick(motionEvent.getX(),motionEvent.getY())) {
                    if (mDefaultMapOnTouchListener != null){
                        return mDefaultMapClickListener.onDoubleTap(motionEvent);//只有当false
                    }
                }
            }
            return super.onDoubleTap(motionEvent);
        }

        @Override
        public boolean onSingleTap(MotionEvent motionEvent) {
            Point arcgisPoint = mMapView.toMapPoint(motionEvent.getX(), motionEvent.getY());
            LatLng point = new LatLng(arcgisPoint.getY(),
                    arcgisPoint.getX());
            if (mOnMapClickListener != null) {
                if (!mOnMapClickListener.onMapClick(motionEvent.getX(),motionEvent.getY())) {
                    if (mDefaultMapOnTouchListener != null){ //当返回false的时候不执行
                        return mDefaultMapClickListener.onSingleTap(motionEvent);
                    }
                }
            }
            return super.onSingleTap(motionEvent);
        }

        @Override
        public void onLongPress(MotionEvent point) {
            super.onLongPress(point);
        }

        @Override
        public boolean onLongPressUp(MotionEvent point) {
            if (mOnMapLongClickListener != null){
                mOnMapLongClickListener.onMapLongClick(point.getX(),point.getY());
                return true;
            }
            return super.onLongPressUp(point);
        }

        @Override
        public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
            if (mOnMapMoveListener != null) {
                if (!mOnMapMoveListener.onMapMove(from.getX(),from.getY(),to.getX(),to.getY())) {
                    if (mDefaultMapOnTouchListener != null){ //当返回false的时候不执行
                        return mDefaultMapClickListener.onDragPointerMove(from,to);
                    }
                }
            }
            return super.onDragPointerMove(from,to);
        }

        @Override
        public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
            if (mOnMapMoveListener != null) {
                if (!mOnMapMoveListener.onMoveOver(from.getX(),from.getY(),to.getX(),to.getY())) {
                    if (mDefaultMapOnTouchListener != null){ //当返回false的时候不执行
                        return mDefaultMapClickListener.onDragPointerUp(from,to);
                    }
                }
            }
            return super.onDragPointerUp(from,to);
        }

        void setMapClickListener(final OnMapClickListener mapClickListener){
            this.mOnMapClickListener = mapClickListener;
        }

        void setMapDoubleClickListener(final OnMapDoubleClickListener mapDoubleClickListener){
            mOnMapDoubleClickListener = mapDoubleClickListener;
        }

        /**
         * 更换默认的点击事件
         * @param mapListener
         */
        void setDefaultMapListener(MapOnTouchListener mapListener){
            mDefaultMapOnTouchListener = mapListener;
        }

        void setMapMoveListener(OnMapMoveListener mapMoveListener) {
            mOnMapMoveListener = mapMoveListener;
        }

        void setMapLongClickListener(OnMapLongClickListener onMapLongClickListener) {
            mOnMapLongClickListener = onMapLongClickListener;
        }
    }
}

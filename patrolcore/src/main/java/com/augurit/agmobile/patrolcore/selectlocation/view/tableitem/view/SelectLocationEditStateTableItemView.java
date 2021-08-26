package com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.editmap.EditMapFeatureActivity;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.ISelectLocationTableItemPresenter;
import com.augurit.agmobile.patrolcore.selectlocation.view.tableitem.ISelectLocationTableItemView;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.common.BaseView;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * 编辑模式下的地图选点控件
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view
 * @createTime 创建时间 ：17/11/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/1
 * @modifyMemo 修改备注：
 */

public class SelectLocationEditStateTableItemView extends BaseView<ISelectLocationTableItemPresenter> implements ISelectLocationTableItemView {


    protected View mRoot;
    protected ViewGroup mViewGroup;
    protected EditText et_shortcut_detail_address;
    protected View mView_change_location;
    protected View mTv_change_location;
    protected LinearLayout ll_address_container;
    protected ViewGroup rl_map;
    protected MapView mMapView;
    protected View mView_above_mapview;
    protected GraphicsLayer mLocationLayer;
    protected ViewGroup spinner_container;
    protected boolean ifEditable = false;  //位置是否是可以编辑的
    protected ViewGroup ll_uneditable_addresss_container;
    /**
     * 是否添加到底图上
     */
    protected boolean ifAddedToMap = false;
    /**
     * 地图是否初始化完成
     */
    protected boolean ifMapInitialized = false;
    protected ViewGroup ll_table_item_container;
    protected Graphic graphic;

    protected List<Callback1<Boolean>> onMapInitializedCallbacks;


    public SelectLocationEditStateTableItemView(Context context) {
        super(context);
        initView();
    }

    protected void initView() {
        mRoot = View.inflate(mContext, R.layout.map_shortcut_view, null);
        rl_map = (ViewGroup) mRoot.findViewById(R.id.rl_map);
        //"在底图上标记地点"控件
        mTv_change_location = mRoot.findViewById(R.id.tv_change_location);
        mTv_change_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity();
            }
        });
        //底部灰色背景
        mView_change_location = mRoot.findViewById(R.id.view_change_location);
        mView_change_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity();
            }
        });

        et_shortcut_detail_address = (EditText) mRoot.findViewById(R.id.et_);
        ll_address_container = (LinearLayout) mRoot.findViewById(R.id.ll_address_container);

        mMapView = (MapView) mRoot.findViewById(R.id.mapview_small);
        mMapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//设置地图背景色、去掉网格线
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (status == STATUS.INITIALIZED) {
                    ifMapInitialized = true;
                    if (!ListUtil.isEmpty(onMapInitializedCallbacks)) {
                        for (Callback1 callback1 : onMapInitializedCallbacks) {
                            callback1.onCallback(true);
                        }
                    }
                }
            }
        });
        mView_above_mapview = mRoot.findViewById(R.id.view_above_mapview);
        mView_above_mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity();
            }
        });

        //不可编辑的位置容器
        ll_uneditable_addresss_container = (ViewGroup) mRoot.findViewById(R.id.ll_uneditable_addresss_container);
        //spinner容器
        spinner_container = (ViewGroup) mRoot.findViewById(R.id.spinner_container);

        ll_table_item_container = (ViewGroup) mRoot.findViewById(R.id.ll_table_item_container);
    }

    protected void startActivity() {
        Intent intent = new Intent(mContext, EditMapFeatureActivity.class);
        if (mPresenter != null) {
            mPresenter.completeIntent(intent);
        }
        mContext.startActivity(intent);
    }


    @Override
    public void addTo(ViewGroup container) {
        container.addView(mRoot);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void setAddress(List<String> spinnerList) {
        et_shortcut_detail_address.setText(spinnerList.get(0));
    }

    @Override
    public void setMapCenter(Point center) {
        mMapView.centerAt(center, true);
    }

    @Override
    public View getAddressEditTextContainer() {
        return ll_address_container;
    }

    @Override
    public void hideMapView() {
        rl_map.setVisibility(View.GONE);
    }

    @Override
    public void showLocationOnMapWhenLayerLoaded(Point point, Callback1 callback1) {
        if (ifMapInitialized && !ifAddedToMap) { //底图加载完成并且此时没有添加
            if (mLocationLayer == null) {
                mLocationLayer = new GraphicsLayer();
                mMapView.addLayer(mLocationLayer);
            }
            drawLocation(point);
        }
    }

    @Override
    public void drawLocationOnMap(Point point) {
        if (mLocationLayer == null) {
            mLocationLayer = new GraphicsLayer();
            mMapView.addLayer(mLocationLayer);
        }
        drawLocation(point);
    }

    protected void drawLocation(Point point) {
        mLocationLayer.removeAll();
        PictureMarkerSymbol symbol = new PictureMarkerSymbol(mContext, ContextCompat.getDrawable(mContext, R.mipmap.ic_select_location));
        mLocationLayer.addGraphic(new Graphic(point, symbol));
        mMapView.centerAt(point, true);
        // mMapView.setScale(mMapView.getMaxScale());
    }

    @Override
    public MapView getMapView() {
        return mMapView;
    }

    @Override
    public void removeMapView() {
        ((ViewGroup) mMapView.getParent()).removeView(mMapView);
    }

    @Override
    public void hideSelectLocationButton() {
        mView_change_location.setVisibility(View.GONE);
        mTv_change_location.setVisibility(View.GONE);
    }

    @Override
    public void showMapView() {
        mMapView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setScale(double scale) {
        mMapView.setScale(scale);
    }

    @Override
    public View getRootView() {
        return mRoot;
    }

    @Override
    public boolean containsLocation(Location location) {
        if (mMapView != null) {
            return GeometryEngine.contains(mMapView.getMaxExtent(), new Point(location.getLongitude(), location.getLatitude()), mMapView.getSpatialReference());
        }
        return false;
    }

    @Override
    public void addView(View view) {
        ll_table_item_container.addView(view);
    }

    @Override
    public void addGraphic(Geometry geometry, boolean ifClearOtherGraphic) {

        if (geometry == null) {
            return;
        }

        Symbol symbol = null;
        switch (geometry.getType()) {
            case LINE:
            case POLYLINE:
                symbol = new SimpleLineSymbol(Color.RED, 5);
                break;
            case POINT:
                symbol = new PictureMarkerSymbol(mContext, ResourcesCompat.getDrawable(mContext.getResources(), R.mipmap.ic_select_location, null));
                break;
            default:
                break;
        }

        this.graphic = new Graphic(geometry, symbol);
        if (mLocationLayer == null) {
            mLocationLayer = new GraphicsLayer();
            mMapView.addLayer(mLocationLayer);
        }

        if (ifClearOtherGraphic) {
            mLocationLayer.removeAll();
        }

        mLocationLayer.addGraphic(graphic);

        if (graphic.getGeometry().getType() == Geometry.Type.LINE) {
            mMapView.setExtent(graphic.getGeometry());

        } else if (graphic.getGeometry().getType() == Geometry.Type.POINT) {
            Point point = (Point) graphic.getGeometry();
            mMapView.centerAt(point, true);
        }
    }

    @Override
    public void registerMapViewInitializedCallback(Callback1<Boolean> callback1) {
        if (onMapInitializedCallbacks == null) {
            onMapInitializedCallbacks = new ArrayList<>();
        }
        this.onMapInitializedCallbacks.add(callback1);
    }

    @Override
    public boolean ifMapInitialized() {
        return ifMapInitialized;
    }

    @Override
    public void loadMap() {

        PatrolLayerPresenter patrolLayerPresenter = new PatrolLayerPresenter(new PatrolLayerView(mContext, mMapView, null));
        patrolLayerPresenter.loadLayer();
    }

}

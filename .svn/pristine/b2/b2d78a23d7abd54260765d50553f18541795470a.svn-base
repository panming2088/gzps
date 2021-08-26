package com.augurit.agmobile.patrolcore.editmap;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.common.widget.scaleview.MapScaleView;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerPresenter;
import com.augurit.agmobile.mapengine.layermanage.view.ILayerView;
import com.augurit.agmobile.mapengine.legend.service.OnlineLegendService;
import com.augurit.agmobile.mapengine.legend.view.ILegendView;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.legend.ImageLegendView;
import com.augurit.agmobile.patrolcore.common.legend.LegendPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView2;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.view.IDrawerController;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.tasks.geocode.Locator;

import org.greenrobot.eventbus.EventBus;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 新增或编辑管线界面
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/10/15
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/15
 * @modifyMemo 修改备注：
 */

public class EditLineFeatureFragment3 extends Fragment {


    protected MapView mapView;
    protected Button btn_sure;
    protected TextView tv_hint;


    public static String EDIT_LINE = "2";//编辑线
    protected String editMode;
    protected Geometry geometry;
    protected boolean ifReadOnly = false;
    protected ILayerView layerView;
    protected GraphicsLayer mGLayerFroDrawLocation;
    protected Graphic graphic;
    protected ViewGroup ll_component_list;

    protected ILayerPresenter layerPresenter;

    protected Location mCurrentLocation = null;
    protected View btn_choose_current_position;
    protected double initScale;
    protected DetailAddress mCurrentAddress;
    protected DetailAddress mUserAddress; //定位到的用户位置
    protected TextView tv_address;
    protected LegendPresenter legendPresenter;
    protected AbsEditLineReEditStateMapListener editLineReEditStateMapListener;
    protected AbsEditlineEditStateMapListener editLineEditStateMapListener;
    private MapScaleView scaleView;


    /**
     * @param editMode 编辑线还是点；
     * @return
     */
    public static EditLineFeatureFragment3 newInstance(String editMode, Geometry geometry, double initScale, boolean ifReadOnly, String address) {

        EditLineFeatureFragment3 selectLocationFragment = new EditLineFeatureFragment3();
        Bundle bundle = new Bundle();
        bundle.putString("mode", editMode);
        bundle.putSerializable("geometry", geometry);
        bundle.putBoolean("read", ifReadOnly);
        bundle.putDouble("scale", initScale);
        bundle.putString("address", address);
        selectLocationFragment.setArguments(bundle);
        return selectLocationFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_editmapfeature, null);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getBundleData();

        initView(view);

        initData();

    }

    protected void initData() {
        //1. 加载地图
        loadMap();
        //2.进行定位
        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (status == STATUS.INITIALIZED) {
                    startLocate();
                    initMapListener();
                    if (PatrolLayerPresenter.initScale != -1 && PatrolLayerPresenter.initScale != 0){
                        mapView.setScale(PatrolLayerPresenter.initScale);
                        scaleView.setScale(PatrolLayerPresenter.initScale);
                    }
                    // scaleView.refreshScaleView();
                }
            }
        });


        if (mCurrentAddress != null) {
            tv_address.setText("当前位置：" + mCurrentAddress.getDetailAddress());
        }


    }


    public void loadMap() {
        IDrawerController drawerController = (IDrawerController) getActivity();
        layerView = new PatrolLayerView2(getActivity(), mapView, drawerController.getDrawerContainer());
        layerPresenter = new PatrolLayerPresenter(layerView);
        layerPresenter.loadLayer();
    }


    private void getBundleData() {
        editMode = getArguments().getString("mode");
        geometry = (Geometry) getArguments().getSerializable("geometry");
        ifReadOnly = getArguments().getBoolean("read");
        initScale = getArguments().getDouble("scale");
        String address = getArguments().getString("address");
        DetailAddress detailAddress = new DetailAddress();
        detailAddress.setDetailAddress(address);
        mCurrentAddress = detailAddress;
    }


    protected void initView(View view) {

        mapView = (MapView) view.findViewById(R.id.mapview);
        mapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//设置地图背景色、去掉网格线
        //图层按钮
        view.findViewById(R.id.btn_layer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof IDrawerController) {
                    IDrawerController drawerController = (IDrawerController) activity;
                    drawerController.openDrawer(new IDrawerController.OnDrawerOpenListener() {
                        @Override
                        public void onOpened(View drawerView) {
                            showLayerList();
                        }
                    });

                }
            }
        });

        tv_hint = (TextView) view.findViewById(R.id.tv_hint);
        if (geometry != null) {
            //再次编辑模式下
            tv_hint.setText("长按要修改的点，再次点击修改该点位置");
        } else {
            //编辑模式下
            tv_hint.setText("在地图上点击进行选择");
        }
        btn_sure = (Button) view.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (graphic == null) {
                    ToastUtil.shortToast(getActivity(), "请先选择后再返回");
                    return;
                }
                EventBus.getDefault().post(new SendMapFeatureEvent(graphic, mapView.getScale(), mCurrentAddress));
                getActivity().finish();
            }
        });

        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("选择部件");

        ll_component_list = (ViewGroup) view.findViewById(R.id.ll_component_list);


        View btn_choose_current_position = view.findViewById(R.id.select_btn);
        TextView select_text = (TextView) view.findViewById(R.id.select_text);
        select_text.setText("选择当前位置");
        btn_choose_current_position.setVisibility(View.VISIBLE);
        btn_choose_current_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCurrentLocation == null){
                    ToastUtil.shortToast(getActivity(),"无法定位到当前位置");
                    return;
                }

                if (EDIT_LINE.equals(editMode) && mCurrentLocation != null) { //编辑线
                   // AMFindResult amFindResult = new AMFindResult();
                    Point point = new Point(mCurrentLocation.getLongitude(), mCurrentLocation.getLatitude());
                   // amFindResult.setGeometry(point);

                    if (editLineEditStateMapListener != null) {
                        editLineEditStateMapListener.clickPoint(point);
                    }

                    if (editLineReEditStateMapListener != null) {
                        editLineReEditStateMapListener.clickPoint(point);
                    }
                }

            }
        });

        tv_address = (TextView) view.findViewById(R.id.tv_address);

        //只读模式下
        if (ifReadOnly) {
            btn_choose_current_position.setVisibility(View.GONE);
            tv_hint.setVisibility(View.GONE);
        }

        /**
         * 比例尺
         */
        scaleView = (MapScaleView) view.findViewById(R.id.scale_view);
        scaleView.setMapView(mapView);
        mapView.setOnZoomListener(new OnZoomListener() {
            @Override
            public void preAction(float v, float v1, double v2) {

            }

            @Override
            public void postAction(float v, float v1, double v2) {
                scaleView.refreshScaleView();
            }
        });

        /**
         * 图例
         */
        View ll_legend = view.findViewById(R.id.ll_legend);
        ll_legend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLegendPresenter();
                legendPresenter.showLegends();
            }
        });
    }

    protected void initMapListener() {
        if (ifReadOnly) {
            /**
             * 只读模式
             */
            EditLineReadStateMapListener editLineReadStateMapListener = new EditLineReadStateMapListener(mapView, getActivity(), (Polyline) geometry, initScale);
            mapView.setOnSingleTapListener(editLineReadStateMapListener);
        } else if (geometry != null) {
            /**
             * 再次编辑模式
             */
            editLineReEditStateMapListener = getEditLineReEditStateMapListener();
            editLineReEditStateMapListener.setOnGraphicChangedListener(new OnGraphicChangedListener() {
                @Override
                public void onGraphicChanged(Graphic graphic) {
                    EditLineFeatureFragment3.this.graphic = graphic;
                    if (graphic.getGeometry().getType() == Geometry.Type.POLYLINE) {
                        btn_sure.setVisibility(View.VISIBLE);
                    } else {
                        btn_sure.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAddressChanged(DetailAddress detailAddress) {
                    mCurrentAddress = detailAddress;
                    tv_address.setText("当前位置：" + detailAddress.getDetailAddress());
                }

                @Override
                public void onGraphicClear() {

                }
            });
            mapView.setOnTouchListener(editLineReEditStateMapListener);
        } else {
            /**
             * 编辑模式
             */
            editLineEditStateMapListener = getEditLineEditStateMapListener();
            editLineEditStateMapListener.setOnGraphicChangedListener(new OnGraphicChangedListener() {
                @Override
                public void onGraphicChanged(Graphic graphic) {
                    EditLineFeatureFragment3.this.graphic = graphic;
                    if (graphic.getGeometry().getType() == Geometry.Type.POLYLINE) {
                        btn_sure.setVisibility(View.VISIBLE);
                    } else {
                        btn_sure.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAddressChanged(DetailAddress detailAddress) {
                    mCurrentAddress = detailAddress;
                    tv_address.setText("当前位置：" + detailAddress.getDetailAddress());
                }


                @Override
                public void onGraphicClear() {
                    btn_sure.setVisibility(View.GONE);
                }
            });
            mapView.setOnSingleTapListener(editLineEditStateMapListener);
        }
    }

    @NonNull
    protected AbsEditlineEditStateMapListener getEditLineEditStateMapListener() {
        return new EditLineEditStateMapListener(mapView, getActivity(), initScale, ll_component_list);
    }

    @NonNull
    protected AbsEditLineReEditStateMapListener getEditLineReEditStateMapListener() {
        return new EditLineReEditStateMapListener(getActivity(), mapView, (Polyline) geometry, initScale, ll_component_list);
    }


    protected void initLegendPresenter() {
        if (legendPresenter == null) {
            ILegendView legendView = new ImageLegendView(getActivity());
            legendPresenter = new LegendPresenter(legendView, new OnlineLegendService(getActivity()));
        }

    }

    /**
     * 在地图上绘制当前位置
     *
     * @param location
     * @return
     */
    protected void drawLocationOnMap(Location location, boolean ifCenter) {
        if (mGLayerFroDrawLocation == null) {
            mGLayerFroDrawLocation = new GraphicsLayer();
            mapView.addLayer(mGLayerFroDrawLocation);
        }

        Point point = new Point(location.getLongitude(), location.getLatitude());
        mGLayerFroDrawLocation.removeAll();
        if (getActivity() != null) {
            PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(getActivity(),
                    getActivity().getResources().getDrawable(R.mipmap.patrol_location_symbol));
            Graphic graphic = new Graphic(new Point(location.getLongitude(), location.getLatitude()), pictureMarkerSymbol);
            mGLayerFroDrawLocation.addGraphic(graphic);
            if (ifCenter) {
                mapView.centerAt(point, true);
            }
        }

    }

    protected void startLocate() {

        final PatrolLocationManager patrolLocationManager = new PatrolLocationManager(getActivity(), null);
        patrolLocationManager.startLocate(new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {

                mCurrentLocation = location;
                drawLocationOnMap(location, geometry == null);
                requestCurrentLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                //完成后关闭定位
                patrolLocationManager.stopLocate();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
    }

    /**
     * @param latitude
     * @param longitude
     */
    public void requestCurrentLocation(double latitude, double longitude) {
        new SelectLocationService(getActivity(), Locator.createOnlineLocator()).parseLocation(new LatLng(latitude, longitude), mapView.getSpatialReference())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DetailAddress>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DetailAddress detailAddress) {
                        String address = detailAddress.getDetailAddress();
                        mCurrentAddress = detailAddress;
                        if (mCurrentAddress == null) {
                            tv_address.setText("当前位置：" + address);
                        }
                        mUserAddress = detailAddress;
                    }
                });
    }

    /**
     * 显示图层列表
     */
    public void showLayerList() {
        if (layerPresenter != null) {
            layerPresenter.showLayerList();
        }
    }
}
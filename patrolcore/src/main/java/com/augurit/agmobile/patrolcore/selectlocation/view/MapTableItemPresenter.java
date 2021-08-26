package com.augurit.agmobile.patrolcore.selectlocation.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.augurit.agmobile.mapengine.layermanage.view.ILayerPresenter;
import com.augurit.agmobile.mapengine.location.ILocationManager;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerPresenter;
import com.augurit.agmobile.patrolcore.layer.view.PatrolLayerView;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.model.OnSelectLocationFinishEvent;
import com.augurit.agmobile.patrolcore.selectlocation.service.ISelectLocationService;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.agmobile.patrolcore.selectlocation.util.SelectLocationConstant;
import com.augurit.am.cmpt.common.Callback2;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.tasks.geocode.LocatorSuggestionResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.augurit.agmobile.patrolcore.selectlocation.util.SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_LOCATION;
import static com.augurit.agmobile.patrolcore.selectlocation.util.SelectLocationConstant.IF_READ_ONLY;

/**
 * 巡查地图Presenter
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.map.view
 * @createTime 创建时间 ：2017-03-03
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-03
 * @modifyMemo 修改备注：
 */

public class MapTableItemPresenter implements IMapTableItemPresenter {

    public ISelectLocationService mISelectLocationService;
    public IMapTableItem mSelectLocationView;



    //是否只允许查阅位置
    protected boolean ifReadOnly = false;

    // 目的地坐标（地图坐标系）
    protected LatLng mDestinationOrLastTimeSelectLocation;
    protected String mLastAddress = ""; //上次选择的位置

    //位置选择完毕的回调监听
    protected OnReceivedSelectedLocationListener mOnReceivedSelectedLocationListener;

    protected ILocationManager mLocationManager;

    public static final String TAG = "MapTableItemPresenter";

    protected ILayerPresenter mILayerPresenter;

    //是否定位过了
    protected boolean ifLocatedBefore = false;

    protected double initialScale = -1;

    protected boolean mIfEditable = true;
    protected Context mContext;
    private String firstWebviewUrl;
    private String twoWebviewUrl;


    /**
     * 构造函数
     *
     * @param context
     * @param ifEditable  位置是否是可以编辑的
     * @param mapService  不能为空
     * @param mapShortCut 不能为空
     */
    public MapTableItemPresenter(Context context, boolean ifEditable,
                                 ISelectLocationService mapService,
                                 IMapTableItem mapShortCut) {

        this.mISelectLocationService = mapService;
        this.mLocationManager = new PatrolLocationManager(context, null);
        this.mContext = context;

        if (mapShortCut == null) {
            mapShortCut = new EmptyMapTableItem(context);
        }

        this.mSelectLocationView = mapShortCut;
        this.mSelectLocationView.setPresenter(this);
        this.mSelectLocationView.setIfEditable(ifEditable);

        if (mSelectLocationView.getMapView() != null) {
            this.mILayerPresenter = new PatrolLayerPresenter(new PatrolLayerView(context, mSelectLocationView.getMapView(), null));
        }

        //注册事件监听
        EventBus.getDefault().register(this);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param mapService  不能为空
     * @param mapShortCut 不能为空
     */
    public MapTableItemPresenter(Context context,
                                 ISelectLocationService mapService,
                                 IMapTableItem mapShortCut) {

        this.mISelectLocationService = mapService;
        this.mLocationManager = new PatrolLocationManager(context, null);
        this.mContext = context;

        if (mapShortCut == null) {
            mapShortCut = new EmptyMapTableItem(context);
        }

        this.mSelectLocationView = mapShortCut;
        this.mSelectLocationView.setPresenter(this);
        this.mIfEditable = SelectLocationConstant.ifLocationAddressEditable;
        this.mSelectLocationView.setIfEditable(mIfEditable);

        if (mSelectLocationView.getMapView() != null) {
            this.mILayerPresenter = new PatrolLayerPresenter(new PatrolLayerView(context, mSelectLocationView.getMapView(), null));
        }

        //注册事件监听
        EventBus.getDefault().register(this);
    }

    @Override
    public void retryWhenLoadedFail() {

    }

    @Override
    public void showLayerDirectory() {

    }

    /**
     * 只有当mSelectLocationView.getMapView()  ！= null时才加载图层
     */
    @Override
    public void loadMap() {
        if (mILayerPresenter != null) {
            mILayerPresenter.loadLayer();
        }
    }

    @Override
    public void showMapShortCut() {
        if (mSelectLocationView != null) {
            mSelectLocationView.addMapShortCutToContainerWithRemoveAllViews();
            loadMap();
        }
    }

    /**
     * 只有当底图是84时才有效
     */
    @Override
    public void startLocate() {
        /*PermissionsUtil2.getInstance()
                .requestPermissions((Activity) mContext,
                        "需要位置权限才能正常工作，点击确定允许", 101,
                        new PermissionsUtil2.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                startLocateWithCheck();
                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);*/

        PermissionsUtil.getInstance()
                .requestPermissions((Activity) mContext,
                        new PermissionsUtil.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                startLocateWithCheck();
                            }

                            @Override
                            public void onPermissionsDenied(List<String> perms) {

                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void startLocateWithCheck() {
        if (ifLocatedBefore || mSelectLocationView == null) { //如果之前已经定位过了，不需要再次定位
            return;
        }
        if (mILayerPresenter != null && mSelectLocationView.getMapView() != null) {//地图控件显示
            //只有当wgs84坐标系时才定位
            mSelectLocationView.showMapView();
            mLocationManager.startLocate(new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    ifLocatedBefore = true;
                    mDestinationOrLastTimeSelectLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mISelectLocationService.parseLocation(mDestinationOrLastTimeSelectLocation,mSelectLocationView.getMapView().getSpatialReference())
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
                                    mLastAddress = detailAddress.getDetailAddress();
                                    if (mSelectLocationView.containsLocation(location)){ //如果是84坐标系
                                        mSelectLocationView.drawLocationOnMap(new Point(location.getLongitude(), location.getLatitude()));
                                        mSelectLocationView.hideSelectLocationButton();
                                    }

                                    showAddress(detailAddress.getDetailAddress(), mDestinationOrLastTimeSelectLocation);
                                    if (mOnReceivedSelectedLocationListener != null) {
                                        mOnReceivedSelectedLocationListener.onReceivedLocation(mDestinationOrLastTimeSelectLocation, mLastAddress);
                                    }
                                }
                            });

                    //完成后关闭定位
                    mLocationManager.stopLocate();
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
        }else {
            //地图控件不显示情况下，单纯返回定位经纬度，其他什么都不做
            mLocationManager.startLocate(new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    ifLocatedBefore = true;
                    mDestinationOrLastTimeSelectLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    if (mOnReceivedSelectedLocationListener != null) {
                        mOnReceivedSelectedLocationListener.onReceivedLocation(mDestinationOrLastTimeSelectLocation, "");
                    }
                    //完成后关闭定位
                    mLocationManager.stopLocate();
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
    }

    @Override
    public MapView getMapView() {

        if (mSelectLocationView != null){
            return mSelectLocationView.getMapView();
        }
        return null;
    }

    private void showAddress(String s, LatLng latLng) {
        if (mIfEditable) { //如果位置是可编辑的
            mSelectLocationView.showEditableMap(latLng,initialScale);
            mSelectLocationView.showEditableAddress(mLastAddress);
        } else {
            //提供附近的地址进行选择
            searchNearbyAddressAndFillSpinner(s, latLng);
        }
    }

    @Override
    public void startMapActivity() {

        Intent intent = new Intent(mSelectLocationView.getApplicationContext(), SelectLocationActivity.class);
        intent.putExtra(SelectLocationConstant.IF_READ_ONLY, ifReadOnly);
        intent.putExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_LOCATION, mDestinationOrLastTimeSelectLocation);
        intent.putExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_ADDRESS, mLastAddress);
        intent.putExtra(SelectLocationConstant.INITIAL_SCALE, initialScale);
        mSelectLocationView.getContext().startActivity(intent);
        ((Activity) mSelectLocationView.getContext()).overridePendingTransition(R.anim.slide_from_righthide_to_leftshow, R.anim.slide_from_lleftshow_to_lefthide);
    }

    @Override
    public void startWebViewMapActivity() {
        Intent intent = new Intent(mSelectLocationView.getApplicationContext(), WebViewSelectLocationActivity.class);
        intent.putExtra(IF_READ_ONLY, ifReadOnly);
        intent.putExtra(DESTINATION_OR_LASTTIME_SELECT_LOCATION, mDestinationOrLastTimeSelectLocation);
        intent.putExtra(SelectLocationConstant.DESTINATION_OR_LASTTIME_SELECT_ADDRESS, mLastAddress);
        intent.putExtra(SelectLocationConstant.INITIAL_SCALE, initialScale);
        mSelectLocationView.getContext().startActivity(intent);
        ((Activity) mSelectLocationView.getContext()).overridePendingTransition(R.anim.slide_from_righthide_to_leftshow, R.anim.slide_from_lleftshow_to_lefthide);
    }

    @Override
    public void setReadOnly(LatLng location, String address) {
        setDestinationOrLastTimeSelectLocationInLocalCoordinateSystem(location, address);
        mSelectLocationView.showLocationOnMapWhenLayerLoaded(new Point(location.getLongitude(), location.getLatitude()), null);
        mSelectLocationView.hideSelectLocationButton();
        mSelectLocationView.setReadOnly(address);
        ifReadOnly = true;
    }

    @Override
    public void setReEdit(LatLng location, String address) {
        setDestinationOrLastTimeSelectLocationInLocalCoordinateSystem(location, address);
        if (location != null) {
            mSelectLocationView.showLocationOnMapWhenLayerLoaded(new Point(location.getLongitude(), location.getLatitude()), null);
            mSelectLocationView.hideSelectLocationButton();
            showAddress(address, new LatLng(location.getLatitude(), location.getLongitude()));
        }
        // searchNearbyAddressAndFillSpinner(address,new LatLng(location.getLatitude(),location.getLongitude()));
        ifReadOnly = false;
    }

    @Override
    public void setDestinationOrLastTimeSelectLocationInLocalCoordinateSystem(LatLng lng, String address) {
        mDestinationOrLastTimeSelectLocation = lng;
        mLastAddress = address;
    }

    @Override
    public void setOnReceivedSelectedLocationListener(OnReceivedSelectedLocationListener onReceivedSelectedLocationListener) {
        mOnReceivedSelectedLocationListener = onReceivedSelectedLocationListener;
    }

    @Override
    public void exit() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        mSelectLocationView = null;
    }


    @Subscribe
    public void onReceivedSelectLocationFinishEvent(final OnSelectLocationFinishEvent onSelectLocationFinishEvent) {
        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ifLocatedBefore = true;

                if (onSelectLocationFinishEvent.getMapLatlng() != null) {
                    mSelectLocationView.drawLocationOnMap(new Point(onSelectLocationFinishEvent.getMapLatlng().getLongitude(),
                            onSelectLocationFinishEvent.getMapLatlng().getLatitude()));
                    mSelectLocationView.setScale(onSelectLocationFinishEvent.getScale());
                }
                setInitialScale(onSelectLocationFinishEvent.getScale());
                mSelectLocationView.hideSelectLocationButton();

                if (mOnReceivedSelectedLocationListener != null) {
                    setDestinationOrLastTimeSelectLocationInLocalCoordinateSystem(onSelectLocationFinishEvent.getMapLatlng(),
                            onSelectLocationFinishEvent.getAddress());
                    mOnReceivedSelectedLocationListener.onReceivedLocation(
                            onSelectLocationFinishEvent.getMapLatlng(),
                            onSelectLocationFinishEvent.getAddress());
                }
                showAddress(onSelectLocationFinishEvent.getAddress(), onSelectLocationFinishEvent.getMapLatlng());
            }
        });

        // searchNearbyAddressAndFillSpinner(onSelectLocationFinishEvent.getAddressFilterList(),onSelectLocationFinishEvent.getMapLatlng());
    }

    private void searchNearbyAddressAndFillSpinner(final String address, LatLng latLng) {
        mISelectLocationService.getNearByThroughSuggest(address,
                latLng, getView().getMapView().getSpatialReference(), 500, new Callback2<List<LocatorSuggestionResult>>() {
                    @Override
                    public void onSuccess(List<LocatorSuggestionResult> locatorSuggestionResults) {
                        List<String> suggestion = new ArrayList<String>();
                        String[] split = address.replace("附近", "").split("区");
                        String simpleAddress = split[1]; //选择后面的
                        suggestion.add(simpleAddress);
                        for (LocatorSuggestionResult locatorSuggestionResult : locatorSuggestionResults) {
                            if (!locatorSuggestionResult.getText().equals(address.replace("附近", ""))) {
                                suggestion.add(locatorSuggestionResult.getText().split("区")[1]);
                            }
                        }
                        getView().showUnEditableAddress(suggestion);
                    }

                    @Override
                    public void onFail(Exception error) {
                        List<String> suggestion = new ArrayList<String>();
                        String simpleAddress = address.replace("附近", "").split("区")[1];
                        suggestion.add(simpleAddress);
                        getView().showUnEditableAddress(suggestion);
                    }
                });
    }

    @Override
    public LatLng getSelectedLocation() {
        return mDestinationOrLastTimeSelectLocation;
    }

    @Override
    public String getSelectedLocationAddress() {
        return mLastAddress;
    }

    @Override
    public IMapTableItem getView() {
        return mSelectLocationView;
    }


    public void setLocationManager(ILocationManager locationManager) {
        mLocationManager = locationManager;
    }

    public void setInitialScale(double initialScale) {
        this.initialScale = initialScale;
    }

    public double getInitialScale() {
        return initialScale;
    }
}

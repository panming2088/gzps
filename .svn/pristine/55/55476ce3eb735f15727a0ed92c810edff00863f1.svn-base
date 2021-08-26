package com.augurit.agmobile.patrolcore.selectlocation.view.tableitem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.editmap.SendMapFeatureEvent;
import com.augurit.agmobile.patrolcore.editmap.util.EditMapConstant;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.ISelectLocationService;
import com.augurit.agmobile.patrolcore.selectlocation.util.PatrolLocationManager;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.permission.PermissionsUtil;
import com.augurit.am.cmpt.permission.PermissionsUtil2;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 编辑模式下的地图选点
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.selectlocation.view.tableitem
 * @createTime 创建时间 ：17/11/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/1
 * @modifyMemo 修改备注：
 */

public class SelectLocationEditTableItemPresenter implements ISelectLocationTableItemPresenter {


    protected PatrolLocationManager mLocationManager;
    protected Context mContext;

    protected ISelectLocationTableItemView selectLocationTableItem;
    protected ISelectLocationService selectLocationService;


    /**
     * 之前是否定位过
     */
    protected boolean ifLocatedBefore = false;
    /**
     * 上次选择的位置
     */
    protected Geometry mDestinationOrLastTimeSelectLocation;
    /**
     * 上次定位的地址
     */
    protected DetailAddress mLastAddress;

    protected double initScale;

    /**
     * 位置选择完毕的回调监听
     */
    protected IReceivedSelectLocationListener mOnReceivedSelectedLocationListener;

    /**
     * 编辑模式；{@link com.augurit.agmobile.patrolcore.editmap.util.EditModeConstant}
     */
    protected String editMode ;

    /**
     * 构造函数
     *
     * @param context
     * @param mapService 不能为空
     */
    public SelectLocationEditTableItemPresenter(Context context,
                                                ISelectLocationService mapService,
                                                ISelectLocationTableItemView selectLocationTableItem) {

        this.selectLocationService = mapService;
        this.mLocationManager = new PatrolLocationManager(context, null);
        this.mContext = context;
        this.selectLocationTableItem = selectLocationTableItem;
        this.selectLocationTableItem.setPresenter(this);


        //注册事件监听
        EventBus.getDefault().register(this);
    }

    @Override
    public void setView(ISelectLocationTableItemView tableItem) {
        selectLocationTableItem = tableItem;
    }

    @Override
    public ISelectLocationTableItemView getView() {
        return selectLocationTableItem;
    }

    @Override
    public void loadMap() {
        if (selectLocationTableItem != null) {
            selectLocationTableItem.loadMap();
        }
    }

    @Override
    public void addTo(ViewGroup container) {
        selectLocationTableItem.addTo(container);
    }

    @Override
    public void setAddress(String address) {
        List<String> addresses = new ArrayList<>();
        addresses.add(address);
        selectLocationTableItem.setAddress(addresses);
    }

    @Override
    public void setGeometry(final Geometry geometry) {

        mDestinationOrLastTimeSelectLocation = geometry;

        if (selectLocationTableItem.ifMapInitialized()) {
            selectLocationTableItem.addGraphic(geometry, true);
        } else {
            selectLocationTableItem.registerMapViewInitializedCallback(new Callback1<Boolean>() {
                @Override
                public void onCallback(Boolean aBoolean) {
                    selectLocationTableItem.addGraphic(geometry, true);
                }
            });
        }

    }


    @Override
    public void startLocate() {
        if (selectLocationTableItem.ifMapInitialized()) {
            locate();
        } else {
            selectLocationTableItem.registerMapViewInitializedCallback(new Callback1<Boolean>() {
                @Override
                public void onCallback(Boolean aBoolean) {
                    locate();
                }
            });
        }
    }

    @Override
    public void setOnReceivedSelectedLocationListener(IReceivedSelectLocationListener onReceivedSelectedLocationListener) {
        this.mOnReceivedSelectedLocationListener = onReceivedSelectedLocationListener;
    }

    /**
     * 只有当底图是84时才有效
     */
    public void locate() {
        /*PermissionsUtil2.getInstance()
                .requestPermissions((Activity) mContext,
                        "需要位置权限才能正常工作，点击确定允许", 101,
                        new PermissionsUtil2.OnPermissionsCallback() {
                            @Override
                            public void onPermissionsGranted(List<String> perms) {
                                startLocateWithCheck();
                            }
                        },
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);*/

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
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }


    private void startLocateWithCheck() {
        /**
         * 如果之前已经定位过了，不需要再次定位
         */
        if (ifLocatedBefore || selectLocationTableItem == null) {
            return;
        }
        /**
         * 地图控件显示
         */
        if (selectLocationTableItem.getMapView() != null) {
            //只有当wgs84坐标系时才定位
            selectLocationTableItem.showMapView();
            mLocationManager.startLocate(new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {

                    ifLocatedBefore = true;
                    mDestinationOrLastTimeSelectLocation = new Point(location.getLatitude(), location.getLongitude());
                    selectLocationService.parseLocation(new LatLng(location.getLatitude(), location.getLongitude()), selectLocationTableItem.getMapView().getSpatialReference())
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

                                    mLastAddress = detailAddress;
                                    if (selectLocationTableItem.containsLocation(location)) { //如果是84坐标系
                                        selectLocationTableItem.drawLocationOnMap(new Point(location.getLongitude(), location.getLatitude()));
                                        selectLocationTableItem.hideSelectLocationButton();
                                    }

                                    List<String> addresses = new ArrayList<>();
                                    addresses.add(detailAddress.getDetailAddress());
                                    selectLocationTableItem.setAddress(addresses);
                                    selectLocationTableItem.setMapCenter((Point) mDestinationOrLastTimeSelectLocation);

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
        } else {
            //地图控件不显示情况下，单纯返回定位经纬度，其他什么都不做
            mLocationManager.startLocate(new LocationListener() {
                @Override
                public void onLocationChanged(final Location location) {
                    ifLocatedBefore = true;
                    mDestinationOrLastTimeSelectLocation = new Point(location.getLatitude(), location.getLongitude());
                    if (mOnReceivedSelectedLocationListener != null) {
                        mOnReceivedSelectedLocationListener.onReceivedLocation(mDestinationOrLastTimeSelectLocation, null);
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
    public void exit() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        selectLocationTableItem = null;
    }

    @Override
    public void addGeometry(final Geometry geometry) {
        if (selectLocationTableItem.ifMapInitialized()) {
            selectLocationTableItem.addGraphic(geometry, true);
        } else {
            selectLocationTableItem.registerMapViewInitializedCallback(new Callback1<Boolean>() {
                @Override
                public void onCallback(Boolean aBoolean) {
                    selectLocationTableItem.addGraphic(geometry, true);
                }
            });
        }
    }

    @Override
    public void completeIntent(Intent intent) {
        if (mDestinationOrLastTimeSelectLocation != null) {
            intent.putExtra(EditMapConstant.BundleKey.GEOMETRY, mDestinationOrLastTimeSelectLocation);
        }
        intent.putExtra(EditMapConstant.BundleKey.EDIT_MODE, editMode);
        intent.putExtra(EditMapConstant.BundleKey.INIT_SCALE, initScale);
        if (mLastAddress != null){
            intent.putExtra(EditMapConstant.BundleKey.ADDRESS, mLastAddress.getDetailAddress());
        }
    }

    @Override
    public void setEditMode(String editMode) {
        this.editMode = editMode;
    }


    @Subscribe
    public void onReceivedSendMapFeatureEventEvent(final SendMapFeatureEvent sendMapFeatureEvent) {

        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ifLocatedBefore = true;
                mDestinationOrLastTimeSelectLocation = sendMapFeatureEvent.getGraphic().getGeometry();
                mLastAddress = sendMapFeatureEvent.getAddress();
                initScale = sendMapFeatureEvent.getScale();

                selectLocationTableItem.setScale(sendMapFeatureEvent.getScale());
                List<String> addresses = new ArrayList<>();
                addresses.add(sendMapFeatureEvent.getAddress().getDetailAddress());
                selectLocationTableItem.setAddress(addresses);
                //selectLocationTableItem.hideSelectLocationButton();
                selectLocationTableItem.setScale(sendMapFeatureEvent.getScale());

                if (sendMapFeatureEvent.getGraphic() != null) {
                    selectLocationTableItem.setMapCenter(GeometryUtil.getGeometryCenter(sendMapFeatureEvent.getGraphic().getGeometry()));
                    selectLocationTableItem.addGraphic(sendMapFeatureEvent.getGraphic().getGeometry(), true);
                }


                if (mOnReceivedSelectedLocationListener != null) {
                    mOnReceivedSelectedLocationListener.onReceivedLocation(
                            sendMapFeatureEvent.getGraphic().getGeometry(),
                            sendMapFeatureEvent.getAddress());
                }
            }
        });

    }
}

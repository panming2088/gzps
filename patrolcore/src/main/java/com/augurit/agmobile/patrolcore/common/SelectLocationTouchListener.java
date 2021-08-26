package com.augurit.agmobile.patrolcore.common;

import android.content.Context;
import android.icu.math.BigDecimal;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.common.model.LocationInfo;
import com.augurit.agmobile.patrolcore.editmap.widget.LocationMarker;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.agmobile.patrolcore.selectlocation.service.SelectLocationService;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.Callout;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.tasks.geocode.Locator;

import java.text.DecimalFormat;
import java.util.logging.Handler;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 模仿百度选点时的地图事件处理
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common
 * @createTime 创建时间 ：17/10/31
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/10/31
 * @modifyMemo 修改备注：
 */

public class SelectLocationTouchListener extends MapOnTouchListener {


    protected MapView mMapView;
    protected Context mContext;
    /**
     * callout中确定按钮的点击事件
     */
    protected View.OnClickListener calloutSureButtonClickListener;

    protected LocationMarker locationMarker;

    protected boolean enableGeocoding = true;

    protected boolean mShouldRequestNewAddress = true;

    /**
     * 是否执行了放大缩小操作
     */
    protected boolean hasZoomBefore = false;
    /**
     * 上次选择的位置
     */
    protected Point mLastSelectedLocation = null;

    private DetailAddress mLastSelectedAddress = null;

    private boolean isShowPoint = false;

    /**
     * 当位置发生移动时回调的监听
     */
    protected OnSelectedLocationChangedListener onSelectedLocationChangedListener;

    public SelectLocationTouchListener(Context context, MapView view, LocationMarker locationMarker, boolean enableGeocoding, View.OnClickListener calloutSureButtonClickListener) {
        super(context, view);
        this.mContext = context;
        this.mMapView = view;
        this.calloutSureButtonClickListener = calloutSureButtonClickListener;
        this.locationMarker = locationMarker;
        this.enableGeocoding = enableGeocoding;
    }

    public SelectLocationTouchListener(Context context, MapView view, LocationMarker locationMarker, View.OnClickListener calloutSureButtonClickListener) {
        super(context, view);
        this.mContext = context;
        this.mMapView = view;
        this.calloutSureButtonClickListener = calloutSureButtonClickListener;
        this.locationMarker = locationMarker;
    }

    public void setCalloutSureButtonClickListener(View.OnClickListener calloutSureButtonClickListener) {
        this.calloutSureButtonClickListener = calloutSureButtonClickListener;
    }

    private Point getMapCenterPoint() {
        //获取当前的位置
        return mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //LogUtil.d("onTouch");
        if (locationMarker != null && locationMarker.getVisibility() == View.GONE) {
            return super.onTouch(v, event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mMapView.getCallout().isShowing()) {
                    mMapView.getCallout().hide();
                }
                locationMarker.startUpAnimation(null);
                // LogUtil.d("ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                centerToNewAddress(calloutSureButtonClickListener);
                /**
                 * 如果之前执行了放大缩小操作，此时不需要重新获取位置
                 */
                break;
            case MotionEvent.ACTION_CANCEL:
                centerToNewAddress(calloutSureButtonClickListener);
                break;
            default:
                break;
        }
        return super.onTouch(v, event);
    }

    public void centerToNewAddress(final View.OnClickListener calloutSureButtonClickListener) {
        locationMarker.startDownAnimation(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                locate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void locate() {
        final Point point = getMapCenterPoint();
        if (point != null) {
            setLastSelectedLocation(point);
            // mLastSelectedLocation = point;
            String showText = "";
            if (enableGeocoding) {
                showText = "定位中......";
            } else {
                showText = "";
            }
            showCallout(showText, null, calloutSureButtonClickListener);
            if (enableGeocoding) {
                requestLocation(point, mMapView.getSpatialReference(), new Callback1<String>() {
                    @Override
                    public void onCallback(String s) {
                        showCallout(s, point, calloutSureButtonClickListener);
                    }
                });
            }
        }

    }


    @Override
    public boolean onPinchPointersDown(MotionEvent event) {
        if (event == null){
            return true;
        }
        // LogUtil.d("onPinchPointersDown");
        mShouldRequestNewAddress = false;
        return super.onPinchPointersDown(event);
    }

    @Override
    public boolean onPinchPointersMove(MotionEvent event) {
        if (event == null){
            return true;
        }
        //LogUtil.d("onPinchPointersMove");
        return super.onPinchPointersMove(event);
    }

    @Override
    public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
        //LogUtil.d("onDragPointerMove");
        if (from == null || to == null){
            return true;
        }
        return super.onDragPointerMove(from, to);
    }

    @Override
    public boolean onPinchPointersUp(MotionEvent event) {
        // LogUtil.d("onPinchPointersUp");
        if (event == null){
            return true;
        }
        if (mLastSelectedLocation != null && locationMarker.getVisibility() == View.VISIBLE) {
            /**
             * 注：以下两句话的顺序不可改变
             */
            mMapView.centerAt(mLastSelectedLocation, true);
        }
        mShouldRequestNewAddress = true;
        hasZoomBefore = true;
        return super.onPinchPointersUp(event);
    }

    @Override
    public boolean onDoubleTap(MotionEvent point) {
        if (mLastSelectedLocation != null && locationMarker.getVisibility() == View.VISIBLE) {
            /**
             * 注：以下两句话的顺序不可改变
             */
            mMapView.centerAt(mLastSelectedLocation, true);
            requestLocation(mLastSelectedLocation, mMapView.getSpatialReference(), new Callback1<String>() {
                @Override
                public void onCallback(String s) {
                    showCallout(s, null, calloutSureButtonClickListener);
                }
            });
        }
        mShouldRequestNewAddress = true;
        hasZoomBefore = true;
        return super.onDoubleTap(point);
    }


    @Override
    public boolean onLongPressUp(MotionEvent point) {
//            handleTap(point);

        super.onLongPressUp(point);
        return true;
    }


    protected void showCallout(String address, Point point, final View.OnClickListener calloutSureButtonClickListener) {
        Point geometry = null;
        final Callout callout = mMapView.getCallout();
        View view = View.inflate(mContext, com.augurit.agmobile.patrolcore.R.layout.editmap_callout, null);
        TextView title = (TextView) view.findViewById(com.augurit.agmobile.patrolcore.R.id.tv_listcallout_title);
        View divider = view.findViewById(R.id.divider);
        if (!enableGeocoding) {
            title.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
            if(isShowPoint && point != null){
                DecimalFormat df = new DecimalFormat();
                df.setMinimumFractionDigits(6);
                address +="\n("+df.format(point.getX())+","+df.format(point.getY())+")";
            }
            title.setText(address);
        }
        view.findViewById(com.augurit.agmobile.patrolcore.R.id.btn_select_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mLastSelectedAddress == null && enableGeocoding) {
                    ToastUtil.shortToast(mContext.getApplicationContext(), "请等待定位完成再点击确定");
                    return;
                }

                if (mMapView.getCallout().isShowing()) {
                    mMapView.getCallout().animatedHide();
                }
                if (calloutSureButtonClickListener != null) {
                    calloutSureButtonClickListener.onClick(view);
                }
            }
        });
        callout.setStyle(com.augurit.agmobile.patrolcore.R.xml.editmap_callout_style);
        callout.setContent(view);
        if (geometry == null) {
            geometry = mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2 - 140);
        }
        callout.show(geometry);
    }



    /**
     * 跟{@link #showCallout(String, Point, View.OnClickListener)}的区别在于:不需要判断{@link #enableGeocoding}变量,
     * 也就是说,一定会显示文本
     *
     * @param message                        要显示的文本
     * @param point                          callout显示的位置,可以为Null
     * @param calloutSureButtonClickListener "确定"按钮点击事件
     */
    public void showCalloutMessage(String message,final Point point, final View.OnClickListener calloutSureButtonClickListener) {
        if (TextUtils.isEmpty(message)) {
            message = "查无位置信息";
        }

        final Point geometry = point;
        final Callout callout = mMapView.getCallout();
        View view = View.inflate(mContext, com.augurit.agmobile.patrolcore.R.layout.editmap_callout, null);
        TextView title = (TextView) view.findViewById(com.augurit.agmobile.patrolcore.R.id.tv_listcallout_title);
        View divider = view.findViewById(R.id.divider);
        title.setText(message);
        view.findViewById(com.augurit.agmobile.patrolcore.R.id.btn_select_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mMapView.getCallout().isShowing()) {
                    mMapView.getCallout().animatedHide();
                }
                if (calloutSureButtonClickListener != null) {
                    calloutSureButtonClickListener.onClick(view);
                }
            }
        });
        callout.setStyle(com.augurit.agmobile.patrolcore.R.xml.editmap_callout_style);
        callout.setContent(view);

//        final Point finalPoint = point;
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Point finalPoint = point;
                if (finalPoint == null) {
                    finalPoint = mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2 - 140);
                }
                callout.show(finalPoint);
            }
        },800);

    }

    /**
     * 请求百度地址
     *
     * @param point
     * @param callback1
     */
    protected void requestLocation(Point point, SpatialReference spatialReference, final Callback1<String> callback1) {
        if (point != null) {
            SelectLocationService selectLocationService = new SelectLocationService(mContext, Locator.createOnlineLocator());
            selectLocationService.parseLocation(new LatLng(point.getY(), point.getX()), spatialReference)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<DetailAddress>() {
                        @Override
                        public void call(DetailAddress detailAddress) {

                            mLastSelectedAddress = detailAddress;
                            if (callback1 != null) {
                                callback1.onCallback(detailAddress.getDetailAddress());
                            }
                            if (onSelectedLocationChangedListener != null) {
                                onSelectedLocationChangedListener.onAddressChanged(detailAddress);
                            }
                        }
                    });
        }

    }

    public Point getLastSelectLocation() {
        return mLastSelectedLocation;
    }

    public LocationInfo getLoationInfo() {
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setPoint(mLastSelectedLocation);
        locationInfo.setDetailAddress(mLastSelectedAddress);
        return locationInfo;
    }


    private void setLastSelectedLocation(Point mLastSelectedLocation) {
        this.mLastSelectedLocation = mLastSelectedLocation;
        if (onSelectedLocationChangedListener != null) {
            onSelectedLocationChangedListener.onLocationChanged(mLastSelectedLocation);
        }
    }

    public void registerLocationChangedListener(OnSelectedLocationChangedListener onSelectedLocationChangedListener) {
        this.onSelectedLocationChangedListener = onSelectedLocationChangedListener;
    }


    public void destroy() {
        mMapView = null;
        mContext = null;
    }

    public void setShowPoint(boolean showPoint) {
        isShowPoint = showPoint;
    }

    /**
     * 当位置发生移动时回调的监听
     */
    public interface OnSelectedLocationChangedListener {
        void onLocationChanged(Point newLocation);

        void onAddressChanged(DetailAddress detailAddress);
    }
}

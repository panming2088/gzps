package com.augurit.agmobile.patrolcore.selectdevice.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.identify.util.IdentifyServiceFactory;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.selectdevice.model.Device;
import com.augurit.agmobile.patrolcore.selectdevice.model.OnSelectDeviceFinishEvent;
import com.augurit.agmobile.patrolcore.selectdevice.service.FakeSelectDeviceService;
import com.augurit.agmobile.patrolcore.selectdevice.service.ISelectDeviceService;
import com.augurit.agmobile.patrolcore.selectdevice.service.SelectDeviceServiceImpl;
import com.augurit.agmobile.patrolcore.selectdevice.utils.SelectDeviceServiceFactory;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.selectdevice
 * @createTime 创建时间 ：2017-03-29
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-29
 * @modifyMemo 修改备注：
 */

public class PatrolSelectDevicePresenterImpl implements IPatrolSelectDevicePresenter {

    protected ISelectDeviceService mISelectDeviceService;

    protected IPatrolSelectDeviceView mIDeviceNameView; //设施名称view

    protected IPatrolSelectDeviceView mIDeviceIdView; //设施id

    protected boolean mIfShowDeviceNameView ;//是否显示设施名称

    protected boolean mIfShowDeviceIdView ;//是否显示设施id

    protected OnReceivedSelectedDeviceListener mOnReceivedSelectedDeviceListener;



    protected Device mCurrentDevice;

    //当前设施id和名称
    public String mCurrentDeviceId = null;
    public String mCurrentDeviceName = null;

    /**
     * PatrolSelectDevicePresenterImpl构造函数
     * @param context
     * @param viewGroup
     * @param ifUseQR 是否使用扫描二维码的方式，如果true，使用扫描二维码的形式获取设施；如果false，使用跳转到地图的方式进行获取设施
     * @param ifReadOnly 是否是只阅读状态，如果true,将无法编辑
     */
    public PatrolSelectDevicePresenterImpl(Context context, ViewGroup viewGroup, boolean ifUseQR,
                                           boolean ifReadOnly,boolean ifShowDeviceIdView,boolean ifShowDeviceNameView){
        mIDeviceNameView = new PatrolSelectDeviceNameViewImpl(context,viewGroup);
        mIDeviceIdView = new PatrolSelectDeviceIdViewImpl(context,viewGroup);
        mIDeviceNameView.setPresenter(this);
        mISelectDeviceService = SelectDeviceServiceFactory.provideLayerService(context);

        mIfShowDeviceIdView = ifShowDeviceIdView;
        mIfShowDeviceNameView = ifShowDeviceNameView;

        EventBus.getDefault().register(this);
    }

    @Override
    public Device getCurrentDevice() {
        return mCurrentDevice;
    }

    public void setCurrentDevice(Device mCurrentDevice) {
        this.mCurrentDevice = mCurrentDevice;
    }

    @Override
    public void showSelectDeviceView() {

        if (mIfShowDeviceIdView){
            mIDeviceIdView.addSelectDeviceViewToContainerWithoutRemoveAllViews();
        }

        if (mIfShowDeviceNameView){
            mIDeviceNameView.addSelectDeviceViewToContainerWithoutRemoveAllViews();
            //当设施id显示的情况下，不需要显示“跳转到地图的按钮”因为当选择完id后会自动填充名称
            if (mIfShowDeviceIdView){
                mIDeviceNameView.hideJumpToMapButton();
            }
        }

    }

    //todo 获取到位置后查询周围的设施，自动填充;等待接口

    /**
     * 获取的位置后查询周围的设施，自动填充，并用下拉列表展示查找到的数据
     * @param location
     */
    @Override
    public void startAutoComplete(MapView mapView,LatLng location) {
        mISelectDeviceService.getSuggestionDevice(mapView,location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Device>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Device> devices) {
                      //  mIDeviceNameView.setDeviceName(devices.get(0).getName());取消自动填充
                        if (!ListUtil.isEmpty(devices)){
                            mIDeviceNameView.setSuggestions(devices);
                        }
                    }
                });
    }

    @Override
    public void search(String text) {
        mISelectDeviceService.getSuggestionDevice(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Device>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Device> devices) {
                        mIDeviceNameView.notifySuggestionDevicesChanged(devices);
                    }
                });
    }


    @Override
    public void setDeviceNameKey(String key) {
        mIDeviceNameView.setKey(key);
    }

    @Override
    public void setDeviceIdKey(String key) {
        mIDeviceIdView.setKey(key);
    }


    @Override
    public void setOnReceivedSelectedDeviceListener(OnReceivedSelectedDeviceListener onReceivedSelectedDeviceListener) {
        mOnReceivedSelectedDeviceListener = onReceivedSelectedDeviceListener;
    }

    @Override
    public IPatrolSelectDeviceView getView() {
        return mIDeviceNameView;
    }

    @Override
    public void setReadOnly(String deviceId,String deviceNames) {

        this.mCurrentDeviceId = deviceId;
        this.mCurrentDeviceName = deviceNames;

        if (mIfShowDeviceIdView){
            mIDeviceIdView.setReadOnly(deviceId);
        }

        if (mIfShowDeviceNameView){
            mIDeviceNameView.setReadOnly(deviceNames);
        }

    }

    @Override
    public void setReEdit(String deviceId, String deviceNames) {
        this.mCurrentDeviceId = deviceId;
        this.mCurrentDeviceName = deviceNames;

        mIDeviceIdView.setDeviceName(deviceId);
        mIDeviceNameView.setDeviceName(deviceNames);

        mIDeviceIdView.resetBottomsheetClickListener();
        mIDeviceNameView.resetBottomsheetClickListener();
    }

    @Override
    public String getDeviceName() {
        return mIDeviceNameView.getDeviceName();
    }

    @Override
    public void loadDeviceWkt(final boolean ifReadOnly) {
        mIDeviceNameView.showLoading();
        if (TextUtils.isEmpty(mCurrentDeviceId)){
            mIDeviceNameView.hideLoading();
            ToastUtil.shortToast(mIDeviceNameView.getApplicationContext(),"抱歉，"+mCurrentDeviceName+"的id为空，无法查询详细数据");
            return;
        }
        mISelectDeviceService.getWktByDeviceId(mCurrentDeviceId,mCurrentDeviceName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Geometry>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIDeviceNameView.hideLoading();
                    }

                    @Override
                    public void onNext(Geometry geometry) {
                        mIDeviceNameView.hideLoading();
                        if (geometry == null){
                            ToastUtil.shortToast(mIDeviceNameView.getApplicationContext(),"抱歉，查无"+mCurrentDeviceName+"的详细信息," +
                                    "请联系服务端人员查看是否配置了相关图层");
                            return;
                        }
                        if (ifReadOnly){
                            mIDeviceNameView.jumpToReadOnlyDeviceActivity(geometry,mCurrentDeviceName);
                        }else {
                            mIDeviceNameView.jumpToReEditDeviceActivity(geometry,mCurrentDeviceName);
                        }
                    }
                });
    }

    @Override
    public void exit() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public String getCurrentDeviceName() {
        return mCurrentDeviceName;
    }

    @Override
    public String getCurrentDeviceId() {
        return mCurrentDeviceId;
    }


    @Override
    public void setUnEditable() {
        mIDeviceNameView.setUnEditable();
        mIDeviceIdView.setUnEditable();
    }

    @Override
    public void searchNearByDevice(MapView mapView,double longitude, double latitude) {

        mISelectDeviceService.getNearbyDevice(mapView,longitude,latitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Device>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Device device) {
                        if (device != null){
                            mIDeviceNameView.setDeviceName(device.getName());
                            mIDeviceIdView.setDeviceName(device.getId());
                            mCurrentDevice = device;
                        }
                    }
                });
    }

    @Subscribe
    public void onReceiveSelectDeviceFinishEvent(OnSelectDeviceFinishEvent onSelectDeviceFinishEvent){

        mIDeviceNameView.setDeviceName(onSelectDeviceFinishEvent.getDeviceName());
        mIDeviceIdView.setDeviceName(onSelectDeviceFinishEvent.getDeviceId());

        if (mOnReceivedSelectedDeviceListener != null){
            mOnReceivedSelectedDeviceListener.onReceivedDevice(onSelectDeviceFinishEvent.getDeviceName()
            ,onSelectDeviceFinishEvent.getDeviceId());
        }
    }
}

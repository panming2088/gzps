package com.augurit.agmobile.patrolcore.selectdevice.service;

import android.content.Context;


import com.augurit.agmobile.mapengine.common.utils.EsriUtil;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.service.ILayersService;
import com.augurit.agmobile.mapengine.layermanage.service.LayerServiceFactory;
import com.augurit.agmobile.mapengine.map.geometry.LatLng;
import com.augurit.agmobile.patrolcore.selectdevice.model.Device;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.tasks.ags.find.FindParameters;
import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.find.FindTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 用来构造假数据的Service
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.selectdevice.service
 * @createTime 创建时间 ：2017-03-29
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-29
 * @modifyMemo 修改备注：
 */

public class FakeSelectDeviceService implements ISelectDeviceService {

    protected Context mContext;
    public FakeSelectDeviceService(Context context){
        mContext = context;
    }

    String[] deviceNames = {"供电管点GD160267",
            "供电管点GD16025",
            "供电管点GD16025",
            "注记管点JK060498",
            "注记管点JK060498",
            "交通管点XD05342",
            "交通管点XD1110",
            "路灯 LD030444",
            "路灯 LD030445",
            "路灯 LD030446",
            "电信管点DX140722",
            "电信管点 DX140721",
            "电信管点DX140642",
            "电信管点DX140649",
            "电信管点 DX140650",
            "电信管点DX40696",
            "联通管点LX010708",
            "联通管点LX010564",
            "联通管点LX010562",
            "联通管点LX010522"};

    String[] deviceids = {"901",
            "902",
            "903",
            "904",
            "905",
            "906",
            "907",
            "908",
            "908",
            "908",
            "908",
            "908",
            "905",
            "906",
            "907",
            "908",
            "908",
            "908",
            "908",
            "908"};

    String[] newDeviceNames = {"软件路901",
            "软件路902",
            "软件路903",
            "软件路904",
            "软件路905",
            "软件路906",
            "软件路907",
            "软件路908"};

    String[] newDeviceids = {"901",
            "902",
            "903",
            "904",
            "905",
            "906",
            "907",
            "908"};


    @Override
    public Observable<List<Device>> getSuggestionDevice(MapView mapView,LatLng latLng) {
        return Observable.fromCallable(new Callable<List<Device>>() {
            @Override
            public List<Device> call() throws Exception {
                List<Device> devices = new ArrayList<Device>();
                for (int i = 0; i < deviceids.length; i++) {
                    Device device = new Device();
                    device.setId(deviceids[i]);
                    device.setName(deviceNames[i]);
                    devices.add(device);
                }
                return devices;
            }
        });
    }


    @Override
    public Observable<List<Device>> getSuggestionDevice(String text) {
        return Observable.fromCallable(new Callable<List<Device>>() {
            @Override
            public List<Device> call() throws Exception {
                List<Device> devices = new ArrayList<Device>();
                for (int i = 0; i < newDeviceNames.length; i++) {
                    Device device = new Device();
                    device.setId(newDeviceids[i]);
                    device.setName(newDeviceNames[i]);
                    devices.add(device);
                }
                return devices;
            }
        });
    }

    @Override
    public Observable<String> getWktByDeviceId(String deviceId) {
        return null;
    }

    @Override
    public Observable<Geometry> getWktByDeviceId(final String deviceId, final String deviceName) {
        ILayersService layerService2 = LayerServiceFactory.provideLayerService(mContext);
       return layerService2.getSortedLayerInfos()
                .map(new Func1<List<LayerInfo>, Geometry>() {
                    @Override
                    public Geometry call(List<LayerInfo> layerInfos) {
                        List<FindResult> finalResult = new ArrayList<FindResult>();
                        for (LayerInfo layerInfo: layerInfos){
                            Map<String, int[]> findableOrIdentifiableLayer = EsriUtil.getFindableOrIdentifiableLayer(layerInfo);
                            Set<Map.Entry<String, int[]>> entries = findableOrIdentifiableLayer.entrySet();
                            for (Map.Entry<String, int[]> entry : entries){
                                //执行findTask，查找displayField等于deviceName的数据
                                FindTask findTask = new FindTask(entry.getKey());
                                FindParameters findParameters = new FindParameters(deviceName,entry.getValue());
                                try {
                                    List<FindResult> findResults = findTask.execute(findParameters);
                                    if (!ValidateUtil.isListNull(findResults)){
                                        finalResult.addAll(findResults);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        //判断是否有id相同
                        for (FindResult findResult : finalResult){
                            if (deviceId.equals(findResult.getAttributes().get("OBJECTID"))
                                    || deviceId.equals(findResult.getAttributes().get("OBJECTID_1"))
                                    || deviceId.equals(findResult.getAttributes().get("唯一标识"))
                                    || deviceId.equals(findResult.getAttributes().get("FID"))){
                                return findResult.getGeometry();
                            }
                        }
                        return null;
                    }
                }).subscribeOn(Schedulers.io());
    }


    /**
     * 根据当前经纬度查找附近的设施，并默认返回第一个
     * @param longitude
     * @param latitude
     * @return
     */
    @Override
    public Observable<Device> getNearbyDevice(MapView mapView,double longitude, double latitude) {
        return Observable.fromCallable(new Callable<Device>() {
            @Override
            public Device call() throws Exception {
                return null;
            }
        });
    }
}

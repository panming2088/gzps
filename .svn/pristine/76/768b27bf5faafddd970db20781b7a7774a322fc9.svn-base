package com.augurit.agmobile.mapengine.common.baidutransform.pointer;


import com.augurit.agmobile.mapengine.common.baidutransform.pointer.base.GeoPointer;
import com.augurit.agmobile.mapengine.common.baidutransform.util.TransformUtil;

/**
 * GPS设备获取的角度坐标，wgs84坐标
 */
public class WGSPointer extends GeoPointer {

    public WGSPointer() {
    }

    public WGSPointer(double latitude, double longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public GCJPointer toGCJPointer() {
        if (TransformUtil.outOfChina(this.latitude, this.longtitude)) {
            return new GCJPointer(this.latitude, this.longtitude);
        }
        double[] delta = TransformUtil.delta(this.latitude, this.longtitude);
        return new GCJPointer(this.latitude + delta[0], this.longtitude + delta[1]);
    }

    public BDPointer toBDPointer() {
        GCJPointer gcjPointer = toGCJPointer();
        return gcjPointer.toBDPointer();
    }
}


package com.augurit.agmobile.mapengine.common.baidutransform.pointer;


import com.augurit.agmobile.mapengine.common.baidutransform.pointer.base.GeoPointer;
import com.augurit.agmobile.mapengine.common.baidutransform.util.TransformUtil;

/**
 * bd09ll(百度经纬度坐标)
 * Created by QiuYue on 2015-12-28.
 */
public class BDPointer extends GeoPointer {

    public BDPointer() {
    }

    public BDPointer(double latitude, double longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public GCJPointer toGCJPointer() {
        double[] gcjPoint = TransformUtil.bd_decrypt(this.latitude, this.longtitude);
        return new GCJPointer(gcjPoint[0], gcjPoint[1]);
    }

    public WGSPointer toWGSPointer() {
        GCJPointer gcjPointer = toGCJPointer();
        return gcjPointer.toWGSPointer();
    }

    public WGSPointer toExactWGSPointer() {
        GCJPointer gcjPointer = toGCJPointer();
        return gcjPointer.toExactWGSPointer();
    }
}

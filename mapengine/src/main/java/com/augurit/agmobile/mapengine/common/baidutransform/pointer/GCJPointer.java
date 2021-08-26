package com.augurit.agmobile.mapengine.common.baidutransform.pointer;


import com.augurit.agmobile.mapengine.common.baidutransform.pointer.base.GeoPointer;
import com.augurit.agmobile.mapengine.common.baidutransform.util.TransformUtil;

/**
 * google地图、soso地图、aliyun地图、mapabc地图和amap地图所用坐标，国测局坐标
 */
public class GCJPointer extends GeoPointer {


    public GCJPointer() {
    }

    public GCJPointer(double latitude, double longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public WGSPointer toWGSPointer() {
        if (TransformUtil.outOfChina(this.latitude, this.longtitude)) {
            return new WGSPointer(this.latitude, this.longtitude);
        }
        double[] delta = TransformUtil.delta(this.latitude, this.longtitude);
        return new WGSPointer(this.latitude - delta[0], this.longtitude - delta[1]);
    }

    public WGSPointer toExactWGSPointer() {
        final double initDelta = 0.01;
        final double threshold = 0.000001;
        double dLat = initDelta, dLng = initDelta;
        double mLat = this.latitude - dLat, mLng = this.longtitude - dLng;
        double pLat = this.latitude + dLat, pLng = this.longtitude + dLng;
        double wgsLat, wgsLng;
        WGSPointer currentWGSPointer = null;
        for (int i = 0; i < 30; i++) {
            wgsLat = (mLat + pLat) / 2;
            wgsLng = (mLng + pLng) / 2;
            currentWGSPointer = new WGSPointer(wgsLat, wgsLng);
            GCJPointer tmp = currentWGSPointer.toGCJPointer();
            dLat = tmp.getLatitude() - this.getLatitude();
            dLng = tmp.getLongtitude() - this.getLongtitude();
            if ((Math.abs(dLat) < threshold) && (Math.abs(dLng) < threshold)) {
                return currentWGSPointer;
            } else {
//                System.out.println(dLat + ":" + dLng);
            }
            if (dLat > 0) {
                pLat = wgsLat;
            } else {
                mLat = wgsLat;
            }
            if (dLng > 0) {
                pLng = wgsLng;
            } else {
                mLng = wgsLng;
            }
        }
        return currentWGSPointer;
    }

    public BDPointer toBDPointer() {
        double[] bdPoint = TransformUtil.bd_encrypt(this.latitude, this.longtitude);
        return new BDPointer(bdPoint[0], bdPoint[1]);
    }
}


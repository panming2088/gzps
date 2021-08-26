package com.augurit.agmobile.patrolcore.selectlocation.util;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.defaultview.location
 * @createTime 创建时间 ：2017-01-19
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-19
 */

public class EmptyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

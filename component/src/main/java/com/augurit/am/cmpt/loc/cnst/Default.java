package com.augurit.am.cmpt.loc.cnst;

import android.Manifest;


/**
 * Created by Yahya Bayramoglu on 10/02/16.
 */
public final class Default {
    public static final float MIN_ACCURACY = 5.0f;
    public static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int SECOND = 1000;
    public static final int WAIT_PERIOD = 5 * SECOND;
    private static final int MINUTE = 60 * SECOND;

    private Default() {
    }

}
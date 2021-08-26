package com.augurit.am.fw.utils;

import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by ac on 2016-07-26.
 */
public final class MotionEventUtil {
    private MotionEventUtil() {
    }

    public static double getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        //		return FloatMath.sqrt(x * x + y * y);
        return Math.sqrt(x * x + y * y);
    }


    public static void resolveMiddlePoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}

package com.augurit.agmobile.mapengine.common.baidutransform.util;

/**
 * Created by xcl on 2017/12/4.
 */

public class TransformUtil {
    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    public static boolean outOfChina(double lat, double lng) {
        if ((lng < 72.004) || (lng > 137.8347)) {
            return true;
        }
        if ((lat < 0.8293) || (lat > 55.8271)) {
            return true;
        }
        return false;
    }

    public static double transformLat(double x, double y) {
        double ret =
                -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret +=
                (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * @param lat 纬度
     * @param lng 经度
     * @return delta[0] 是纬度差，delta[1]是经度差
     */
    public static double[] delta(double lat, double lng) {
        double[] delta = new double[2];
        double a = 6378245.0;
        double ee = 0.00669342162296594323;
        double dLat = transformLat(lng - 105.0, lat - 35.0);
        double dLng = transformLon(lng - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        delta[0] = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * Math.PI);
        delta[1] = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * Math.PI);
        return delta;
    }

    /**
     * @param gcj_lat 国测局纬度
     * @param gcj_lon 国测局经度
     * @return 百度经纬度坐标
     */
    public static double[] bd_encrypt(double gcj_lat, double gcj_lon) {
        double[] bdPoint = new double[2];
        double x = gcj_lon, y = gcj_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        bdPoint[0] = z * Math.sin(theta) + 0.006;
        bdPoint[1] = z * Math.cos(theta) + 0.0065;
        return bdPoint;
    }

    /**
     * @param bd_lat 百度纬度
     * @param bd_lon 百度经度
     * @return 国测局经纬度坐标
     */
    public static double[] bd_decrypt(double bd_lat, double bd_lon) {
        double[] gcjPoint = new double[2];
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        gcjPoint[0] = z * Math.sin(theta);
        gcjPoint[1] = z * Math.cos(theta);
        return gcjPoint;
    }
}

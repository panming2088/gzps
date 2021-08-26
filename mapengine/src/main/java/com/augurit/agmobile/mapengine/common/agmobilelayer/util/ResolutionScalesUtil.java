package com.augurit.agmobile.mapengine.common.agmobilelayer.util;

import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.agmobile.mapengine.common.agmobilelayer.util
 * @createTime 创建时间 ：2017-08-28
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-08-28 13:57
 */
public class ResolutionScalesUtil {
    private static double meterPerInch = 0.0254d;

    public static double getScalesFromResolutions(double resolution, double dpi, SpatialReference spatialReference) {
        double param = getParam(spatialReference, dpi);
        return resolution * dpi * param / meterPerInch;
    }

    public static double getResolutionsFromScales(double scale, double dpi, SpatialReference spatialReference) {
        double param = getParam(spatialReference, dpi);
        return scale * meterPerInch / (dpi * param);
    }

    private static double getParam(SpatialReference spatialReference, double dpi) {
        double param = 1.0d;
        if (spatialReference != null) {
            Unit unit = spatialReference.getUnit();
            if (unit == null) {
                return param;
            }
            Unit.UnitType unitType = unit.getUnitType();
            if (unitType == Unit.UnitType.ANGULAR || unitType == Unit.UnitType.LINEAR) {
                if (unitType == Unit.UnitType.ANGULAR) {
                    param = 111319.491D;
                } else if (unitType == Unit.UnitType.LINEAR) {
                    param = 1.0D;
                }
            }
        }
        return param;
    }
}

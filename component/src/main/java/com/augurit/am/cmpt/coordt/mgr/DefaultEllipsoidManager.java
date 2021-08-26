package com.augurit.am.cmpt.coordt.mgr;

import com.augurit.am.cmpt.coordt.model.EllipsoidParameter;

/**
 * 用于产生WGS84,北京54,国测2000和西安80坐标系默认的椭球参数
 */
public final class DefaultEllipsoidManager {
    /**
     * WGS84坐标系的椭球参数中的长半轴，默认是6378137
     */
    public static final double ELLIPSOIDPARAMETER_84_A = 6378137;
    /**
     * WGS84坐标系椭球参数中的扁率，默认是1 / 298.257223563
     */
    public static final double ELLIPSOIDPARAMETER_84_F = 1 / 298.257223563;
    /**
     * 西安80坐标系椭球参数中的长半轴，默认是6378140
     */
    public static final double ELLIPSOIDPARAMETER_80_A = 6378140;
    /**
     * 西安80坐标系椭球参数中的扁率，默认是1 / 298.257
     */
    public static final double ELLIPSOIDPARAMETER_80_F = 1 / 298.257;
    /**
     * 北京54坐标系椭球参数中的长半轴，默认是6378245
     */
    public static final double ELLIPSOIDPARAMETER_54_A = 6378245;
    /**
     * 北京54坐标系椭球参数中的扁率，默认是1 / 298.3
     */
    public static final double ELLIPSOIDPARAMETER_54_F = 1 / 298.3;
    /**
     * 国测2000坐标系椭球参数中的长半轴，默认是6378245
     */
    public static final double ELLIPSOIDPARAMETER_2000_A = 6378245;
    /**
     * 国测2000坐标系椭球参数中的扁率，默认是1 / 298.257222101
     */
    public static final double ELLIPSOIDPARAMETER_2000_F = 1 / 298.257222101;
    private DefaultEllipsoidManager() {
    }

    /**
     * 产生坐标系的默认椭球参数
     *
     * @param type 坐标系类型
     * @return 默认椭球参数
     */
    public static EllipsoidParameter getDefalutEllipsoidParameter(EllipsoidType type) {
        EllipsoidParameter ellipsoidParameter = new EllipsoidParameter();
        switch (type) {
            case XIAN1980:
                ellipsoidParameter.setA(ELLIPSOIDPARAMETER_80_A);
                ellipsoidParameter.setF(ELLIPSOIDPARAMETER_80_F);
                break;
            case WGS84:
                ellipsoidParameter.setA(ELLIPSOIDPARAMETER_84_A);
                ellipsoidParameter.setF(ELLIPSOIDPARAMETER_84_F);
                break;
            case CGCS2000:
                ellipsoidParameter.setA(ELLIPSOIDPARAMETER_2000_A);
                ellipsoidParameter.setF(ELLIPSOIDPARAMETER_2000_F);
                break;
            case BEIJING54:
                ellipsoidParameter.setA(ELLIPSOIDPARAMETER_54_A);
                ellipsoidParameter.setF(ELLIPSOIDPARAMETER_54_F);
                break;
        }
        return ellipsoidParameter;
    }

    /**
     * 枚举类，用于判断要产生哪个坐标系的椭球参数
     */
    public enum EllipsoidType {
        /**
         * WGS84坐标系
         */
        WGS84,
        /**
         * 西安80坐标系
         */
        XIAN1980,
        /**
         * 国测2000坐标系
         */
        CGCS2000,
        /**
         * 北京54坐标系
         */
        BEIJING54
    }
}

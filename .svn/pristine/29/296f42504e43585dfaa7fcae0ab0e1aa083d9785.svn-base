package com.augurit.am.cmpt.coordt.mgr;


import com.augurit.am.cmpt.coordt.model.Coordinate;
import com.augurit.am.cmpt.coordt.model.EllipsoidParameter;
import com.augurit.am.cmpt.coordt.model.ParameterFour;
import com.augurit.am.cmpt.coordt.model.ParameterSeven;
import com.augurit.am.cmpt.coordt.intfc.ICoordConvertor;
import com.augurit.am.cmpt.coordt.intfc.ICoordConvertorBase;
import com.augurit.am.cmpt.coordt.impl.CoordConvertorBaseImpl;
import com.augurit.am.cmpt.coordt.impl.CoordConvertorImpl;

import java.util.List;

/**
 *
 * 包名：com.augurit.am.cmpt.coordt.mgr
 * 文件描述：这个类用于管理所有的转换工具类，
 *          里面封装了不同坐标系之间的转换方法,包括WGS84，西安1980和国测2000坐标系间的互转以及WGS84坐标转当地坐标
 * 创建人：xuciluan
 * 创建时间：2016-08-31 11:08
 * 修改人：xuciluan
 * 修改时间：2016-08-31 11:08
 * 修改备注：
 * @version
 *
 */
public class CoordTransformManager {

    private static ICoordConvertor sICoordConvertor = new CoordConvertorImpl();
    private static ICoordConvertorBase sICoordConvertorBase = new CoordConvertorBaseImpl();

    /**
     *
     */
    /**
     * 将WGS84坐标转成西安80坐标
     * @param origin 要转换的WGS84坐标点
     * @param ellipsoid_84  WGS84坐标系椭球定义
     * @param ellipsoid_80  西安80坐标系椭球定义
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param lz 本初子午线（该数据来自AGCOM部门）
     * @return 转换后的西安80坐标点
     */
    public static Coordinate transFromWGS84ToXian80(Coordinate origin,
                                                    EllipsoidParameter ellipsoid_84,
                                                    EllipsoidParameter ellipsoid_80,
                                                    ParameterSeven seven,
                                                    double lz){

       return sICoordConvertor.CS_1ToCS_2(origin,seven,lz,ellipsoid_84,ellipsoid_80,"LBH","xyh");
    }

    /**
     * 将WGS84坐标转成西安80坐标,采用默认的WGS84和西安80坐标系的椭球参数<br>
     * <p>关于各坐标系的默认椭球参数，请参考{@link DefaultEllipsoidManager}</p>
     * @param origin 要转换的WGS84坐标点
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param lz 本初子午线（该数据来自AGCOM部门）
     * @return 转换后的西安80坐标点
     */
    public static Coordinate transFromWGS84ToXian80(Coordinate origin, ParameterSeven seven, double lz){
        EllipsoidParameter ellipsoid_84= DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.WGS84);
        EllipsoidParameter ellipsoid_80 = DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.XIAN1980);
        return sICoordConvertor.CS_1ToCS_2(origin,seven,lz,ellipsoid_84,ellipsoid_80,"LBH","xyh");
    }

    /**
     * 将WGS84坐标系转换成地方坐标系
     * @param origin 要转换的WGS84坐标点
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param four 四参数（该数据来自AGCOM部门）
     * @param lz 本地本初子午线（该数据来自AGCOM部门）
     * @return 转换后的当地坐标点
     */
    public static Coordinate transFromWGS84ToLocal(Coordinate origin,
                                                   ParameterSeven seven,
                                                   ParameterFour four, double lz){
        Coordinate coordinate80 = transFromWGS84ToXian80(origin, seven,lz);
        return transFromPlaToPlaWithFourParam(coordinate80, four);
    }


    /**
     * 将地方坐标系转换成WGS84坐标系,原理：先将本地坐标转成西安80坐标，之后再从西安80坐标转成WGS84坐标
     * @param origin 要转换的地方坐标系
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param four 四参数（该数据来自AGCOM部门）
     * @param lz 本地本初子午线（该数据来自AGCOM部门）
     * @return 转换后的WGS84坐标点
     */
    public static Coordinate transFromLocalToWGS84(Coordinate origin,
                                                   ParameterSeven seven,
                                                   ParameterFour four, double lz){
        Coordinate coordinate = transFromPlaToPlaWithFourParam(origin, four);
        EllipsoidParameter ellipsoid_84= DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.WGS84);
        EllipsoidParameter ellipsoid_80 = DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.XIAN1980);
        return sICoordConvertor.CS_1ToCS_2(coordinate,seven,lz,ellipsoid_80,ellipsoid_84,"xyh","LBH");
    }


    public static Coordinate transFromLocalToXian80(Coordinate origin,
                                                   ParameterFour four){
        return transFromPlaToPlaWithFourParam(origin, four);
    }


    /**
     * 将西安80坐标转成WGS84坐标，需要传入西安80和84坐标系的椭球参数
     * @param origin 要转换的西安80坐标点
     * @param ellipsoid_80  西安80坐标系椭球定义
     * @param ellipsoid_84  WGS84坐标系椭球定义
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param lz 本初子午线（该数据来自AGCOM部门）
     * @return 转换后的WGS84坐标点
     */
    public static Coordinate transFromGeo80ToWGS84(Coordinate origin,EllipsoidParameter ellipsoid_80,EllipsoidParameter ellipsoid_84,ParameterSeven seven,double lz){

        return sICoordConvertor.CS_1ToCS_2(origin,seven,lz,ellipsoid_80,ellipsoid_84,"xyh","LBH");
    }

    /**
     * 将西安80坐标转成WGS84坐标，采用默认的WGS84和西安80坐标系的椭球参数<br>
     * <p>关于各坐标系的默认椭球参数，请参考{@link DefaultEllipsoidManager}</p>
     * @param origin 要转换的西安80坐标点
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param lz 本初子午线（该数据来自AGCOM部门）
     * @return 转换后的WGS84坐标点
     */
    public static Coordinate transFromGeo80ToWGS84(Coordinate origin,ParameterSeven seven,double lz){
        EllipsoidParameter ellipsoid_84= DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.WGS84);
        EllipsoidParameter ellipsoid_80 = DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.XIAN1980);
        return sICoordConvertor.CS_1ToCS_2(origin,seven,lz,ellipsoid_80,ellipsoid_84,"xyh","LBH");
    }

    /**
     * 将WGS84坐标转成北京54坐标,需要传入椭球参数
     * @param origin 要转换的WGS84坐标点
     * @param ellipsoid_84  WGS84坐标系椭球定义
     * @param ellipsoid_54  北京54坐标系椭球定义
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param lz 本初子午线（该数据来自AGCOM部门）
     * @return 转换后的北京54坐标点
     */
    public static Coordinate transFromWGS84To54(Coordinate origin,EllipsoidParameter ellipsoid_84,EllipsoidParameter ellipsoid_54,ParameterSeven seven,double lz){

        return sICoordConvertor.CS_1ToCS_2(origin,seven,lz,ellipsoid_84,ellipsoid_54,"LBH","xyh");
    }

    /**
     *将WGS84坐标转成北京54坐标，采用默认的椭球参数<br>
     * <p>关于各坐标系的默认椭球参数，请参考{@link DefaultEllipsoidManager}</p>
     * @param origin 要转换的WGS84坐标点
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param lz 本初子午线（该数据来自AGCOM部门）
     * @return 转换后的北京54坐标点
     */
    public static Coordinate transFromWGS84To54(Coordinate origin,ParameterSeven seven,double lz){
        EllipsoidParameter ellipsoid_84= DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.WGS84);
        EllipsoidParameter ellipsoid_54 = DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.BEIJING54);
        return sICoordConvertor.CS_1ToCS_2(origin,seven,lz,ellipsoid_84,ellipsoid_54,"LBH","xyh");
    }


    /**
     * 将北京54坐标转成WGS84坐标
     * @param origin 要转换的北京54坐标点
     * @param ellipsoid_54  北京54坐标系椭球定义
     * @param ellipsoid_84  WGS84坐标系椭球定义
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param lz 本初子午线（该数据来自AGCOM部门）
     * @return 转换后的WGS84坐标点
     */
    public static Coordinate transFrom54ToWGS84(Coordinate origin,EllipsoidParameter ellipsoid_54,EllipsoidParameter ellipsoid_84,ParameterSeven seven,double lz){
        return sICoordConvertor.CS_1ToCS_2(origin,seven,lz,ellipsoid_54,ellipsoid_84,"xyh","LBH");
    }

    /**
     * 将北京54坐标转成WGS84坐标,采用默认的椭球参数<br>
     * <p>关于各坐标系的默认椭球参数，请参考{@link DefaultEllipsoidManager}</p>
     * @param origin 要转换的北京54坐标点
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param lz 本初子午线（该数据来自AGCOM部门）
     * @return 转换后的WGS84坐标点
     */
    public static Coordinate transFrom54ToWGS84(Coordinate origin,ParameterSeven seven,double lz){
        EllipsoidParameter ellipsoid_84= DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.WGS84);
        EllipsoidParameter ellipsoid_54 = DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.BEIJING54);
        return sICoordConvertor.CS_1ToCS_2(origin,seven,lz,ellipsoid_54,ellipsoid_84,"xyh","LBH");
    }

    /**
     * 将WGS84坐标转成国测2000坐标,采用默认的椭球参数<br>
     * <p>关于各坐标系的默认椭球参数，请参考{@link DefaultEllipsoidManager}</p>
     * @param origin 要转换的WGS84坐标点
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param lz 本初子午线（该数据来自AGCOM部门）
     * @return 转换后的国测2000坐标点
     */
    public static Coordinate transFromWGS84To2000(Coordinate origin,ParameterSeven seven,double lz){
        EllipsoidParameter ellipsoid_84= DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.WGS84);
        EllipsoidParameter ellipsoid_2000 = DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.CGCS2000);
        return sICoordConvertor.CS_1ToCS_2(origin,seven,lz,ellipsoid_84,ellipsoid_2000,"LBH","xyh");
    }
    /**
     * 将WGS84坐标转成国测2000坐标
     * @param origin 要转换的WGS84坐标点
     * @param ellipsoid_84  WGS84坐标系椭球定义
     * @param ellipsoid_2000  国测2000坐标系椭球定义
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param lz 本初子午线（该数据来自AGCOM部门）
     * @return 转换后的国测2000坐标点
     */
    public static Coordinate transFromWGS84To2000(Coordinate origin,EllipsoidParameter ellipsoid_84,EllipsoidParameter ellipsoid_2000,ParameterSeven seven,double lz){
        return sICoordConvertor.CS_1ToCS_2(origin,seven,lz,ellipsoid_84,ellipsoid_2000,"LBH","xyh");
    }

    /**
     * 将国测2000坐标转成WGS84坐标,采用默认的椭球参数<br>
     * <p>关于各坐标系的默认椭球参数，请参考{@link DefaultEllipsoidManager}</p>
     * @param origin 要转换的国测2000坐标
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param lz 本初子午线（该数据来自AGCOM部门）
     * @return 转换后的WGS84坐标
     */
    public static Coordinate transFrom2000ToWGS84(Coordinate origin,ParameterSeven seven,double lz){
        EllipsoidParameter ellipsoid_84= DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.WGS84);
        EllipsoidParameter ellipsoid_2000 = DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.CGCS2000);
        return sICoordConvertor.CS_1ToCS_2(origin,seven,lz,ellipsoid_2000,ellipsoid_84,"xyh","LBH");
    }
    /**
     * 将国测2000坐标转成WGS84坐标
     * @param origin 要转换的国测2000坐标点
     * @param ellipsoid_84  国测2000坐标系椭球定义
     * @param ellipsoid_2000  WGS84坐标系椭球定义
     * @param seven 七参数（该数据来自AGCOM部门）
     * @param lz 本初子午线（该数据来自AGCOM部门）
     * @return 转换后的WGS84坐标点
     */
    public static Coordinate transFrom2000ToWGS84(Coordinate origin,EllipsoidParameter ellipsoid_84,EllipsoidParameter ellipsoid_2000,ParameterSeven seven,double lz){
        return sICoordConvertor.CS_1ToCS_2(origin,seven,lz,ellipsoid_2000,ellipsoid_84,"xyh","LBH");
    }

    /**
     * 平面坐标之间的转换（xy_1-&gtxy_2,已知四参数）
     * @param coordinate 原坐标系坐标
     * @param parameterFour 目标坐标系的四参数
     * @return 目标坐标系坐标
     */
    public static Coordinate transFromPlaToPlaWithFourParam(Coordinate coordinate,ParameterFour parameterFour){
        return sICoordConvertor.CS_1ToCS_2(coordinate, parameterFour);
    }

    /**
     * 计算平面两个点之间的距离，主要用于计算坐标转换的误差
     * @param c  由计算得到的坐标点
     * @param c_2 已知点坐标
     * @return 点位中误差，也就是两点间距离
     */
    public static double calculateProError(Coordinate c,Coordinate c_2){
        return sICoordConvertorBase.CalcuteErrorTwo(c, c_2);
    }

    /**
     * 计算空间中两个点之间的距离，主要用于计算坐标转换的误差
     * @param c 由计算得到的点坐标
     * @param c_2 已知点坐标
     * @return 点位中误差，也就是空间中两点距离
     */
    public static double calculateSpaceError(Coordinate c,Coordinate c_2){
        return sICoordConvertorBase.CalcuteErrorThree(c, c_2);
    }


    /**
     * 通过重合点坐标计算出七参数，进而将一个WGS84坐标转成80坐标
     * @param coor_1 WGS84坐标
     * @param coincidentPoint 重合点集合，至少3个点
     * @param Lz 中央经度
     * @return 转换后的80坐标
     */
    public static Coordinate transFromWGS84TOXian80ByCoincidentPoints(Coordinate coor_1, List coincidentPoint, double Lz)
    {
        EllipsoidParameter ellipsoid_84= DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.WGS84);
        EllipsoidParameter ellipsoid_80 = DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.XIAN1980);
        return sICoordConvertor.CS_1ToCS_2(coor_1,coincidentPoint,Lz,ellipsoid_84,ellipsoid_80,"LBH","xyh");
    }

    /**
     * 通过重合点坐标计算出七参数，进而将一个80坐标转成WGS84坐标
     *
     * @param coor_1 80坐标
     * @param coincidentPoint 重合点集合，至少3个点
     * @param Lz 中央经度
     * @return WGS84坐标
     */
    public static Coordinate transFromXian80TOWGS84ByCoincidentPoints(Coordinate coor_1, List coincidentPoint, double Lz)
    {
        EllipsoidParameter ellipsoid_84= DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.WGS84);
        EllipsoidParameter ellipsoid_80 = DefaultEllipsoidManager.getDefalutEllipsoidParameter(DefaultEllipsoidManager.EllipsoidType.XIAN1980);
        return sICoordConvertor.CS_1ToCS_2(coor_1,coincidentPoint,Lz,ellipsoid_80,ellipsoid_84,"xyh","LBH");
    }

    /**
     * 已经重合点，进行坐标转换。
     * @param coor_1
     * @param coincidentPoint
     * @return
     */
    public static Coordinate transByCoincidentPoints (Coordinate coor_1, List coincidentPoint){
        return sICoordConvertor.CS_1ToCS_2(coor_1,coincidentPoint);
    }
}

package com.augurit.am.cmpt.loc;

import android.location.Location;

import com.augurit.am.cmpt.coordt.mgr.CoordTransformManager;
import com.augurit.am.cmpt.coordt.model.Coordinate;
import com.augurit.am.cmpt.coordt.model.EllipsoidParameter;
import com.augurit.am.cmpt.coordt.model.ParameterFour;
import com.augurit.am.cmpt.coordt.model.ParameterSeven;
import com.augurit.am.fw.utils.log.LogUtil;

/**
 * WGS84转厦门92坐标系
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.loc
 * @createTime 创建时间 ：17/7/27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/27
 * @modifyMemo 修改备注：
 */

public class XiamenLocationTransform implements ILocationTransform {


    /**
     * 以下是七参数的设置，不同坐标系，七参数的值不同
     */
    public static final double PARAMSEVEN_DX = 430.5331497192383;
    public static final double PARAMSEVEN_DY =  -802.6758804321289;
    public static final double PARAMSEVEN_DZ = 1403.6879329681396;
    public static final double PARAMSEVEN_M =   0.9997551347769331;
    public static final double PARAMSEVEN_WX = 9.304166906076716E-5;
    public static final double PARAMSEVEN_WY = 8.278110588832988E-5;
    public static final double PARAMSEVEN_WZ = 0.02620268470582232;


    /**
     * 四参数
     */
    public static double A = 1.00001162066127;
    public static double B = -7.37825147362203E-06;
    public static double DX = -88.2437415263848;
    public static double DY = -30.0190511553082;


    /**
     * 这是初始坐标系的椭球定义，这里的参数是84坐标系的椭球定义
     */
    public static final double ORIGIN_ELLIPSOIDPARAMETER_A = 6378137;
    public static final double ORIGIN_ELLIPSOIDPARAMETER_F = 1 / 298.257223563;
    /**
     * 这是目标坐标系的椭球定义，这里的参数是80坐标系的椭球定义
     */
    public static final double TARGET_ELLIPSOIDPARAMETER_A = 6378140;
    public static final double TARGET_ELLIPSOIDPARAMETER_F = 1 / 298.257;

    /**
     * 本初子午线，不同坐标系本初子午线不同，这里表示的是萝岗本地坐标系的本初子午线
     */
    public static final double LZ = 117;

    @Override
    public Coordinate changeWGS84ToCurrentLocation(Location location) {
        Coordinate coordinate = new Coordinate(location.getLongitude(), location.getLatitude());
        EllipsoidParameter ep_1 = new EllipsoidParameter();//84
        ep_1.setA(ORIGIN_ELLIPSOIDPARAMETER_A);
        ep_1.setF(ORIGIN_ELLIPSOIDPARAMETER_F);

        EllipsoidParameter ep_2 = new EllipsoidParameter();//80
        ep_2.setA(TARGET_ELLIPSOIDPARAMETER_A);
        ep_2.setF(TARGET_ELLIPSOIDPARAMETER_F);

        ParameterSeven seven = new ParameterSeven();
        seven.setDX(PARAMSEVEN_DX);
        seven.setDY(PARAMSEVEN_DY);
        seven.setDZ(PARAMSEVEN_DZ);
        seven.setWX(PARAMSEVEN_WX);
        seven.setWY(PARAMSEVEN_WY);
        seven.setWZ(PARAMSEVEN_WZ);
        seven.setM(PARAMSEVEN_M);

        ParameterFour parameterFour = new ParameterFour();
        parameterFour.setA(A);
        parameterFour.setB(B);
        parameterFour.setDX(DX);
        parameterFour.setDY(DY);

      /*
       根据重合点计算WGS84转本地坐标
      List<CoincidentPoint> coincidentPoints = new ArrayList<>();
        //第一组
        Coordinate co1_1 = new Coordinate(118.1463889,24.71611111);
        Coordinate co1_2 = new Coordinate(2734712.237,464147.6307);
        CoincidentPoint co1 = new CoincidentPoint(co1_1,co1_2);
        coincidentPoints.add(co1);

        //第一组
        Coordinate co2_1 = new Coordinate(117.9986111,24.58222222);
        Coordinate co2_2 = new Coordinate(2719928.387,449141.116);
        CoincidentPoint co2 = new CoincidentPoint(co2_1,co2_2);
        coincidentPoints.add(co2);

        //第一组
        Coordinate co3_1 = new Coordinate(118.3261111,24.56944444);
        Coordinate co3_2 = new Coordinate(2718431.3,482311.3377);
        CoincidentPoint co3 = new CoincidentPoint(co3_1,co3_2);
        coincidentPoints.add(co3);

        //第一组
        Coordinate co4_1 = new Coordinate(118.2830556,24.77611111);
        Coordinate co4_2 = new Coordinate(2741329.457,477986.2323);
        CoincidentPoint co4 = new CoincidentPoint(co4_1,co4_2);
        coincidentPoints.add(co4);


        //第一组
        Coordinate co5_1 = new Coordinate(117.99,24.82444444);
        Coordinate co5_2 = new Coordinate(2746762.434,448368.786);
        CoincidentPoint co5 = new CoincidentPoint(co5_1,co5_2);
        coincidentPoints.add(co5);
        Coordinate coordinate1 = CoordTransformManager.transFromWGS84TOXian80ByCoincidentPoints(coordinate,coincidentPoints,117);
        Coordinate coordinate2 = CoordTransformManager.transByCoincidentPoints(coordinate1, parameterFour);*/


        Coordinate coordinate1 = CoordTransformManager.transFromWGS84ToLocal(coordinate, seven, parameterFour, LZ);
        LogUtil.d("转换后的坐标：" + coordinate1.getX() +  ":" + coordinate.getY());
        // Coordinate coordinate1 = new Coordinate(coordinate2.getY(),coordinate2.getX());

        return coordinate1;
    }

    @Override
    public Coordinate changeWGS84ToCurrentLocation(double log, double lat) {
        Coordinate coordinate = new Coordinate(log, lat);
        EllipsoidParameter ep_1 = new EllipsoidParameter();//84
        ep_1.setA(ORIGIN_ELLIPSOIDPARAMETER_A);
        ep_1.setF(ORIGIN_ELLIPSOIDPARAMETER_F);

        EllipsoidParameter ep_2 = new EllipsoidParameter();//80
        ep_2.setA(TARGET_ELLIPSOIDPARAMETER_A);
        ep_2.setF(TARGET_ELLIPSOIDPARAMETER_F);

        ParameterSeven seven = new ParameterSeven();
        seven.setDX(PARAMSEVEN_DX);
        seven.setDY(PARAMSEVEN_DY);
        seven.setDZ(PARAMSEVEN_DZ);
        seven.setWX(PARAMSEVEN_WX);
        seven.setWY(PARAMSEVEN_WY);
        seven.setWZ(PARAMSEVEN_WZ);
        seven.setM(PARAMSEVEN_M);

        ParameterFour parameterFour = new ParameterFour();
        parameterFour.setA(A);
        parameterFour.setB(B);
        parameterFour.setDX(DX);
        parameterFour.setDY(DY);

      /*
       根据重合点计算WGS84转本地坐标
      List<CoincidentPoint> coincidentPoints = new ArrayList<>();
        //第一组
        Coordinate co1_1 = new Coordinate(118.1463889,24.71611111);
        Coordinate co1_2 = new Coordinate(2734712.237,464147.6307);
        CoincidentPoint co1 = new CoincidentPoint(co1_1,co1_2);
        coincidentPoints.add(co1);

        //第一组
        Coordinate co2_1 = new Coordinate(117.9986111,24.58222222);
        Coordinate co2_2 = new Coordinate(2719928.387,449141.116);
        CoincidentPoint co2 = new CoincidentPoint(co2_1,co2_2);
        coincidentPoints.add(co2);

        //第一组
        Coordinate co3_1 = new Coordinate(118.3261111,24.56944444);
        Coordinate co3_2 = new Coordinate(2718431.3,482311.3377);
        CoincidentPoint co3 = new CoincidentPoint(co3_1,co3_2);
        coincidentPoints.add(co3);

        //第一组
        Coordinate co4_1 = new Coordinate(118.2830556,24.77611111);
        Coordinate co4_2 = new Coordinate(2741329.457,477986.2323);
        CoincidentPoint co4 = new CoincidentPoint(co4_1,co4_2);
        coincidentPoints.add(co4);


        //第一组
        Coordinate co5_1 = new Coordinate(117.99,24.82444444);
        Coordinate co5_2 = new Coordinate(2746762.434,448368.786);
        CoincidentPoint co5 = new CoincidentPoint(co5_1,co5_2);
        coincidentPoints.add(co5);
        Coordinate coordinate1 = CoordTransformManager.transFromWGS84TOXian80ByCoincidentPoints(coordinate,coincidentPoints,117);
        Coordinate coordinate2 = CoordTransformManager.transByCoincidentPoints(coordinate1, parameterFour);*/


        Coordinate coordinate1 = CoordTransformManager.transFromWGS84ToLocal(coordinate, seven, parameterFour, LZ);
        LogUtil.d("转换后的坐标：" + coordinate1.getX() +  ":" + coordinate.getY());
        // Coordinate coordinate1 = new Coordinate(coordinate2.getY(),coordinate2.getX());

        return coordinate1;
    }
}

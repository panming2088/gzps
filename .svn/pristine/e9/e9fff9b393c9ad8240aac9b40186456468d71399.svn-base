package com.augurit.am.cmpt.loc;

import com.augurit.am.cmpt.coordt.mgr.CoordTransformManager;
import com.augurit.am.cmpt.coordt.model.CoincidentPoint;
import com.augurit.am.cmpt.coordt.model.Coordinate;
import com.augurit.am.cmpt.coordt.model.EllipsoidParameter;
import com.augurit.am.cmpt.coordt.model.ParameterFour;
import com.augurit.am.cmpt.coordt.model.ParameterSeven;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * 厦门92坐标转WGS84坐标
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.cmpt.loc
 * @createTime 创建时间 ：17/7/27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/27
 * @modifyMemo 修改备注：
 */

public class XiamenReverseLocationTransform implements IReverseLocationTransform {


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
     * 四参数
     */
    public static final double A = 0.9999883794007474;
    public static final double B = 7.3780807734458165E-6;
    public static final double DX = 88.24254587973701;
    public static final double DY = 30.0193583186483;

    /**
     * 七参数
     */
    public static final double PARAMSEVEN_DX = 477.8366584777832;
    public static final double PARAMSEVEN_DY = -950.1920680999756;
    public static final double PARAMSEVEN_DZ = 1334.6993570327759;
    public static final double PARAMSEVEN_M = 0.9997805281411133;
    public static final double PARAMSEVEN_WX = 1.1356576169418986E-4;
    public static final double PARAMSEVEN_WY = 8.384180873122205E-5;
    public static final double PARAMSEVEN_WZ =  -0.026178942433034535;

    /**
     * 中央经纬度
     */
    public static final double LZ = 117;

    @Override
    public Coordinate changeCurrentLocationToWGS84(double longitude, double latitude) {
        Coordinate origin = new Coordinate(longitude,latitude);

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
        //根据重合点计算本地坐标转WGS84坐标

        List<CoincidentPoint> coincidentPoints = new ArrayList<>();
        //第一组
        Coordinate co1_1 = new Coordinate(118.1463889,24.71611111);
        Coordinate co1_2 = new Coordinate(2734712.237,464147.6307);
        CoincidentPoint co1 = new CoincidentPoint(co1_2,co1_1);
        coincidentPoints.add(co1);

        //第一组
        Coordinate co2_1 = new Coordinate(117.9986111,24.58222222);
        Coordinate co2_2 = new Coordinate(2719928.387,449141.116);
        CoincidentPoint co2 = new CoincidentPoint(co2_2,co2_1);
        coincidentPoints.add(co2);

        //第一组
        Coordinate co3_1 = new Coordinate(118.3261111,24.56944444);
        Coordinate co3_2 = new Coordinate(2718431.3,482311.3377);
        CoincidentPoint co3 = new CoincidentPoint(co3_2,co3_1);
        coincidentPoints.add(co3);

        //第一组
        Coordinate co4_1 = new Coordinate(118.2830556,24.77611111);
        Coordinate co4_2 = new Coordinate(2741329.457,477986.2323);
        CoincidentPoint co4 = new CoincidentPoint(co4_2,co4_1);
        coincidentPoints.add(co4);


        //第一组
        Coordinate co5_1 = new Coordinate(117.99,24.82444444);
        Coordinate co5_2 = new Coordinate(2746762.434,448368.786);
        CoincidentPoint co5 = new CoincidentPoint(co5_2,co5_1);
        coincidentPoints.add(co5);



        List<CoincidentPoint> fourPoints = new ArrayList<>();
        Coordinate co6_1 = new Coordinate(2734659.226,464102.8555);
        Coordinate co6_2 = new Coordinate(2734712.237,464147.6307);
        CoincidentPoint co6 = new CoincidentPoint(co6_1,co6_2);
        fourPoints.add(co6);

        Coordinate co7_1 = new Coordinate(2719874.965,449096.1781);
        Coordinate co7_2 = new Coordinate(2719928.387,449141.116);
        CoincidentPoint co7 = new CoincidentPoint(co7_1,co7_2);
        fourPoints.add(co7);

        Coordinate co8_1 = new Coordinate(2718378.263,482266.8368);
        Coordinate co8_2 = new Coordinate(2718431.3,482311.3377);
        CoincidentPoint co8 = new CoincidentPoint(co8_1,co8_2);
        fourPoints.add(co8);

        Coordinate co9_1 = new Coordinate(2741276.597,477941.5398);
        Coordinate co9_2 = new Coordinate(2741329.457,477986.2323);
        CoincidentPoint co9 = new CoincidentPoint(co9_1,co9_2);
        fourPoints.add(co9);


        Coordinate co10_1 = new Coordinate(2746709.429,448323.7844);
        Coordinate co10_2 = new Coordinate(2746762.434,448368.786);
        CoincidentPoint co10 = new CoincidentPoint(co10_1,co10_2);
        fourPoints.add(co10);
        Coordinate coordinate1 = CoordTransformManager.transByCoincidentPoints(origin,fourPoints);
        Coordinate coordinate2 = CoordTransformManager.transFromXian80TOWGS84ByCoincidentPoints(coordinate1,coincidentPoints,117);*/


        Coordinate coordinate = CoordTransformManager.transFromLocalToWGS84(origin, seven, parameterFour, LZ);
        return coordinate;
    }
}

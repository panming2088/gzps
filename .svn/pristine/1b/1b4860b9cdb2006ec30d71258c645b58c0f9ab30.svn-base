package com.augurit.am.cmpt.coordt.intfc;


import com.augurit.am.cmpt.coordt.model.Coordinate;
import com.augurit.am.cmpt.coordt.model.EllipsoidParameter;
import com.augurit.am.cmpt.coordt.model.ParameterFour;
import com.augurit.am.cmpt.coordt.model.ParameterFourModel;
import com.augurit.am.cmpt.coordt.model.ParameterSeven;
import com.augurit.am.cmpt.coordt.model.ParameterSevenModel;

import java.util.List;

public interface ICoordConvertorBase {

	/**
	 * 投影坐标转大地坐标,(不带带号) 80平面转80经纬度;
	 *
	 * 高斯投影反算
	 * @param xyh:投影坐标
	 * @param ep:椭球参数定义
	 * @param Lz:本初子午线
	 * @return LBH 大地坐标
	 */
	Coordinate ProToGeo(Coordinate xyh, EllipsoidParameter ep, double Lz);

	/**
	 * 大地坐标转投影坐标,（不带带号）80经纬度转80平面.
	 * 高斯投影正算
	 * @param LBH:大地坐标
	 * @param ep:椭球参数定义
	 * @param Lz:本初子午线
	 * @return xyh 投影坐标
	 */
	Coordinate GeoToPro(Coordinate LBH, EllipsoidParameter ep, double Lz);

	/**
	 * 大地坐标转空间直角坐标,84经纬度转84空间.
	 * @param LBH:大地坐标
	 * @param ep:椭球参数定义
	 * @return XYZ 空间直角坐标
	 */
	Coordinate GeoToSpa(Coordinate LBH, EllipsoidParameter ep);

	/**
	 * 空间直角坐标转大地坐标,80空间转80经纬度.
	 * @param XYZ:空间直角坐标
	 * @param ep:椭球参数定义
	 * @return LBH 大地坐标
	 */
	Coordinate SpaToGeo(Coordinate XYZ, EllipsoidParameter ep);

	/**
	 * 不同椭球间空间直接坐标的转换,84空间转80空间.
	 * 布尔莎七参数模型
	 * @param XYZ_1:原椭球空间直角坐标
	 * @param p7:布尔莎七参数
	 * @return XYZ_1:目标空间直角坐标
	 */
	Coordinate Spa_1ToSpa_2(Coordinate XYZ_1, ParameterSeven p7);

	/**
	 * 平面坐标之间的转换（);
	 * 二维四参数模型转换
	 * @param xy_1:原坐标系坐标
	 * @param p4:二维四参数
	 * @return xy_2:目标坐标系坐标
	 */
	Coordinate Pla_1ToPla_2(Coordinate xy_1, ParameterFour p4);

	/**
	 * 反算四参数
	 * 二维四参数模型
	 * @param p4m:四参数模型 包含二维重合点数据列
	 * @return 四参数模型
	 */
	ParameterFourModel CalcuteParameterFour(ParameterFourModel p4m);

	/**
	 * 反算七参数
	 * 布尔莎七参数模型
	 * @param p7m:七参数模型 包含三维重合点
	 * @return 七参数模型
	 */
	ParameterSevenModel CalcuteParameterSeven(ParameterSevenModel p7m);

	/**
	 * 计算二维坐标点位中误差
	 * 
	 * @param coor_1:计算结果点坐标
	 * @param coor_2：已知点坐标
	 * @return 点位中误差
	 */
	double CalcuteErrorTwo(Coordinate coor_1, Coordinate coor_2);

	/**
	 * 计算三维坐标点位中误差
	 * 
	 * @param coor_1:计算结果点坐标
	 * @param coor_2：已知点坐标
	 * @return 点位中误差
	 */
	double CalcuteErrorThree(Coordinate coor_1, Coordinate coor_2);

	/**
	 * 根据已知重合点,将重合点所在范围划分成以s为边长的小区域,并计算该区域四参数
	 * 
	 * @param coincidentPoint：重合点
	 * @param s：小区域边长
	 * @return 四参数模型
	 */
	List getParameterFourModelList(List coincidentPoint, double s);

	/**
	 * 通过四参数模型，得到当前坐标的四参数
	 * 
	 * @param coor：当前坐标
	 * @param parameterFourModelList：四参数模型列表
	 * @param s：小区域边长
	 * @return 四参数模型
	 */
	ParameterFourModel getLastParameterFourModel(Coordinate coor, List parameterFourModelList, double s);
	
	/**
	 * 平面坐标计算带号
	 * 
	 * @param coor：当前坐标（xyh）
	 * @return 带号
	 */
	int CalcuteProjNo(Coordinate coor);
	
	/**
	 * 平面坐标去带号
	 * 
	 * @param coor：当前坐标（xyh）
	 * @param ProjNo：带号
	 * @return 去带号后的坐标
	 */
	Coordinate deleteProjNo(Coordinate coor, int ProjNo);
	
	/**
	 * 平面坐标加带号
	 * 
	 * @param coor：当前坐标（xyh）
	 * @param ProjNo：带号
	 * @return 加带号后的坐标
	 */
	Coordinate addProjNo(Coordinate coor, int ProjNo);
}

package com.augurit.am.cmpt.coordt.intfc;


import com.augurit.am.cmpt.coordt.model.Coordinate;
import com.augurit.am.cmpt.coordt.model.EllipsoidParameter;
import com.augurit.am.cmpt.coordt.model.ParameterFour;
import com.augurit.am.cmpt.coordt.model.ParameterSeven;

import java.util.List;

public interface ICoordConvertor {

	/**
	* 坐标系1转换为坐标系2（二维 已知四参数）
	* 
	* @param coor_1:坐标系1坐标（xy）
	* @param p4：已知四参数
	* @return 坐标系2坐标（xy）
	*/
	Coordinate CS_1ToCS_2(Coordinate coor_1, ParameterFour p4);

	/**
	 * 坐标系1转换为坐标系2（二维 已知重合点）
	 * 
	 * @param coor_1:坐标系1坐标（xy）
	 * @param coincidentPoint：坐标系1和坐标系2的重合点
	 * @return 坐标系2坐标（xy）
	 */
	Coordinate CS_1ToCS_2(Coordinate coor_1, List coincidentPoint);

	/**
	 * 坐标系1转换为坐标系2（二维 已知重合点 区域较大需要划分区域进行坐标转换）
	 * 
	 * @param coor_1:坐标系1坐标（xy）
	 * @param coincidentPoint：坐标系1和坐标系2的重合点
	 * @param s:小区域边界设定
	 * @return 坐标系2坐标（xy）
	 */
	Coordinate CS_1ToCS_2(Coordinate coor_1, List coincidentPoint, double s);

	/**
	* 坐标系1转换为坐标系2（三维 已知七参数）
	* 
	* @param coor_1:坐标系1坐标
	* @param p7：已知七参数
	* @param Lz: 本初子午线
	* @param ep_1:坐标系1椭球定义
	* @param ep_2:坐标系2椭球定义
	* @param t_1:坐标系1坐标类型(xyh,LBH,XYZ)
	* @param t_2:坐标系2坐标类型(xyh,LBH,XYZ)
	* @return 坐标系2坐标
	*/
	Coordinate CS_1ToCS_2(Coordinate coor_1, ParameterSeven p7,
						  double Lz, EllipsoidParameter ep_1,
						  EllipsoidParameter ep_2,
						  String t_1,
						  String t_2);

	/**
	* 坐标系1转换为坐标系2（三维 未知七参数 只有重合点）
	* 
	* @param coor_1:坐标系1坐标
	* @param coincidentPoint：坐标系1和坐标系2的重合点
	* @param Lz: 本初子午线
	* @param ep_1:坐标系1椭球定义
	* @param ep_2:坐标系2椭球定义
	* @param t_1:坐标系1坐标类型(xyh,LBH,XYZ)
	* @param t_2:坐标系2坐标类型(xyh,LBH,XYZ)
	* @return 坐标系2坐标
	*/
	Coordinate CS_1ToCS_2(Coordinate coor_1, List coincidentPoint, double Lz, EllipsoidParameter ep_1, EllipsoidParameter ep_2, String t_1, String t_2);

}

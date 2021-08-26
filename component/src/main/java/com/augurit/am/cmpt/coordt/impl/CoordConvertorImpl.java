package com.augurit.am.cmpt.coordt.impl;


import com.augurit.am.cmpt.coordt.model.CoincidentPoint;
import com.augurit.am.cmpt.coordt.model.Coordinate;
import com.augurit.am.cmpt.coordt.model.EllipsoidParameter;
import com.augurit.am.cmpt.coordt.model.ParameterFour;
import com.augurit.am.cmpt.coordt.model.ParameterFourModel;
import com.augurit.am.cmpt.coordt.model.ParameterSeven;
import com.augurit.am.cmpt.coordt.model.ParameterSevenModel;
import com.augurit.am.cmpt.coordt.intfc.ICoordConvertor;

import java.util.ArrayList;
import java.util.List;


public class CoordConvertorImpl implements ICoordConvertor {

	private com.augurit.am.cmpt.coordt.intfc.ICoordConvertorBase ICoordConvertorBase = new CoordConvertorBaseImpl();

    public Coordinate CS_1ToCS_2(Coordinate coor_1, ParameterFour p4) {
		return ICoordConvertorBase.Pla_1ToPla_2(coor_1, p4);
	}

	public Coordinate CS_1ToCS_2(Coordinate coor_1, List coincidentPoint) {
		if (coincidentPoint == null || coincidentPoint.size() < 2) {
			//重合点数据太少
			return null;
		}
		ParameterFourModel p4m = new ParameterFourModel();
		p4m.setCoincidentPoint(coincidentPoint);
		ParameterFour p4 = ICoordConvertorBase.CalcuteParameterFour(p4m).getParameterFour();
		return ICoordConvertorBase.Pla_1ToPla_2(coor_1, p4);
	}

	public Coordinate CS_1ToCS_2(Coordinate coor_1, List coincidentPoint, double s) {
		if (coincidentPoint == null || coincidentPoint.size() < 2) {
			//重合点数据太少
			return null;
		}
		List parameterFourModelList = ICoordConvertorBase.getParameterFourModelList(coincidentPoint, s);
		ParameterFourModel p4m = ICoordConvertorBase.getLastParameterFourModel(coor_1, parameterFourModelList, s);
		return ICoordConvertorBase.Pla_1ToPla_2(coor_1, p4m.getParameterFour());
	}

	/*
	* 这里直接把两个坐标系进行转换
	*/
	public Coordinate CS_1ToCS_2(Coordinate coor_1, ParameterSeven p7, double Lz, EllipsoidParameter ep_1, EllipsoidParameter ep_2, String t_1, String t_2) {
		int ProjNo = 0;
		Coordinate coor_1_xyh = new Coordinate();
		Coordinate coor_1_LBH = new Coordinate();
		Coordinate coor_1_XYZ = new Coordinate();
		Coordinate coor_2_xyh = new Coordinate();
		Coordinate coor_2_LBH = new Coordinate();
		Coordinate coor_2_XYZ = new Coordinate();
		//如果是平面坐标
		if (t_1 == "xyh" || t_1.equals("xyh")) {
			coor_1_xyh = coor_1;
			ProjNo = (int) (coor_1.getY() / 1000000L);
			double y_1 = coor_1.getY() - ProjNo * 1000000L;
			coor_1_xyh.setY(y_1);
			//转成经纬度坐标
			coor_1_LBH = ICoordConvertorBase.ProToGeo(coor_1_xyh, ep_1, Lz);
			//把经纬度坐标转成空间坐标
			coor_1_XYZ = ICoordConvertorBase.GeoToSpa(coor_1_LBH, ep_1);
			//转成最终的空间坐标
			coor_2_XYZ = ICoordConvertorBase.Spa_1ToSpa_2(coor_1_XYZ, p7);
		} else if (t_1 == "LBH" || t_1.equals("LBH")) {
			coor_1_LBH = coor_1;
			coor_1_XYZ = ICoordConvertorBase.GeoToSpa(coor_1_LBH, ep_1);
			coor_2_XYZ = ICoordConvertorBase.Spa_1ToSpa_2(coor_1_XYZ, p7);
		} else if (t_1 == "XYZ" || t_1.equals("XYZ")) {
			coor_1_XYZ = coor_1;
			coor_2_XYZ = ICoordConvertorBase.Spa_1ToSpa_2(coor_1_XYZ, p7);
		}
		if (t_2 == "XYZ" || t_2.equals("XYZ")) {
			//如果最终需要的是空间坐标，直接返回刚刚转换的
			return coor_2_XYZ;
		} else if (t_2 == "LBH" || t_2.equals("LBH")) {
			//否则，空间转经纬度
			return ICoordConvertorBase.SpaToGeo(coor_2_XYZ, ep_2);
		} else if (t_2 == "xyh" || t_2.equals("xyh")) {
			//空间转经纬度
			coor_2_LBH = ICoordConvertorBase.SpaToGeo(coor_2_XYZ, ep_2);
			//经纬度转平面
			coor_2_xyh = ICoordConvertorBase.GeoToPro(coor_2_LBH, ep_2, Lz);
			double y_2 = coor_2_xyh.getY() + ProjNo * 1000000L;
			coor_2_xyh.setY(y_2);
			return coor_2_xyh;
		}
		return null;
	}

	public Coordinate CS_1ToCS_2(Coordinate coor_1, List coincidentPoint, double Lz, EllipsoidParameter ep_1, EllipsoidParameter ep_2, String t_1, String t_2) {
		if (coincidentPoint == null || coincidentPoint.size() < 3) {
			//重合点数据太少
			return null;
		}
		//计算七参数
		List coincidentPoint_XYZ = new ArrayList<CoincidentPoint>();
		for (int i = 0; i < coincidentPoint.size(); i++) {
			Coordinate coor_1_xyh = new Coordinate();
			Coordinate coor_1_LBH = new Coordinate();
			Coordinate coor_1_XYZ = new Coordinate();
			Coordinate coor_2_xyh = new Coordinate();
			Coordinate coor_2_LBH = new Coordinate();
			Coordinate coor_2_XYZ = new Coordinate();
			CoincidentPoint ci = (CoincidentPoint) coincidentPoint.get(i);
			if (t_1 == "xyh" || t_1.equals("xyh")) {
				coor_1_xyh = ci.getCoor_1();
				int ProjNo = (int) (ci.getCoor_1().getY() / 1000000L);
				double y_1 = ci.getCoor_1().getY() - ProjNo * 1000000L;
				coor_1_xyh.setY(y_1);
				coor_1_LBH = ICoordConvertorBase.ProToGeo(coor_1_xyh, ep_1, Lz);
				coor_1_XYZ = ICoordConvertorBase.GeoToSpa(coor_1_LBH, ep_1);
			} else if (t_1 == "LBH" || t_1.equals("LBH")) {
				coor_1_LBH = ci.getCoor_1();
				coor_1_XYZ = ICoordConvertorBase.GeoToSpa(coor_1_LBH, ep_1);
			} else if (t_1 == "XYZ" || t_1.equals("XYZ")) {
				coor_1_XYZ = ci.getCoor_1();
			}
			if (t_2 == "xyh" || t_2.equals("xyh")) {
				coor_2_xyh = ci.getCoor_2();
				int ProjNo = (int) (ci.getCoor_2().getY() / 1000000L);
				double y_2 = ci.getCoor_2().getY() - ProjNo * 1000000L;
				coor_2_xyh.setY(y_2);
				coor_2_LBH = ICoordConvertorBase.ProToGeo(coor_2_xyh, ep_2, Lz);
				coor_2_XYZ = ICoordConvertorBase.GeoToSpa(coor_2_LBH, ep_2);
			} else if (t_2 == "LBH" || t_2.equals("LBH")) {
				coor_2_LBH = ci.getCoor_2();
				coor_2_XYZ = ICoordConvertorBase.GeoToSpa(coor_2_LBH, ep_2);
			} else if (t_2 == "XYZ" || t_2.equals("XYZ")) {
				coor_2_XYZ = ci.getCoor_2();
			}
			CoincidentPoint c_XYZ_i = new CoincidentPoint(coor_1_XYZ, coor_2_XYZ);
			coincidentPoint_XYZ.add(c_XYZ_i);
		}
		ParameterSevenModel p7m = new ParameterSevenModel();
		p7m.setCoincidentPoint(coincidentPoint_XYZ);
		p7m = ICoordConvertorBase.CalcuteParameterSeven(p7m);
		return this.CS_1ToCS_2(coor_1, p7m.getParameterSeven(), Lz, ep_1, ep_2, t_1, t_2);
	}
}

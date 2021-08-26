package com.augurit.am.cmpt.coordt.impl;


import com.augurit.am.cmpt.coordt.model.CoincidentPoint;
import com.augurit.am.cmpt.coordt.model.Coordinate;
import com.augurit.am.cmpt.coordt.model.EllipsoidParameter;
import com.augurit.am.cmpt.coordt.model.ParameterFour;
import com.augurit.am.cmpt.coordt.model.ParameterFourModel;
import com.augurit.am.cmpt.coordt.model.ParameterSeven;
import com.augurit.am.cmpt.coordt.model.ParameterSevenModel;
import com.augurit.am.cmpt.coordt.model.SpaceRange;
import com.augurit.am.cmpt.coordt.intfc.ICoordConvertorBase;
import com.augurit.am.cmpt.coordt.util.CoordConvertorAction;

import java.util.ArrayList;
import java.util.List;


public class CoordConvertorBaseImpl implements ICoordConvertorBase {

	private static double PI = Math.PI;
	private static double iPI = Math.PI / 180.0;

	private static ParameterSeven getP7(double[][] X) {
		ParameterSeven p7 = new ParameterSeven();
		if (X.length != 7) {
			return null;
		} else {
			p7.setDX(X[0][0]);
			p7.setDY(X[1][0]);
			p7.setDZ(X[2][0]);
			p7.setM(X[3][0]);
			p7.setWX(X[4][0]);
			p7.setWY(X[5][0]);
			p7.setWZ(X[6][0]);
		}
		return p7;
	}

	private static ParameterFour getP4(double[][] X) {
		ParameterFour p4 = new ParameterFour();
		if (X.length != 4) {
			return null;
		} else {
			p4.setDX(X[0][0]);
			p4.setDY(X[1][0]);
			p4.setA(X[2][0]);
			p4.setB(X[3][0]);
		}
		return p4;
	}

	private static double MinLast(double min, double s) {
		double value = Math.floor(min / s) * s;
		return value;
	}

	private static SpaceRange setSpaceRange(double minX, double minY, double maxX, double maxY) {
		SpaceRange range = new SpaceRange();
		range.setMinX(minX);
		range.setMinY(minY);
		range.setMaxX(maxX);
		range.setMaxY(maxY);
		return range;
	}

	private static boolean isContain(SpaceRange range, Coordinate coor) {
        return coor.getX() >= range.getMinX() && coor.getX() <= range.getMaxX() && coor.getY() >= range.getMinY() && coor.getY() <= range.getMaxY();
    }

	public Coordinate GeoToPro(Coordinate LBH, EllipsoidParameter ep, double Lz) {

		double L = LBH.getX();
		double B = LBH.getY();
		double H = LBH.getZ();
		double a = ep.getA();
		double f = ep.getF();
		double L0 = Lz * iPI;
		double L1 = L * iPI;
		double B1 = B * iPI;
		double e2 = 2 * f - f * f;
		double ee = e2 / (1.0 - e2);
		double NN = a / Math.sqrt(1.0 - e2 * Math.sin(B1) * Math.sin(B1));
		double T = Math.tan(B1) * Math.tan(B1);
		double C = ee * Math.cos(B1) * Math.cos(B1);
		double A = (L1 - L0) * Math.cos(B1);
		double M = a * ((1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256) * B1 - (3 * e2 / 8 + 3 * e2 * e2 / 32 + 45 * e2 * e2 * e2 / 1024) * Math.sin(2 * B1) + (15 * e2 * e2 / 256 + 45 * e2 * e2 * e2 / 1024) * Math.sin(4 * B1) - (35 * e2 * e2 * e2 / 3072) * Math.sin(6 * B1));
		double xval = M + NN * Math.tan(B1) * (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24 + (61 - 58 * T + T * T + 600 * C - 330 * ee) * A * A * A * A * A * A / 720);
		double yval = NN * (A + (1 - T + C) * A * A * A / 6 + (5 - 18 * T + T * T + 72 * C - 58 * ee) * A * A * A * A * A / 120);
		double y0 = 500000L;
		double x0 = 0;
		xval = xval + x0;
		yval = yval + y0;
		Coordinate xyh = new Coordinate(xval, yval, H);
		return xyh;
	}

	public Coordinate GeoToSpa(Coordinate LBH, EllipsoidParameter ep) {
		double L = LBH.getX() * iPI;
		double B = LBH.getY() * iPI;
		double H = LBH.getZ();
		double a = ep.getA();
		double f = ep.getF();
		double e = Math.sqrt(2 * f - f * f);
		double W = Math.sqrt(1 - e * e * Math.sin(B) * Math.sin(B));
		double N = a / W;
		double X = (N + H) * Math.cos(B) * Math.cos(L);
		double Y = (N + H) * Math.cos(B) * Math.sin(L);
		double Z = (N * (1 - e * e) + H) * Math.sin(B);
		Coordinate XYZ = new Coordinate(X, Y, Z);
		return XYZ;
	}

	public Coordinate Pla_1ToPla_2(Coordinate xy_1, ParameterFour p4) {
		double dX = p4.getDX();
		double dY = p4.getDY();
		double a = p4.getA();
		double b = p4.getB();
		double x_1 = xy_1.getX();
		double y_1 = xy_1.getY();
		double x_2 = dX + a * x_1 - b * y_1;
		double y_2 = dY + b * x_1 + a * y_1;
		Coordinate xy_2 = new Coordinate(x_2, y_2);
		return xy_2;
	}

	public Coordinate ProToGeo(Coordinate xyh, EllipsoidParameter ep, double Lz) {
		double x = xyh.getX();
		double y = xyh.getY();
		double h = xyh.getZ();
		double a = ep.getA();
		double f = ep.getF();
		double L0 = Lz * iPI;
		double x0 = 0;
		double y0 = 500000L;
		double xval = x - x0;
		double yval = y - y0;
		double e2 = 2 * f - f * f;
		double e1 = (1.0 - Math.sqrt(1 - e2)) / (1.0 + Math.sqrt(1 - e2));
		double ee = e2 / (1 - e2);
		double M = xval;
		double u = M / (a * (1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256));
		double fai = u + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * Math.sin(2 * u) + (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32) * Math.sin(4 * u) + (151 * e1 * e1 * e1 / 96) * Math.sin(6 * u) + (1097 * e1 * e1 * e1 * e1 / 512) * Math.sin(8 * u);
		double C = ee * Math.cos(fai) * Math.cos(fai);
		double T = Math.tan(fai) * Math.tan(fai);
		double NN = a / Math.sqrt(1.0 - e2 * Math.sin(fai) * Math.sin(fai));
		double R = a * (1 - e2) / Math.sqrt((1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin(fai) * Math.sin(fai)));
		double D = yval / NN;
		//计算经度(L) 纬度(B)
		double L1 = L0 + (D - (1 + 2 * T + C) * D * D * D / 6 + (5 - 2 * C + 28 * T - 3 * C * C + 8 * ee + 24 * T * T) * D * D * D * D * D / 120) / Math.cos(fai);
		double B1 = fai - (NN * Math.tan(fai) / R) * (D * D / 2 - (5 + 3 * T + 10 * C - 4 * C * C - 9 * ee) * D * D * D * D / 24 + (61 + 90 * T + 298 * C + 45 * T * T - 256 * ee - 3 * C * C) * D * D * D * D * D * D / 720);
		Coordinate LBH = new Coordinate(L1 / iPI, B1 / iPI, h);
		return LBH;
	}

	public Coordinate SpaToGeo(Coordinate XYZ, EllipsoidParameter ep) {
		double X = XYZ.getX();
		double Y = XYZ.getY();
		double Z = XYZ.getZ();
		double a = ep.getA();
		double f = ep.getF();
		double e2 = 2 * f - f * f;
		double g = 0;
		double m = 0;
		double m1 = 0;
		double g1 = 0;
		double L = Math.atan(Y / X) / iPI;
		if (L < 0)
			L = 180 + L;
		do {
			m = (Z + a * e2 * g / Math.sqrt(1 + g * g - e2 * g * g)) / Math.sqrt(X * X + Y * Y);
			m1 = Math.atan(m);
			g1 = Math.atan(g);
			m1 = m1 / PI * 180 * 3600;
			g1 = g1 / PI * 180 * 3600;
			g = m;
		} while (Math.sqrt((m1 - g1) * (m1 - g1)) > 0.0000001);
		double B = Math.atan(m) / iPI;
		double w = Math.sqrt(1 - e2 * Math.sin(B / 180 * Math.PI) * Math.sin(B / 180 * Math.PI));
		double n = a / w;
		double H = Z / Math.sin(B / 180 * Math.PI) - n * (1 - e2);
		Coordinate LBH = new Coordinate(L, B, H);
		return LBH;
	}

	public Coordinate Spa_1ToSpa_2(Coordinate XYZ_1, ParameterSeven p7) {
		double X_1 = XYZ_1.getX();
		double Y_1 = XYZ_1.getY();
		double Z_1 = XYZ_1.getZ();
		double dX = p7.getDX();
		double dY = p7.getDY();
		double dZ = p7.getDZ();
		double m = p7.getM();
		double wX = p7.getWX();
		double wY = p7.getWY();
		double wZ = p7.getWZ();
		double X_2 = m * X_1 + wZ * Y_1 - wY * Z_1 + dX;
		double Y_2 = m * Y_1 - wZ * X_1 + wX * Z_1 + dY;
		double Z_2 = m * Z_1 + wY * X_1 - wX * Y_1 + dZ;
		Coordinate XYZ_2 = new Coordinate(X_2, Y_2, Z_2);
		return XYZ_2;
	}

	public ParameterFourModel CalcuteParameterFour(ParameterFourModel p4m) {
		List coincidentPoint = p4m.getCoincidentPoint();
		if (coincidentPoint == null || coincidentPoint.size() < 2) {
			return null;
		} else {
			int n = coincidentPoint.size();
			double B[][] = CoordConvertorAction.newB2(1, n, coincidentPoint);
			double L[][] = CoordConvertorAction.newL2(1, n, coincidentPoint);
			double BT[][] = CoordConvertorAction.trans(B);
			double BTB[][] = CoordConvertorAction.multi(BT, B);
			double BTB_1[][] = CoordConvertorAction.inv(BTB);
			double BTL[][] = CoordConvertorAction.multi(BT, L);
			double X[][] = CoordConvertorAction.multi(BTB_1, BTL);
			ParameterFour p4 = getP4(X);
			double BX[][] = CoordConvertorAction.multi(B, X);
			double V[][] = CoordConvertorAction.add(CoordConvertorAction.kmulti(-1, BX), L);
			double VT[][] = CoordConvertorAction.trans(V);
			double D[][] = CoordConvertorAction.multi(VT, V);
			double D2 = D[0][0] / (2 * n - 1);
			p4m.setParameterFour(p4);
			p4m.setD2(D2);
			for (int i = 1; i < n + 1; i++) {
				double Bi[][] = CoordConvertorAction.newB2(i, i, coincidentPoint);
				double Li[][] = CoordConvertorAction.newL2(i, i, coincidentPoint);
				double BiX[][] = CoordConvertorAction.multi(Bi, X);
				double Vi[][] = CoordConvertorAction.add(CoordConvertorAction.kmulti(-1, BiX), Li);
				double ViT[][] = CoordConvertorAction.trans(Vi);
				double Di[][] = CoordConvertorAction.multi(ViT, Vi);
				double Di2 = Di[0][0] / (2 * n - 1);
				CoincidentPoint CoincidentPoint = (CoincidentPoint) coincidentPoint.get(i - 1);
				CoincidentPoint.setD2(Di2);
				coincidentPoint.set(i - 1, CoincidentPoint);
			}
			p4m.setCoincidentPoint(coincidentPoint);
		}
		return p4m;
	}

	public ParameterSevenModel CalcuteParameterSeven(ParameterSevenModel p7m) {
		List coincidentPoint = p7m.getCoincidentPoint();
		if (coincidentPoint == null || coincidentPoint.size() < 3) {
			return null;
		} else {
			int n = coincidentPoint.size();
			double B[][] = CoordConvertorAction.newB3(1, n, coincidentPoint);
			double L[][] = CoordConvertorAction.newL3(1, n, coincidentPoint);
			double BT[][] = CoordConvertorAction.trans(B);
			double BTB[][] = CoordConvertorAction.multi(BT, B);
			double BTB_1[][] = CoordConvertorAction.inv(BTB);
			double BTL[][] = CoordConvertorAction.multi(BT, L);
			double X[][] = CoordConvertorAction.multi(BTB_1, BTL);
			ParameterSeven p7 = getP7(X);
			double BX[][] = CoordConvertorAction.multi(B, X);
			double V[][] = CoordConvertorAction.add(CoordConvertorAction.kmulti(-1, BX), L);
			double VT[][] = CoordConvertorAction.trans(V);
			double D[][] = CoordConvertorAction.multi(VT, V);
			double D2 = D[0][0] / (3 * n - 7);
			p7m.setParameterSeven(p7);
			p7m.setD2(D2);
			for (int i = 1; i < n + 1; i++) {
				double Bi[][] = CoordConvertorAction.newB3(i, i, coincidentPoint);
				double Li[][] = CoordConvertorAction.newL3(i, i, coincidentPoint);
				double BiX[][] = CoordConvertorAction.multi(Bi, X);
				double Vi[][] = CoordConvertorAction.add(CoordConvertorAction.kmulti(-1, BiX), Li);
				double ViT[][] = CoordConvertorAction.trans(Vi);
				double Di[][] = CoordConvertorAction.multi(ViT, Vi);
				double Di2 = Di[0][0] / (3 * n - 7);
				CoincidentPoint CoincidentPoint = (CoincidentPoint) coincidentPoint.get(i - 1);
				CoincidentPoint.setD2(Di2);
				coincidentPoint.set(i - 1, CoincidentPoint);
			}
			p7m.setCoincidentPoint(coincidentPoint);
		}
		return p7m;
	}

	public double CalcuteErrorThree(Coordinate coor_1, Coordinate coor_2) {
		double X_1 = coor_1.getX();
		double Y_1 = coor_1.getY();
		double Z_1 = coor_1.getZ();
		double X_2 = coor_2.getX();
		double Y_2 = coor_2.getY();
		double Z_2 = coor_2.getZ();
		double M = Math.sqrt((X_1 - X_2) * (X_1 - X_2) + (Y_1 - Y_2) * (Y_1 - Y_2) + (Z_1 - Z_2) * (Z_1 - Z_2));
		return M;
	}

	public double CalcuteErrorTwo(Coordinate coor_1, Coordinate coor_2) {
		double X_1 = coor_1.getX();
		double Y_1 = coor_1.getY();
		double X_2 = coor_2.getX();
		double Y_2 = coor_2.getY();
		double M = Math.sqrt((X_1 - X_2) * (X_1 - X_2) + (Y_1 - Y_2) * (Y_1 - Y_2));
		return M;
	}

	public List getParameterFourModelList(List coincidentPoint, double s) {
		if (coincidentPoint == null || coincidentPoint.size() < 2) {
			return null;
		}
		//获取重合点的最大范围
		CoincidentPoint cpR = (CoincidentPoint) coincidentPoint.get(0);
		Coordinate coor_R = cpR.getCoor_1();
		double minX = coor_R.getX();
		double minY = coor_R.getY();
		double maxX = coor_R.getX();
		double maxY = coor_R.getY();//初始范围
		for (int i = 0; i < coincidentPoint.size(); i++) {
			cpR = (CoincidentPoint) coincidentPoint.get(i);
			coor_R = cpR.getCoor_1();
			if (coor_R.getX() >= maxX) {
				maxX = coor_R.getX();
			}
			if (coor_R.getY() >= maxY) {
				maxY = coor_R.getY();
			}
			if (coor_R.getX() <= minX) {
				minX = coor_R.getX();
			}
			if (coor_R.getY() <= minY) {
				minY = coor_R.getY();
			}
		}
		List parameterFourModelList = new ArrayList<ParameterFourModel>();
		//设置一个最大区域
		double startX = MinLast(minX, s);
		double endX = MinLast(maxX, s) + s;
		double startY = MinLast(minY, s);
		double endY = MinLast(maxY, s) + s;
		ParameterFourModel p0 = new ParameterFourModel();
		SpaceRange range0 = setSpaceRange(startX, startY, endX, endY);
		p0.setCoincidentPoint(coincidentPoint);
		p0.setRange(range0);
		parameterFourModelList.add(p0);
		//划分范围
		double xSide = maxX - minX;
		double ySide = maxY - minY;
		int xNum = (int) Math.floor(xSide / s) + 1;
		int yNum = (int) Math.floor(ySide / s) + 1;
		for (int i = 0; i < xNum; i++) {
			for (int j = 0; j < yNum; j++) {
				double X1 = startX + i * s;
				double Y1 = startY + j * s;
				double X2 = startX + (i + 1) * s;
				double Y2 = startY + (j + 1) * s;
				SpaceRange rangei = setSpaceRange(X1, Y1, X2, Y2);
				ParameterFourModel pj = new ParameterFourModel();
				pj.setRange(rangei);
				parameterFourModelList.add(pj);
			}
		}
		//重新分组重合点
		for (int n = 0; n < coincidentPoint.size(); n++) {
			CoincidentPoint cpn = (CoincidentPoint) coincidentPoint.get(n);
			Coordinate coor_n = cpn.getCoor_1();
			for (int i = 1; i < parameterFourModelList.size(); i++) {
				ParameterFourModel pn = (ParameterFourModel) parameterFourModelList.get(i);
				if (isContain(pn.getRange(), coor_n)) {
					List coincidentPointi = new ArrayList();
					if (pn.getCoincidentPoint() != null) {
						coincidentPointi = pn.getCoincidentPoint();
					}
					coincidentPointi.add(cpn);
					pn.setCoincidentPoint(coincidentPointi);
					break;
				}
			}
		}
		//计算每个区域的四参数
		for (int i = 0; i < parameterFourModelList.size(); i++) {
			ParameterFourModel pi = (ParameterFourModel) parameterFourModelList.get(i);
			if (pi.getCoincidentPoint() != null && pi.getCoincidentPoint().size() > 1) {
				pi = this.CalcuteParameterFour(pi);
			}
			parameterFourModelList.set(i, pi);
		}
		return parameterFourModelList;
	}

	public ParameterFourModel getLastParameterFourModel(Coordinate coor, List parameterFourModelList, double s) {
		if (parameterFourModelList == null || parameterFourModelList.size() < 1) {
			return null;
		}
		ParameterFourModel p0 = (ParameterFourModel) parameterFourModelList.get(0);
		SpaceRange r0 = p0.getRange();
		int xN = (int) Math.max((coor.getX() - r0.getMinX()) / s, (r0.getMaxX() - coor.getX()) / s);
		int yN = (int) Math.max((coor.getY() - r0.getMinY()) / s, (r0.getMaxY() - coor.getY()) / s);
		for (int i = 0; i < Math.max(xN, yN); i++) {
			for (int j = 0; j <= 2 * i; j++) {
				if (j % 2 == 0) {
					Coordinate newCoor = new Coordinate(coor.getX() + i * s, coor.getY() + (j / 2) * s);
					for (int n = 1; n < parameterFourModelList.size(); n++) {
						ParameterFourModel pi = (ParameterFourModel) parameterFourModelList.get(n);
						if (isContain(pi.getRange(), newCoor) && pi.getParameterFour() != null) {
							return pi;
						}
					}
					newCoor.setX(coor.getX() - i * s);
					newCoor.setY(coor.getY() + (j / 2) * s);
					for (int n = 1; n < parameterFourModelList.size(); n++) {
						ParameterFourModel pi = (ParameterFourModel) parameterFourModelList.get(n);
						if (isContain(pi.getRange(), newCoor) && pi.getParameterFour() != null) {
							return pi;
						}
					}
				} else {
					Coordinate newCoor = new Coordinate(coor.getX() - i * s, coor.getY() - Math.floor(j / 2) * s);
					for (int n = 1; n < parameterFourModelList.size(); n++) {
						ParameterFourModel pi = (ParameterFourModel) parameterFourModelList.get(n);
						if (isContain(pi.getRange(), newCoor) && pi.getParameterFour() != null) {
							return pi;
						}
					}
					newCoor.setX(coor.getX() + i * s);
					newCoor.setY(coor.getY() - Math.floor(j / 2) * s);
					for (int n = 1; n < parameterFourModelList.size(); n++) {
						ParameterFourModel pi = (ParameterFourModel) parameterFourModelList.get(n);
						if (isContain(pi.getRange(), newCoor) && pi.getParameterFour() != null) {
							return pi;
						}
					}
				}
			}
		}
		return p0;
	}

	public int CalcuteProjNo(Coordinate coor) {
		return (int) (coor.getY() / 1000000L);
	}
	
	public Coordinate deleteProjNo(Coordinate coor, int ProjNo) {
		Coordinate coor_1 = coor;
		double y_1 = coor.getY() - ProjNo * 1000000L;
		coor_1.setY(y_1);
		return coor_1;
	}

	public Coordinate addProjNo(Coordinate coor, int ProjNo) {
		Coordinate coor_1 = coor;
		double y_1 = coor.getY() + ProjNo * 1000000L;
		coor_1.setY(y_1);
		return coor_1;
	}
}

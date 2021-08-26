package com.augurit.am.cmpt.coordt.model;

/**
 * 重合点
 */
public class CoincidentPoint {

	private Coordinate coor_1;
	private Coordinate coor_2;
	/**
	 * 点位中误差
	 */
	private double D2;

	public CoincidentPoint(Coordinate coor_1, Coordinate coor_2) {
		this.coor_1 = coor_1;
		this.coor_2 = coor_2;
	}


	public Coordinate getCoor_1() {
		return coor_1;
	}

	public void setCoor_1(Coordinate coor_1) {
		this.coor_1 = coor_1;
	}

	public Coordinate getCoor_2() {
		return coor_2;
	}


	public void setCoor_2(Coordinate coor_2) {
		this.coor_2 = coor_2;
	}

	/**
	 * 返回点位中误差
	 * @return 点位中误差
     */
	public double getD2() {
		return D2;
	}

	/**
	 * 点位中误差
	 * @param d2 点位中误差
     */
	public void setD2(double d2) {
		D2 = d2;
	}

}

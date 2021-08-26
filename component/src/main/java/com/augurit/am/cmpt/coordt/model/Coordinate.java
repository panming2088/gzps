package com.augurit.am.cmpt.coordt.model;

/**
 * 坐标，包括平面和空间
 */
public class Coordinate {

	/**
	 * 经度
	 */
	private double x;
	/**
	 * 纬度
	 */
	private double y;
	/**
	 * 大地高
	 */
	private double z;

	/**
	 * 坐标点
	 * @param x 经度
	 * @param y 纬度
     */
	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 坐标点
	 * @param x 经度
	 * @param y 纬度
     * @param z 大地高
     */
	public Coordinate(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Coordinate() {

	}

	/**
	 * 返回经度
	 * @return 经度
     */
	public double getX() {
		return x;
	}

	/**
	 * 设置经度
	 * @param x 经度
     */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * 返回纬度
	 * @return 纬度
     */
	public double getY() {
		return y;
	}

	/**
	 * 设置纬度
	 * @param y 纬度
     */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * 返回大地高度
	 * @return 大地高度
     */
	public double getZ() {
		return z;
	}

	/**
	 * 设置大地高度
	 * @param z 大地高度
     */
	public void setZ(double z) {
		this.z = z;
	}

}

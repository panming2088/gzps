package com.augurit.am.cmpt.coordt.model;

/**
 * 四参数
 */
public class ParameterFour {

	/**
	 * dX,dY表示的是偏移量
	 */
	private double dX;
	private double dY;
	/**
	 * a表示的是（1+m）cosw的计算结果，其中m表示的是尺度因子，w表示的是旋转角度
	 */
	private double a;
	/**
	 * b表示的是（1+m）sinw的计算结果，其中m表示的是尺度因子，w表示的是旋转角度
	 */
	private double b;

	/**
	 * 返回x轴偏移量
	 * @return x轴偏移量
	 */
	public double getDX() {
		return dX;
	}

	/**
	 * 设置偏移量
	 * @param dx 偏移量
     */
	public void setDX(double dx) {
		dX = dx;
	}

	/**
	 * 返回y轴偏移量
	 * @return y轴偏移量
     */
	public double getDY() {
		return dY;
	}

	/**
	 * 设置偏移量
	 * @param dy 偏移量
     */
	public void setDY(double dy) {
		dY = dy;
	}

	/**
	 * 返回（1+m）cosw的计算结果
	 * @return （1+m）cosw的计算结果，也就是a
     */
	public double getA() {
		return a;
	}

	/**
	 * a表示的是（1+m）cosw的计算结果，其中m表示的是尺度因子，w表示的是旋转角度
	 * @param a （1+m）cosw的计算结果
     */
	public void setA(double a) {
		this.a = a;
	}

	/**
	 * 返回(1+m）sinw的计算结果
	 * @return (1+m)sinw的计算结果,也就是b
     */
	public double getB() {
		return b;
	}

	/**
	 * b表示的是（1+m）sinw的计算结果，其中m表示的是尺度因子，w表示的是旋转角度
	 * @param b （1+m）sinw的计算结果
     */
	public void setB(double b) {
		this.b = b;
	}

}

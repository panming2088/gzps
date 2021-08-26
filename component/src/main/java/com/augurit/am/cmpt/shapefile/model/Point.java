package com.augurit.am.cmpt.shapefile.model;

public class Point {
	
	private double x;
	private double y;
	
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	public String toString(){
		String str = "";
		str = str + "x=" + x + ", y=" + y + "  ";
		return str;
	}

}

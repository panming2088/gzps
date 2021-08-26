package com.augurit.am.cmpt.shapefile.model;

public class PointShp {
	
	private int recordNumber;
	private int contentLength;
	private int shapeType;
	private double x;
	private double y;
	private double z;
	private double m;
	
	
	public int getRecordNumber() {
		return recordNumber;
	}
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}
	public int getContentLength() {
		return contentLength;
	}
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
	public int getShapeType() {
		return shapeType;
	}
	public void setShapeType(int shapeType) {
		this.shapeType = shapeType;
	}
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
	
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public double getM() {
		return m;
	}
	public void setM(double m) {
		this.m = m;
	}
	@Override
	public String toString(){
		String str = "";
		str = str + "recordNumber=" + recordNumber + ",  ";
		str = str + "contentLength=" + contentLength + ",  ";
		str = str + "shapeType=" + shapeType + ",  ";
		str = str + "x=" + x + ",  ";
		str = str + "y=" + y + "\n";
		
		return str;
	}

}

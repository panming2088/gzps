package com.augurit.am.cmpt.shapefile.model;

import java.util.ArrayList;

public class ShapeFile {
	
	private String filename;
	
	public static final int NULLSHAPE = 0;  // （表示这个 Shapefile 文件不含坐标）
	public static final int POINT = 1;  // （表示 Shapefile 文件记录的是点状目标，但不是多点）
	public static final int POLYLINE = 3;  // （表示 Shapefile 文件记录的是线状目标）
	public static final int POLYGON = 5;  // （表示 Shapefile 文件记录的是面状目标）
	public static final int MultiPoint = 8;  // （表示 Shapefile 文件记录的是多点，即点集合）
	public static final int PointZ = 11;  // （表示 Shapefile 文件记录的是三维点状目标）
	public static final int PolyLineZ = 13;  // （表示 Shapefile 文件记录的是三维线状目标）
	public static final int PolygonZ = 15;  // （表示 Shapefile 文件记录的是三维面状目标）
	public static final int MultiPointZ = 18;  // （表示 Shapefile 文件记录的是三维点集合目标）
	public static final int PointM = 21;  // （表示含有 Measure 值的点状目标）
	public static final int PolyLineM = 23;  // （表示含有 Measure 值的线状目标）
	public static final int PolygonM = 25;  // （表示含有 Measure 值的面状目标）
	public static final int MultiPointM = 28;  // （表示含有 Measure 值的多点目标）
	public static final int MultiPatch = 31;  // （表示复合目标）
	
	//以下为文件头信息
		private int fileCode;   //固定为9994
		private int fileLength;  //文件的实际长度
		private int version;    //1000
		private int shapeType; //表示这个 Shapefile 文件所记录的空间数据的几何类型
		private double xmin;      //空间数据所占空间范围的 X 方向最小值
		private double ymin;      //空间数据所占空间范围的 Y 方向最小值
		private double xmax;      //空间数据所占空间范围的 X 方向最大值
		private double ymax;      //空间数据所占空间范围的 Y 方向最大值
		private double zmin;      //空间数据所占空间范围的 Z 方向最小值
		private double zmax;      //空间数据所占空间范围的 Z 方向最大值
		private double mmin;      //最小 Measure 值
		private double mmax;      //最大 Measure 值
		//以上为文件头信息
		
		private ArrayList<PointShp> pointShps = new ArrayList<PointShp>();
		private ArrayList<PolyLineAndPolygonShp> polyLineAndPolygonShp = new ArrayList<PolyLineAndPolygonShp>();
		

	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getFileCode() {
		return fileCode;
	}
	public void setFileCode(int fileCode) {
		this.fileCode = fileCode;
	}
	public int getFileLength() {
		return fileLength;
	}
	public void setFileLength(int fileLength) {
		this.fileLength = fileLength;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getShapeType() {
		return shapeType;
	}
	public void setShapeType(int shapeType) {
		this.shapeType = shapeType;
	}
	public double getXmin() {
		return xmin;
	}
	public void setXmin(double xmin) {
		this.xmin = xmin;
	}
	public double getYmin() {
		return ymin;
	}
	public void setYmin(double ymin) {
		this.ymin = ymin;
	}
	public double getXmax() {
		return xmax;
	}
	public void setXmax(double xmax) {
		this.xmax = xmax;
	}
	public double getYmax() {
		return ymax;
	}
	public void setYmax(double ymax) {
		this.ymax = ymax;
	}
	public double getZmin() {
		return zmin;
	}
	public void setZmin(double zmin) {
		this.zmin = zmin;
	}
	public double getZmax() {
		return zmax;
	}
	public void setZmax(double zmax) {
		this.zmax = zmax;
	}
	public double getMmin() {
		return mmin;
	}
	public void setMmin(double mmin) {
		this.mmin = mmin;
	}
	public double getMmax() {
		return mmax;
	}
	public void setMmax(double mmax) {
		this.mmax = mmax;
	}
	public ArrayList<PointShp> getPointShps() {
		return pointShps;
	}
	public void setPointShps(ArrayList<PointShp> pointShps) {
		this.pointShps = pointShps;
	}
	public ArrayList<PolyLineAndPolygonShp> getPolyLineAndPolygonShp() {
		return polyLineAndPolygonShp;
	}
	public void setPolyLineAndPolygonShp(ArrayList<PolyLineAndPolygonShp> polyLineAndPolygonShp) {
		this.polyLineAndPolygonShp = polyLineAndPolygonShp;
	}
	
	
	
	
	
}

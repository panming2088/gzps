package com.augurit.am.cmpt.shapefile.model;

import java.util.ArrayList;

public class PolyLineAndPolygonShp {
	
	private int recordNumber;
	private int contentLength;
	private int shapeType; //3，线；5面
	private double[] Box; // 线：当前线状目标的坐标范围; 面：当前面状目标的坐标范围
	private int numParts; // 线：当前线目标所包含的子线段的个数; 面：当前面目标所包含的子环的个数
	private int numPoints; // 线：当前线目标所包含的顶点个数; 面：构成当前面状目标的所有顶点的个数
	private int[] parts; // 线：每个子线段的第一个坐标点在 Points 的位置; 面：每个子环的第一个坐标点在 Points 的位置
	private Point[] points; // 记录所有坐标点的数组
	//以上为shp文件直接读取到的数据
	
	//以下为通过处理后的线面数据
	private ArrayList<SubPolygon> subpolygons = null;
	
	
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



	public double[] getBox() {
		return Box;
	}



	public void setBox(double[] box) {
		Box = box;
	}



	public int getNumParts() {
		return numParts;
	}



	public void setNumParts(int numParts) {
		this.numParts = numParts;
	}



	public int getNumPoints() {
		return numPoints;
	}



	public void setNumPoints(int numPoints) {
		this.numPoints = numPoints;
	}



	public int[] getParts() {
		return parts;
	}



	public void setParts(int[] parts) {
		this.parts = parts;
	}



	public Point[] getPoints() {
		return points;
	}



	public void setPoints(Point[] points) {
		this.points = points;
	}



	public ArrayList<SubPolygon> getSubpolygons() {
		if(parts==null || parts.length==0
				|| points==null || points.length==0){
			return null;
		}
		if(subpolygons==null){
			subpolygons = new ArrayList<SubPolygon>();
			for(int i=0; i<parts.length; i++){
				SubPolygon subpolygon = new SubPolygon();
				int startIndex, endIndex;
				startIndex = parts[i];
				if(i==parts.length-1){
					endIndex = points.length-1;
				} else {
					endIndex = parts[i+1]-1;
				}
				for(int j=startIndex; j<=endIndex; j++){
					Point point = points[j];
					subpolygon.getPoints().add(point);
				}
				subpolygons.add(subpolygon);
			}
			
		}
		return subpolygons;
	}



	public void setSubpolygons(ArrayList<SubPolygon> subpolygons) {
		this.subpolygons = subpolygons;
	}

	@Override
	public String toString(){
		String str = "";
		str = str + "recordNumber=" + recordNumber + ",  ";
		str = str + "contentLength=" + contentLength + ",  ";
		str = str + "shapeType=" + shapeType + ",  ";
		str = str + "numParts=" + numParts + ",  ";
		str = str + "numPoints=" + numPoints + ", \n";
		for(int i=0; i<parts.length; i++){
			str = str + "parts["+i+"]=" + parts[i] + ",  ";
		}
		str = str + "\n";
		for(int i=0; i<points.length; i++){
			str = str + "points["+i+"]=" + points[i].toString() + ",  \n";
		}
		return str;
	}

}

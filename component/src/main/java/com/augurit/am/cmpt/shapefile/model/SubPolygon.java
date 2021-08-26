package com.augurit.am.cmpt.shapefile.model;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SubPolygon {
	
	private ArrayList<Point> points = new ArrayList<Point>();

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
	public String toString(){
		String str = "";
		for(int i=0; i<points.size(); i++){
			DecimalFormat df = new DecimalFormat("###############0.0##########");
			String x = df.format(points.get(i).getX());
			String y = df.format(points.get(i).getY());
			if(i!=points.size()-1){
				str = str + x + " " + y + ", ";
			} else {
				str = str + x + " " + y;
			}
			
		}
		
		return str;
	}
}

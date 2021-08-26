/**
 * 
 * ACCMap - ACC map development platform
 * Copyright (c) 2015, AfirSraftGarrier, afirsraftgarrier@qq.com
 * 
 */
package com.augurit.agmobile.mapengine.common.model;

public class Geometry
// implements Serializable
{
	// public static String GEOMETRY_POINT = "st_point";
	// public static String GEOMETRY_LINE = "st_linestring";
	// public static String GEOMETRY_POLYGON = "st_polygon";
	// public static String GEOMETRY_MULTIPOINT = "st_multipoint";
	// public static String GEOMETRY_MULTILINE = "st_multiline";
	// public static String GEOMETRY_MULTIPOLYGON = "st_multipolygon";

	// private String type;
	private String wkt;

	// private Geomety

	// public Geometry(String wkt) {
	// this.wkt = wkt;
	// try {
	// String gtype = wkt.trim().substring(0, wkt.indexOf("(")).trim();
	// if (gtype.toUpperCase().startsWith("POINT")) {
	// this.setType(Geometry.GEOMETRY_POINT);
	// } else if (gtype.toUpperCase().startsWith("POLYGON")) {
	// this.setType(Geometry.GEOMETRY_POLYGON);
	// } else if (gtype.toUpperCase().startsWith("LINESTRING")) {
	// this.setType(Geometry.GEOMETRY_LINE);
	// } else if (gtype.toUpperCase().startsWith("MULTIPOINT")) {
	// this.setType(Geometry.GEOMETRY_MULTIPOINT);
	// } else if (gtype.toUpperCase().startsWith("MULTILINESTRING")) {
	// this.setType(Geometry.GEOMETRY_MULTILINE);
	// } else if (gtype.toUpperCase().startsWith("MULTIPOLYGON")) {
	// this.setType(Geometry.GEOMETRY_MULTIPOLYGON);
	// }
	// } catch (Exception e) {
	// this.wkt = null;
	// this.type = null;
	// }
	// }

	// public Geometry() {
	// }

	// public String getType() {
	// return type;
	// }

	public String getWkt() {
		return wkt;
	}

	// public void setType(String type) {
	// this.type = type;
	// }

	public void setWkt(String wkt) {
		this.wkt = wkt;
	}
}
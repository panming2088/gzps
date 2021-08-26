package com.augurit.agmobile.mapengine.common.utils.wktutil;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：Agcom geometry 转ArcGIS
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.psgw.util.wktutil
 * @createTime 创建时间 ：2017-03-17
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-03-17
 * @modifyMemo 修改备注：
 */
public class WktUtil2 {


    /**
     * 把WKT形式的几何图形转换成ArcGIS中的Geometry
     * @param wkt WKT字符串
     * @return
     */
    public static Geometry getGeometryFromWKT(String wkt){
        if(wkt.contains("POINT")){
            return getPointGeometryFromWKT(wkt);
        } else if(wkt.contains("LINESTRING")){
            return getLineStringGeometryFromWKT(wkt);
        } else if(wkt.contains("MULTILINESTRING")){
            return null;
        } else if(wkt.contains("POLYGON") || wkt.contains("MULTIPOLYGON")){
            return getPolygonGeometryFromWKT(wkt);
        } else {
            return null;
        }
    }

    /**
     * WKT转Point
     * @param wkt WKT字符串
     * @return Point
     */
    private static Geometry getPointGeometryFromWKT(String wkt) {
        Point point = null;
        try {
            Pattern p = Pattern.compile(".*\\(\\s*(\\d*.?\\d*)\\s(\\d*.?\\d*)\\).*");
            Matcher m = p.matcher(wkt);
            if (m.matches()) {
                double x = Double.parseDouble(m.group(1));
                double y = Double.parseDouble(m.group(2));
                point = new Point();
                point.setXY(x, y);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return point;
    }

    /**
     * WKT转Polyline
     * @param wkt WKT字符串
     * @return Polyline
     */
    private static Geometry getLineStringGeometryFromWKT(String wkt) {
        Polyline polyline = null;
        try {
            Pattern p1 = Pattern.compile(".*\\((.*)\\).*");   // 匹配括号中内容
            Matcher m1 = p1.matcher(wkt);
            if (m1.matches()) {
                ArrayList<Point> points = new ArrayList<Point>();
                String[] pointStrs = m1.group(1).split(",");
                Pattern p2 = Pattern.compile("\\s*(\\d*.?\\d*)\\s(\\d*.?\\d*)");    // 匹配每个点坐标
                for (String pointStr : pointStrs) {
                    Matcher m2 = p2.matcher(pointStr);
                    if (m2.matches()) {
                        Point point = new Point();
                        double x = Double.parseDouble(m2.group(1));
                        double y = Double.parseDouble(m2.group(2));
                        point.setXY(x, y);
                        points.add(point);
                    }
                }
                if (points.size() != 0) {
                    polyline = new Polyline();
                    polyline.startPath(points.get(0));
                    for (int i = 1; i < points.size(); i++) {
                        polyline.lineTo(points.get(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return polyline;
    }

    /**
     * WKT转Polygon
     * @param wkt WKT字符串
     * @return Polygon
     */
    private static Geometry getPolygonGeometryFromWKT(String wkt) {
        // 该方法逻辑与转Polyline一致
        Polygon polygon = null;
        try {
            Pattern p1 = Pattern.compile(".*\\((.*)\\).*");   // 匹配括号中内容
            Matcher m1 = p1.matcher(wkt);
            if (m1.matches()) {
                ArrayList<Point> points = new ArrayList<Point>();
                String[] pointStrs = m1.group(1).split(",");
                Pattern p2 = Pattern.compile("\\s*(\\d*.?\\d*)\\s(\\d*.?\\d*)");    // 匹配每个点坐标
                for (String pointStr : pointStrs) {
                    Matcher m2 = p2.matcher(pointStr);
                    if (m2.matches()) {
                        Point point = new Point();
                        double x = Double.parseDouble(m2.group(1));
                        double y = Double.parseDouble(m2.group(2));
                        point.setXY(x, y);
                        points.add(point);
                    }
                }
                if (points.size() != 0) {
                    polygon = new Polygon();
                    polygon.startPath(points.get(0));
                    for (int i = 1; i < points.size(); i++) {
                        polygon.lineTo(points.get(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return polygon;
    }

}

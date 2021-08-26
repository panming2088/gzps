package com.augurit.agmobile.mapengine.common.utils.wktutil;

import com.augurit.agmobile.mapengine.common.utils.wktutil.model.Envelope;
import com.augurit.agmobile.mapengine.common.utils.wktutil.model.LineString;
import com.augurit.agmobile.mapengine.common.utils.wktutil.model.MultLinesString;
import com.augurit.agmobile.mapengine.common.utils.wktutil.model.MultiIPoint;
import com.augurit.agmobile.mapengine.common.utils.wktutil.model.Point;
import com.augurit.agmobile.mapengine.common.utils.wktutil.model.Polygon;
import com.augurit.am.fw.utils.DoubleUtil;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MapGeometry;
import com.esri.core.geometry.SpatialReference;
import com.google.gson.Gson;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 把WKT字符串转为ArcGIS中的Geometry
 * @author 创建人 ：liangshenghong
 * @package 包名 ：com.augurit.am.map.arcgis.util.wktutil
 * @createTime 创建时间 ：2017-02-15
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-15
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public final class WktUtil {
    private WktUtil() {
    }

    /**
     * 点 转换 JSON
     *
     * @param wkt
     * @param wkid
     * @return
     */
    private static String getPOINTWktToJson(String wkt, int wkid) {

        String[] strHead = wkt.split("\\(");
        String strContent = strHead[1].substring(0, strHead[1].length() - 1);
        String[] strResult = strContent.split(" ");

        Point Point = new Point();
        Point.setX(Double.parseDouble(strResult[0]));
        Point.setY(Double.parseDouble(strResult[1]));

        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);

        Point.setSpatialReference(spatialReference);

        Gson gson = new Gson();

        return gson.toJson(Point);

    }

    /**
     * JSON 转换 点
     * @param json
     * @return
     */
    private static String getWKTFromPointGeometryJSON(String json){
        String wkt = "POINT (";
        Point point = new Gson().fromJson(json, Point.class);
        wkt = wkt + DoubleUtil.formatDecimal(point.getX()) + " " + DoubleUtil.formatDecimal(point.getY()) + ")";
        return wkt;
    }

    /**
     * 多点 转换 JSON
     *
     * @param wkt
     * @param wkid
     * @return
     */
    private static String getMULTIPOINTWktToJson(String wkt, int wkid) {

        MultiIPoint MultiIPoint = new MultiIPoint();

        String ToTailWkt = wkt.substring(0, wkt.length() - 1);
        String[] strHead = ToTailWkt.split("\\(\\(");
        String strMiddle = strHead[1].substring(0, strHead[1].length() - 1);
        String[] strMiddles = strMiddle.split(",");

        List<Double[]> list = new ArrayList<Double[]>();

        for (int i = 0; i < strMiddles.length; i++) {

            if (i == 0) {

                String item = strMiddles[i].substring(0,
                        strMiddles[i].length() - 1);
                String[] items = item.split(" ");
                Double[] listResult = new Double[]{
                        Double.parseDouble(items[0]),
                        Double.parseDouble(items[1])};

                list.add(listResult);

            } else if (i == strMiddles.length) {

                String item = strMiddles[i]
                        .substring(1, strMiddles[i].length());
                String[] items = item.split(" ");
                Double[] listResult = new Double[]{
                        Double.parseDouble(items[0]),
                        Double.parseDouble(items[1])};

                list.add(listResult);

            } else {

                String strItem = strMiddles[i].trim();
                String item = strItem.substring(1, strItem.length() - 1);
                String[] items = item.split(" ");
                Double[] listResult = new Double[]{
                        Double.parseDouble(items[0]),
                        Double.parseDouble(items[1])};

                list.add(listResult);

            }

        }

        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);

        MultiIPoint.setPoints(list);
        MultiIPoint.setSpatialReference(spatialReference);

        Gson gson = new Gson();

        return gson.toJson(MultiIPoint);

    }

    /**
     * JSON 转 多点WKT
     * @param json
     * @return
     */
    private static String getWKTFromMultiPointGeometryJSON(String json){
        String wkt = "MULTIPOINT (";
        MultiIPoint multiIPoint = new Gson().fromJson(json, MultiIPoint.class);
        List<Double[]> points = multiIPoint.getPoints();
        for(Double[] ps : points){
            wkt = wkt + "(" + DoubleUtil.formatDecimal(ps[0]) + " " + DoubleUtil.formatDecimal(ps[1])
                    + "),";
        }
        wkt = wkt.substring(0, wkt.length()-1);
        wkt = wkt + ")";
        return wkt;
    }

    /**
     * 线 转换 JSON
     *
     * @param wkt
     * @param wkid
     * @return
     */
    private static String getLINESTRINGWktToJson(String wkt, int wkid) {

        LineString LineString = new LineString();

        List<List<Double[]>> lists = new ArrayList<List<Double[]>>();
        List<Double[]> list = new ArrayList<Double[]>();

        String[] strHead = wkt.split("\\(");
        String strContent = strHead[1].substring(0, strHead[1].length() - 1);
        String[] strResult = strContent.split(",");

        for (int i = 0; i < strResult.length; i++) {

            String itme = strResult[i].trim();
            String[] items = itme.split(" ");
            Double[] listResult = new Double[]{Double.parseDouble(items[0]),
                    Double.parseDouble(items[1])};
            list.add(listResult);

        }

        lists.add(list);

        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);

        LineString.setPaths(lists);
        LineString.setSpatialReference(spatialReference);

        Gson gson = new Gson();

        return gson.toJson(LineString);

    }

    /**
     * json 转 线WKT
     * @param json
     * @return
     */
    private static String getWKTFromLINESTRINGGeometryJSON(String json){
        String wkt = "LINESTRING (";
        LineString lineString = new Gson().fromJson(json, LineString.class);
        List<Double[]> paths = lineString.getPaths().get(0);
        for(Double[] ps : paths){
            wkt = wkt + DoubleUtil.formatDecimal(ps[0]) + " " + DoubleUtil.formatDecimal(ps[1]) + ",";
        }
        wkt = wkt.substring(0, wkt.length()-1);
        wkt = wkt + ")";
        return wkt;
    }

    /**
     * 多线 转换 JSON
     *
     * @param wkt
     * @param wkid
     * @return
     */
    private static String getMULTILINESTRINGWktToJson(String wkt, int wkid) {

        MultLinesString LineString = new MultLinesString();

        List<List<Double[]>> lists = new ArrayList<List<Double[]>>();

        String ToTailWkt = wkt.substring(0, wkt.length() - 1);
        String[] strHead = ToTailWkt.split("\\(", 2);

        String[] strList = strHead[1].split("\\),\\(");

        for (int i = 0; i < strList.length; i++) {

            String item = strList[i].trim();
            item = item.replace("(", "");
            item = item.replace(")", "");
//            item = item.substring(1, item.length() - 1);
            String[] items = item.split(",");

            List<Double[]> list = new ArrayList<Double[]>();

            for (int j = 0; j < items.length; j++) {

                String jItem = items[j].trim();
                String[] jItems = jItem.split(" ");

                Double[] listResult = new Double[]{
                        Double.parseDouble(jItems[0]),
                        Double.parseDouble(jItems[1])};

                list.add(listResult);

            }

            lists.add(list);

        }

        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);

        LineString.setRings(lists);
        LineString.setSpatialReference(spatialReference);

        Gson gson = new Gson();

        return gson.toJson(LineString);

    }

    /**
     * json 转 多线WKT
     * @param json
     * @return
     */
    private static String getWKTFromMULTILINESTRINGGeometryJSON(String json){
        json = json.replace("paths", "rings");   //因MultLinesString中的属性为rings
        String wkt = "MULTILINESTRING (";
        MultLinesString multLinesString = new Gson().fromJson(json, MultLinesString.class);
        List<List<Double[]>> rings = multLinesString.getRings();
        for(List<Double[]> rs1 : rings){
            wkt = wkt + "(";
            for(Double[] rs2 : rs1){
                wkt = wkt + DoubleUtil.formatDecimal(rs2[0]) + " " + DoubleUtil.formatDecimal(rs2[1]) + ",";
            }
            wkt = wkt.substring(0, wkt.length()-1);
            wkt = wkt + "),";
        }
        wkt = wkt.substring(0, wkt.length()-1);
        wkt = wkt + ")";
        return wkt;
    }

    /**
     * 多边形转JSON
     * @param wkt
     * @param wkid
     * @return
     */
    private static String getPOLYGONWktToJson(String wkt, int wkid) {

        Polygon Polygon = new Polygon();

        List<List<Double[]>> lists = new ArrayList<List<Double[]>>();

        String ToTailWkt = wkt.substring(0, wkt.length() - 1);
        String[] strHead = ToTailWkt.split("\\(", 2);
        strHead[1] = strHead[1].replace("), (", "),(");
        String[] strList = strHead[1].split("\\),\\(");

        for (int i = 0; i < strList.length; i++) {

            String item = strList[i].trim();
            item = item.replace("(", "");
            item = item.replace(")", "");
//            item = item.substring(1, item.length() - 1);
            String[] items = item.split(",");

            List<Double[]> list = new ArrayList<Double[]>();

            for (int j = 0; j < items.length; j++) {

                String jItem = items[j].trim();
                String[] jItems = jItem.split(" ");

                Double[] listResult = new Double[]{
                        Double.parseDouble(jItems[0]),
                        Double.parseDouble(jItems[1])};

                list.add(listResult);

            }

            lists.add(list);

        }

        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);

        Polygon.setRings(lists);
        Polygon.setSpatialReference(spatialReference);

        Gson gson = new Gson();

        return gson.toJson(Polygon);
    }

    /**
     * JSON 转 多边形WKT
     * @param json
     * @return
     */
    private static String getWKTFromPOLYGONGeometryJSON(String json){
        String wkt = "POLYGON ((";
        Polygon polygon = new Gson().fromJson(json, Polygon.class);
        if(polygon.getRings().size()>1){
            return getWKTFromMULTIPOLYGONGeometryJSON(json);
        }
        List<Double[]> rings = polygon.getRings().get(0);
        for(Double[] rs : rings){
            wkt = wkt + DoubleUtil.formatDecimal(rs[0]) + " " + DoubleUtil.formatDecimal(rs[1]) + ",";
        }
        wkt = wkt.substring(0, wkt.length()-1);
        wkt = wkt + "))";
        return wkt;
    }

    private static String getWKTFromENVELOPEGeometryJSON(String json){
        String wkt = "POLYGON ((";
        Envelope envelope = new Gson().fromJson(json, Envelope.class);
        wkt += DoubleUtil.formatDecimal(envelope.getXmin()) + " " + DoubleUtil.formatDecimal(envelope.getYmin()) + ",";
        wkt += DoubleUtil.formatDecimal(envelope.getXmin()) + " " + DoubleUtil.formatDecimal(envelope.getYmax()) + ",";
        wkt += DoubleUtil.formatDecimal(envelope.getXmax()) + " " + DoubleUtil.formatDecimal(envelope.getYmax()) + ",";
        wkt += DoubleUtil.formatDecimal(envelope.getXmax()) + " " + DoubleUtil.formatDecimal(envelope.getYmin()) + ",";
        wkt += DoubleUtil.formatDecimal(envelope.getXmin()) + " " + DoubleUtil.formatDecimal(envelope.getYmin());
        wkt = wkt + "))";
        return wkt;
    }

    /**
     * 多多边形转JSON
     * @param wkt
     * @param wkid
     * @return
     */
    private static String getMULTIPOLYGONWktToJson(String wkt, int wkid) {

        Polygon Polygon = new Polygon();

        List<List<Double[]>> lists = new ArrayList<List<Double[]>>();

        String ToTailWkt = wkt.substring(0, wkt.length() - 1);
        String[] strHead = ToTailWkt.split("\\(", 2);
        ToTailWkt = strHead[1].substring(0, strHead[1].length() - 1);
        String[] strHeads = ToTailWkt.split("\\(", 2);
        strHeads[1] = strHeads[1].replace("), (", "),(");
        String[] strList = strHeads[1].split("\\),\\(");

        if (strList.length == 1) {

            for (int i = 0; i < strList.length; i++) {

                String item = strList[i].trim();
                item = item.replace("(", "");
                item = item.replace(")", "");
//                item = item.substring(1, item.length() - 1);
                String[] items = item.split(",");

                List<Double[]> list = new ArrayList<Double[]>();

                for (int j = 0; j < items.length; j++) {
                    String jItem = items[j].trim();
                    String[] jItems = jItem.split(" ");

                    Double[] listResult = new Double[]{
                            Double.parseDouble(jItems[0]),
                            Double.parseDouble(jItems[1])};

                    list.add(listResult);

                }

                lists.add(list);

            }

        } else {

            for (int i = 0; i < strList.length; i++) {

                String item = strList[i].trim();
                item = item.substring(1, item.length() - 1);
                String[] items = item.split(",");

                List<Double[]> list = new ArrayList<Double[]>();

                for (int j = 1; j < items.length; j++) {
                    String jItem = items[j].trim();
                    String[] jItems = jItem.split(" ");

                    Double[] listResult = new Double[]{
                            Double.parseDouble(jItems[0]),
                            Double.parseDouble(jItems[1])};

                    list.add(listResult);

                }

                lists.add(list);

            }

        }

        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);

        Polygon.setRings(lists);
        Polygon.setSpatialReference(spatialReference);

        Gson gson = new Gson();

        return gson.toJson(Polygon);

    }

    /**
     * JSON 转 多多边形WKT
     * @param json
     * @return
     */
    private static String getWKTFromMULTIPOLYGONGeometryJSON(String json) {
        String wkt = "POLYGON (";
        Polygon polygon = new Gson().fromJson(json, Polygon.class);
        List<List<Double[]>> rings = polygon.getRings();
        for(List<Double[]> rs1 : rings){
            wkt = wkt + "((";
            for(Double[] rs2 : rs1){
                wkt = wkt + DoubleUtil.formatDecimal(rs2[0]) + " " +DoubleUtil.formatDecimal(rs2[1]) + ",";
            }
            wkt = wkt.substring(0, wkt.length()-1);
            wkt = wkt + ")),";
        }
        wkt = wkt.substring(0, wkt.length()-1);
        wkt = wkt + ")";
        return wkt;
    }

    /**
     * 由JSON生成ArcGIS的Geometry类
     * @param json
     * @return
     */
    private static Geometry getGeometryFromJSON(String json) {
        try {
            JsonParser jsonParser = new JsonFactory().createJsonParser(json);
            MapGeometry mapGeometry = GeometryEngine.jsonToGeometry(jsonParser);
            return mapGeometry.getGeometry();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 把代表点的WKT转换成ArcGIS的Geometry
     * @param wkt WKT字符串
     * @param wkid 坐际系ID
     * @return
     */
    public static Geometry getPointGeometryFromWKT(String wkt, int wkid){
        String json = getPOINTWktToJson(wkt, wkid);
        return getGeometryFromJSON(json);
    }


    /**
     * 把代表线的WKT转换成ArcGIS的Geometry
     * @param wkt WKT字符串
     * @param wkid 坐际系ID
     * @return
     */
    public static Geometry getLineStringGeometryFromWKT(String wkt, int wkid){
        String json = getLINESTRINGWktToJson(wkt, wkid);
        return getGeometryFromJSON(json);
    }

    public static Geometry getMultiLineStringGeometryFromWKT(String wkt, int wkid){
        String json = getMULTILINESTRINGWktToJson(wkt, wkid);
        json = json.replace("rings", "paths");  //不加这句会把Polyline解析成Polygon
        return getGeometryFromJSON(json);
    }

    /**
     * 把代表面的WKT转换成ArcGIS的Geometry
     * @param wkt WKT字符串
     * @param wkid 坐际系ID
     * @return
     */
    public static Geometry getPolygonGeometryFromWKT(String wkt, int wkid) {
        String json = null;
        if (wkt.contains("(((")) {
            json = getMULTIPOLYGONWktToJson(wkt, wkid);
        } else {
            json = getPOLYGONWktToJson(wkt, wkid);
        }
        return getGeometryFromJSON(json);
    }


    /**
     * 把WKT形式的几何图形转换成ArcGIS中的Geometry
     * @param wkt WKT字符串
     * @param wkid 坐际系ID
     * @return
     */
    public static Geometry getGeometryFromWKT(String wkt, int wkid){
        if(wkt.contains("POINT")){
            return getPointGeometryFromWKT(wkt, wkid);
        } else if(wkt.startsWith("LINESTRING")){
            return getLineStringGeometryFromWKT(wkt, wkid);
        } else if(wkt.startsWith("MULTILINESTRING")){
            return getMultiLineStringGeometryFromWKT(wkt, wkid);
        } else if(wkt.contains("POLYGON") || wkt.contains("MULTIPOLYGON")){
            return getPolygonGeometryFromWKT(wkt, wkid);
        } else {
            return null;
        }

    }

    /**
     * ArcGIS的Geometry转换成WKT
     * @param spatialReference
     * @param geometry
     * @return
     */
    public static String getWKTFromGeometry(SpatialReference spatialReference, Geometry geometry){
        String wkt = null;
        String json = GeometryEngine.geometryToJson(spatialReference, geometry);
        switch(geometry.getType()){
            case POINT:
                wkt =  getWKTFromPointGeometryJSON(json);
                break;
            case MULTIPOINT:
                wkt = getWKTFromMultiPointGeometryJSON(json);
                break;
            case LINE:
                wkt = getWKTFromLINESTRINGGeometryJSON(json);
                break;
            case POLYLINE:
                wkt = getWKTFromMULTILINESTRINGGeometryJSON(json);
                break;
            case ENVELOPE:
                wkt = getWKTFromENVELOPEGeometryJSON(json);
                break;
            case POLYGON:
                wkt = getWKTFromPOLYGONGeometryJSON(json);
                break;
            default:
                break;
        }
        return wkt;
    }
}

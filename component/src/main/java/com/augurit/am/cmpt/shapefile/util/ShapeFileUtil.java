package com.augurit.am.cmpt.shapefile.util;


import com.augurit.am.cmpt.shapefile.model.PointShp;
import com.augurit.am.cmpt.shapefile.model.PolyLineAndPolygonShp;
import com.augurit.am.cmpt.shapefile.model.ShapeFile;
import com.augurit.am.cmpt.shapefile.model.SubPolygon;
import com.augurit.am.cmpt.shapefile.reader.ShapeFileReader;
import com.augurit.am.fw.utils.StringUtil;

import java.util.ArrayList;

/**
 * Created by ac on 2016-04-18.
 */
public final class ShapeFileUtil {
    private ShapeFileUtil() {
    }

    public static String getWktFromShapeFile(String filePath) {
        ShapeFileReader shpReader = new ShapeFileReader(filePath);
        shpReader.read();
        ShapeFile shp = shpReader.getShapeFile();
        return getWktFromShapeFile(shp);
    }

    public static String getWktFromShapeFile(ShapeFile shp) {

        return null;
    }

    /**
     * @param filePath shp文件路径
     * @return shp文件的wkt集合
     */
    public static ArrayList<String> getWKTListFromShpFile(String filePath) {
        ShapeFileReader shpReader = new ShapeFileReader(filePath);
        shpReader.read();
        ShapeFile shp = shpReader.getShapeFile();
        ArrayList<String> wkts = getWKTListFromShapeFile(shp);
        return wkts;
    }

    /**
     * @param shp shp文件
     * @return shp文件wkt集合
     */
    public static ArrayList<String> getWKTListFromShapeFile(ShapeFile shp) {
        ArrayList<String> wkts = new ArrayList<String>();
        switch (shp.getShapeType()) {
            case ShapeFile.PointZ:
            case ShapeFile.POINT:
                ArrayList<PointShp> pointShps = shp.getPointShps();
                for (PointShp pointShp : pointShps) {
                    String wkt = "";

                    wkt = wkt + "POINT(" + StringUtil.valueOf(pointShp.getX()) + " " + StringUtil.valueOf(pointShp.getY()) + ")";

                    wkts.add(wkt);
                }
                break;
            case ShapeFile.POLYLINE:
                ArrayList<PolyLineAndPolygonShp> polyLines = shp.getPolyLineAndPolygonShp();
                for (PolyLineAndPolygonShp poly : polyLines) {
                    String wkt = "";
                    SubPolygon subpolygon = poly.getSubpolygons().get(0);
                    wkt = "LINESTRING  ( " + subpolygon.toString() + ")";
                    wkts.add(wkt);
                }
                break;
            case ShapeFile.POLYGON:
                ArrayList<PolyLineAndPolygonShp> polygons = shp.getPolyLineAndPolygonShp();
                for (PolyLineAndPolygonShp poly : polygons) {
                    String wkt = "";
                    SubPolygon subpolygon = poly.getSubpolygons().get(0);
                    wkt = "POLYGON  (( " + subpolygon.toString() + "))";
                    wkts.add(wkt);
                }
                break;
            default:
                break;
        }

        return wkts;
    }


}

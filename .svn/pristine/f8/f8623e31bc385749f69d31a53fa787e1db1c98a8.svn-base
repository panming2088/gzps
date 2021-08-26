package com.augurit.agmobile.mapengine.common.utils;

import android.graphics.Color;
import android.support.annotation.Keep;

import com.augurit.agmobile.mapengine.common.GraphicStyle;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.panorama.model.GraphicInfo;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;

import java.util.Iterator;
import java.util.List;

/**
 * 绘制点、线、面的工具类
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.layerq.util
 * @createTime 创建时间 ：2016-10-14 14:20
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-14 14:20
 */
@Keep
public final class GeometryUtil {
    private GeometryUtil() {
    }

    /**
     * 画线
     *
     * @param points     点的集合
     * @param lineSymbol 线的样式
     * @return 返回一个graphis
     */
    public static Graphic drawLine(List<Point> points, Symbol lineSymbol) {
        Polyline polyline = new Polyline();
        for (int i = 0; i < points.size() - 1; i++) {
            Point startPoint = points.get(i);
            Point endPoint = points.get(i + 1);
            Line line = new Line();
            line.setStart(startPoint);
            line.setEnd(endPoint);
            polyline.addSegment(line, false);
        }
        Graphic graphic = new Graphic(polyline, lineSymbol);
        return graphic;
    }

    /**
     * 画线
     *
     * @param points     点的集合
     * @param lineSymbol 线的样式
     * @return 返回一个graphis
     */
    public static Graphic drawLineString(List<Point> points, Symbol lineSymbol) {
        Polyline polyline = new Polyline();
        for (int i = 0; i < points.size() - 1; i++) {
            Point startPoint = points.get(i);
            Point endPoint = points.get(i + 1);
            Line line = new Line();
            line.setStart(startPoint);
            line.setEnd(endPoint);
            polyline.addSegment(line, false);
        }
        Graphic graphic = new Graphic(polyline, lineSymbol);
        return graphic;
    }


    /**
     * 区域绘制，画面
     *
     * @param points     点的集合
     * @param fillSymbol 面的样式
     * @return 返回一个graphis
     */
    public static Graphic drawPolygon(List<Point> points, Symbol fillSymbol) {
        Polygon polygon = new Polygon();
        polygon.startPath(points.get(0));
        for (int i = 1; i < points.size(); i++) {
            polygon.lineTo(points.get(i));
        }
        Graphic graphic = new Graphic(polygon, fillSymbol);
        return graphic;
    }

    public static Polygon getPolygon(List<Point> points) {
        Polygon polygon = new Polygon();
        polygon.startPath(points.get(0));
        for (int i = 1; i < points.size() - 1; i++) {
            polygon.lineTo(points.get(i));
        }
        return polygon;
    }

    public static Graphic drawCircle(Point center, double radius, Symbol fillSymbol) {
        Polygon polygon = new Polygon();
        Point[] points = getPoints(center, radius);
        polygon.startPath(points[0]);
        for (int i = 1; i < points.length; i++) {
            polygon.lineTo(points[i]);
        }
        Graphic graphic = new Graphic(polygon, fillSymbol);
        return graphic;
    }

    private static Point[] getPoints(Point center, double radius) {
        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y);
        }
        return points;
    }

    /**
     * 获取面的中心点
     *
     * @param polygon
     * @return
     */
    public static Point getPolygonCenterPoint(Polygon polygon) {
        Envelope envelope = new Envelope();
        //获取面的外切矩形
        polygon.queryEnvelope(envelope);
        return envelope.getCenter();
    }

    /*public static Point getPolygonCenterPoint(IPolygon polygon) {
        if (polygon == null)
            return new Point();
        int count = polygon.getPointCount();
        int x = 0, y = 0;
        for (int i = 0; i < count; i++) {
            Point point = polygon.getPoint(i);
            x += point.getX();
            y += point.getY();
        }
        return new Point(x / count, y / count);
    }*/

    /**
     * 获取线的中心点
     *
     * @param polyline
     * @return
     */
    public static Point getLineCenterPoint(Polyline polyline) {
        if (polyline == null)
            return new Point();
        Point startPoint = polyline.getPoint(0);
        Point endPoint = polyline.getPoint(polyline.getPointCount() - 1);
        return new Point((startPoint.getX() + endPoint.getX()) / 2, (startPoint.getY() + endPoint.getY()) / 2);
    }


    public static Point getGeometryCenter(Geometry geometry) {
        switch (geometry.getType()) {
            case POINT:
                return (Point) geometry;

            case POLYLINE:
            case LINE:
                Polyline polyline = (Polyline) geometry;
                return getLineCenterPoint(polyline);

            case POLYGON:
                Polygon polygon = (Polygon) geometry;
                return getPolygonCenterPoint(polygon);
            default:
                break;

        }
        return null;
    }

    /**
     * 应花都项目的要求，将显示线的callout的位置摆放在线的第一个点
     *
     * @param geometry
     * @return
     */
    public static Point getMarkGeometryCenter(Geometry geometry) {
        switch (geometry.getType()) {
            case POINT:
                return (Point) geometry;

            case POLYLINE:
                Polyline polyline = (Polyline) geometry;
                Point point = polyline.getPoint(polyline.getPointCount() / 2);
                return point;

            case POLYGON:
                Polygon polygon = (Polygon) geometry;
                return getPolygonCenterPoint(polygon);

        }
        return null;
    }


    /**
     * 在地图上添加圆
     *
     * @param center 圆心
     * @param radius 半径
     * @return 圆形范围
     */
    public static GraphicInfo addCircle(Point center, double radius, GraphicsLayer graphicsLayer) {

        Polygon circle = drawCircle(center, radius);
        FillSymbol fillSymbol = new SimpleFillSymbol(Color.parseColor("#2196F3"));
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                2, SimpleLineSymbol.STYLE.SOLID);
        fillSymbol.setAlpha(20);
        fillSymbol.setOutline(lineSymbol);
        Graphic graphicCircle = new Graphic(circle, fillSymbol);
        int id = graphicsLayer.addGraphic(graphicCircle);
        GraphicInfo grahpic = new GraphicInfo(circle, id);
        return grahpic;
    }

    /**
     * 画正圆
     *
     * @param center 中心点
     * @param radius 半径(米)
     * @return 圆形Polygon
     */
    public static Polygon drawCircle(Point center, double radius) {
        Polygon circle = new Polygon();
        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y);

        }
        circle.startPath(points[0]);
        for (int i = 1; i < points.length; i++) {
            circle.lineTo(points[i]);
        }
        return circle;
    }

    /**
     * 在地图上添加正方形
     *
     * @param startPoint 开始点
     * @param foot       另一点
     * @return 正方形范围
     */
    public static GraphicInfo addSquare(Point startPoint, Point foot, GraphicsLayer graphicsLayer) {
        Polygon square = drawSquare(startPoint, foot);
        FillSymbol fillSymbol = new SimpleFillSymbol(Color.parseColor("#2196F3"));
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                2, SimpleLineSymbol.STYLE.SOLID);
        fillSymbol.setAlpha(20);
        fillSymbol.setOutline(lineSymbol);
        Graphic graphicSquare = new Graphic(square, fillSymbol);
        int id = graphicsLayer.addGraphic(graphicSquare);
        GraphicInfo graphicInfo = new GraphicInfo(square, id);
        return graphicInfo;
    }

    /**
     * 画正方
     *
     * @param center 中心点
     * @param foot   另一点
     * @return 正方形Polygon
     */
    private static Polygon drawSquare(Point center, Point foot) {
        Polygon square = new Polygon();
        Point[] points = new Point[4];
        double dx;
        double dy;
        double d;
        dx = foot.getX() - center.getX();
        dy = foot.getY() - center.getY();
        d = Math.max(Math.abs(dx), Math.abs(dy));
        points[0] = new Point(center.getX(), center.getY());
        points[1] = new Point(center.getX() + dx, center.getY());
        points[2] = new Point(center.getX() + dx, center.getY() + dy);
        points[3] = new Point(center.getX(), center.getY() + dy);
        square.startPath(points[0]);
        for (int i = 1; i < points.length; i++) {
            square.lineTo(points[i]);
        }
        return square;
    }


    /**
     * 画多边形
     *
     * @return 多边形geometry
     */
    public static GraphicInfo addPolygon(List<Point> points, GraphicsLayer grahpicLayers) {
        // mGLayer.removeAll();
        if (points.size() != 0) {
            int index = 0;
            Polygon polygon = new Polygon();
            for (Iterator labelPointForPolygon = points.iterator(); labelPointForPolygon.hasNext(); ++index) {
                Point screenPoint = (Point) labelPointForPolygon.next();
                //遍历画点
                if (index == 0) {
                    polygon.startPath(screenPoint);
                } else {
                    polygon.lineTo(screenPoint);
                }
            }
            FillSymbol fillSymbol = new SimpleFillSymbol(Color.parseColor("#2196F3"));
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                    2, SimpleLineSymbol.STYLE.SOLID);
            fillSymbol.setAlpha(20);
            fillSymbol.setOutline(lineSymbol);
            int id = grahpicLayers.addGraphic(new Graphic(polygon, fillSymbol));
            //当绘制完成后要把点重新清空
            GraphicInfo graphicInfo = new GraphicInfo(polygon, id);

            return graphicInfo;
        }
        return null;
    }


    /**
     * 在地图上绘制图形
     *
     * @param graphicsLayer
     */
    public static void drawGraphicOnMap(GraphicsLayer graphicsLayer, Geometry geometry) {

        Graphic lGraphic = null;
        switch (geometry.getType()) {
            case POLYLINE:
                lGraphic = new Graphic(geometry, GraphicStyle.getCommonLineSymbol());
                break;
            case POINT:
                lGraphic = new Graphic(geometry, GraphicStyle.getCommonPointSymbol());

                break;
            case POLYGON:
                lGraphic = new Graphic(geometry, GraphicStyle.getCommonPolygonSymbol());
                break;
        }
        graphicsLayer.addGraphic(lGraphic);
    }


    /**
     *
     * 获取两个点之间的角度
     * x2,y2为固定点（可以看做原点），x1,y1为变化点
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double getAngle(double x1, double y1, double x2, double y2) {

        //首先要判断是在第几象限
        //第一象限
        if (x1 > x2 && y1 < y2){
            double x = Math.abs(x1 - x2);
            double y = Math.abs(y1 - y2);
            double z = Math.sqrt(x * x + y * y);
            return - Math.round((Math.asin(y / z) / Math.PI * 180));
        }

        //第二象限
        else if (x1 < x2 && y1 < y2){
            double x = Math.abs(x1 - x2);
            double y = Math.abs(y1 - y2);
            double z = Math.sqrt(x * x + y * y);
            return 270 - Math.round((Math.asin(x / z) / Math.PI * 180));
        }


        //第三象限
        else if (x1 < x2 && y1 > y2){
            double x = Math.abs(x1 - x2);
            double y = Math.abs(y1 - y2);
            double z = Math.sqrt(x * x + y * y);
            return 90 +  Math.round((Math.asin(x / z) / Math.PI * 180));
        }

        //第四象限
        //if (x1 > x2 && y1 < y2)
        else {
            double x = Math.abs(x1 - x2);
            double y = Math.abs(y1 - y2);
            double z = Math.sqrt(x * x + y * y);
            return Math.round((Math.asin(y / z) / Math.PI * 180));
        }

    }
}
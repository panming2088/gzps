package com.augurit.agmobile.mapengine.edit.util;

import android.graphics.Color;

import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.util.List;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augur.agmobile.ammap.edit.utils
 * @createTime 创建时间 ：16/11/15
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 16/11/15
 */

public final class DrawUtils {
    //新增点,被选中点标记符号为红色
    public static SimpleMarkerSymbol mRedMarkerSymbol = new SimpleMarkerSymbol(Color.RED, 20, SimpleMarkerSymbol.STYLE.CIRCLE);
    //中间点标记符号为绿色
    public static SimpleMarkerSymbol mGreenMarkerSymbol = new SimpleMarkerSymbol(Color.GREEN, 15, SimpleMarkerSymbol.STYLE.CIRCLE);
    //一般点(非中间点,新增点,选中点)标记符号为黑色
    public static SimpleMarkerSymbol mBlackMarkerSymbol = new SimpleMarkerSymbol(Color.BLACK, 20, SimpleMarkerSymbol.STYLE.CIRCLE);

    private DrawUtils() {
    }

    public static void drawPolylineOrPolygon(GraphicsLayer graphicsLayer, List<Point> points, EditMode mode) {

        if (graphicsLayer == null) return;

        Graphic graphic;
        MultiPath multiPath;

        if (points.size() > 1) {

            // Build a MultiPath containing the vertices
            if (mode == EditMode.POLYLINE) {
                multiPath = new Polyline();
            } else {
                multiPath = new Polygon();
            }

            multiPath.startPath(points.get(0));
            for (int i = 1; i < points.size(); i++) {
                multiPath.lineTo(points.get(i));
            }

            // Draw it using a line or fill symbol
            if (mode == EditMode.POLYLINE) {
                graphic = new Graphic(multiPath, new SimpleLineSymbol(Color.BLACK, 4));
            } else {
                SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(Color.YELLOW);
                simpleFillSymbol.setAlpha(100);
                simpleFillSymbol.setOutline(new SimpleLineSymbol(Color.BLACK, 4));
                graphic = new Graphic(multiPath, (simpleFillSymbol));
            }

            graphicsLayer.addGraphic(graphic);
        }
    }

    /**
     * 画点集合中两点间的中间点
     *
     * @param graphicsLayer
     * @param points
     * @param mode
     */
    public static void drawMidPoints(GraphicsLayer graphicsLayer, List<Point> points, EditMode mode, int insertIndex, boolean isMidPointSelected, List<Point> midPoints) {

        if (graphicsLayer == null) {
            return;
        }

        int index;
        Graphic graphic;
        midPoints.clear();
        //    List<Point> midPoints = new ArrayList<>();

        if (points.size() > 1) {
            // Build new list of mid-points
            for (int i = 1; i < points.size(); i++) {
                Point p1 = points.get(i - 1);
                Point p2 = points.get(i);
                midPoints.add(new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2));
            }

            if (mode == EditMode.POLYGON && points.size() > 2) {
                //Complete the circle
                Point p1 = points.get(0);
                Point p2 = points.get(points.size() - 1);
                midPoints.add(new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2));
            }

            // Draw the mid-points
            index = 0;
            for (Point pt : midPoints) {
                if (insertIndex >= 0 && insertIndex == index && isMidPointSelected) {
                    graphic = new Graphic(pt, mRedMarkerSymbol);
                } else {
                    graphic = new Graphic(pt, mGreenMarkerSymbol);
                }
                graphicsLayer.addGraphic(graphic);
                index++;
            }
        }
    }

    public static void drawVertices(GraphicsLayer graphicsLayer, List<Point> points, int insertIndex, boolean isPointSelected) {
        if (graphicsLayer == null) {
            return;
        }

        int index = 0;
        SimpleMarkerSymbol symbol;

        for (Point pt : points) {
            if (insertIndex >= 0 && index == insertIndex && isPointSelected) {
                //当前选中点为红色
                symbol = mRedMarkerSymbol;
                LogUtil.i("mRedMarkerSymbol 111111111111");
            } else if (index == points.size() - 1 && insertIndex < 0) {
                //当前没有选中点则默认最后一个为红色
                symbol = mRedMarkerSymbol;

                LogUtil.i("mRedMarkerSymbol 222222222222");
            } else {
                //其他为黑色
                symbol = mBlackMarkerSymbol;
            }

            Graphic graphic = new Graphic(pt, symbol);
            graphicsLayer.addGraphic(graphic);
            index++;
        }
    }
}

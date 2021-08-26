package com.augurit.agmobile.patrolcore.layerdownload;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Keep;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.augurit.agmobile.mapengine.common.GeographyInfoManager;
import com.augurit.am.fw.utils.VibratorUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.Unit;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * 图层查询地图操作监听
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.layerq.listener
 * @createTime 创建时间 ：2016-10-14 14:18
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2016-10-14 14:18
 *
 * @modifiBy 修改人：梁胜洪
 * @modifyTime 修改时间：2016-12-09 16:32
 * @modifyContent 修改内容：
 *                  增加点和线两种类型；
 *                  长按时，如果选中了面图形，设备会有振动反馈；暂不支持移动点和线;
 *                  增加了生成缓冲区的方法，移动时原图形和缓冲区一起移动
 */
@Keep
public class LayerQueryTouchListener extends MapOnTouchListener {

    private Context mContext;
    private MapView mMapView;
    private GraphicsLayer mGLayer;
    private Geometry mGeometry;     // 当前画出的图形
    private Geometry mBufferGeometry;
    private int mCurrentGraphicID;  // 当前Graphic的UID
    private int mKnobGraphicID;     // 缩放点UID
    private int mCirclePointGraphicID;   // 范围圆的中心点UID
    private int mBufferGraphicID;
    private Point mStartPoint;
    private Polyline mPolyline;
    private ArrayList<Point> mPoints = new ArrayList<>(); //多边形点的集合


    private OperationState mOperationState;

    public enum OperationState {
        STATE_NONE,         // 无状态
        STATE_GLOBAL,       //全图
        STATE_POINT,        //点
        STATE_LINE,         //线
        STATE_SQUARE,       //正方形
        STATE_POLYGON,      //多边形
        STATE_CIRCLE,       //圆
        STATE_CIRCLE_WITH_POINT,   //带中心点的圆，中心点固定，圆大小可改
        STATE_DRAW_OVER,     // 绘制完毕
        STATE_SCALE,         // 缩放图形
        STATE_MOVE            //移动图形
    }

    public LayerQueryTouchListener(Context context, MapView view, GraphicsLayer graphicsLayer) {
        super(context, view);
        mContext = context;
        mMapView = view;
        init(graphicsLayer);
    }

    private void init(GraphicsLayer graphicsLayer) {
        mGLayer = graphicsLayer;
        mOperationState = OperationState.STATE_NONE;
        mCurrentGraphicID = -1;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Point point = mMapView.toMapPoint(event.getX(), event.getY());
        switch (mOperationState) {
            case STATE_CIRCLE:
            case STATE_SQUARE:
                if (mStartPoint == null) {
                    mStartPoint = point;    // 设置圆与正方的中心点
                }
                break;
            default:
                break;
        }
        return super.onTouch(v, event);
    }

    @Override
    public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
        Point toPoint = mMapView.toMapPoint(to.getX(), to.getY());
        switch (mOperationState) {
            case STATE_CIRCLE:
                double radius = Math.sqrt(
                        Math.pow(mStartPoint.getX() - toPoint.getX(), 2)
                                + Math.pow(mStartPoint.getY() - toPoint.getY(), 2)); // 计算半径
                mGLayer.removeAll();
                addCircle(mStartPoint, radius);  // 画圆
                return true;
            case STATE_SQUARE:
                mGLayer.removeAll();
                addSquare(mStartPoint, toPoint); // 画正方形
                return true;
            case STATE_MOVE:
                if (moveGraphic(from, to) == null) {    // 拖动地图,或图形
                    return super.onDragPointerMove(from, to);
                } else {
                    return true;
                }
            case STATE_SCALE: //点选
                scaleCircle(from, to);  // 缩放范围圆
                return true;
            default:
                return super.onDragPointerMove(from, to);
        }
    }

    @Override
    public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
        Point toPoint = mMapView.toMapPoint(to.getX(), to.getY());
        switch (mOperationState) {
            case STATE_CIRCLE:
                double radius = Math.sqrt(
                        Math.pow(mStartPoint.getX() - toPoint.getX(), 2)
                                + Math.pow(mStartPoint.getY() - toPoint.getY(), 2));
                mGLayer.removeAll();
                mGeometry = addCircle(mStartPoint, radius); // 画圆完毕，记录Geometry
                mStartPoint = null;
                mOperationState = OperationState.STATE_DRAW_OVER;
                Toast.makeText(mContext, "长按移动图形位置", Toast.LENGTH_SHORT).show();
                return true;
            case STATE_SQUARE:
                mGLayer.removeAll();
                mGeometry = addSquare(mStartPoint, toPoint);
                mStartPoint = null;
                mOperationState = OperationState.STATE_DRAW_OVER;
                Toast.makeText(mContext, "长按移动图形位置", Toast.LENGTH_SHORT).show();
                return true;
            case STATE_MOVE:
                //说明绘制完毕
                mOperationState = OperationState.STATE_DRAW_OVER;
                Geometry geometry = moveGraphic(from, to);
                if (geometry == null) {
                    return super.onDragPointerUp(from, to);
                } else {
                    // 拖动完毕,记录Geometry
                    mGeometry = geometry;
                    return true;
                }
            case STATE_SCALE:
                Geometry geometry1 = scaleCircle(from, to);
                if (geometry1 == null) {
                    return super.onDragPointerUp(from, to);
                } else {
                    // 缩放完毕,记录Geometry
                    mGeometry = geometry1;
                    mOperationState = OperationState.STATE_DRAW_OVER;
                    Toast.makeText(mContext, "长按移动图形位置", Toast.LENGTH_SHORT).show();
                    return true;
                }
            default:
                return super.onDragPointerUp(from, to);
        }


    }

    @Override
    public boolean onSingleTap(MotionEvent point) {

        //是否有callout在显示，有的话隐藏
        if (mMapView.getCallout().isShowing()){
            mMapView.getCallout().hide();
        }

        Point mapPoint = mMapView.toMapPoint(point.getX(), point.getY());
        switch (mOperationState) {
            case STATE_POINT:
                mGLayer.removeAll();
                mGeometry = addPoint(mapPoint);
                return true;
            case STATE_POLYGON:
                mPoints.add(mapPoint);
                addLine(mapPoint);  // 添加或更新线
                return true;
            case STATE_LINE:
                mPoints.add(mapPoint);
                mGeometry = addLine(mapPoint);  // 添加或更新线
                return true;
            case STATE_CIRCLE_WITH_POINT:
                mGeometry = addCircleWithPoint(mapPoint);     // 画一个带中心点的圆
                mOperationState = OperationState.STATE_DRAW_OVER;
                return true;
            default:
                return super.onSingleTap(point);
        }
    }

    @Override
    public boolean onDoubleTap(MotionEvent point) {
        Point mapPoint = mMapView.toMapPoint(point.getX(), point.getY());
        switch (mOperationState) {
            case STATE_POLYGON:
                mPoints.add(mapPoint);
                mPoints.add(mPoints.get(0));//首尾相连
               // mGeometry = addLine(mapPoint);  // 结束画线
                mGLayer.removeAll();
                mGeometry = addPolygon();
                //mCurrentGraphicID = -1; // （以后要用到则不清除）
                mPolyline = null;
                mOperationState = OperationState.STATE_DRAW_OVER;
                Toast.makeText(mContext, "长按移动图形位置", Toast.LENGTH_SHORT).show();
                return true;
            default:
                break;
        }
        return super.onDoubleTap(point);
    }

    @Override
    public void onLongPress(MotionEvent point) {
        switch (mOperationState) {
            case STATE_DRAW_OVER:
                if (longPressKnob(point)) {
                    //使设备震动
                    VibratorUtil.getInstance(mContext).vibrate(200);
                    // 长按中了缩放按钮则进入缩放图形模式
                    mOperationState = OperationState.STATE_SCALE;
                    Toast.makeText(mContext, "拖动缩放范围", Toast.LENGTH_SHORT).show();
                }else {
                    //判断当前点击范围是否在所在图形范围内
                    int[] ids = mGLayer.getGraphicIDs(point.getX(), point.getY(), 20);
                    if (!ValidateUtil.isObjectNull(ids) && ids.length !=0){
                        //如果在范围内
                        //使设备震动
                        VibratorUtil.getInstance(mContext).vibrate(200);
                        mOperationState = OperationState.STATE_MOVE;
                    }
                }
                break;
            default:
                break;
        }
        super.onLongPress(point);
    }

    /**
     * 添加中心点及范围圆
     * @param center 点击位置
     * @return 圆形范围FilterGeometry
     */
    private Geometry addCircleWithPoint(Point center) {
        mGLayer.removeAll();
        // 绘制范围圆
        Polygon circle = drawCircle(center, 500);
        FillSymbol fillSymbol = new SimpleFillSymbol(Color.parseColor("#2196F3"));
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                2, SimpleLineSymbol.STYLE.SOLID);
        fillSymbol.setAlpha(20);
        fillSymbol.setOutline(lineSymbol);
        Graphic graphicCircle = new Graphic(circle, fillSymbol);
        mCurrentGraphicID = mGLayer.addGraphic(graphicCircle);  // 记录范围圆ID
        // 绘制中心点
        SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.BLUE, 5, SimpleMarkerSymbol.STYLE.CIRCLE);
        Graphic graphicPoint = new Graphic(center, sms);
        mCirclePointGraphicID = mGLayer.addGraphic(graphicPoint);   // 记录中心点ID
        // 绘制缩放点
        sms.setSize(10);
        Point knob = new Point(center.getX() + 500, center.getY());
        Graphic graphicKnob = new Graphic(knob, sms);
        mKnobGraphicID = mGLayer.addGraphic(graphicKnob);       // 记录缩放点ID
        return circle;
    }

    private Geometry addPoint(Point point){
        SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
        Graphic pointGraphic = new Graphic(point, simpleMarker);
        mCurrentGraphicID = mGLayer.addGraphic(pointGraphic);
        return point;
    }

    /**
     * 添加线
     * @param point 节点
     * @return 线Geometry
     */
    private Geometry addLine(Point point) {
        if (mPolyline == null) {   // 第一次添加
            mPolyline = new Polyline();
            mPolyline.startPath(point);
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                    2, SimpleLineSymbol.STYLE.SOLID);
            Graphic graphicLine = new Graphic(mPolyline, lineSymbol);
            mCurrentGraphicID = mGLayer.addGraphic(graphicLine);    // 记录线GraphicID
        } else {    // 更新
            mPolyline.lineTo(point);
            mGLayer.updateGraphic(mCurrentGraphicID, mPolyline);
        }
        // 添加节点
        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(Color.parseColor("#F50057"), 5, SimpleMarkerSymbol.STYLE.CIRCLE);
        Graphic graphicPoint = new Graphic(point, markerSymbol);
        mGLayer.addGraphic(graphicPoint);
        return mPolyline;
    }
    /**
     * 画多边形
     * @return 多边形geometry
     */
    private Geometry addPolygon() {
        mGLayer.removeAll();
        if(this.mPoints.size() != 0) {
            int index = 0;
            Polygon polygon = new Polygon();
            for(Iterator labelPointForPolygon = this.mPoints.iterator(); labelPointForPolygon.hasNext(); ++index) {
                Point screenPoint = (Point) labelPointForPolygon.next();
                //遍历画点
                if (index ==0){
                    polygon.startPath(screenPoint);
                }else {
                    polygon.lineTo(screenPoint);
                }
            }
            FillSymbol fillSymbol = new SimpleFillSymbol(Color.parseColor("#2196F3"));
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                    2, SimpleLineSymbol.STYLE.SOLID);
            fillSymbol.setAlpha(20);
            fillSymbol.setOutline(lineSymbol);
            mCurrentGraphicID = mGLayer.addGraphic(new Graphic(polygon, fillSymbol));
            //当绘制完成后要把点重新清空
            mPoints.clear();
            return polygon;
        }
        return null;
    }

    /**
     * 添加圆
     * @param center 圆心
     * @param radius 半径
     * @return 圆形范围
     */
    private Geometry addCircle(Point center, double radius) {
        mGLayer.removeAll();
        Polygon circle = drawCircle(center, radius);
        FillSymbol fillSymbol = new SimpleFillSymbol(Color.parseColor("#2196F3"));
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                2, SimpleLineSymbol.STYLE.SOLID);
        fillSymbol.setAlpha(20);
        fillSymbol.setOutline(lineSymbol);
        Graphic graphicCircle = new Graphic(circle, fillSymbol);
        mCurrentGraphicID = mGLayer.addGraphic(graphicCircle);
        return circle;
    }

    /**
     * 添加正方形
     * @param center 中心点
     * @param foot   另一点
     * @return 正方形范围
     */
    private Geometry addSquare(Point center, Point foot) {
        mGLayer.removeAll();
        Polygon square = drawSquare(center, foot);
        FillSymbol fillSymbol = new SimpleFillSymbol(Color.parseColor("#2196F3"));
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                2, SimpleLineSymbol.STYLE.SOLID);
        fillSymbol.setAlpha(20);
        fillSymbol.setOutline(lineSymbol);
        Graphic graphicSquare = new Graphic(square, fillSymbol);
        mCurrentGraphicID = mGLayer.addGraphic(graphicSquare);

        return square;
    }

    /**
     * 长按处是否为缩放点
     * @param event 长按操作
     * @return 是否为缩放点
     */
    private boolean longPressKnob(MotionEvent event) {
        // 获取点击处的Graphic
        int[] ids = mGLayer.getGraphicIDs(event.getX(), event.getY(), 20);
        if (ids.length != 0) {
            for (int id : ids) {
                if (mKnobGraphicID == id) {
                    // TODO 震一下
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 缩放范围圆
     * @param from 缩放前位置
     * @param to 缩放后位置
     * @return 缩放后的图形范围
     */
    private Geometry scaleCircle(MotionEvent from, MotionEvent to) {
        Polygon circle;
        // 计算差值(X方向)
        Point fromPoint = mMapView.toMapPoint(from.getX(), from.getY());
        Point toPoint = mMapView.toMapPoint(to.getX(), to.getY());
        double dx = toPoint.getX() - fromPoint.getX();
        // 移动缩放点
        Point knob = (Point) mGLayer.getGraphic(mKnobGraphicID).getGeometry();
        knob.setX(knob.getX() + dx);
        mGLayer.updateGraphic(mKnobGraphicID, knob);
        // 缩放范围圆
        Point center = (Point) mGLayer.getGraphic(mCirclePointGraphicID).getGeometry();
        double radius = Math.abs(knob.getX() - center.getX());
        circle = drawCircle(center, radius);
        mGLayer.updateGraphic(mCurrentGraphicID, circle);

        return circle;
    }

    /**
     * 移动Graphic
     * @param from 移动前
     * @param to   移动后
     * @return 移动的图形范围
     */
    private Geometry moveGraphic(MotionEvent from, MotionEvent to) {
        // 获取点击处的Graphic
        int[] ids = mGLayer.getGraphicIDs(from.getX(), from.getY(), 20);
        if (ids.length != 0) {
            // 计算差值
            Point fromPoint = mMapView.toMapPoint(from.getX(), from.getY());
            Point toPoint = mMapView.toMapPoint(to.getX(), to.getY());
            double dx = toPoint.getX() - fromPoint.getX();
            double dy = toPoint.getY() - fromPoint.getY();
            // 修改Graphic位置
            Graphic graphic = mGLayer.getGraphic(ids[0]);
            Geometry geometry = graphic.getGeometry();
            if (geometry.getType() == Geometry.Type.POLYGON) {  // 多边形
                //生成缓冲区后，此处的geometry为缓冲区的几何图形
                Polygon polygon = (Polygon) geometry;
                for (int i = 0; i < polygon.getPointCount(); i++) { // 修改每个点的位置
                    Point point = polygon.getPoint(i);
                    polygon.setPoint(i, new Point(point.getX() + dx, point.getY() + dy));
                }
                Polygon originalPolygon = null;
                if(mGeometry != null
                        && mCurrentGraphicID != -1
                        && mBufferGraphicID == ids[0]){
                    //由于在生成缓冲区后，选中的只能是缓冲区，所以在这里要一同把原图形也移动
                    originalPolygon = (Polygon) mGeometry;
                    for (int i = 0; i < originalPolygon.getPointCount(); i++) { // 修改每个点的位置
                        Point point = originalPolygon.getPoint(i);
                        originalPolygon.setPoint(i, new Point(point.getX() + dx, point.getY() + dy));
                    }
                }
                if (mCirclePointGraphicID != -1) {  // 如果存在中心点
                    Point centerPoint = (Point) mGLayer.getGraphic(mCirclePointGraphicID).getGeometry();
                    centerPoint.setX(centerPoint.getX() + dx);
                    centerPoint.setY(centerPoint.getY() + dy);
                    mGLayer.updateGraphic(mCirclePointGraphicID, centerPoint);
                }
                if (mKnobGraphicID != -1) { // 如果存在缩放点
                    Point knobPoint = (Point) mGLayer.getGraphic(mKnobGraphicID).getGeometry();
                    knobPoint.setX(knobPoint.getX() + dx);
                    knobPoint.setY(knobPoint.getY() + dy);
                    mGLayer.updateGraphic(mKnobGraphicID, knobPoint);
                }
                mGLayer.updateGraphic(ids[0], polygon); // 更新Graphic
                if(originalPolygon != null){
                    mGLayer.updateGraphic(mCurrentGraphicID, originalPolygon); // 更新Graphic
                    return originalPolygon;
                }
                return polygon;
            }
        }
        return null;
    }

    /**
     * 清除
     */
    public void clear() {
        if (mGLayer != null) {  // xjx 由于统计功能的引用，这里可能会空指针
            mGLayer.removeAll();
        }
        mPoints.clear();
        mPolyline = null;
        mStartPoint = null;
        mCurrentGraphicID = -1;
        mKnobGraphicID = -1;
        mCirclePointGraphicID = -1;
        mBufferGraphicID = -1;
        mGeometry = null;
        mBufferGeometry = null;
    }

    /**
     * 画正圆
     * @param center 中心点
     * @param radius 半径(米)
     * @return 圆形Polygon
     */
    private Polygon drawCircle(Point center, double radius) {
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
     * 画正方
     * @param center 中心点
     * @param foot   另一点
     * @return 正方形Polygon
     */
    private Polygon drawSquare(Point center, Point foot) {
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
        points[3] = new Point(center.getX() , center.getY() + dy);
        square.startPath(points[0]);
        for (int i = 1; i < points.length; i++) {
            square.lineTo(points[i]);
        }
        return square;
    }

    /**
     * 为已画的图形生成缓冲区，如果当前未画图形则返回null
     * @param radius 缓冲半径
     * @return 缓冲后的区域
     */
    public Geometry buffer(double radius){
        if(mGeometry == null
                || mCurrentGraphicID == -1){
            return null;
        }
        if(mBufferGraphicID != -1){
            mGLayer.removeGraphic(mBufferGraphicID);
            mBufferGeometry = null;
        }
        Graphic graphic = mGLayer.getGraphic(mCurrentGraphicID);
//        Geometry geometry = graphic.getGraphic();
        //生成缓冲区
        Geometry bufferGeometry = GeometryEngine.buffer(mGeometry, GeographyInfoManager.getInstance().getSpatialReference(),
                radius, Unit.create(LinearUnit.Code.METER));
        FillSymbol fillSymbol = new SimpleFillSymbol(Color.parseColor("#2196F3"));
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#aa0000"),
                2, SimpleLineSymbol.STYLE.SOLID);
        fillSymbol.setAlpha(20);
        fillSymbol.setOutline(lineSymbol);
        Graphic graphicBuffer = new Graphic(bufferGeometry, fillSymbol);
        mBufferGraphicID = mGLayer.addGraphic(graphicBuffer);
        mBufferGeometry = bufferGeometry;
        return bufferGeometry;
    }

    public OperationState getOperationState() {
        return mOperationState;
    }

    public void setOperationState(OperationState operationState) {
        mOperationState = operationState;
        clear();
    }

    public Geometry getGeometry() {
        return mGeometry;
    }

    public Geometry getBufferGeometry(){
        return mBufferGeometry;
    }
}

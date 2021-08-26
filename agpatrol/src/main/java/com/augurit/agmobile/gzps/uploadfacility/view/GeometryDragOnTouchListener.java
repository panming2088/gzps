package com.augurit.agmobile.gzps.uploadfacility.view;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.mapengine.common.constant.GraphicStyle;
import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.panorama.model.GraphicInfo;
import com.augurit.am.fw.utils.VibratorUtil;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;

import java.util.ArrayList;

/**
 * 在地图上进行圈选范围时的地图监听，包括：绘制圆，绘制正方形，长按移动Graphic等操作，同时，
 * 提供了一系列的setXXXListener方法，比如：setOnDoubleClickListener。
 * 绘制圆，正方形等方法已经整理成工具类放在：{@link GeometryUtil}下。
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.layerq.listener
 * @createTime 创建时间 ：2016-12-22
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-22
 */

public class GeometryDragOnTouchListener extends MapOnTouchListener {

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

   /************************各种监听****************************************************************/
    private OnTouchListener mOnTouchListener;
    private OnSingleTapListener mOnSingleTapListener;
    private OnDragPointerMoveListner mOnDragPointerMoveListner;
    private OnDragPointerUpListener mOnDragPointerUpListener;
    private OnDoubleClickListener mOnDoubleClickListener;

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

    public GeometryDragOnTouchListener(Context context, MapView view, GraphicsLayer graphicsLayer) {
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
        if (mOnTouchListener != null){
            mOnTouchListener.onTouch(v,event);
        }
        return super.onTouch(v, event);
    }

    @Override
    public boolean onPinchPointersDown(MotionEvent event) {
        hideCallout();
        return super.onPinchPointersDown(event);
    }

    @Override
    public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
        Point toPoint = mMapView.toMapPoint(to.getX(), to.getY());
        switch (mOperationState) {
            case STATE_CIRCLE:
                mGLayer.removeAll();
                double radius = Math.sqrt(
                        Math.pow(mStartPoint.getX() - toPoint.getX(), 2)
                                + Math.pow(mStartPoint.getY() - toPoint.getY(), 2)); // 计算半径

                GraphicInfo graphicInfo = GeometryUtil.addCircle(mStartPoint, radius, mGLayer);// 画圆
                mCurrentGraphicID = graphicInfo.getId();
                if (mOnDragPointerMoveListner != null){
                    mOnDragPointerMoveListner.onDragPointerMove(from,to);
                }
                return true;
            case STATE_SQUARE:
                mGLayer.removeAll();
                GraphicInfo graphicInfo1 = GeometryUtil.addSquare(mStartPoint, toPoint, mGLayer);// 画正方形
                mCurrentGraphicID = graphicInfo1.getId();
                if (mOnDragPointerMoveListner != null){
                    mOnDragPointerMoveListner.onDragPointerMove(from,to);
                }
                return true;
            case STATE_MOVE:
                if (moveGraphic(from, to) == null) {    // 拖动地图,或图形
                    if (mOnDragPointerMoveListner != null){
                        mOnDragPointerMoveListner.onDragPointerMove(from,to);
                    }
                    return super.onDragPointerMove(from, to);
                } else {
                    if (mOnDragPointerMoveListner != null){
                        mOnDragPointerMoveListner.onDragPointerMove(from,to);
                    }
                    return true;
                }
            case STATE_SCALE: //点选
                scaleCircle(from, to);  // 缩放范围圆
                if (mOnDragPointerMoveListner != null){
                    mOnDragPointerMoveListner.onDragPointerMove(from,to);
                }
                return true;
            default:
                if (mOnDragPointerMoveListner != null){
                    mOnDragPointerMoveListner.onDragPointerMove(from,to);
                }
                return super.onDragPointerMove(from, to);
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
                if (mOnSingleTapListener != null){
                    mOnSingleTapListener.onSingleTap(point);
                }
                return true;
            case STATE_POLYGON:
                mPoints.add(mapPoint);
                addLine(mapPoint);  // 添加或更新线
                if (mOnSingleTapListener != null){
                    mOnSingleTapListener.onSingleTap(point);
                }
                return true;
            case STATE_LINE:
                mPoints.add(mapPoint);
                mGeometry = addLine(mapPoint);  // 添加或更新线
                if (mOnSingleTapListener != null){
                    mOnSingleTapListener.onSingleTap(point);
                }
                return true;
            case STATE_CIRCLE_WITH_POINT:
                mGeometry = addCircleWithPoint(mapPoint);     // 画一个带中心点的圆
                mOperationState = OperationState.STATE_DRAW_OVER;
                if (mOnSingleTapListener != null){
                    mOnSingleTapListener.onSingleTap(point);
                }
                return true;
            default:
                if (mOnSingleTapListener != null){
                    mOnSingleTapListener.onSingleTap(point);
                }
                return super.onSingleTap(point);
        }
    }

    private Geometry addPoint(Point point){
        Symbol symbol = GraphicStyle.getCommonPointSymbol();
        Graphic pointGraphic = new Graphic(point, symbol);
        mCurrentGraphicID = mGLayer.addGraphic(pointGraphic);
        return point;
    }

    @Override
    public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
        Point toPoint = mMapView.toMapPoint(to.getX(), to.getY());
        switch (mOperationState) {
            case STATE_CIRCLE:
                double radius = Math.sqrt(
                        Math.pow(mStartPoint.getX() - toPoint.getX(), 2)
                                + Math.pow(mStartPoint.getY() - toPoint.getY(), 2));
                GraphicInfo graphicInfo = GeometryUtil.addCircle(mStartPoint, radius, mGLayer);// 画圆完毕，记录Geometry
                mGeometry = graphicInfo.getGeometry();
                mStartPoint = null;
                mOperationState = OperationState.STATE_DRAW_OVER;
               // Toast.makeText(mContext, "长按移动图形位置", Toast.LENGTH_SHORT).show();
                if (mOnDragPointerUpListener != null){
                    mOnDragPointerUpListener.onDragPointerUp(from,to);
                }
                return true;
            case STATE_SQUARE:
                GraphicInfo squareInfo =GeometryUtil.addSquare(mStartPoint, toPoint,mGLayer);
                mGeometry =  squareInfo.getGeometry();
                mStartPoint = null;
                mOperationState = OperationState.STATE_DRAW_OVER;
              //  Toast.makeText(mContext, "长按移动图形位置", Toast.LENGTH_SHORT).show();
                if (mOnDragPointerUpListener != null){
                    mOnDragPointerUpListener.onDragPointerUp(from,to);
                }
                return true;
            case STATE_MOVE:
                //说明绘制完毕
                mOperationState = OperationState.STATE_DRAW_OVER;
                Geometry geometry = moveGraphic(from, to);
                if(geometry instanceof Point){
                    showCallout((Point) geometry,to,null);
                }
                if (geometry == null) {
                    if (mOnDragPointerUpListener != null){
                        mOnDragPointerUpListener.onDragPointerUp(from,to);
                    }
                    return super.onDragPointerUp(from, to);
                } else {
                    // 拖动完毕,记录Geometry
                    mGeometry = geometry;
                    if (mOnDragPointerUpListener != null){
                        mOnDragPointerUpListener.onDragPointerUp(from,to);
                    }
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
                //    Toast.makeText(mContext, "长按移动图形位置", Toast.LENGTH_SHORT).show();
                    if (mOnDragPointerUpListener != null){
                        mOnDragPointerUpListener.onDragPointerUp(from,to);
                    }
                    return true;
                }
            default:
                return super.onDragPointerUp(from, to);
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
                GraphicInfo graphicInfo = GeometryUtil.addPolygon(mPoints, mGLayer);
                mGeometry =graphicInfo.getGeometry();
                mCurrentGraphicID =  graphicInfo.getId();
                mPoints.clear();
                //mCurrentGraphicID = -1; // （以后要用到则不清除）
                mPolyline = null;
                mOperationState = OperationState.STATE_DRAW_OVER;
             //   Toast.makeText(mContext, "长按移动图形位置", Toast.LENGTH_SHORT).show();
                if (mOnDoubleClickListener != null){
                    mOnDoubleClickListener.onDoubleClick(point);
                }
                return true;
            default:
                break;
        }
        return super.onDoubleTap(point);
    }


    @Override
    public void  onLongPress(MotionEvent point) {
        hideCallout();
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
     * 隐藏callout
     */
    private void hideCallout() {
        final Callout callout = mMapView.getCallout();
        if(callout != null && callout.isShowing()){
            callout.animatedHide();
        }
    }

    /**
     * 添加中心点及范围圆
     * @param center 点击位置
     * @return 圆形范围FilterGeometry
     */
    private Geometry addCircleWithPoint(Point center) {
        mGLayer.removeAll();
        // 绘制范围圆
        Polygon circle = GeometryUtil.drawCircle(center, 500);
        FillSymbol fillSymbol = GraphicStyle.getCommonPolygonSymbol();
        /*FillSymbol fillSymbol = new SimpleFillSymbol(Color.parseColor("#2196F3"));
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                2, SimpleLineSymbol.STYLE.SOLID);
        fillSymbol.setAlpha(20);
        fillSymbol.setOutline(lineSymbol);*/
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
            }else if(geometry.getType() == Geometry.Type.POINT){
                //生成缓冲区后，此处的geometry为缓冲区的几何图形
                Point point = (Point) geometry;
                point.setXY(point.getX() + dx, point.getY() + dy);
                mGLayer.updateGraphic(ids[0], point); // 更新Graphic
                return point;
            }
        }
        return null;
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
        circle = GeometryUtil.drawCircle(center, radius);
        mGLayer.updateGraphic(mCurrentGraphicID, circle);

        return circle;
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



    public OnTouchListener getOnTouchListener() {
        return mOnTouchListener;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    public OnSingleTapListener getOnSingleTapListener() {
        return mOnSingleTapListener;
    }

    public void setOnSingleTapListener(OnSingleTapListener onSingleTapListener) {
        mOnSingleTapListener = onSingleTapListener;
    }

    public OnDragPointerMoveListner getOnDragPointerMoveListner() {
        return mOnDragPointerMoveListner;
    }

    public void setOnDragPointerMoveListner(OnDragPointerMoveListner onDragPointerMoveListner) {
        mOnDragPointerMoveListner = onDragPointerMoveListner;
    }

    public OnDragPointerUpListener getOnDragPointerUpListener() {
        return mOnDragPointerUpListener;
    }

    public void setOnDragPointerUpListener(OnDragPointerUpListener onDragPointerUpListener) {
        mOnDragPointerUpListener = onDragPointerUpListener;
    }

    public OnDoubleClickListener getOnDoubleClickListener() {
        return mOnDoubleClickListener;
    }

    public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        mOnDoubleClickListener = onDoubleClickListener;
    }

    public interface OnTouchListener {
        void onTouch(View v, MotionEvent event);
    }

    public interface OnSingleTapListener {
        void onSingleTap(MotionEvent point);
    }


    public interface OnDragPointerUpListener {
        void onDragPointerUp(MotionEvent from, MotionEvent to);
    }

    public interface OnDragPointerMoveListner {
        void onDragPointerMove(MotionEvent from, MotionEvent to);
    }

    public interface OnDoubleClickListener {
        void onDoubleClick(MotionEvent point);
    }

    /**
     *  @param point                          callout显示的位置,可以为Null
     * @param to
     * @param calloutSureButtonClickListener "确定"按钮点击事件
     */
    public void showCallout(final Point point, final MotionEvent to, final View.OnClickListener calloutSureButtonClickListener) {

        final Point geometry = point;
        final Callout callout = mMapView.getCallout();
        View view = View.inflate(mContext,R.layout.drag_geometry_device, null);
        view.findViewById(R.id.btn_select_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mMapView.getCallout().isShowing()) {
                    mMapView.getCallout().animatedHide();
                }
                if (calloutSureButtonClickListener != null) {
                    calloutSureButtonClickListener.onClick(view);
                }
            }
        });
        view.findViewById(R.id.btn_select_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mMapView.getCallout().isShowing()) {
                    mMapView.getCallout().animatedHide();
                }
            }
        });
        callout.setStyle(com.augurit.agmobile.patrolcore.R.xml.editmap_callout_style);
        callout.setContent(view);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Point finalPoint = point;
                int[] ids = mGLayer.getGraphicIDs( to.getX(), to.getY(), 20);
                if (ids.length != 0) {
                    Graphic graphic = mGLayer.getGraphic(ids[0]);
                    Symbol symbol = graphic.getSymbol();
                    if(symbol instanceof PictureMarkerSymbol){
                        PictureMarkerSymbol markerSymbol = (PictureMarkerSymbol) symbol;
                        finalPoint = mMapView.toMapPoint((float) to.getX(), to.getY() + markerSymbol.getHeight() / 2 - 100);
                        finalPoint.setX(point.getX());
                    }
                }
//                if (finalPoint == null) {
//                    finalPoint = mMapView.toMapPoint(locationMarker.getX() + locationMarker.getWidth() / 2, locationMarker.getY() + locationMarker.getHeight() / 2 - 140);
//                }
                callout.show(finalPoint);
            }
        },800);

    }




}

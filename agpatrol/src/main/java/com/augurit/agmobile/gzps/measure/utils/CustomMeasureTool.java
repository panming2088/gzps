package com.augurit.agmobile.gzps.measure.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.measure.model.KilometerResult;
import com.augurit.agmobile.gzps.measure.model.KilometerquareResult;
import com.augurit.agmobile.gzps.measure.model.MeterResult;
import com.augurit.agmobile.gzps.measure.model.MetersquareResult;
import com.augurit.agmobile.mapengine.measure.model.IMeasureResult;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.android.map.CalloutPopupWindow;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.AreaUnit;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.Unit;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.TextSymbol;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 测距和测面的工具
 * <p>当开始测量时调用startDraw()方法，清除全部时调用clear()方法，撤销调用undo()方法，当结束时
 * 调用stopDraw()方法即可结束。除此之外，如果你想采用下拉框的方式让用户选择测量单位，采用setCurrentAreaUnit和setCurrentLinearUnit
 * 就可以将结果转换成不同的测量单位。如果想改变测量视图，比如测量的颜色，大小等，通过设置setLineSymbol，setMarkerSymbol，setFillSymbol等
 * 进行设置。详细的请参考sample。
 * </p>
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.defaultview.measure.utils
 * @createTime 创建时间 ：2017-02-13
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-13
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public class CustomMeasureTool implements OnSingleTapListener {
    private static final long serialVersionUID = 1L;

    private MapView mMap;
    private GraphicsLayer mLayer;
    private OnSingleTapListener mOldOnSingleTapListener;
    private View.OnTouchListener mOldOnTouchListener;
    private MarkerSymbol mMarkerSymbol;
    private LineSymbol mLineSymbol;
    private double mResult;
    private TextView mText;
    private MeasureType mMeasureMode;
    private Unit[] mLinearUnits;
    private Unit[] mDefaultLinearUnits;
    private Unit[] mAreaUnits;
    private Unit[] mDefaultAreaUnits;
    private Context mContext;
    private ArrayList<Point> mPoints;
    private FillSymbol mFillSymbol;
    private CalloutPopupWindow mCallout;
    private boolean isFinished = false;//判断是否退出了测量

    private Polyline mLine;
    private Polygon mPolygon;

    private LinearUnit mDefaultLienarUnit;
    private AreaUnit mDefaultAreaUnit;

    public CustomMeasureTool(MapView map) {
        this.mMeasureMode = MeasureType.LINEAR;
        this.mMap = map;
        this.mContext = this.mMap.getContext();
//        this.mMarkerSymbol = new SimpleMarkerSymbol(-65536, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),  R.mipmap.map_symbol_point4);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        this.mMarkerSymbol = new PictureMarkerSymbol(drawable);
//        this.mLineSymbol = new SimpleLineSymbol(-16777216, 3.0F);
        int lineColor = ResourcesCompat.getColor(mContext.getResources(), R.color.agmobile_blue, null);
        this.mLineSymbol = new SimpleLineSymbol(lineColor, 2.0F);
        this.mDefaultLinearUnits = new Unit[]{Unit.create(9001), Unit.create(9036), Unit.create(9002), Unit.create(9093)};
        this.mDefaultAreaUnits = new Unit[]{Unit.create(109404), Unit.create(109414), Unit.create(109405), Unit.create(109439)};
        this.mFillSymbol = new SimpleFillSymbol(Color.argb(150, 252, 252, 252));
        this.mFillSymbol.setOutline(new SimpleLineSymbol(0, 0.0F));
        this.mDefaultLienarUnit= new LinearUnit(LinearUnit.Code.METER);
        this.mDefaultAreaUnit= new AreaUnit(AreaUnit.Code.SQUARE_METER);
        this.mMeasureMode = MeasureType.LINEAR;
    }

    /**
     * 开始测量
     */
    public void startDraw() {
        //获取旧的点击事件，并进行保存，当退出的时候进行还原
        this.mOldOnSingleTapListener = this.mMap.getOnSingleTapListener();
        this.mMap.setOnSingleTapListener(this);
        this.mMap.setOnTouchListener(new MapOnTouchListener(mContext,mMap){

            @Override
            public boolean onSingleTap(MotionEvent point) {
                addPoint(point.getX(), point.getY());
                return true;
            }
        });
        this.mLayer = new GraphicsLayer();
        this.mMap.addLayer(this.mLayer);
        this.mPoints = new ArrayList();
        isFinished = false;
    }


    /**
     * 清除测量
     */
    public void clear() {
        this.mLayer.removeAll();
        this.mResult = 0.0D;
        if(mPoints != null) {
            this.mPoints.clear();
        }
        this.showResult();
        //this.updateMenu();
    }



    /**
     * 停止测量
     */
    public void stopDraw() {
        //如果已经退出了测量，那么就不用再进行回收
        if (!isFinished){
            this.hideCallout();
            if (mLayer != null && !mLayer.isRecycled()){
                this.mMap.removeLayer(this.mLayer);
            }
            this.mLayer = null;
            this.mMap.setOnSingleTapListener(this.mOldOnSingleTapListener);
            if(mPoints != null) {
                this.mPoints.clear();
            }
        }
        isFinished = true;
        mMeasureMode = MeasureType.LINEAR;  // 模式还原为测距
    }

    private void hideCallout() {
        if(this.mCallout != null && this.mCallout.isShowing()) {
            this.mCallout.hide();
        }

    }


    public void onSingleTap(float x, float y) {
        this.addPoint(x, y);
    }

    private void addPoint(float x, float y) {
        Point point = this.mMap.toMapPoint(x, y);
        this.mPoints.add(point);
        this.clearAndDraw();

        if (mPoints.size() == 1){
            if (mOnMeasureGraphicChangeListner != null){
                mOnMeasureGraphicChangeListner.onBeginDrawGraphic();
            }
        }
    }

    /**
     * 撤销，回到上一步
     */
    public void undo() {
        if (mPoints != null){
            if (mPoints.size() >0){
                this.mPoints.remove(this.mPoints.size() - 1);
                this.clearAndDraw();
                //如果移除后界面上的内容为空，隐藏工具条
                if(mPoints.size()== 0){
                    if (mOnMeasureGraphicChangeListner != null){
                        mOnMeasureGraphicChangeListner.onGraphicEmpty(); //界面没有Graphic
                    }
                }
            }
        }

    }

    private void clearAndDraw() {
        if(this.mLayer != null){
            int[] oldGraphics = this.mLayer.getGraphicIDs();
            this.draw();
            this.mLayer.removeGraphics(oldGraphics);
        }
       // this.updateMenu();
    }

    private void draw() {
        if(this.mPoints.size() != 0) {
            int index = 0;
            this.mResult = 0.0D;
            this.mLine = new Polyline();
            this.mPolygon = new Polygon();

            Point screenPoint;
            Polyline lastPolyline = new Polyline();
            for(Iterator labelPointForPolygon = this.mPoints.iterator(); labelPointForPolygon.hasNext(); ++index) {
                screenPoint = (Point)labelPointForPolygon.next();
                this.mLayer.addGraphic(new Graphic(screenPoint, this.mMarkerSymbol, 100));
                if(index == 0) {
                    this.mLine.startPath(screenPoint);
                    if(this.mMeasureMode == MeasureType.AREA) {
                        this.mPolygon.startPath(screenPoint);
                    }
                } else {
                    this.mLine.lineTo(screenPoint);
                    if(this.mMeasureMode == MeasureType.AREA) {
                        this.mPolygon.lineTo(screenPoint);
                    }
                }

                this.mLayer.addGraphic(new Graphic(this.mLine, this.mLineSymbol));
                lastPolyline.startPath(screenPoint);
            }

            Point var4;
            if(this.mMeasureMode == MeasureType.LINEAR) {
                this.mResult += GeometryEngine.geodesicLength(this.mLine, this.mMap.getSpatialReference(), getCurrentLinearUnit());
                var4 = this.mMap.toScreenPoint(this.mPoints.get(index - 1));
                this.showResult((float)var4.getX(), (float)var4.getY());
            } else if(this.mMeasureMode == MeasureType.AREA) {
                this.mLine.lineTo(this.mPoints.get(0));
                this.mLayer.addGraphic(new Graphic(this.mLine, this.mLineSymbol));
                //判断是否自相交
                boolean crosses = GeometryEngine.crosses(mLine, mPolygon, mMap.getSpatialReference());
                /*if (crosses){
                    //如果相交，进行提示
                    TextSymbol textSymbol = new TextSymbol(20,mContext.getString(R.string.the_area_has_crossed),Color.BLUE);
//                    textSymbol.setFontFamily("DroidSansFallback.ttf"); //加这句就可以解决乱码问题
                    Point point = mPoints.get(mPoints.size()-1);
                    Graphic graphic = new Graphic(point,textSymbol);
                    this.mLayer.addGraphic(graphic);
                }*/
                this.mPolygon.lineTo(this.mPoints.get(0));
                this.mLayer.addGraphic(new Graphic(this.mPolygon, this.mFillSymbol));
                this.mResult = GeometryEngine.geodesicArea(this.mPolygon, this.mMap.getSpatialReference(), getCurrentAreaUnit());
                var4 = GeometryEngine.getLabelPointForPolygon(this.mPolygon, this.mMap.getSpatialReference());
                screenPoint = this.mMap.toScreenPoint(var4);
                this.showResult((float)screenPoint.getX(), (float)screenPoint.getY());
            }
        }
    }



    private void showResult(float x, float y) {
        if(this.mResult > 0.0D) {
            if(this.mCallout == null) {
                this.mText = new TextView(this.mContext);
                this.mCallout = new CalloutPopupWindow(this.mText);
            }

            this.mText.setText(this.getResultString());
            try {
                this.mCallout.showCallout(this.mMap, this.mMap.toMapPoint(x, y), 0, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(this.mCallout != null && this.mCallout.isShowing()) {
            this.mCallout.hide();
        }

    }

    private void showResult() {
        if (mText != null){
            if(this.mResult > 0.0D) {
                this.mText.setText(this.getResultString());
                this.mCallout.showCallout(this.mMap);
            } else if(this.mCallout.isShowing()) {
                this.mCallout.hide();
            }
        }
    }
    /**
     * 得到当前测量的单位
     * @return 当前测量的单位
     */
    Unit getCurrentUnit() {
        return this.mMeasureMode == MeasureType.LINEAR?this.mDefaultLienarUnit:this.mDefaultAreaUnit;
    }

    /**
     * 设置线条样式
     * @param symbol 线条样式
     */
    public void setLineSymbol(LineSymbol symbol) {
        this.mLineSymbol = symbol;
    }

    /**
     * 设置线条两端样式
     * @param symbol 线条两端样式
     */
    public void setMarkerSymbol(MarkerSymbol symbol) {
        this.mMarkerSymbol = symbol;
    }

    /**
     * 设置面积填充样式
     * @param symbol 面积填充样式
     */
    public void setFillSymbol(FillSymbol symbol) {
        this.mFillSymbol = symbol;
    }


    private String getResultString() {

        IMeasureResult iMeasureResult = null;

       if (this.mMeasureMode == MeasureType.LINEAR){
           //如果是测线
           if (mResult / 1000 >= 1 ){
               //如果大于1千米，转成千米
               iMeasureResult = new KilometerResult();
           }else {
                iMeasureResult = new MeterResult();
           }
       }else {
           //侧面
           if (mResult / 100000 >= 1 ){
               //如果大于1平方公里
               iMeasureResult = new KilometerquareResult();
           }else {
               iMeasureResult = new MetersquareResult();
           }

       }
        return iMeasureResult.getResult(mResult);
    }

    /**
     * 测量方式，这里只有两种：测线和测面
     */
    public enum MeasureType {
        LINEAR,
        AREA;

        MeasureType() {
        }

        public static MeasureType getType(int i) {
            switch(i) {
                case 0:
                    return LINEAR;
                case 1:
                    return AREA;
                default:
                    return LINEAR;
            }
        }
    }


    /**
     * 得到当前测线的单位,常用的有以下选择:LinearUnit.Code.METER,LinearUnit.Code.CENTIMETER,LinearUnit.Code.KILOMETER,
     * 更多请查看类{@link LinearUnit.Code}
     * @return 当前测线的单位
     */
    public LinearUnit getCurrentLinearUnit(){
        return mDefaultLienarUnit;
    }

    /**
     * 得到当前侧面单位，常用的有以下选择:AreaUnit.Code.ACRE,AreaUnit.Code.ACRE_US,
     * 更多单位请查看{@link AreaUnit.Code}
     * @return 当前侧面单位
     */
    public AreaUnit getCurrentAreaUnit(){
        return mDefaultAreaUnit;
    }

    private void setCurrentMeasureType(MeasureType measureMode){
        this.mMeasureMode = measureMode;
        clear();
    }

    /**
     * 开启测线模式
     */
    public void measureLine(){
        setCurrentMeasureType(MeasureType.LINEAR);
    }

    /**
     * 开启测面模式
     */
    public void measureArea(){
        setCurrentMeasureType(MeasureType.AREA);
    }



    //TODO 未完成
    private Boolean mCanIntersect = true;
    /**
     * 设置是否允许自相交
     * @param canIntersect
     */
    public void enableIntersect(Boolean canIntersect){
       this.mCanIntersect= canIntersect;
    }


    private OnMeasureGraphicChangeListner mOnMeasureGraphicChangeListner;
    public void setOnGraphicChangeListener(OnMeasureGraphicChangeListner onMeasureGraphicChangeListner) {
        this.mOnMeasureGraphicChangeListner = onMeasureGraphicChangeListner;
    }

    public interface OnMeasureGraphicChangeListner{
        void onGraphicEmpty();  //当界面上已经没有Graphic的时候
        void onBeginDrawGraphic(); //当开始绘制Graphic的时候
    }
}
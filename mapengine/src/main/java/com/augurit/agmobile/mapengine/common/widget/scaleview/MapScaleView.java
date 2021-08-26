package com.augurit.agmobile.mapengine.common.widget.scaleview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.augurit.agmobile.mapengine.R;
import com.esri.android.map.MapView;


/**
 * 地图比例尺控件
 * Created by xcl on 2017/10/30.
 */
public class MapScaleView extends View {

    private Context context;
    private int scaleWidth;
    private int scaleHeight;
    private int textColor;
    private String text;
    private int textSize;
    private int scaleSpaceText;
    private Paint mPaint;
    private MapView mapView;

    public MapScaleView(Context context) {
        this(context, null);
        this.context = context;
        this.initVariables();
    }

    public MapScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
        this.initVariables();
    }

    public MapScaleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this.initVariables();
    }

    private void initVariables() {
        scaleWidth = 104;//
        scaleHeight = 35;//比比例尺宽度例尺高度
        textColor = Color.BLACK;//比例尺字体颜色
        text = "20公里";//比例尺文本
        textSize = 32;//比例尺宽度
        scaleSpaceText = 8;//比例尺文本与图形的间隔高度
        mPaint = new Paint();//画笔
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = getWidthSize(widthMeasureSpec);
        int heightSize = getHeightSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 测量ScaleView的宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private int getWidthSize(int widthMeasureSpec) {
        return View.MeasureSpec.getSize(widthMeasureSpec);
    }

    /**
     * 测量ScaleView的高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int getHeightSize(int heightMeasureSpec) {
        int mode = View.MeasureSpec.getMode(heightMeasureSpec);
        int height = 0;
        switch (mode) {
            case View.MeasureSpec.AT_MOST:
                height = textSize + scaleSpaceText + scaleHeight;
                break;
            case MeasureSpec.EXACTLY: {
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
            }
            case MeasureSpec.UNSPECIFIED: {
                height = Math.max(textSize + scaleSpaceText + scaleHeight, MeasureSpec.getSize(heightMeasureSpec));
                break;
            }
        }

        return height;
    }


    /**
     * 绘制上面的文字和下面的比例尺，因为比例尺是.9.png，我们需要利用drawNinepath方法绘制比例尺
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = scaleWidth;
        mPaint.setColor(textColor);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        float textWidth = mPaint.measureText(text);
        canvas.drawText(text, (width - textWidth) / 2, textSize, mPaint);
        Rect scaleRect = new Rect(0, textSize + scaleSpaceText, width, textSize + scaleSpaceText + scaleHeight);
        drawNinepath(canvas, R.drawable.ic_scale, scaleRect);
    }

    private void drawNinepath(Canvas canvas, int resId, Rect rect) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resId);
        NinePatch patch = new NinePatch(bmp, bmp.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
    }

    public void setScale(double scale){
        refresh(scale/ 100);//结果单位米，表示图上1厘米代表*米
    }

    /**
     * 根据缩放级别更新ScaleView的文字以及比例尺的长度
     */
    public void refreshScaleView() {
        if (mapView == null) {
            throw new NullPointerException("you can call setMapView(MapView mapView) at first");
        }
        double scale = this.mapView.getScale() / 100;//结果单位米，表示图上1厘米代表*米
        refresh(scale);
    }

    private void refresh(double scale) {
        double ppi = getPPIOfDevice();

        if (scale > 0 && scale <= 0.5) {
            refreshView(scale, ppi, 0.5);
        } else if (scale > 0.5 && scale <= 1) {
            refreshView(scale, ppi, 1);
        } else if (scale > 1 && scale <= 2) {
            refreshView(scale, ppi, 2);
        } else if (scale > 2 && scale <= 3) {
            refreshView(scale, ppi, 3);
        } else if (scale > 3 && scale <= 4) {
            refreshView(scale, ppi, 4);
        } else if (scale > 4 && scale <= 5) {
            refreshView(scale, ppi, 5);
        } else if (scale > 5 && scale <= 10) {//换算20米
            refreshView(scale, ppi, 10);
        } else if (scale > 10 && scale <= 20) {//换算20米
            refreshView(scale, ppi, 20);
        } else if (scale > 20 && scale <= 50) {//换算50米
            String unit = "50米";
            int scaleWidth = (int) (50 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 50 && scale <= 100) {//换算20米
            String unit = "100米";
            int scaleWidth = (int) (100 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 100 && scale <= 200) {//换算20米
            String unit = "200米";
            int scaleWidth = (int) (200 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 200 && scale <= 500) {//换算20米
            String unit = "500米";
            int scaleWidth = (int) (500 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 500 && scale <= 1000) {//换算20米
            String unit = "1公里";
            int scaleWidth = (int) (1000 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 1000 && scale <= 2000) {//换算20米
            String unit = "2公里";
            int scaleWidth = (int) (2000 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 2000 && scale <= 5000) {//换算20米
            String unit = "5公里";
            int scaleWidth = (int) (5000 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 5000 && scale <= 10000) {//换算20米
            String unit = "10公里";
            int scaleWidth = (int) (10000 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 10000 && scale <= 20000) {//换算20米
            String unit = "20公里";
            int scaleWidth = (int) (20000 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 20000 && scale <= 25000) {//换算20米
            String unit = "25公里";
            int scaleWidth = (int) (25000 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 25000 && scale <= 50000) {//换算20米
            String unit = "50公里";
            int scaleWidth = (int) (50000 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 50000 && scale <= 100000) {//换算20米
            String unit = "100公里";
            int scaleWidth = (int) (100000 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 100000 && scale <= 200000) {//换算20米
            String unit = "200公里";
            int scaleWidth = (int) (200000 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 200000 && scale <= 250000) {//换算20米
            String unit = "250公里";
            int scaleWidth = (int) (250000 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 250000 && scale <= 500000) {//换算20米
            String unit = "500公里";
            int scaleWidth = (int) (500000 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        } else if (scale > 500000 && scale <= 1000000) {//换算20米
            String unit = "1000公里";
            int scaleWidth = (int) (1000000 * ppi / 2.54 / scale);
            setText(unit);//更新文字
            setScaleWidth(scaleWidth);//更新比例尺长度
        }

        invalidate();
    }

    private void refreshView(double scale, double ppi, double metersPerCm) {
        int scaleWidth = (int) (metersPerCm * ppi / 2.54 / scale);// 20/scale = 20米在 上是多少厘米：ppi为每英尺像素数，ppi/2.54为1厘米的像素数
        String unit = null;
        if (metersPerCm < 1){
            unit = metersPerCm + "米";
        }else{
            unit = (int)metersPerCm + "米";
        }
        setText(unit);//更新文字
        setScaleWidth(scaleWidth);//更新比例尺长度
    }

    public void setScaleWidth(int scaleWidth) {
        this.scaleWidth = scaleWidth;
    }

    public void setText(String unit) {
        this.text = unit;
    }

    private double getPPIOfDevice() {
        Point point = new Point();
        Activity activity = (Activity) context;
        activity.getWindowManager().getDefaultDisplay().getRealSize(point);//获取屏幕的真实分辨率
        DisplayMetrics dm = getResources().getDisplayMetrics();
        double x = Math.pow(point.x / dm.xdpi, 2);//
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Double ppi = Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2)) / screenInches;
        return ppi;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }
}

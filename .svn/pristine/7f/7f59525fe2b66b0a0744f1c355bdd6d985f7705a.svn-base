package com.augurit.agmobile.mapengine.marker.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.augurit.agmobile.mapengine.R;
import com.augurit.agmobile.mapengine.layermanage.dao.LocalLayerStorageDao;
import com.augurit.agmobile.mapengine.marker.dao.LocalDatabaseMarkDao;
import com.augurit.agmobile.mapengine.marker.model.Attachment;
import com.augurit.agmobile.mapengine.marker.model.LocalMark;
import com.augurit.agmobile.mapengine.marker.model.Mark;
import com.augurit.agmobile.mapengine.marker.model.MarkStyle;
import com.augurit.agmobile.mapengine.marker.model.PointStyle;

import com.augurit.am.fw.utils.ColorUtil;
import com.augurit.am.fw.utils.ResourceUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.augurit.am.fw.utils.log.LogUtil;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个工具类的作用：
 * （1）用来根据featurelayer返回的字段生成对应的symbol
 * （2）用来获取点标注/线标注/面标注对应的featurelayer
 * （3）根据定义好的上传规则生成对应的map
 *
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.mark.util
 * @createTime 创建时间 ：2016-12-11
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-11
 */

public class MarkUtil {


    public static BitmapDrawable getPointDrawable(Context context, String styleStr) {

        styleStr = getPointDrawableName(context, styleStr);
        if (styleStr == null) {
            return null;
        }
        Resources resources = context.getResources();
        //TODO 这里要加上找不到资源的判断
        int mipMapId = ResourceUtil.getMipMapId(context, styleStr);
        Bitmap bitmap = BitmapFactory.decodeResource(resources, mipMapId);
        //int resId =  resources.getIdentifier(picName, "mipmap", context.getPackageName());
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    private static String getPointDrawableName(Context context, String styleStr) {
        if (styleStr == null) {
            styleStr = MarkConstant.POINT_STYLE.DEFALUT_POINT_STYLE;
        } else {
            if (styleStr.equals("null")) {
                styleStr = MarkConstant.POINT_STYLE.DEFALUT_POINT_STYLE;
            } else if (styleStr.contains("1") || styleStr.contains("0") || styleStr.contains("#")) {
                styleStr = MarkConstant.POINT_STYLE.DEFALUT_POINT_STYLE;
            }
        }
        //如果存在大写，统一替换成小写,在mipmap中存放的名称都是小写
        styleStr = styleStr.toLowerCase();
        return styleStr;
    }

    public static Drawable getPointDrawable(Context context, Map<String, Object> attr) {
        String styleStr = (String) attr.get(MarkConstant.GUANGZHOU.POINT_STYLE);
        if (styleStr == null) {
            return null;
        } else {
            if (styleStr.equals("null")) {
                styleStr = MarkConstant.POINT_STYLE.DEFALUT_POINT_STYLE;
            } else if (styleStr.contains("1") || styleStr.contains("0") || styleStr.contains("#")) {
                styleStr = MarkConstant.POINT_STYLE.DEFALUT_POINT_STYLE;
            }
            //如果存在大写，统一替换成小写,在mipmap中存放的名称都是小写
            styleStr = styleStr.toLowerCase();
            Resources resources = context.getResources();
            int mipMapId = ResourceUtil.getMipMapId(context, styleStr);
            //int resId =  resources.getIdentifier(picName, "mipmap", context.getPackageName());
            Drawable drawable = resources.getDrawable(mipMapId);
            return drawable;
        }
    }

    public static MarkerSymbol getPointSymbol(Context context, Map<String, Object> attr) {
        MarkerSymbol symbol = null;
        String styleStr = (String) attr.get(MarkConstant.GUANGZHOU.POINT_STYLE);
        BitmapDrawable drawable = getPointDrawable(context, styleStr);
     /*    styleStr = getPointDrawableName(context,styleStr);
        Resources resources = context.getResources();
        int mipMapId = ResourceUtil.getMipMapId(context, styleStr);
        //int resId =  resources.getIdentifier(picName, "mipmap", context.getPackageName());
        Drawable drawable = resources.getDrawable(mipMapId);*/
        if (drawable == null) {
            symbol = new SimpleMarkerSymbol(Color.RED, 20, SimpleMarkerSymbol.STYLE.DIAMOND);
        } else {
            symbol = new PictureMarkerSymbol(context, drawable);
        }
        return symbol;
    }

    public static MarkerSymbol getPointSymbolFromLocalMark(Context context, LocalMark localMark) {
        MarkerSymbol symbol = null;
        String styleStr = localMark.getPointDrawableName();
        BitmapDrawable drawable = getPointDrawable(context, styleStr);
        if (drawable == null) {
            symbol = new SimpleMarkerSymbol(Color.RED, 20, SimpleMarkerSymbol.STYLE.DIAMOND);
        } else {
            symbol = new PictureMarkerSymbol(context, drawable);
        }
        return symbol;
    }


    public static LineSymbol getLineSymbol(Map<String, Object> attrs) {
        LineSymbol symbol = null;
        String polylineColor = (String) attrs.get(MarkConstant.GUANGZHOU.LINE_COLOR); //获取到线的颜色
        String lineWidthStr = attrs.get(MarkConstant.GUANGZHOU.LINE_WIDTH).toString();
        Integer lineWidth = (int) Float.valueOf(lineWidthStr).floatValue();
//        Integer lineWidth = (Integer) attrs.get(MarkConstant.GUANGZHOU.LINE_WIDTH); //获取到线的宽度

        if (polylineColor == null || !polylineColor.contains("#") || polylineColor.equals("#0")) {
            polylineColor = MarkConstant.DEFAULT_LINE_COLOR;
          /*  symbol = new SimpleLineSymbol(Color.RED,
                    7, SimpleLineSymbol.STYLE.SOLID);*/
        }
        if (lineWidth == null) {
            lineWidth = MarkConstant.DEFAULT_LINE_WIDTH;
        }
        int color = Color.parseColor(polylineColor);
        symbol = new SimpleLineSymbol(color, lineWidth);
        return symbol;
    }

    public static LineSymbol getLineSymbolFromLocalMark(LocalMark localMark) {
        LineSymbol symbol = null;
        String polylineColor = localMark.getLineColor(); //获取到线的颜色
        Integer lineWidth = localMark.getLineWidth(); //获取到线的宽度

        if (polylineColor == null || !polylineColor.contains("#") || polylineColor.equals("#0")) {
            polylineColor = MarkConstant.DEFAULT_LINE_COLOR;
          /*  symbol = new SimpleLineSymbol(Color.RED,
                    7, SimpleLineSymbol.STYLE.SOLID);*/
        }
        if (lineWidth == null) {
            lineWidth = MarkConstant.DEFAULT_LINE_WIDTH;
        }
        int color = Color.parseColor(polylineColor);
        symbol = new SimpleLineSymbol(color, lineWidth);
        return symbol;
    }


    public static FillSymbol getPolygonSymbol(Map<String, Object> attrs) {
        FillSymbol symbol = null;
        String lineColor = (String) attrs.get(MarkConstant.GUANGZHOU.POLYGON_LINECOLOR);
        String inColor = (String) attrs.get(MarkConstant.GUANGZHOU.POLYGON_FILLCOLOR);

        if (lineColor == null || inColor == null || inColor.contains("*")
                || !lineColor.contains("#")
                || !inColor.contains("#")) { //过滤掉不正常的情况
            FillSymbol fillSymbol = new SimpleFillSymbol(Color.RED);
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                    2, SimpleLineSymbol.STYLE.SOLID);
            fillSymbol.setAlpha(60);
            fillSymbol.setOutline(lineSymbol);
            symbol = fillSymbol;
        } else {
            int newInColor = 0;
            try {
                newInColor = Color.parseColor(inColor);
            } catch (Exception e) {
                newInColor = ColorUtil.getColorInt("#e12727");
                LogUtil.d("解析面标注的颜色出错，颜色是：" + inColor);
            }

            int newLineColor = 0;
            try {
                newLineColor = Color.parseColor(lineColor);
            } catch (Exception e) {
                newLineColor = ColorUtil.getColorInt("#116de6");
                LogUtil.d("解析面标注的颜色出错,颜色是：" + lineColor);
            }

            //  int newLineColor = Color.parseColor(lineColor);
            FillSymbol fillSymbol = new SimpleFillSymbol(newInColor);
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(newLineColor,
                    2, SimpleLineSymbol.STYLE.SOLID);
            fillSymbol.setOutline(lineSymbol);
            symbol = fillSymbol;
        }
        return symbol;
    }

    public static FillSymbol getPolygonSymbolFromLocalMark(LocalMark localMark) {
        FillSymbol symbol = null;
        String lineColor = localMark.getLineColor();
        String inColor = localMark.getInColor();

        if (lineColor == null || inColor == null || inColor.contains("*")
                || !lineColor.contains("#")
                || !inColor.contains("#")) { //过滤掉不正常的情况
            FillSymbol fillSymbol = new SimpleFillSymbol(Color.RED);
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                    2, SimpleLineSymbol.STYLE.SOLID);
            fillSymbol.setAlpha(60);
            fillSymbol.setOutline(lineSymbol);
            symbol = fillSymbol;
        } else {
            int newInColor = 0;
            try {
                newInColor = Color.parseColor(inColor);
            } catch (Exception e) {
                newInColor = ColorUtil.getColorInt("#e12727");
                LogUtil.d("解析面标注的颜色出错，颜色是：" + inColor);
            }

            int newLineColor = 0;
            try {
                newLineColor = Color.parseColor(lineColor);
            } catch (Exception e) {
                newLineColor = ColorUtil.getColorInt("#116de6");
                LogUtil.d("解析面标注的颜色出错,颜色是：" + lineColor);
            }

            //  int newLineColor = Color.parseColor(lineColor);
            FillSymbol fillSymbol = new SimpleFillSymbol(newInColor);
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(newLineColor,
                    2, SimpleLineSymbol.STYLE.SOLID);
            fillSymbol.setOutline(lineSymbol);
            symbol = fillSymbol;
        }
        return symbol;
    }

    public static List<Mark> completeMarkInfo(Context context, FeatureSet featureSet, ArcGISFeatureLayer pointLayer) {
        List<Mark> marks = new ArrayList<>();
        for (Graphic graphic : featureSet.getGraphics()) {
            Mark mark = completeMark(context, pointLayer, graphic);
            if (mark == null) continue;
            if (graphic.getGeometry().getType() == Geometry.Type.POINT) {
                //如果是点，另外传入drawable名称
                String styleStr = (String) graphic.getAttributes().get(MarkConstant.GUANGZHOU.POINT_STYLE);
                mark.setPointDrawableName(getPointDrawableName(context, styleStr));
            }
            marks.add(mark);
        }
        return marks;
    }

    @Nullable
    public static Mark completeMark(Context context, ArcGISFeatureLayer pointLayer, Graphic graphic) {
        String name = (String) graphic.getAttributes().get(MarkConstant.GUANGZHOU.MARK_NAME_KEY);
        String memo = (String) graphic.getAttributes().get(MarkConstant.GUANGZHOU.MARK_MEMO_KEY);
        Integer id = (Integer) graphic.getAttributes().get(pointLayer.getObjectIdField());
        Long createTime = (Long) graphic.getAttributes().get(MarkConstant.GUANGZHOU.MARK_UPLOAD_DATE);
        Symbol symbol = null;
        String pointDrawableName = null;
        if (graphic.getGeometry() == null) {
            LogUtil.e("标注的geometry为空！！！！");
            return null;
        }
        switch (graphic.getGeometry().getType()) {
            case POLYGON:
                symbol = MarkUtil.getPolygonSymbol(graphic.getAttributes());
                break;
            case POLYLINE:
                symbol = getLineSymbol(graphic.getAttributes());
                break;
            case POINT:
                symbol = getPointSymbol(context, graphic.getAttributes());
                pointDrawableName = (String) graphic.getAttributes().get(MarkConstant.GUANGZHOU.POINT_STYLE);
        }
        Mark mark = new Mark();
        mark.setId(id);
        mark.setMarkName(name);
        mark.setMarkMemo(memo);
        mark.setCreateDate(createTime);
        mark.setGeometry(graphic.getGeometry());
        mark.setSymbol(symbol);
        if (pointDrawableName != null) {
            mark.setPointDrawableName(pointDrawableName);
        }
        return mark;
    }


    public static final ArcGISFeatureLayer getFeatureLayer(Context context, Geometry geometry) {

        ArcGISFeatureLayer featureLayer = null;
        String url = null;
        LocalLayerStorageDao layerDataManager = new LocalLayerStorageDao();
        switch (geometry.getType()) {
            case POINT:
                url = layerDataManager.getMarkPointFeatureLayerUrl(context);
                // featureLayer = new ArcGISFeatureLayer(pointUrl, ArcGISFeatureLayer.MODE.SNAPSHOT);
                break;
            case POLYLINE:
                url = layerDataManager.getMarkLineFeatureLayerUrl(context);
                //    featureLayer = new ArcGISFeatureLayer(lineUrl, ArcGISFeatureLayer.MODE.SNAPSHOT);
                break;
            case POLYGON:
                url = layerDataManager.getMarkPolygonFeatureLayerUrl(context);
                //    featureLayer = new ArcGISFeatureLayer(polygonUrl, ArcGISFeatureLayer.MODE.SNAPSHOT);
                break;
        }
        featureLayer = new ArcGISFeatureLayer(url, ArcGISFeatureLayer.MODE.SNAPSHOT);
        return featureLayer;
    }

    public static final ArcGISFeatureLayer getFeatureLayer(Context context, Geometry.Type type) {

        ArcGISFeatureLayer featureLayer = null;
        String url = null;
        LocalLayerStorageDao layerDataManager = new LocalLayerStorageDao();
        switch (type) {
            case POINT:
                url = layerDataManager.getMarkPointFeatureLayerUrl(context);
                // featureLayer = new ArcGISFeatureLayer(pointUrl, ArcGISFeatureLayer.MODE.SNAPSHOT);
                break;
            case POLYLINE:
                url = layerDataManager.getMarkLineFeatureLayerUrl(context);
                //    featureLayer = new ArcGISFeatureLayer(lineUrl, ArcGISFeatureLayer.MODE.SNAPSHOT);
                break;
            case POLYGON:
                url = layerDataManager.getMarkPolygonFeatureLayerUrl(context);
                //    featureLayer = new ArcGISFeatureLayer(polygonUrl, ArcGISFeatureLayer.MODE.SNAPSHOT);
                break;
        }
        featureLayer = new ArcGISFeatureLayer(url, ArcGISFeatureLayer.MODE.SNAPSHOT);
        return featureLayer;
    }


    public static final Map<String, Object> provideEditPointAttribute(ArcGISFeatureLayer featureLayer,
                                                                      int id,
                                                                      String markName, String markDemo,
                                                                      String styleName, Long currentDate) {
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(featureLayer.getObjectIdField(), id);
        attr.put(MarkConstant.GUANGZHOU.MARK_NAME_KEY, markName);
        attr.put(MarkConstant.GUANGZHOU.MARK_MEMO_KEY, markDemo); //备注
        attr.put(MarkConstant.GUANGZHOU.MARK_UPLOAD_DATE, currentDate);
        attr.put(MarkConstant.GUANGZHOU.POINT_STYLE, styleName);
        return attr;
    }

    public static final Map<String, Object> provideEditPointAttribute(Context context,
                                                                      int id,
                                                                      String markName, String markDemo,
                                                                      String styleName, Long currentDate) {
        Map<String, Object> attr = new HashMap<String, Object>();
        ArcGISFeatureLayer featureLayer = getFeatureLayer(context, Geometry.Type.POINT);
        attr.put(featureLayer.getObjectIdField(), id);
        attr.put(MarkConstant.GUANGZHOU.MARK_NAME_KEY, markName);
        attr.put(MarkConstant.GUANGZHOU.MARK_MEMO_KEY, markDemo); //备注
        attr.put(MarkConstant.GUANGZHOU.MARK_UPLOAD_DATE, currentDate);
        attr.put(MarkConstant.GUANGZHOU.POINT_STYLE, styleName);
        return attr;
    }

    public static final Map<String, Object> provideAddPointAttribute(
            String markName, String markDemo,
            String styleName, Long currentDate) {
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(MarkConstant.GUANGZHOU.MARK_NAME_KEY, markName);
        attr.put(MarkConstant.GUANGZHOU.MARK_MEMO_KEY, markDemo); //备注
        attr.put(MarkConstant.GUANGZHOU.MARK_UPLOAD_DATE, currentDate);
        attr.put(MarkConstant.GUANGZHOU.POINT_STYLE, styleName);
        return attr;
    }

    /**
     * 提供删除一个标注所需要提供的属性
     *
     * @param featureLayer
     * @param id
     * @return
     */
    public static final Map<String, Object> provideDeleteAttribute(ArcGISFeatureLayer featureLayer,
                                                                   int id) {
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(featureLayer.getObjectIdField(), id);
        return attr;
    }


    public static final Mark provideMark(Geometry geometry, Symbol symbol,
                                         int id,
                                         String markName,
                                         String markDemo,
                                         String styleName,
                                         Long currentDate,
                                         List<Attachment> attachments) {
        Mark mark = new Mark();
        mark.setId(id);
        mark.setPointDrawableName(styleName);
        mark.setGeometry(geometry);
        mark.setMarkMemo(markDemo);
        mark.setMarkName(markName);
        mark.setCreateDate(currentDate);
        mark.setSymbol(symbol);
        mark.setAttachments(attachments);
        return mark;
    }

    /**
     * 返回点的样式字符串（这个要跟ios那边协商，两边现在约定好的规则是上传图片的名称上去），默认的点标注的图片名称是:marker_redpin
     *
     * @param pointStyle
     * @return
     */
    public static final String getPointStyleStr(PointStyle pointStyle) {
        return pointStyle.getName();
    }

    /**
     * 返回默认的点标注的样式字符串（这个要跟ios那边协商，两边现在约定好的规则是上传图片的名称上去），默认的点标注的图片名称是:marker_redpin
     *
     * @return
     */
    public static final String getDefaultStyleStr() {
        return MarkConstant.POINT_STYLE.DEFALUT_POINT_STYLE;
    }

    /**
     * 返回默认的点标注图标
     *
     * @param context
     * @return
     */
    public static final Drawable getDefaultPointMarkerPicture(Context context) {
        return context.getResources().getDrawable(R.mipmap.marker_redpin);
    }

    public static final Map<String, Object> provideLineAttribute(
            ArcGISFeatureLayer featureLayer,
            String markName,
            String markMemo,
            int lineColor,
            int width,
            Long currentTimeMillis,
            int id
    ) {
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(MarkConstant.GUANGZHOU.MARK_NAME_KEY, markName);
        attr.put(MarkConstant.GUANGZHOU.MARK_MEMO_KEY, markMemo); //备注
        attr.put(MarkConstant.GUANGZHOU.LINE_COLOR, "#" + Integer.toHexString(lineColor));  //线条颜色
        attr.put(MarkConstant.GUANGZHOU.LINE_WIDTH, width);//线条宽度
        attr.put(MarkConstant.GUANGZHOU.MARK_UPLOAD_DATE, currentTimeMillis);
        attr.put(featureLayer.getObjectIdField(), id);
        return attr;
    }

    public static final Map<String, Object> provideLineAttribute(
            Context context,
            String markName,
            String markMemo,
            int lineColor,
            int width,
            Long currentTimeMillis,
            int id
    ) {
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(MarkConstant.GUANGZHOU.MARK_NAME_KEY, markName);
        attr.put(MarkConstant.GUANGZHOU.MARK_MEMO_KEY, markMemo); //备注
        attr.put(MarkConstant.GUANGZHOU.LINE_COLOR, "#" + Integer.toHexString(lineColor));  //线条颜色
        attr.put(MarkConstant.GUANGZHOU.LINE_WIDTH, width);//线条宽度
        attr.put(MarkConstant.GUANGZHOU.MARK_UPLOAD_DATE, currentTimeMillis);
        ArcGISFeatureLayer featureLayer = getFeatureLayer(context, Geometry.Type.LINE);
        attr.put(featureLayer.getObjectIdField(), id);
        return attr;
    }

    public static final Map<String, Object> provideAddLineAttribute(String markName,
                                                                    String markMemo,
                                                                    int lineColor,
                                                                    int width,
                                                                    Long currentTimeMillis

    ) {
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(MarkConstant.GUANGZHOU.MARK_NAME_KEY, markName);
        attr.put(MarkConstant.GUANGZHOU.MARK_MEMO_KEY, markMemo); //备注
        attr.put(MarkConstant.GUANGZHOU.LINE_COLOR, "#" + Integer.toHexString(lineColor));  //线条颜色
        attr.put(MarkConstant.GUANGZHOU.LINE_WIDTH, width);//线条宽度
        attr.put(MarkConstant.GUANGZHOU.MARK_UPLOAD_DATE, currentTimeMillis);
        return attr;
    }


    public static final Map<String, Object> provideEditPolygonAttribute(ArcGISFeatureLayer featureLayer,
                                                                        String markName,
                                                                        String markMemo,
                                                                        int lineColor,
                                                                        int inColor,
                                                                        Long currentTimeMillis,
                                                                        int id) {
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(featureLayer.getObjectIdField(), id);
        attr.put(MarkConstant.GUANGZHOU.MARK_NAME_KEY, markName);
        attr.put(MarkConstant.GUANGZHOU.MARK_MEMO_KEY, markMemo); //备注
        attr.put(MarkConstant.GUANGZHOU.POLYGON_LINECOLOR, "#" + Integer.toHexString(lineColor));  //线条颜色
        attr.put(MarkConstant.GUANGZHOU.POLYGON_FILLCOLOR, "#" + Integer.toHexString(inColor));//填充颜色
        attr.put(MarkConstant.GUANGZHOU.MARK_UPLOAD_DATE, currentTimeMillis);
        return attr;
    }

    public static final Map<String, Object> provideEditPolygonAttribute(Context context,
                                                                        String markName,
                                                                        String markMemo,
                                                                        int lineColor,
                                                                        int inColor,
                                                                        Long currentTimeMillis,
                                                                        int id) {
        Map<String, Object> attr = new HashMap<String, Object>();
        ArcGISFeatureLayer featureLayer = getFeatureLayer(context, Geometry.Type.POLYGON);
        attr.put(featureLayer.getObjectIdField(), id);
        attr.put(MarkConstant.GUANGZHOU.MARK_NAME_KEY, markName);
        attr.put(MarkConstant.GUANGZHOU.MARK_MEMO_KEY, markMemo); //备注
        attr.put(MarkConstant.GUANGZHOU.POLYGON_LINECOLOR, "#" + Integer.toHexString(lineColor));  //线条颜色
        attr.put(MarkConstant.GUANGZHOU.POLYGON_FILLCOLOR, "#" + Integer.toHexString(inColor));//填充颜色
        attr.put(MarkConstant.GUANGZHOU.MARK_UPLOAD_DATE, currentTimeMillis);
        return attr;
    }

    public static final Map<String, Object> provideAddPolygonAttribute(String markName,
                                                                       String markMemo,
                                                                       int lineColor,
                                                                       int inColor,
                                                                       Long currentTimeMillis) {
        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(MarkConstant.GUANGZHOU.MARK_NAME_KEY, markName);
        attr.put(MarkConstant.GUANGZHOU.MARK_MEMO_KEY, markMemo); //备注
        attr.put(MarkConstant.GUANGZHOU.POLYGON_LINECOLOR, "#" + Integer.toHexString(lineColor));  //线条颜色
        attr.put(MarkConstant.GUANGZHOU.POLYGON_FILLCOLOR, "#" + Integer.toHexString(inColor));//填充颜色
        attr.put(MarkConstant.GUANGZHOU.MARK_UPLOAD_DATE, currentTimeMillis);
        return attr;
    }


    /**
     * 提供默认的标注
     *
     * @param context
     * @param geometry
     * @return
     */
    public static Mark createDefaultMark(Context context, Geometry geometry) {
        Mark mark = new Mark();
        switch (geometry.getType()) {
            case POINT:
                mark.setMarkName("新增点");
                mark.setMarkMemo("备注");
                mark.setGeometry(geometry);
                mark.setPointDrawableName(MarkConstant.POINT_STYLE.DEFALUT_POINT_STYLE);
                return mark;
            case POLYLINE:
                mark.setMarkName("新增标注");
                mark.setMarkMemo("备注");
                mark.setGeometry(geometry);
                LineSymbol lineSymbol = new SimpleLineSymbol(context.getResources().getColor(R.color.mark_red), 7);
                mark.setSymbol(lineSymbol);
                return mark;
            case POLYGON:
                mark.setMarkName("新增标注");
                mark.setMarkMemo("备注");
                mark.setGeometry(geometry);
                FillSymbol fillSymbol = new SimpleFillSymbol(context.getResources().getColor(R.color.mark_blue));
                LineSymbol polygonLineSymbol = new SimpleLineSymbol(context.getResources().getColor(R.color.mark_light_yellow), 7);
                fillSymbol.setOutline(polygonLineSymbol);
                mark.setSymbol(fillSymbol);
                return mark;
        }
        return mark;
    }


    /**
     * 从标注中获取到它的点样式
     *
     * @param mark
     * @return
     */
    public static PointStyle getPointStyleFromMark(Mark mark, List<PointStyle> pointStyles) {
        for (PointStyle pointStyle : pointStyles) {
            if (pointStyle.getName().equals(mark.getPointDrawableName())) {
                return pointStyle;
            }
        }
        return null;
    }


    public static Attachment getAttachment(String path) {
        Attachment attachment = new Attachment();
        attachment.setFileName(getFileNameFromFilePath(path));
        attachment.setFilePath(path);
        Date date = new Date(System.currentTimeMillis());
        String createdDate = TimeUtil.getStringTimeYYMMDD(date);
        attachment.setCreateDate(createdDate);
        attachment.setAttachmentId(MarkConstant.UNUPLOAD_ATTACHMENT_ID);
        return attachment;
    }

    private static String getFileNameFromFilePath(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    public static Map<String, Object> getAttributeFromMark(Mark mark) {
        switch (mark.getGeometry().getType()) {
            case POINT:
                return provideAddPointAttribute(mark.getMarkName(), mark.getMarkMemo(), mark.getPointDrawableName(), System.currentTimeMillis());
            case LINE:
            case POLYLINE:
                return provideAddLineAttribute(mark.getMarkName(), mark.getMarkMemo(), ((LineSymbol) mark.getSymbol()).getColor(),
                        (int) ((LineSymbol) mark.getSymbol()).getWidth(), System.currentTimeMillis());
            case POLYGON:
                return provideAddPolygonAttribute(mark.getMarkName(),
                        mark.getMarkMemo(),
                        ((FillSymbol) mark.getSymbol()).getOutline().getColor(),
                        ((FillSymbol) mark.getSymbol()).getColor(),
                        System.currentTimeMillis());
        }
        return null;
    }


}

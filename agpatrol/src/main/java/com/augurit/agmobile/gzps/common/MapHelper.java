package com.augurit.agmobile.gzps.common;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;

import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.identification
 * @createTime 创建时间 ：17/11/9
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/9
 * @modifyMemo 修改备注：
 */

public class MapHelper {

    private MapView mapView;
    private Context mContext;
    private List<Integer> mapGraphicIds = new ArrayList<>();
    private OnGraphicClickListener onGraphicClickListener;
    GraphicsLayer mGLayer = null;

    /**
     * 将位置和地图上的点联系起来
     */
    private Map<Integer, Integer> idToPositionMap = new LinkedHashMap<>();

    public MapHelper(Context context, MapView mapView) {
        this.mapView = mapView;
        this.mContext = context;
    }

    public MapHelper(Context context, MapView mapView,GraphicsLayer graphicsLayer) {
        this.mapView = mapView;
        this.mContext = context;
        this.mGLayer = graphicsLayer;
    }

    private void initGLayer() {
        if (mGLayer == null) {
            mGLayer = new GraphicsLayer();
            mGLayer.setSelectionColor(Color.BLUE);
            mapView.addLayer(mGLayer);
        }
    }

    public void setOnClickGraphicListener(OnGraphicClickListener onGraphicClickListener) {
        this.onGraphicClickListener = onGraphicClickListener;
    }

    public void drawPoints(List<Geometry> geometryList, boolean ifRemoveAll) {

        initGLayer();

        if (ifRemoveAll) {
            mGLayer.removeAll();
            mapGraphicIds.clear();
            idToPositionMap.clear();
        }

        for (int i = 0; i < geometryList.size(); i++) {
            Geometry geometry = geometryList.get(i);
            int id = drawGeometry(geometry, mGLayer, false, false);
            mapGraphicIds.add(id);
            idToPositionMap.put(id, i);
        }
    }


    public int[] getGraphicId(float v, float v2, int tolerant) {
        if (mGLayer != null) {
            return mGLayer.getGraphicIDs(v, v2, tolerant);
        }
        return null;
    }

    public int getposition(int graphicId) {
        return idToPositionMap.get(graphicId);
    }

    public void drawGeometry(Geometry geometry, boolean ifRemoveAll) {

        initGLayer();

        if (ifRemoveAll) {
            mGLayer.removeAll();
            mapGraphicIds.clear();
        }

        int i = drawGeometry(geometry, mGLayer, false, false);
        mapGraphicIds.add(i);
    }

    public int drawOldPoints(Geometry geometry, boolean ifRemoveAll) {

        initGLayer();

        if (ifRemoveAll) {
            mGLayer.removeAll();
            mapGraphicIds.clear();
        }

        int i = drawOldGeometry(geometry, mGLayer, false, false);
        mapGraphicIds.add(i);
        return i;
    }


    public void highLightGraphic(int position, boolean ifCenter) {

        if (mGLayer == null || position >= mapGraphicIds.size()) {
            return;
        }

        mGLayer.clearSelection();
        if (mapGraphicIds.get(position) != -1) {
            mGLayer.setSelectedGraphics(new int[]{mapGraphicIds.get(position)}, true);

            if (ifCenter) {
                Geometry geometry = mGLayer.getGraphic(mapGraphicIds.get(position)).getGeometry();
                mapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
            }
        }

    }


    /**
     * 在地图上画图形
     *
     * @param graphicsLayer
     * @param ifRemoveAll   在添加前是否移除掉所有
     * @param geometry
     */
    private int drawGeometry(Geometry geometry, GraphicsLayer graphicsLayer, boolean ifRemoveAll, boolean ifCenter) {

        if (graphicsLayer == null) {
            return -1;
        }

        Symbol symbol = null;

        switch (geometry.getType()) {
            case LINE:
            case POLYLINE:
                symbol = new SimpleLineSymbol(Color.RED, 7);
                break;
            case POINT:
//                symbol = new SimpleMarkerSymbol(Color.RED, 25, SimpleMarkerSymbol.STYLE.CIRCLE);
                Point point = (Point) geometry;
                if (point.getX() != 0 && point.getY() != 0) {
                    symbol = getPointSymbol();
                }
                break;
            default:
                break;
        }

        if (ifRemoveAll) {
            graphicsLayer.removeAll();
        }

        if (ifCenter) {
            mapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        }

        if (symbol != null) {
            Graphic graphic = new Graphic(geometry, symbol);
            return graphicsLayer.addGraphic(graphic);
        }

        return -1;
    }


    public void removeGeometry(int id) {
        if (mGLayer != null && id != -1) {
            mGLayer.removeGraphic(id);
        }
    }


    public void removeLayer() {
        if (mGLayer != null && mapView != null) {
            try {
                mGLayer.removeAll();
                mapView.removeLayer(mGLayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void removeAllGraphic() {
        if (mGLayer != null) {
            try {
                mGLayer.removeAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 在地图上画原位置的图形
     *
     * @param graphicsLayer
     * @param ifRemoveAll   在添加前是否移除掉所有
     * @param geometry
     */
    private int drawOldGeometry(Geometry geometry, GraphicsLayer graphicsLayer, boolean ifRemoveAll, boolean ifCenter) {

        if (graphicsLayer == null) {
            return -1;
        }

        Symbol symbol = null;

        switch (geometry.getType()) {
            case LINE:
            case POLYLINE:
                symbol = new SimpleLineSymbol(Color.RED, 7);
                break;
            case POINT:
//                symbol = new SimpleMarkerSymbol(Color.RED, 25, SimpleMarkerSymbol.STYLE.CIRCLE);
                Point point = (Point) geometry;
                if (point.getX() != 0 && point.getY() != 0) {
                    symbol = getOldPointSymbol();
                }
                break;
            default:
                break;
        }

        if (ifRemoveAll) {
            graphicsLayer.removeAll();
        }

        if (ifCenter) {
            mapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
        }

        if (symbol != null) {
            Graphic graphic = new Graphic(geometry, symbol);
            return graphicsLayer.addGraphic(graphic);
        }

        return -1;
    }

    @NonNull
    private Symbol getPointSymbol() {
        Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), com.augurit.agmobile.patrolcore.R.mipmap.ic_select_location, null);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext, drawable);// xjx 改为兼容api19的方式获取drawable
        pictureMarkerSymbol.setOffsetY(16);
        return pictureMarkerSymbol;
    }

    @NonNull
    private Symbol getOldPointSymbol() {
        Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), com.augurit.agmobile.patrolcore.R.drawable.old_map_point, null);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mContext, drawable);
        pictureMarkerSymbol.setOffsetY(16);
        return pictureMarkerSymbol;    // xjx 改为兼容api19的方式获取drawable
    }

    public interface OnGraphicClickListener {
        void onClick(int position);
    }
}

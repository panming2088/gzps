package com.augurit.agmobile.patrolcore.editmap;


import android.content.Context;
import android.graphics.Color;

import com.augurit.agmobile.mapengine.common.utils.GeometryUtil;
import com.augurit.agmobile.mapengine.map.Map;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;

import static com.augurit.agmobile.patrolcore.common.table.TableViewManager.geometry;

/**
 * 阅读管线状态
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.editmap
 * @createTime 创建时间 ：17/11/1
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/1
 * @modifyMemo 修改备注：
 */

public class EditLineReadStateMapListener implements OnSingleTapListener {


    private MapView mapView;
    private Context mContext;
    private Polyline polyline;
    private GraphicsLayer mGLayer;
    private double initScale;

    public EditLineReadStateMapListener(MapView mapView, Context context, Polyline polyline,double initScale) {

        this.mapView = mapView;
        this.mContext = context;
        this.polyline = polyline;
        this.initScale = initScale;
        drawPolyLine();
    }

    private void drawPolyLine() {
        initGLayer();
        drawGeometry(geometry, mGLayer, false, true);
    }

    private void initGLayer() {
        if (mGLayer == null) {
            mGLayer = new GraphicsLayer();
            mapView.addLayer(mGLayer);
        }
    }

    /**
     * 在地图上画图形
     *
     * @param graphicsLayer
     * @param ifRemoveAll   在添加前是否移除掉所有
     * @param geometry
     */
    private void drawGeometry(Geometry geometry, GraphicsLayer graphicsLayer, boolean ifRemoveAll, boolean ifCenter) {

        if (graphicsLayer == null) {
            return;
        }

        Symbol symbol = null;
        switch (geometry.getType()) {
            case LINE:
            case POLYLINE:
                symbol = new SimpleLineSymbol(Color.RED, 5);
                break;
            case POINT:
                symbol = new SimpleMarkerSymbol(Color.RED, 15, SimpleMarkerSymbol.STYLE.CIRCLE);
                break;
            default:
                break;
        }

        if (ifRemoveAll) {
            graphicsLayer.removeAll();
        }

        if (symbol != null) {
            Graphic graphic = new Graphic(geometry, symbol);
            graphicsLayer.addGraphic(graphic);
        }

        if (ifCenter) {
            mapView.centerAt(GeometryUtil.getGeometryCenter(geometry), true);
            if (initScale != 0 && initScale != -1) {
                mapView.setScale(initScale);
            }
        }
    }

    @Override
    public void onSingleTap(float v, float v1) {

    }
}

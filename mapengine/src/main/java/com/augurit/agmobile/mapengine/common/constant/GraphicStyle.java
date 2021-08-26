package com.augurit.agmobile.mapengine.common.constant;

import android.graphics.Color;

import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.comn.util
 * @createTime 创建时间 ：2016-12-21
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-21
 */

public final class GraphicStyle {

    private GraphicStyle() {
    }


    public static LineSymbol getCommonLineSymbol() {
        return null;
    }

    public static MarkerSymbol getCommonPointSymbol() {
        SimpleMarkerSymbol simpleMarker = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
        return simpleMarker;
    }

    public static FillSymbol getCommonPolygonSymbol() {
        FillSymbol fillSymbol = new SimpleFillSymbol(Color.parseColor("#2196F3"));
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                2, SimpleLineSymbol.STYLE.SOLID);
        fillSymbol.setAlpha(20);
        fillSymbol.setOutline(lineSymbol);
        return fillSymbol;
    }
}

package com.augurit.agmobile.mapengine.common;

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
        return new SimpleLineSymbol(Color.BLUE, 7, SimpleLineSymbol.STYLE.SOLID);
    }

    public static MarkerSymbol getCommonPointSymbol() {
        return new SimpleMarkerSymbol(Color.BLUE, 10, SimpleMarkerSymbol.STYLE.CIRCLE);
    }

    public static FillSymbol getCommonPolygonSymbol() {
       /* FillSymbol fillSymbol = new SimpleFillSymbol(Color.parseColor("#2196F3"));
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.parseColor("#2196F3"),
                2, SimpleLineSymbol.STYLE.SOLID);
        fillSymbol.setAlpha(20);
        fillSymbol.setOutline(lineSymbol);
        return fillSymbol;*/
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.BLUE,
                4, SimpleLineSymbol.STYLE.SOLID);
        SimpleFillSymbol fillSymbol = new SimpleFillSymbol(Color.GREEN);
        fillSymbol.setOutline(lineSymbol);
        fillSymbol.setAlpha(20);
        return fillSymbol;
    }
}

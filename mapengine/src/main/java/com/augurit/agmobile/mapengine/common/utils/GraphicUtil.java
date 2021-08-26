package com.augurit.agmobile.mapengine.common.utils;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;

import com.augurit.agmobile.mapengine.R;
import com.esri.core.geometry.Geometry;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.common.utils
 * @createTime 创建时间 ：17/11/3
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/3
 * @modifyMemo 修改备注：
 */

public class GraphicUtil {

    public static Symbol getSymbol(Context context, Geometry geometry){

        if (geometry == null || context == null){
            return null;
        }

        Symbol symbol = null;
        switch (geometry.getType()){
            case POLYLINE:
            case LINE:
                symbol = new SimpleLineSymbol(Color.RED,7);
                break;
            case POINT:
                symbol = new PictureMarkerSymbol(context, ResourcesCompat.getDrawable(context.getResources(), R.mipmap.ic_select_location,null));
                break;
            case POLYGON:
                SimpleLineSymbol simpleLineSymbol = new SimpleLineSymbol(Color.RED,7);
                symbol = new SimpleFillSymbol(Color.YELLOW);
                ((SimpleFillSymbol)symbol).setOutline(simpleLineSymbol);
                break;
                default:
                    break;
        }
        return symbol;
    }
}

package com.esri.android.toolkit.map;

/**
 * Created by liangsh on 2016-11-10.
 */
public class AMSketchLayer extends SketchLayer {

    public AMSketchLayer(MapViewHelper mapHelper){
        super(mapHelper);
    }

    public int addPolygonGraphic(double[][] latlon, String title, String snippet, int resID, int fillColor, int strokeColor, float width, int zorder) {
        return super.addPolygonGraphic(latlon, title, snippet, resID, fillColor, strokeColor, width, zorder);
    }
}

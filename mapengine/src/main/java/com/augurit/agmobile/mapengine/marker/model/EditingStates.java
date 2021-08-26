package com.augurit.agmobile.mapengine.marker.model;

import com.esri.core.geometry.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名：com.augurit.am.map.arcgis.mark.model
 * 文件描述：
 * 创建人：xuciluan
 * 创建时间：2016-09-28 14:02
 * 修改人：xuciluan
 * 修改时间：2016-09-28 14:02
 * 修改备注：
 */

public class EditingStates {
    public ArrayList<Point> points = new ArrayList<Point>();

    public boolean midPointSelected = false;

    public  boolean vertexSelected = false;

    public int insertingIndex;

    public EditingStates(List<Point> points, boolean midpointselected,
                         boolean vertexselected, int insertingindex) {
        this.points.addAll(points);
        this.midPointSelected = midpointselected;
        this.vertexSelected = vertexselected;
        this.insertingIndex = insertingindex;
    }
}

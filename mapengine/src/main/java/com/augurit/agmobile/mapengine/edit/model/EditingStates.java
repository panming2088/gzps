package com.augurit.agmobile.mapengine.edit.model;

import com.esri.core.geometry.Point;

import java.util.ArrayList;

/**
 *
 * 编辑操作实体类,按步记录点的增加 移动和删除操作,以便操作撤回和恢复
 * Created by guokunhu on 16/9/19.
 */
public class EditingStates {
    public ArrayList<Point> points = new ArrayList<Point>();

    public boolean midPointSelected = false;

    public boolean vertexSelected = false;

    public int insertingIndex;

    public EditingStates(ArrayList<Point> points, boolean midpointselected, boolean vertexselected, int insertingindex) {
        this.points.addAll(points);
        this.midPointSelected = midpointselected;
        this.vertexSelected = vertexselected;
        this.insertingIndex = insertingindex;
    }
}
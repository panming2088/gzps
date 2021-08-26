package com.augurit.agmobile.mapengine.common.utils.wktutil.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by liangsh on 2016-12-01.
 */
public class MultiIPoint {
    private List<Double[]> points;
    private HashMap<String, Integer> spatialReference;

    public List<Double[]> getPoints() {
        return points;
    }

    public void setPoints(List<Double[]> points) {
        this.points = points;
    }

    public HashMap<String, Integer> getSpatialReference() {
        return spatialReference;
    }

    public void setSpatialReference(HashMap<String, Integer> spatialReference) {
        this.spatialReference = spatialReference;
    }
}

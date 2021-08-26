package com.augurit.agmobile.mapengine.layerimport.view;

import com.esri.android.map.FeatureLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Feature;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by liangsh on 2016-11-01.
 */
public interface OnFeatureClickedListener {
    void onClick(Map<FeatureLayer, ArrayList<Feature>> clickedFeatureMap, Point clickPoint);
}

package com.augurit.agmobile.mapengine.layerimport.view;

import android.content.Context;
import android.view.MotionEvent;

import com.esri.android.map.Callout;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.map.Feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangsh on 2016-10-31.
 */
public class LayerImportMapOnTouchListener extends MapOnTouchListener {
    private Context mContext;
    private MapView mMapView;
    private OnFeatureClickedListener onFeatureClickedListener;

    private ArrayList<Long> mLayerIDList;

    public LayerImportMapOnTouchListener(Context context, MapView mapView, OnFeatureClickedListener onFeatureClickedListener){
        super(context, mapView);
        mContext = context;
        mMapView = mapView;
        this.onFeatureClickedListener = onFeatureClickedListener;
        mLayerIDList = new ArrayList<>();
    }

    @Override
    public boolean onSingleTap(MotionEvent e) {
        Callout callout = mMapView.getCallout();
        if(callout.isShowing()){
            callout.animatedHide();
            return true;
        }
        Map<FeatureLayer, ArrayList<Feature>> clickedFeatureMap = new HashMap<>();
        for(long lid : mLayerIDList){
            ArrayList<Feature> clickedFeature = new ArrayList<>();
            FeatureLayer featureLayer = (FeatureLayer) mMapView.getLayerByID(lid);
            featureLayer.clearSelection();
            long[] featureIDs = featureLayer.getFeatureIDs(e.getX(), e.getY(), 25);
            for(long id : featureIDs){
                Feature feature = featureLayer.getFeature(id);
                clickedFeature.add(feature);
//                featureLayer.selectFeature(id);
            }
            if(featureIDs.length>0){
                clickedFeatureMap.put(featureLayer, clickedFeature);
            }
        }
        if(onFeatureClickedListener != null) {
            onFeatureClickedListener.onClick(clickedFeatureMap, mMapView.toMapPoint(e.getX(), e.getY()));
        }
        return true;
    }

    public void addLayerID(long layerID){
        mLayerIDList.add(layerID);
    }

    public void removeLayerID(long layerID){
        Long id = -1l;
        for(Long i : mLayerIDList){
            if(i == layerID){
                id = i;
                break;
            }
        }
        mLayerIDList.remove(id);
    }

    public void clearLayer(){
        mLayerIDList.clear();
    }
}

package com.augurit.agmobile.mapengine.panorama.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 创建人 ： guokunhu
 * @package 包名：com.augurit.agmobile.mapengine.panorama.service
 * @createTime 创建时间 ：17/1/18
 * @modifyBy 修改人 ： guokunhu
 * @modifyTime 修改时间 17/1/18
 */

public class PanoListDataService implements IPanoListDataService{
    private Handler handler;
    private Callback callback;
    private List<Graphic> results;
    private int SUCCESS = 1;
    private int FAIL = 2;
    private Context context;

    public PanoListDataService(Context context){
        this.context = context;

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == SUCCESS) {
                    callback.onSuccess(results);
                } else if (msg.what == FAIL) {
                    callback.onFail();
                }
                super.handleMessage(msg);
            }
        };

    }

    @Override
    public void getPanoListData(MapView mapView, ArcGISFeatureLayer featureLayer, Callback _callback) {
        this.callback = _callback;
        Query query = new Query();
        query.setOutFields(new String[]{"*"});
        query.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        //onGraphicSelected.setGeometry(mapView.getMaxExtent());
        query.setWhere("objectId < 100000");
        query.setInSpatialReference(mapView.getSpatialReference());
        //调用API去查找当前点要素
        featureLayer.selectFeatures(query, ArcGISFeatureLayer.SELECTION_METHOD.NEW, new CallbackListener<FeatureSet>() {
            @Override
            public void onCallback(FeatureSet queryResults) {
                Log.d("PanoDataManager", "select successfully !");
                if (queryResults.getGraphics().length >= 0) {
                    results = new ArrayList<Graphic>(Arrays.asList(queryResults.getGraphics()));
                    handler.sendEmptyMessage(SUCCESS);

                }
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("PanoDataManage", "Select Features Error" + throwable.getLocalizedMessage());
                handler.sendEmptyMessage(FAIL);

            }
        });
    }
}

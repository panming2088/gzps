package com.augurit.agmobile.gzps.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.augurit.agmobile.gzps.BaseActivity;

import com.augurit.agmobile.mapengine.common.agmobilelayer.AGMyTileLayer;
import com.augurit.agmobile.mapengine.common.agmobilelayer.AGTiledMapSeriviceLayer;
import com.augurit.agmobile.mapengine.common.utils.FilePathUtil;
import com.augurit.agmobile.mapengine.layermanage.util.EncryptTileCacheHelper;
import com.augurit.agmobile.mapengine.layermanage.util.NonEncryptTileCacheHelper;
import com.augurit.am.fw.utils.AMFileUtil;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.ags.MapServiceInfo;
import com.esri.core.geometry.Point;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import java.io.File;

/**
 * Created by xcl on 2017/11/15.
 */

public class TestActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MapView mapView = new MapView(this);
        setContentView(mapView);

//        MoreLevelTdtLayer tdtLayer = new MoreLevelTdtLayer(123111,
//                "http://t0.tianditu.com/vec_c/wmts",6,
//                new NonEncryptTileCacheHelper(TestActivity.this.getApplicationContext(), "123111"),
//                true);
//        mapView.addLayer(tdtLayer);
//
//        AGTiledMapSeriviceLayer agTiledMapSeriviceLayer3 = new AGTiledMapSeriviceLayer(11112,
//                "http://139.159.247.149:6080/arcgis/rest/services/SW1028/swjfwd1/MapServer",
//                new EncryptTileCacheHelper(TestActivity.this, "11112"));
//        mapView.addLayer(agTiledMapSeriviceLayer3);
//                AGMyTileLayer agTiledMapSeriviceLayer = new AGMyTileLayer(11113,
//                "http://139.159.243.230:6080/arcgis/re st/services/ditu/image1/MapServer",
//                new EncryptTileCacheHelper(TestActivity.this, "11113"));
//        mapView.addLayer(agTiledMapSeriviceLayer);
        AGMyTileLayer agTiledMapSeriviceLayer = new AGMyTileLayer( 1500,
                "http://139.159.243.230:6080/arcgis/rest/services/ditu/img/MapServer",
                new NonEncryptTileCacheHelper(TestActivity.this, "1500"),
                false);
        try {
            String mapServiceInfoJson = AMFileUtil.readUtf8(
                    new File(new FilePathUtil(getApplicationContext()).getSavePath() + "/MapServiceInfo.txt"));
            JsonParser jsonParser = new JsonFactory().createJsonParser(mapServiceInfoJson);
            MapServiceInfo mapServiceInfo = MapServiceInfo.fromJson(jsonParser, "http://139.159.243.230:6080/arcgis/rest/services/ditu/img/MapServer");
            agTiledMapSeriviceLayer.setMapServiceInfo(mapServiceInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        agTiledMapSeriviceLayer.initLayer();
//        mapView.addLayer(agTiledMapSeriviceLayer);
//        AGTiledMapSeriviceLayer agTiledMapSeriviceLayer3 = new AGTiledMapSeriviceLayer(11113,
//                "http://139.159.243.230:6080/arcgis/rest/services/vec/vec/MapServer",
//                new EncryptTileCacheHelper(TestActivity.this, "11113"));
//        mapView.addLayer(agTiledMapSeriviceLayer3);

        AGTiledMapSeriviceLayer agTiledMapSeriviceLayer4 = new AGTiledMapSeriviceLayer(11114,
                "http://139.159.243.230:6080/arcgis/rest/services/vec/img1/MapServer",
                new EncryptTileCacheHelper(TestActivity.this, "11114"));
        mapView.addLayer(agTiledMapSeriviceLayer4);
//        ArcGISDynamicMapServiceLayer agTiledMapSeriviceLayer2 = new ArcGISDynamicMapServiceLayer("http://139.159.243.230:6080/arcgis/rest/services/ditu/vec/MapServer");
//        mapView.addLayer(agTiledMapSeriviceLayer2);
        ArcGISDynamicMapServiceLayer arcGISDynamicMapServiceLayer = new ArcGISDynamicMapServiceLayer("http://139.159.243.230:6080/arcgis/rest/services/GZPS/GZSWPSGXOwnDept/MapServer");
        mapView.addLayer(arcGISDynamicMapServiceLayer);

        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (status == STATUS.INITIALIZED){
                    mapView.setScale(7612.17042487376);
                    Point point = new Point(113.33835968915169, 23.14901648232401);
                    mapView.centerAt(point, true);
                }
            }
        });
    }
}

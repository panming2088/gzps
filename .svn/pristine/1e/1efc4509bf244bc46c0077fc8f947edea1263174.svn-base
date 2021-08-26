package com.augurit.agmobile.patrolcore.layerdownload.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.common.GeographyInfoManager;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.layer.util.PatrolLayerFactory;
import com.augurit.agmobile.patrolcore.layerdownload.LayerDownloadToolView;
import com.augurit.agmobile.patrolcore.layerdownload.presenter.LayerDownloadPresenter;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Geometry;

/**
 * 描述：图层下载自定义区域Activity
 *
 * @author 创建人 ：liangsh
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.defaultview.layerdownload.activity
 * @createTime 创建时间 ：2016-11-28
 * @modifyBy 修改人 ：liangsh
 * @modifyTime 修改时间 ：2016-11-28
 * @modifyMemo 修改备注：
 */
public class LayerDownloadAreaActivity extends AppCompatActivity {
    private View backBtn;
    private View done;
    private MapView mMapView;
    private ViewGroup mRightContainer;
    private LayerInfo layerInfo;
    private Layer mLayer ;

    private LayerDownloadToolView mLayerDownloadToolView;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dnl_activity_area);
        init();
    }

    public void init(){
        layerInfo = getIntent().getExtras().getParcelable("layerinfo");
        PatrolLayerFactory layerFactory = new PatrolLayerFactory();
        mLayer = layerFactory.getLayer(LayerDownloadAreaActivity.this,layerInfo);
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.setMapBackground(Color.WHITE, Color.TRANSPARENT, 0f, 0f);//设置地图背景色、去掉网格线
        mMapView.addLayer(mLayer);
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(GeographyInfoManager.getInstance().getEnvelope() != null){
                    mMapView.setExtent(GeographyInfoManager.getInstance().getEnvelope());
                }
            }
        });

        mRightContainer = (ViewGroup) findViewById(R.id.right_container);
        backBtn = findViewById(R.id.btn_back);
        done = findViewById(R.id.btn_next_step);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayerDownloadPresenter.geometrys.clear();
                finish();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geometry geometry = mLayerDownloadToolView.getCurrentGeometry();
                if(geometry == null){
                    ToastUtil.longToast(LayerDownloadAreaActivity.this, "未绘制下载范围！");
                    return;
                }
                /*Intent intent = getIntent();
                intent.putExtra("geometry", geometry);
                setResult(1234, intent);*/
                LayerDownloadPresenter.geometrys.clear();
                LayerDownloadPresenter.geometrys.add(geometry);
                finish();
            }
        });

        mLayerDownloadToolView = new LayerDownloadToolView(this, mMapView, mRightContainer);
        mLayerDownloadToolView.show();
    }
}

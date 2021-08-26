package com.augurit.agmobile.patrolcore.layerdownload.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.augurit.agmobile.mapengine.common.GeographyInfoManager;
import com.augurit.agmobile.mapengine.layerdownload.view.presenter.ILayerDownloadPresenter;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.layerdownload.presenter.LayerDownloadPresenter;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.utils.actres.CallbackManager;
import com.augurit.am.fw.utils.actres.CallbackManagerImpl;

/**
 * 描述：图层下载Activity
 *
 * @author 创建人 ：liangsh
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.defaultview.layerdownload.activity
 * @createTime 创建时间 ：2016-11-09
 * @modifyBy 修改人 ：liangsh
 * @modifyTime 修改时间 ：2016-11-09
 * @modifyMemo 修改备注：
 */
public class LayerDownloadActivity extends AppCompatActivity {

    private ILayerDownloadPresenter mLayerDownloadPresenter;
    private CallbackManager callbackManager;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dnl_activity_layerdownload);
        callbackManager = CallbackManager.Factory.create();
        LinearLayout container = (LinearLayout) findViewById(R.id.dnl_activity_container);
        mLayerDownloadPresenter = new LayerDownloadPresenter(this);
        mLayerDownloadPresenter.setCallbackManagerImpl((CallbackManagerImpl) callbackManager);
        mLayerDownloadPresenter.startLayerDownload(GeographyInfoManager.getInstance().getMapView(), container, null);
        mLayerDownloadPresenter.setBackListener(new Callback1(){
            public void onCallback(Object data){
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}

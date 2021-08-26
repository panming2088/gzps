package com.augurit.agmobile.patrolcore.layerdownload.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.layerdownload.presenter.LayerDownloadAreaSelectPresenter;
import com.augurit.am.cmpt.common.Callback1;

/**
 * 描述：图层下载区域选择Activity
 *
 * @author 创建人 ：liangsh
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.defaultview.layerdownload.activity
 * @createTime 创建时间 ：2016-12-27
 * @modifyBy 修改人 ：liangsh
 * @modifyTime 修改时间 ：2016-12-27
 * @modifyMemo 修改备注：
 */
public class LayerDownloadAreaSelectActivity  extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dnl_activity_area_select_layout);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        LayerDownloadAreaSelectPresenter presenter = new LayerDownloadAreaSelectPresenter(this);
        presenter.setBackListener(new Callback1() {
            @Override
            public void onCallback(Object o) {
                finish();
            }
        });
        presenter.startAreaSelect(container);
    }
}

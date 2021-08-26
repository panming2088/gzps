package com.augurit.agmobile.patrolcore.layerdownload;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.statistics.view.StatisticsBaseView;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.layerdownload.presenter.PadLayerDownloadPresenter;


/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.dnl.view
 * @createTime 创建时间 ：2016-12-09
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-09
 */

public class PadAreaSelectTopBarView  extends StatisticsBaseView {

    private PadLayerDownloadPresenter mPadLayerDownloadPresenter2;
    public PadAreaSelectTopBarView(Context context, ViewGroup container, PadLayerDownloadPresenter padLayerDownloadPresenter2) {
        super(context, container);
        this.mPadLayerDownloadPresenter2 = padLayerDownloadPresenter2;
    }

    @Override
    protected ViewGroup getLayout() {
        return (ViewGroup) View.inflate(mContext, R.layout.dnl_select_area_topbar, null); // 工具按钮视图;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init() {
        super.init();
        mView.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        mView.findViewById(R.id.btn_next_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPadLayerDownloadPresenter2.onFinishSelectArea();
            }
        });
    }

    public void showTopView(){
        mContainer.setVisibility(View.VISIBLE);
        mView.setVisibility(View.VISIBLE);
    }
    public void hideTopView(){
        mContainer.setVisibility(View.GONE);
        mView.setVisibility(View.GONE);
    }
}

package com.augurit.agmobile.patrolcore.common.legend;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.augurit.agmobile.mapengine.legend.model.GroupLegend;
import com.augurit.agmobile.mapengine.legend.model.LayerLegend;
import com.augurit.agmobile.mapengine.legend.model.Legend;
import com.augurit.agmobile.mapengine.legend.view.ILegendView;
import com.augurit.am.fw.common.BaseView;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.view.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcl on 2017/10/30.
 */

public class LegendView  extends BaseView<ILegendPresenter> implements ILegendView {

    private LegendDialog lineDialog;
    private List<GroupLegend> allGpLegends;

    public LegendView(Context context) {

        super(context);

        initView();
    }

    protected void initView() {

        lineDialog = LegendDialog.newInstance();
        allGpLegends = new ArrayList<>();
    }


    @Override
    public void showLegends(List<Legend> legends) {

        showDialog();

        if (ListUtil.isEmpty(legends)) {

            lineDialog.showEmpty(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPresenter != null) {
                        mPresenter.showLegends();
                    }
                }
            });

        } else {

            lineDialog.showLegends(legends);
        }


    }

    @Override
    public void showLayerLegends(List<LayerLegend> legends) {
        showDialog();
        if (ListUtil.isEmpty(allGpLegends) && ListUtil.isEmpty(legends)) {

            lineDialog.showEmpty(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPresenter != null) {
                        mPresenter.showLegends();
                    }
                }
            });

        } else {
            GroupLegend legend = new GroupLegend();
            if(!ListUtil.isEmpty(legends)){
                legend.setParentName(legends.get(0).getParentName());
                legend.setLayerLegends(legends);
                allGpLegends.add(legend);
            }
            lineDialog.showLayerLegends(allGpLegends);
        }
    }

    private void showDialog() {
        if (!lineDialog.isAdded()) {
            lineDialog.show(((Activity) mContext).getFragmentManager(), "legendDialog");
        }
    }

    @Override
    public void showLoadingLegend() {

        showDialog();

        lineDialog.showLoading();
    }

    @Override
    public void hideLoadingLegend() {
        lineDialog.hideLoading();
    }

    @Override
    public void showLoadingLegendError(String message) {
        lineDialog.showError(message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.showLegends();
                }
            }
        });
    }

    @Override
    public void showToast(String message) {
        ToastUtil.shortToast(mContext,message);
    }

    @Override
    public void clearData() {
        allGpLegends.clear();
    }
}


package com.augurit.agmobile.patrolcore.layerdownload;

import android.content.Context;
import android.view.View;

import com.augurit.agmobile.patrolcore.layerdownload.presenter.LayerDownloadPresenter;
import com.augurit.agmobile.patrolcore.layerdownload.presenter.PadLayerDownloadPresenter;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;


/**
 * Created by liangsh on 2016-09-19.
 */
public class PadLayerDownloadView extends LayerDownloadView {

    public PadLayerDownloadView(Context context, LayerDownloadPresenter layerDownloadPresenter) {
        super(context, layerDownloadPresenter);
    }

    @Override
    public void onClick(View view, LayerInfo layerInfo) {
        ((PadLayerDownloadPresenter)mLayerDownloadPresenter).onDownloadBtnClick(view,layerInfo);
    }
}

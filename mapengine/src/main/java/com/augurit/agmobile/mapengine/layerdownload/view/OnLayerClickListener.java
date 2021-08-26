package com.augurit.agmobile.mapengine.layerdownload.view;

import android.view.View;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;


/**
 * Created by liangsh on 2016-09-19.
 */
public interface OnLayerClickListener{

    //xcl 加入View参数
    void onClick(View view, LayerInfo layerInfo);
}

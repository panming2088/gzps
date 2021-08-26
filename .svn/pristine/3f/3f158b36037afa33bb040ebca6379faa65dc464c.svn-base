package com.augurit.agmobile.mapengine.area.view.presenter;

import android.content.Context;

import com.augurit.agmobile.mapengine.area.view.IAreaView;
import com.augurit.am.cmpt.common.Callback1;
import com.esri.android.map.MapView;

/**
 * Created by liangsh on 2016-12-27.
 */
public class PadAreaPresenter extends AreaPresenter {

    public PadAreaPresenter(Context context, MapView mapView, IAreaView areaView){
        super(context, mapView, areaView);
    }

    protected void onAreaClickBefore(){
    }

    public void setSelectItem(){
        mAreaView.setSelectItem(true);
    }

    public void back(){
        /*if(!showChild && mBackListener!=null){
            mBackListener.onCallback(null);
        }*/
        super.back();
    }
}

package com.augurit.agmobile.patrolcore.selectdevice.view;

import android.os.Bundle;
import android.text.TextUtils;

import com.esri.core.geometry.Geometry;
import com.augurit.agmobile.patrolcore.R;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.selectdevice
 * @createTime 创建时间 ：2017-03-31
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-03-31
 * @modifyMemo 修改备注：
 */

public class ReadOnlyDeviceFragment extends SelectDeviceFragment {

    public static ReadOnlyDeviceFragment newInstance(String wkt,String deviceName,String title){

        Bundle bundle = getBundle(wkt, deviceName,title);
        ReadOnlyDeviceFragment fragment = new ReadOnlyDeviceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ReadOnlyDeviceFragment newInstance(Geometry geometry,String deviceName,String title){
        Bundle bundle = getBundle(geometry, deviceName,title);
        ReadOnlyDeviceFragment fragment = new ReadOnlyDeviceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    /**
     * 当点击地图的时候什么也不做
     */
    @Override
    protected void setMapOnTapListener() {

    }

    @Override
    protected void setTitle(String title) {
        if (!TextUtils.isEmpty(title)){
            mTv_title.setText(getString(R.string.show_selected_device,title));
        }
    }
}

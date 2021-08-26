package com.augurit.agmobile.mapengine.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author 创建人 ：weiqiuyue
 * @package 包名 ：com.augurit.am.intro.maps.arcgis.view
 * @createTime 创建时间 ：2017-01-12
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-12 23:22
 */
public abstract class AbstractMapFragment extends Fragment {
    protected IMapDelegate mMapDelegate;
    private View mView;

    public abstract IMapDelegate createMapDelegate(IMapContainer iMapContainer);


    public abstract IMapContainer getMapContainer(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            IMapContainer mapContainer = getMapContainer(inflater, container, savedInstanceState);
            this.mMapDelegate = createMapDelegate(mapContainer);
            mView = mapContainer.getSelf();
        }
        return mView;
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            this.getMapAsync((OnMapReadyCallback) getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void getMapAsync(final OnMapReadyCallback callback) {
        if (this.mMapDelegate != null) {
            this.mMapDelegate.getMapAsync(callback);
        }
    }
}

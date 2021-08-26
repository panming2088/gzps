package com.augurit.agmobile.mapengine.common.widget.rotateview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.R;
import com.esri.android.map.MapView;

/**
 * Created by lsh on 2018/3/5.
 */

public class MapRotateView {

    private Context mContext;
    private MapView mMapView;
    private ViewGroup mContainer;
    private View mView;

    private RotateManager mMapRotateManager;
    private boolean mRotateEnable = false;

    public RotateManager getMapRotateManager() {
        return mMapRotateManager;
    }

    public MapRotateView(Context context, MapView mapView, ViewGroup container) {
        mContext = context;
        mMapView = mapView;
        mContainer = container;
        mMapRotateManager = RotateManager.getInstance(context);
        mMapRotateManager.addOnRotateChangedListener(this, new RotateManager.OnRotateChangedListener() {
            @Override
            public void onRotateChanged(float direction) {
                if(!mRotateEnable){
                    return;
                }
                mMapView.setRotationAngle(direction);
            }
        });
        init();
    }

    private void init() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_map_rotate, null);
        final View ll_open_map_rotate = mView.findViewById(R.id.ll_open_map_rotate);
        final View ll_close_map_rotate = mView.findViewById(R.id.ll_close_map_rotate);
        ll_open_map_rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_open_map_rotate.setVisibility(View.GONE);
                ll_close_map_rotate.setVisibility(View.VISIBLE);
                mRotateEnable = true;
            }
        });
        ll_close_map_rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_open_map_rotate.setVisibility(View.VISIBLE);
                ll_close_map_rotate.setVisibility(View.GONE);
                mRotateEnable = false;
            }
        });
        mContainer.addView(mView);
    }

    public void onDestroy() {
        mMapRotateManager.removeListener(this);
        mMapRotateManager = null;
        mContext = null;
    }
}

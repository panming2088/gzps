package com.augurit.agmobile.mapengine.common.widget.compassview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.augurit.agmobile.mapengine.R;
import com.augurit.agmobile.mapengine.common.widget.rotateview.RotateManager;
import com.esri.android.map.MapView;

/**
 * Created by lsh on 2018/3/15.
 */

public class CompassView {
    private Context mContext;
    private ViewGroup mContainer;
    private MapView mMapView;

    private double mDefaulttRotationAngle;

    private View mView;
    private ImageView iv;
    private boolean mRotatable = true;


    public CompassView(Context context, ViewGroup container, MapView mapView){
        this.mContext = context;
        this.mContainer = container;
        this.mMapView = mapView;
        this.mDefaulttRotationAngle = mMapView.getRotationAngle();
        init();
    }

    private void init(){
        RotateManager.getInstance(mContext).addOnRotateChangedListener(this, new RotateManager.OnRotateChangedListener() {
            @Override
            public void onRotateChanged(float direction) {
                if(mRotatable){
                    iv.setRotation(-direction);
                }
            }
        });
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_compass_view, null);
        iv = (ImageView) mView.findViewById(R.id.iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mRotatable) {
                    mMapView.setRotationAngle(mDefaulttRotationAngle, true);
                    mView.setVisibility(View.GONE);
                }
            }
        });
        mContainer.addView(mView);
        mView.setVisibility(View.GONE);
    }

    public void setVisible(boolean visible){
        if(visible){
            mView.setVisibility(View.VISIBLE);
        } else {
            mView.setVisibility(View.GONE);
        }
    }

    public void setRotatable(boolean rotatable){
        mRotatable = rotatable;
    }

    public void onDestroy(){
        RotateManager.getInstance(mContext).removeListener(this);
        mContext = null;
        mContainer = null;
        mMapView = null;
    }
}

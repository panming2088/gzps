package com.augurit.agmobile.gzps.measure.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.augurit.agmobile.gzps.R;
import com.augurit.am.cmpt.common.Callback4;
import com.esri.android.map.MapView;

/**
 * Created by lsh on 2018/3/6.
 */

public class MapMeasureView {

    private Context mContext;
    private ViewGroup mContainer;
    private ViewGroup mTopbarContainer;
    private ViewGroup mToolContainer;

    private View mView;
    private ImageButton measureBtn;
    private boolean isOnMeasure;

    private MeasureView mMeasureView;

    private Callback4 mOnStartCallback;
    private Callback4 mOnStopCallback;


    public MapMeasureView(Context context, MapView mapView,
                          ViewGroup container, ViewGroup topbarContainer, ViewGroup toolContainer){
        this.mContext = context;
        this.mContainer = container;
        this.mTopbarContainer = topbarContainer;
        this.mToolContainer = toolContainer;
        mMeasureView = new MeasureView(context, mapView);
        init();
    }

    public void init(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_measure_btn, null);
        isOnMeasure = false;
        measureBtn = (ImageButton) mView.findViewById(R.id.btn_measure);
        measureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOnMeasure){
                    startMeasure();
                    isOnMeasure = true;
                }else {
                    stopMeasure();
                    isOnMeasure = false;
                }
            }
        });
        mContainer.addView(mView);
    }

    public void startMeasure(){
        if(mOnStartCallback != null){
            mOnStartCallback.before();
        }
        // 获取工具视图
        View toolView = mMeasureView.getMeasureView(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolContainer.setVisibility(View.GONE);
            }
        });
        // 获取顶栏视图
        View topbarView = mMeasureView.getTopBarView(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTopbarContainer.setVisibility(View.GONE);
                mToolContainer.setVisibility(View.GONE);
                isOnMeasure =false;
                stopMeasure();

            }
        });
        mTopbarContainer.removeAllViews();
        mTopbarContainer.addView(topbarView);
        mTopbarContainer.setVisibility(View.VISIBLE);
        mToolContainer.removeAllViews();
        mToolContainer.addView(toolView);
        mToolContainer.setVisibility(View.VISIBLE);

        if(mOnStartCallback != null){
            mOnStartCallback.after();
        }
    }

    public void stopMeasure(){
        if(mOnStopCallback != null){
            mOnStopCallback.before();
        }
        if(mMeasureView != null){
            mMeasureView.stopMeasure();
        }
        mTopbarContainer.setVisibility(View.GONE);
        mToolContainer.setVisibility(View.GONE);

        if(mOnStopCallback != null){
            mOnStopCallback.after();
        }
    }


    public void setOnStartCallback(Callback4 callback){
        this.mOnStartCallback = callback;
    }

    public void setOnStopCallback(Callback4 callback){
        this.mOnStopCallback = callback;
    }
}

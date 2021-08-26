package com.augurit.agmobile.gzps.uploadfacility.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.augurit.agmobile.gzps.R;
import com.augurit.am.cmpt.common.Callback4;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;

/**
 * Created by lsh on 2018/3/6.
 */

public class MapDrawQuestionView {

    private Context mContext;
    private ViewGroup mContainer;
    private ViewGroup mTopbarContainer;
    private ViewGroup mToolContainer;

    private View mView;
    private ImageButton measureBtn;
    private boolean isOnMeasure;

    private DrawQuestionView mMeasureView;

    private Callback4 mOnStartCallback;
    private Callback4 mOnStopCallback;


    public MapDrawQuestionView(Context context, MapView mapView,
                               ViewGroup container, ViewGroup topbarContainer, ViewGroup toolContainer){
        this.mContext = context;
        this.mContainer = container;
        this.mTopbarContainer = topbarContainer;
        this.mToolContainer = toolContainer;
        mMeasureView = new DrawQuestionView(context, mapView);
        mMeasureView.setQuestionSureListener(new DrawQuestionView.OnQuestionSureListener() {
            @Override
            public void onClickQuestionSure(Geometry geometry) {
                if(mOnSubmitListener != null){
                    mOnSubmitListener.onClickQuestionSubmit(geometry);
                }
            }

            @Override
            public void onStatusChange(int state) {
                if(mOnSubmitListener != null){
                    mOnSubmitListener.onStatusChange(state);
                }
            }
        });
//        init();
    }

    public void init(){
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_measure_btn, null);
        isOnMeasure = false;
        measureBtn = (ImageButton) mView.findViewById(R.id.btn_measure);
        measureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isOnMeasure){
                    startDraw();
                    isOnMeasure = true;
                }else {
                    stopDraw();
                    isOnMeasure = false;
                }
            }
        });
        mContainer.addView(mView);
    }

    public void startDraw(){
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
                stopDraw();

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

    public void stopDraw(){
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

    public interface OnSubmitListener {
        void onClickQuestionSubmit(Geometry geometry);
        void onStatusChange(int state);//0:点查 1：缓存疑面
    }
    private OnSubmitListener mOnSubmitListener;

    public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
        mOnSubmitListener = onSubmitListener;
    }
    public void clear(){
        if(mMeasureView != null){
            mMeasureView.clear();
        }
    }
}

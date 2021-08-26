package com.augurit.agmobile.gzps.uploadfacility.view;

import android.content.Context;
import android.support.annotation.Keep;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.mapengine.measure.view.IMeasureView;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;

/**
 * 默认的测量视图
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.defaultview.measure.view
 * @createTime 创建时间 ：2017-02-13
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-13
 * @modifyMemo 修改备注：
 * @version 1.0
 */
@Keep
public class DrawQuestionView implements IMeasureView {

    protected Context mContext;
    protected DrawQustionViewTool mCustomMeasureTool;
    protected View btn_clear;
    protected View btn_undo;
    private View btn_submit;

    public DrawQuestionView(Context context, MapView mapView){
        this.mContext = context;
        mCustomMeasureTool = new DrawQustionViewTool(mapView);
    }

    @Override
    public View getMeasureView(final View.OnClickListener oncCloseClickListener) {
        mCustomMeasureTool.startDraw();
        // 不再使用MeasureToolView的工具视图
//        View view = MeasureToolView.createView(mContext, R.color.dark_blue, new AMSpringSwitchButton.OnToggleListener() {
//                    @Override
//                    public void onToggle(boolean left) {
//                        if (left) {
//                            mCustomMeasureTool.measureLine();
//                        } else {
//                            mCustomMeasureTool.measureArea();
//                        }
//                    }
//                }, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mCustomMeasureTool.exit();
//                    }
//                },
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mCustomMeasureTool.undo();
//                    }
//                },
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mCustomMeasureTool.stopDraw();
//                        oncCloseClickListener.onClick(v);
//                    }
//                });

        View toolView = View.inflate(mContext, R.layout.meas_widget_tool, null);
        btn_clear = toolView.findViewById(R.id.btn_clear);
        btn_undo = toolView.findViewById(R.id.btn_undo);
        btn_submit = toolView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mQuestionSureListener != null){
                    mQuestionSureListener.onClickQuestionSure(mCustomMeasureTool.getCurrentGeometry());
                }
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomMeasureTool.clear();
                btn_clear.setVisibility(View.GONE);
                btn_undo.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
            }
        });
        btn_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomMeasureTool.undo();
            }
        });

        mCustomMeasureTool.setOnGraphicChangeListener(new DrawQustionViewTool.OnMeasureGraphicChangeListner() {
            @Override
            public void onGraphicEmpty() {
                btn_clear.setVisibility(View.GONE);
                btn_undo.setVisibility(View.GONE);
            }

            @Override
            public void onBeginDrawGraphic() {
                btn_clear.setVisibility(View.VISIBLE);
                btn_undo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBeginButtonShow() {
                btn_submit.setVisibility(View.VISIBLE);
            }

            @Override
            public void onButtonEmpty() {
                btn_submit.setVisibility(View.GONE);
            }
        });
        mCustomMeasureTool.setSureListener(new DrawQustionViewTool.OnQuestionSureListener() {
            @Override
            public void onClickQuestionSure(Geometry geometry) {
                if(mQuestionSureListener != null){
                    mQuestionSureListener.onClickQuestionSure(geometry);
                }
            }
        });
        return toolView;
    }

    /**
     * 获取顶栏视图
     * @param context context
     * @param closeClickListener    返回按钮监听
     * @return 顶栏视图
     */
    public View getTopBarView(Context context, final View.OnClickListener closeClickListener) {
        View topbar_view = View.inflate(context, R.layout.draw_question_topbar_view, null);
        topbar_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        AMSpringSwitchButton switchButton = (AMSpringSwitchButton) topbar_view.findViewById(R.id.switch_button);
//        switchButton.setOnToggleListener(new AMSpringSwitchButton.OnToggleListener() {
//            @Override
//            public void onToggle(boolean left) {
//                if (left) {
//                    mCustomMeasureTool.measureLine();
//                } else {
//                    mCustomMeasureTool.measureArea();
//                }
//            }
//        });
        mCustomMeasureTool.measureArea();
//        mCustomMeasureTool.stopDraw();
//        if(mQuestionSureListener != null){
//            mQuestionSureListener.onStatusChange(0);
//        }
//        SegmentControlView scv = (SegmentControlView) topbar_view.findViewById(R.id.scv);
//        scv.setOnSegmentChangedListener(new SegmentControlView.OnSegmentChangedListener() {
//            @Override
//            public void onSegmentChanged(int newSelectedIndex) {
//                switch (newSelectedIndex) {
//                    case 0:
//                        mCustomMeasureTool.stopDraw();
//                        btn_clear.setVisibility(View.GONE);
//                        btn_undo.setVisibility(View.GONE);
//                        btn_submit.setVisibility(View.GONE);
//                        if(mQuestionSureListener != null){
//                            mQuestionSureListener.onStatusChange(0);
//                        }
//                        break;
//                    case 1:
//                        mCustomMeasureTool.startDraw();
//                        btn_clear.setVisibility(View.GONE);
//                        btn_undo.setVisibility(View.GONE);
//                        btn_submit.setVisibility(View.GONE);
//                        if(mQuestionSureListener != null){
//                            mQuestionSureListener.onStatusChange(1);
//                        }
//                        break;
//                }
//            }
//        });
//        scv.setSelectedIndex(0);
        btn_clear.setVisibility(View.GONE);
        btn_undo.setVisibility(View.GONE);
        btn_submit.setVisibility(View.GONE);

        View btn_back = topbar_view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mCustomMeasureTool.stopDraw();
                closeClickListener.onClick(v);
            }
        });
        topbar_view.findViewById(R.id.container).setOnClickListener(null);  // 拦截测量时的顶栏点击事件，避免点到地图上
        return topbar_view;
    }

    @Override
    public void stopMeasure() {
        mCustomMeasureTool.stopDraw();
    }

    public interface OnQuestionSureListener{
        void onClickQuestionSure(Geometry geometry);
        void onStatusChange(int state);//0:点查 1：缓存疑面
    }

    private OnQuestionSureListener mQuestionSureListener;

    public void setQuestionSureListener(OnQuestionSureListener questionSureListener) {
        mQuestionSureListener = questionSureListener;
    }

    public void clear(){
        if(mCustomMeasureTool != null){
            mCustomMeasureTool.clear();
        }
        if(btn_clear != null) {
            btn_clear.setVisibility(View.GONE);
        }
        if(btn_undo != null) {
            btn_undo.setVisibility(View.GONE);
        }
        if(btn_submit != null) {
            btn_submit.setVisibility(View.GONE);
        }
    }
}

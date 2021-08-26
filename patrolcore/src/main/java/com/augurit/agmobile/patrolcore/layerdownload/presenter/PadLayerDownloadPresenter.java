package com.augurit.agmobile.patrolcore.layerdownload.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.augurit.agmobile.mapengine.common.GeographyInfoManager;
import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.agmobile.mapengine.layermanage.model.LayerType;
import com.augurit.agmobile.patrolcore.R;
import com.augurit.agmobile.patrolcore.layerdownload.LayerDownloadToolView;
import com.augurit.agmobile.patrolcore.layerdownload.PadAreaSelectTopBarView;
import com.augurit.agmobile.patrolcore.layerdownload.PadLayerDownloadView;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.utils.DisplayUtil;
import com.augurit.am.fw.utils.view.ToastUtil;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;

import java.util.ArrayList;


/**
 * Created by liangsh on 2016-09-18.
 */
public class PadLayerDownloadPresenter extends LayerDownloadPresenter {

    private PadAreaSelectTopBarView mPadAreaSelectTopBarView;

    public PadLayerDownloadPresenter(Context context) {
        super(context);
        this.mLayerDownloadView = new PadLayerDownloadView(context, this);
    }

    public void startLayerDownload(MapView mapView, ViewGroup mgrViewContainer, ViewGroup toolViewContainer,
                                   ViewGroup topBarContainer){
        if(toolViewContainer!=null){
            addToolView(mapView, toolViewContainer);
            addTopBarView(topBarContainer);
        }
        mgrViewContainer.removeAllViews();
        mgrViewContainer.setVisibility(View.VISIBLE);
        mgrViewContainer.addView(mLayerDownloadView.getDnlMgrView());
        mLayerDnlTasks = new ArrayList<>();
        mLayerDownloadView.initTaskList(mLayerDnlTasks);
        getProjectInfos();
        getLayerInfos();
    }

    private void addTopBarView(ViewGroup topBarContainer) {
        mPadAreaSelectTopBarView = new PadAreaSelectTopBarView(mContext,topBarContainer,this);
    }


    private void addToolView(MapView mapView, ViewGroup container){
        this.mLayerDownloadToolView = new LayerDownloadToolView(mContext, mapView, container);
    }

    public void showTopView(){
        this.mPadAreaSelectTopBarView.showTopView();
    }

    private void showToolView(){
        this.mLayerDownloadToolView.show();
    }

    public void hideTopView(){
        if(mPadAreaSelectTopBarView != null){
            this.mPadAreaSelectTopBarView.hideTopView();
        }

    }


    /**
     * 在平板改成在点击地方的旁边弹出Popup
     * @param layerInfo
     */
    public void onDownloadBtnClick(View view,LayerInfo layerInfo){
    /*    super.onDownloadBtnClick(layerInfo);
        mAreaPopupWindow = new PopupWindow(areaSelMenu,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mAreaPopupWindow.setTouchable(true);
        mAreaPopupWindow.setOutsideTouchable(true);
        mAreaPopupWindow.dismiss();
     //   mAreaPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.popup_right));
        userarea.setOnClickListener(v -> {
            mAreaPopupWindow.dismiss();
            showToolView();
            showTopView();
           // Intent intent = new Intent(mContext, LayerDownloadAreaActivity.class);
           // intent.putExtra("layerUrl", layerInfo.getUrl());
           // ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE);
        });
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAreaPopupWindow.dismiss();
                showToolView();
                showTopView();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAreaPopupWindow.dismiss();
            }
        });
        mAreaPopupWindow.showAsDropDown(view);*/
        if(mAreaPopupWindow != null
                && mAreaPopupWindow.isShowing()){
            return;
        }
        this.clickLayerInfo = layerInfo;
        areaSelMenu = LayoutInflater.from(mContext).inflate(R.layout.dnl_pop_area, null);
        mAreaPopupWindow = new PopupWindow(areaSelMenu,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mAreaPopupWindow.setTouchable(true);
        mAreaPopupWindow.setOutsideTouchable(true);
        mAreaPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.dnl_popup_right));
        //mAreaPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View max = areaSelMenu.findViewById(R.id.dnl_pop_area_max); // 全国
        View userarea = areaSelMenu.findViewById(R.id.dnl_pop_area_userarea); //行政
        View custom = areaSelMenu.findViewById(R.id.dnl_pop_area_custom); //自定义范围
        View cancel = areaSelMenu.findViewById(R.id.dnl_pop_area_cancel); //取消
        if(LayerType.FeatureLayer == layerInfo.getType()){
            userarea.setVisibility(View.GONE);
            custom.setVisibility(View.GONE);
        }
        max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAreaPopupWindow.dismiss();
                Geometry geometry = GeographyInfoManager.getInstance().getMaxExtent();
                geometrys.clear();
                geometrys.add(geometry);
                startDownload();
            }
        });
        userarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAreaPopupWindow.dismiss();
                showToolView();
                showTopView();
                // Intent intent = new Intent(mContext, LayerDownloadAreaActivity.class);
                // intent.putExtra("layerUrl", layerInfo.getUrl());
                // ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE);
            }
        });
        userarea.setVisibility(View.GONE);
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAreaPopupWindow.dismiss();
                showToolView();
                showTopView();
                // Intent intent = new Intent(mContext, LayerDownloadAreaActivity.class);
                // intent.putExtra("layerUrl", layerInfo.getUrl());
                // ((Activity)mContext).startActivityForResult(intent, REQUEST_CODE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAreaPopupWindow.dismiss();
            }
        });
//        mAreaPopupWindow.showAsDropDown(view);
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        Drawable bm = mContext.getResources().getDrawable(R.drawable.dnl_item_bg);
        int bgHeight = bm.getIntrinsicHeight();
        int location[] = new int[2];
        view.getLocationInWindow(location);
        float viewY = location[1];
        int windowHeight = DisplayUtil.getWindowHeight(mContext);

        if(viewY > windowHeight/2){
            mAreaPopupWindow.showAsDropDown(view, viewWidth, -bgHeight*3);
        } else {
            mAreaPopupWindow.showAsDropDown(view, viewWidth, -bgHeight);
        }

    }


    public void back(){
        if(mBackListener != null){
            mBackListener.onCallback(null);
        }
    }

    public void setBackListener(Callback1 callback){
        this.mBackListener = callback;
    }

    /**
     * 结束绘制后开始绘制图形
     */
    public void onFinishSelectArea(){
        Geometry currentGeometry = mLayerDownloadToolView.getCurrentGeometry();
        if(currentGeometry == null){
            ToastUtil.longToast(mContext, "未绘制下载范围！");
            return;
        }
        geometrys.clear();
        geometrys.add(currentGeometry);
        startDownload();
        mLayerDownloadToolView.dismiss();
        hideTopView();
        geometrys.clear();
    }

    public void onClose(){
        super.onClose();
        hideTopView();
    }
}

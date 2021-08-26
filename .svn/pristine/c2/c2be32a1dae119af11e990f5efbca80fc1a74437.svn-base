package com.augurit.agmobile.mapengine.edit.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.esri.android.map.MapView;


/**
 * 主要是提供属性编辑工具栏UI
 * Created by guokunhu on 16/9/5.
 */
public interface IAttrEditToolView {


    /**
     * 添加属性编辑工具控件
     *
     * @param context
     * @param parentView
     */
    void addAttrEditTools(Context context, ViewGroup parentView);

    /**
     * 移除属性编辑工具
     */
    void removeAttrEditTools();


    void setSaveBtnVisible(int isVisible);

    void setAllBtnVisible(int isVisible);


    /**
     * 设置编辑视图界面顶部
     *
     * @param context
     * @param mapView
     * @param backClickListener
     * @return
     */
    View getTopBarView(Context context, MapView mapView, View.OnClickListener backClickListener, IBackAfterEditListener listener);

    void clear();

    View getBtn_add();

    Button getBtn_finish();

    View getBtn_back();
}

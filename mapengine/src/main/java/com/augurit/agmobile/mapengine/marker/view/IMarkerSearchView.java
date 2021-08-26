package com.augurit.agmobile.mapengine.marker.view;

import android.view.View;

import com.augurit.am.fw.common.IView;

/**
 * 标注搜索视图接口
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.marker
 * @createTime 创建时间 ：2017-02-25
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-02-25
 * @modifyMemo 修改备注：
 */

public interface IMarkerSearchView extends IView{

    void showView();

    void hideView();

    void setOnFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener);

}

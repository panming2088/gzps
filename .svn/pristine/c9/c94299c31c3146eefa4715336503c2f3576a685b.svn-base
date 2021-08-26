package com.augurit.agmobile.mapengine.layerdownload.view.presenter;

import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.common.model.Area;
import com.augurit.am.cmpt.common.Callback1;

/**
 * 描述：图层下载区域选择Presenter接口
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerdownload.view.presenter
 * @createTime 创建时间 ：2017-02-15
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-15
 * @modifyMemo 修改备注：
 */
public interface ILayerDownloadAreaSelectPresenter {

    /**
     * 返回
     */
    void goBack();

    /**
     * 返回到父区域
     */
    void backToParentArea();

    /**
     * 区域点击
     * @param area 区域
     */
    void onAreaClick(Area area);

    /**
     * 下载点击
     * @param area 区域
     */
    void onDownloadClick(Area area);

    /**
     * 设置返回监听
     * @param backListener 返回监听
     */
    void setBackListener(Callback1 backListener);

    /**
     * 开启区域选择
     * @param container 视图容器
     */
    void startAreaSelect(ViewGroup container);
}

package com.augurit.agmobile.mapengine.layerdownload.view.presenter;

import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.layermanage.model.LayerInfo;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.fw.utils.actres.CallbackManagerImpl;
import com.esri.android.map.MapView;

/**
 * 描述：图层下载Presenter接口
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerdownload.view.presenter
 * @createTime 创建时间 ：2017-02-14
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-02-14
 * @modifyMemo 修改备注：
 */
public interface ILayerDownloadPresenter {

    /**
     * 开启图层下载
     * @param mapView MapView
     * @param mgrViewContainer 管理视图容器
     * @param toolViewContainer 工具视图容器
     */
    void startLayerDownload(MapView mapView, ViewGroup mgrViewContainer, ViewGroup toolViewContainer);

    /**
     * 下载按钮点击
     * @param layerInfo LayerInfo
     */
    void onDownloadBtnClick(LayerInfo layerInfo);

    /**
     * 设置CallbackManager
     * @param callbackManager
     */
    void setCallbackManagerImpl(CallbackManagerImpl callbackManager);

    /**
     * 返回
     */
    void back();

    /**
     * 设置返回监听
     * @param callback
     */
    void setBackListener(Callback1 callback);

    /**
     * 功能关闭
     */
    void onClose();
}

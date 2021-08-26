package com.augurit.agmobile.mapengine.addrsearch.view;

import com.augurit.agmobile.mapengine.addrsearch.model.LocationResult;
import com.augurit.am.fw.common.IPresenter;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;

import java.util.List;

/**
 * 描述：地名地址查询结果显示逻辑控制基类
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.addrsearch.view
 * @createTime 创建时间 ：2017-01-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-20
 */

public interface IAddressSearchResultPresenter extends IPresenter {

    /**
     * 显示查询结果
     * @param searchKey 查询关键字
     * @param locationResults 查询结果
     */
    void showSearchResult(String searchKey,List<LocationResult> locationResults);

    /**
     * 当结果列表中的选项被点击时要做的事情
     * @param locationResult 选中的列表选项
     */
    void onResultItemClick(LocationResult locationResult);


    void searchBySuggestionResult(String location);

    void searchByKeyWord(String inputText);

    void clearMap();

    void onBackButtonClick();

    void setOnRecyclerItemClickListener(OnRecyclerItemClickListener<LocationResult> locationResultOnRecyclerItemClickListener);

    void searchDetail(final LocationResult locationResult);
}

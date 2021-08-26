package com.augurit.agmobile.mapengine.addrsearch.view;

import com.augurit.agmobile.mapengine.addrsearch.model.SearchHistory;
import com.augurit.am.fw.common.IPresenter;

/**
 * 描述：地名地址查询搜索栏和历史记录视图行逻辑控制基类
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.agmobile.mapengine.addrsearch.view
 * @createTime 创建时间 ：2017-01-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2017-01-20
 */

public interface IAddressSearchPresenter extends IPresenter {


    /**
     * 显示搜索视图
     */
    void showSearchView();

    /**
     * 点击模糊搜索选项
     * @param suggestion 模糊搜索选项
     */
    void onSuggestionItemClick(String suggestion);

    /**
     * 禁止进行模糊搜索，调用这句话后，当用户继续输入关键字将不会进行模糊搜索
     */
    void disableSuggestion();
    /**
     * 开启模糊搜索
     */
    void enableSuggestion();

    /**
     * 根据模糊搜索的结果进行查询
     * @param location
     */
    void searchBySuggestionResult(String location);

    /**
     * 直接根据用户输入的关键字进行查询
     * @param inputText 用户输入的关键字
     */
    void searchByKeyWord(String inputText);

    /**
     * 清空搜索历史
     */
    void clearSearchHistory();

    /**
     * 当用户输入的关键字发生变化时调用的方法
     * @param text 用户输入的关键字
     */
    void onSearchTextChanged(String text);

    /**
     * 开始进行查询
     * @param text 查询关键字
     * @param isDetailedAddress 是否是模糊查询后获取到的结果，如果是true，那么直接进行精确查询；如果是false
     *                            ，先进行模糊查询，然后根据模糊查询的结果进行精确查询
     */
    void beginSearch(String text,boolean isDetailedAddress);

    /**
     * 搜索关键字输入栏中的"x"（删除）按钮点击事件
     */
    void onDeleteButtonClick();

    /**
     * 点击历史选项列表
     * @param position 在列表中的位置
     * @param text 历史选项列表第position个的名称
     */
    void onHistoryItemClick(int position,String text);

    /**
     * 保存搜索记录
     * @param searchText 搜索记录
     */
    void saveSearchHistory(SearchHistory searchText);
}

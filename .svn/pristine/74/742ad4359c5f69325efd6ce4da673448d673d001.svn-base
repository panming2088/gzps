package com.augurit.agmobile.mapengine.bookmark.view;

import android.content.Context;
import android.view.View;

import com.augurit.agmobile.mapengine.bookmark.model.BookMark;
import com.augurit.agmobile.mapengine.bookmark.view.presenter.IBookMarkPresenter;

import java.util.List;

/**
 * 书签视图接口
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.bookmark.view
 * @createTime 创建时间 ：2017-01-19 10:50
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-01-19 10:50
 */
public interface IBookMarkView {

    /**
     * 获取书签列表视图
     * @param bookMarks 书签List
     * @param listener 书签项点击监听
     * @return 书签列表视图
     */
    View getBookMarkListView(List<BookMark> bookMarks, OnBookMarkClickListener listener);

    /**
     * 获取书签顶栏视图
     * @return 书签顶栏视图
     */
    View getTopBarView();

    /**
     * 设置该View的Presenter
     * @param presenter IBookMarkPresenter
     */
    void setPresenter(IBookMarkPresenter presenter);

    /**
     * 显示添加书签对话框
     */
    void showDialog();

    /**
     * 书签项点击监听
     */
    interface OnBookMarkClickListener {

        /**
         * 书签项点击
         * @param bookMark 被点击的书签
         * @param position 点击书签所在位置
         */
        void onBookMarkClicked(BookMark bookMark, int position);
    }
}

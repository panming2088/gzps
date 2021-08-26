package com.augurit.agmobile.mapengine.bookmark.view.presenter;

import android.view.ViewGroup;

import com.augurit.agmobile.mapengine.bookmark.model.BookMark;
import com.augurit.am.cmpt.common.Callback1;
import com.augurit.am.cmpt.common.Callback2;

import java.util.List;

/**
 * 书签Presenter接口
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.bookmark.view.presenter
 * @createTime 创建时间 ：2017-01-19 10:50
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-01-19 10:50
 */
public interface IBookMarkPresenter {

    /**
     * 获取书签列表
     * @param callback 获取数据回调
     */
    void getBookMarks(Callback2<List<BookMark>> callback);

    /**
     * 上传书签
     * @param bookMark 书签
     * @param callback 上传操作回调
     */
    void uploadBookMark(BookMark bookMark, Callback2<Integer> callback);

    /**
     * 删除书签
     * @param bookMark 书签
     * @param callback 删除操作回调
     */
    void deleteBookMark(BookMark bookMark, Callback2<Integer> callback);

    /**
     * 显示书签
     * @param topBarContainer 顶栏容器
     * @param bookMarkContainer 书签列表容器
     * @param loadCallback 加载回调
     */
    void showBookMarks(final ViewGroup topBarContainer, final ViewGroup bookMarkContainer,
                       final Callback2<Void> loadCallback);

    /**
     * 显示添加书签对话框
     */
    void showDialog();

    /**
     * 功能关闭
     */
    void onClose();

    /**
     * 设置返回监听
     * @param backListener 返回监听
     */
    void setBackListener(Callback1<Void> backListener);
}

package com.augurit.agmobile.mapengine.bookmark.service;

import com.augurit.agmobile.mapengine.bookmark.model.BookMark;
import com.augurit.am.cmpt.common.Callback2;

import java.util.List;

/**
 * 书签Service接口
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.bookmark.service
 * @createTime 创建时间 ：2017-01-19 10:47
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-01-19 10:47
 */
public interface IBookMarkService {

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
}

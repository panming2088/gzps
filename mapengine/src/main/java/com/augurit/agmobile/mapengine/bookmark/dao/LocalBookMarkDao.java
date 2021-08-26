package com.augurit.agmobile.mapengine.bookmark.dao;

import com.augurit.agmobile.mapengine.bookmark.model.BookMark;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.db.AMQueryBuilder;

import java.util.List;

/**
 * 本地书签数据访问
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.bookmark.dao
 * @createTime 创建时间 ：2017-01-19 10:40
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-01-19 10:40
 */
public class LocalBookMarkDao {

    private AMDatabase mDatabase;

    /**
     * 构造方法
     * @param database 数据库
     */
    public LocalBookMarkDao(AMDatabase database) {
        mDatabase = database;
    }

    /**
     * 获取本地书签列表
     * @return 书签List
     */
    public List<BookMark> getBookMarks() {
        AMQueryBuilder amQueryBuilder = new AMQueryBuilder(BookMark.class);
        amQueryBuilder.appendOrderDescBy("bookMarkId");     // 以ID降序
        return mDatabase.query(amQueryBuilder);
    }

    /**
     * 添加书签到本地
     * @param bookMark 书签
     * @throws Exception 出错信息
     */
    public void addBookMark(BookMark bookMark) throws Exception {
        mDatabase.save(bookMark);
    }

    /**
     * 添加一组书签到本地
     * @param bookMarks 书签List
     * @throws Exception 出错信息
     */
    public void addBookMarks(List<BookMark> bookMarks) throws Exception {
        mDatabase.saveAll(bookMarks);
    }

    /**
     * 更新书签
     * @param bookMark 书签
     * @throws Exception 出错信息
     */
    public void updateBookMark(BookMark bookMark) throws Exception {
        mDatabase.update(bookMark);
    }

    /**
     * 删除本地所有书签
     */
    public void deleteAllBookMark() {
        mDatabase.deleteAll(BookMark.class);
    }

    /**
     * 删除本地指定书签
     * @param bookMark 书签
     */
    public void deleteBookMark(BookMark bookMark) {
        mDatabase.delete(bookMark);
    }
}

package com.augurit.agmobile.mapengine.bookmark.router;

import com.augurit.agmobile.mapengine.bookmark.dao.LocalBookMarkDao;
import com.augurit.agmobile.mapengine.bookmark.dao.RemoteBookMarkDao;
import com.augurit.agmobile.mapengine.bookmark.model.BookMark;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.common.ValidateUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 书签数据路由
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.bookmark.router
 * @createTime 创建时间 ：2017-01-19 10:43
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2017-01-19 10:43
 */
public class BookMarkRouter {

    private LocalBookMarkDao mLocalDao;
    private RemoteBookMarkDao mRemoteDao;

    /**
     * 构造方法
     * @param database 数据库
     */
    public BookMarkRouter(AMDatabase database) {
        mLocalDao = new LocalBookMarkDao(database);
    }

    /**
     * 获取书签列表
     */
    public List<BookMark> getBookMarks() throws IOException {
        // 从本地读取
        List<BookMark> bookMarks = getFromLocal();
        if (ValidateUtil.isListNull(bookMarks)) {
            // 若本地为空则从网络读取
            // TODO
        }
        return bookMarks;
    }

    /**
     * 添加并上传书签
     * @param bookMark 书签
     */
    public int uploadBookMark(BookMark bookMark) throws IOException {
        List<BookMark> bookMarks = new ArrayList<>();
        bookMarks.add(bookMark);
        saveToLocal(bookMarks);
        // 上传到服务器
        // TODO
        return bookMarks.get(0).getBookMarkId();
    }

    /**
     * 从本地及服务端删除书签
     * @param bookMark 书签
     */
    public void deleteBookMark(BookMark bookMark) throws IOException {
        // 从服务端删除
        // TODO
        // 从本地删除
        mLocalDao.deleteBookMark(bookMark);
    }

    /**
     * 从本地获取书签
     * @return 本地书签列表
     */
    private List<BookMark> getFromLocal() {
        return mLocalDao.getBookMarks();
    }

    /**
     * 保存一组书签到本地
     * @param bookMarks 书签列表
     */
    private void saveToLocal(List<BookMark> bookMarks) {
        try {
            mLocalDao.addBookMarks(bookMarks);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("保存书签不成功,原因是:"+e.getMessage());
        }
    }
}

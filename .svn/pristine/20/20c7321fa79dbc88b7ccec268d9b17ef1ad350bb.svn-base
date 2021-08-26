package com.augurit.agmobile.mapengine.marker.dao;

import com.augurit.agmobile.mapengine.bookmark.model.BookMark;
import com.augurit.agmobile.mapengine.marker.model.LocalMark;
import com.augurit.agmobile.mapengine.marker.model.Mark;
import com.augurit.agmobile.mapengine.marker.model.MarkQueryHistory;
import com.augurit.agmobile.mapengine.marker.model.SimpleMarkInfo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.db.AMQueryBuilder;


import java.util.ArrayList;
import java.util.List;

/**
 * 标注相关信息的存放
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.mark.dao
 * @createTime 创建时间 ：2016-12-20
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-12-20
 */

public class LocalDatabaseMarkDao {


    private final AMDatabase mAmDatabase;

    public LocalDatabaseMarkDao(){
        mAmDatabase = AMDatabase.getInstance();
    }

    /**
     * 获取查询历史
     * @return
     */
    public List<String> getQueryHistory(){
        List<MarkQueryHistory> allHistory = mAmDatabase.getQueryAll(MarkQueryHistory.class);
        List<String> historyStr = new ArrayList<>();
        for (MarkQueryHistory history : allHistory){
                historyStr.add(history.getHistory());
        }
        return historyStr;
    }


    /**
     * 保存标注名到本地
     * @param infos
     */
    public void  saveMarkTitlesToDatabase(List<SimpleMarkInfo> infos){
        for (SimpleMarkInfo info : infos){
            mAmDatabase.save(info);
        }
    }

    /**
     * 保存查询历史
     * @param text
     */
    public void  saveQueryHistory(String text){
        MarkQueryHistory history = new MarkQueryHistory();
        history.setHistory(text);
        mAmDatabase.save(history);
    }

    /**
     * 进行查询
     * @param searchText
     * @return
     */
    public List<SimpleMarkInfo> query(String searchText){
        List<String> queryResult = new ArrayList<>();
        AMQueryBuilder<SimpleMarkInfo> builder = new AMQueryBuilder<>(SimpleMarkInfo.class);
        builder.where("markName like"+" '%"+searchText.trim()+"%'");
        ArrayList<SimpleMarkInfo> result = mAmDatabase.query(builder);
        return result;
    }

    public void deleteMark(SimpleMarkInfo simpleMarkInfo){
        AMDatabase amDatabase = AMDatabase.getInstance();
        amDatabase.delete(simpleMarkInfo);
    }


    public void clearPreviousData(){
        AMDatabase amDatabase = AMDatabase.getInstance();
        amDatabase.deleteAll(SimpleMarkInfo.class);
    }


    /**
     * 获取本地标注列表
     * @return 书签List
     */
    public List<LocalMark> getMarks() {
        return mAmDatabase.getQueryAll(LocalMark.class);
    }


    public void addMark(LocalMark mark) throws Exception {
        mAmDatabase.save(mark);
    }


    public void addMarks(List<LocalMark> bookMarks) throws Exception {
        mAmDatabase.saveAll(bookMarks);
    }


    public void updateMark(LocalMark bookMark) throws Exception {
        mAmDatabase.update(bookMark);
    }


    public void deleteAllMark() {
        mAmDatabase.deleteAll(LocalMark.class);
    }


    public void deleteMark(LocalMark bookMark) {
        mAmDatabase.delete(bookMark);
    }
}

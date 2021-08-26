package com.augurit.agmobile.gzps.track.dao;

import com.augurit.agmobile.gzps.track.model.Track;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.db.AMQueryBuilder;

import java.util.List;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.track.dao
 * @createTime 创建时间 ：2017-07-31
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-31
 * @modifyMemo 修改备注：
 */

public class LocalTrackSQLiteDao {

    public void save(Track track){
        AMDatabase amDatabase = AMDatabase.getInstance();
        amDatabase.save(track);
    }

    public List<Track> queryAll(){
        AMDatabase amDatabase = AMDatabase.getInstance();
        return amDatabase.getQueryAll(Track.class);
    }

    /**
     * 分页查询
     * @param pageNo 第几页，从1开始
     * @param pageSize  每页记录数
     * @return
     */
    public List<Track> query(int pageNo, int pageSize){
        int start = (pageNo-1) * pageSize;
        AMDatabase amDatabase = AMDatabase.getInstance();
        AMQueryBuilder<Track> queryBuilder = new AMQueryBuilder<Track>(Track.class);
        queryBuilder.appendOrderDescBy("trackId").limit(start, pageSize);
        return amDatabase.query(queryBuilder);
    }

    public void delete(int id){
        AMDatabase amDatabase = AMDatabase.getInstance();
        amDatabase.deleteWhere(Track.class, "trackId", id+"");
    }

    public void deleteAll(){
        AMDatabase amDatabase = AMDatabase.getInstance();
        amDatabase.deleteAll(Track.class);
    }
}

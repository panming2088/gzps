package com.augurit.agmobile.gzps.worknews.dao;

import com.augurit.agmobile.gzps.common.model.Result2;
import com.augurit.agmobile.gzps.worknews.model.WorkNews;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by xcl on 2017/11/15.
 */

public interface WorkNewsApi {

    /**
     * 获取最新的10条巡检动态
     * @return
     */
    @GET("rest/parts/getLatestTen")
    Observable<Result2<List<WorkNews>>>  getRecentTenWorkNews();

}

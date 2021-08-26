package com.augurit.agmobile.mapengine.legend.dao;

import com.augurit.agmobile.mapengine.legend.model.Legend;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.legend.dao
 * @createTime 创建时间 ：17/7/19
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/7/19
 * @modifyMemo 修改备注：
 */

public interface ILegendApi {

    @GET()
    Observable<List<Legend>>  getLegends(@Url String url);

    @GET()
    Observable<ResponseBody>  getLegend(@Url String url);
}

package com.augurit.agmobile.patrolcore.common.table.dao.remote.api;



import com.augurit.agmobile.patrolcore.common.table.dao.remote.TableChildItems;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model.manager.remote.api
 * @createTime 创建时间 ：17/3/6
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/6
 * @modifyMemo 修改备注：
 */

public interface TableChildItemsApi {
    /*
    @GET("agweb/rest/agdic/agdicByTypeCode/{typeCode}")
    Observable<TableChildItems> getTableChildItemsByTypeCode(@Path("typeCode") String type_code);
    */
    @GET
    Observable<TableChildItems> getTableChildItemsByCode(@Url String url);
}

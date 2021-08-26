package com.augurit.agmobile.patrolcore.common.table.dao.remote.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model.manager.remote.api
 * @createTime 创建时间 ：17/3/13
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/13
 * @modifyMemo 修改备注：
 */

public interface UploadTableItemsApi {
    @POST("")
    Observable<ResponseBody> getTableItemsData(@Url String url, @Body RequestBody body);
}

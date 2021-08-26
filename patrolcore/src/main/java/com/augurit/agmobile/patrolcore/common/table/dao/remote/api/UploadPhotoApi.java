package com.augurit.agmobile.patrolcore.common.table.dao.remote.api;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 描述：图片上传接口
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model.manager.remote.api
 * @createTime 创建时间 ：17/3/14
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/14
 * @modifyMemo 修改备注：
 */

public interface UploadPhotoApi {
    @Multipart
    @POST("")
    Observable<ResponseBody> uploadFiles(@Url String url, @PartMap() HashMap<String, RequestBody> requestBody);
  //  Observable<ResponseBody> uploadFiles(@Path("patrolCode") String patrolCode, @PartMap() Map<String, RequestBody> maps);

    @Multipart
    @POST("")
    Observable<ResponseBody> uploadFiles(@Url String url, @Query("field1") String field1, @PartMap() HashMap<String, RequestBody> requestBody);

}

package com.augurit.am.fw.log.upload.http;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.am.fw.log.upload.http
 * @createTime 创建时间 ：2017/7/28
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/7/28
 * @modifyMemo 修改备注：
 */

public interface UploadLogApi {
    @Multipart
    @POST("")
    Observable<ResponseBody> uploadFiles(@Url String url, @PartMap() HashMap<String, RequestBody> requestBody);
}

package com.augurit.am.cmpt.update.dao;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.common.update.dao
 * @createTime 创建时间 ：17/5/10
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/5/10
 * @modifyMemo 修改备注：
 */

public class UpdateServiceFactory {
    private static final String BASEURL="http://139.159.247.149/";
    public static <T> T createServiceFrom(final Class<T> serviceClass) {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .build();
        return adapter.create(serviceClass);
    }
}

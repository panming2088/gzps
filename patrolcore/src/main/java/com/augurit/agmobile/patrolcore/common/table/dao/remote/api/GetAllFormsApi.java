package com.augurit.agmobile.patrolcore.common.table.dao.remote.api;



import com.augurit.agmobile.patrolcore.common.table.dao.remote.AllFormTableItems;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 描述：获取所有项目的表单字段
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agpatrol.common.table.model.manager.remote.api
 * @createTime 创建时间 ：17/3/21
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：17/3/21
 * @modifyMemo 修改备注：
 */

public interface GetAllFormsApi {
    @GET
    Observable<AllFormTableItems> getAllForms(@Url String url);
}

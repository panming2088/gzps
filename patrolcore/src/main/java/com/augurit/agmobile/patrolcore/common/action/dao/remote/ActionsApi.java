package com.augurit.agmobile.patrolcore.common.action.dao.remote;


import com.augurit.agmobile.patrolcore.common.action.model.ActionConfigureModel;
import com.augurit.agmobile.patrolcore.common.action.model.ActionModel;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.common.action.service
 * @createTime 创建时间 ：2017-06-12
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-06-12
 * @modifyMemo 修改备注：
 */

public interface ActionsApi {


    /**
     * 获取角色功能列表接口
     * @param roleId
     * @return
     */
    @GET("rest/funcService/getAllfeatures/{roleId}")
    Observable<List<ActionModel>> getAllfeatures(@Path("roleId") String roleId);

    /**
     * 获取某个功能的操作项接口
     *
     * @param featureCode
     * @return
     */
    @GET("rest/system/getOperationItemByCode/{featureCode}/{type}")
    Observable<List<TableItem>> getOperationItemByCode(@Path("featureCode") String featureCode, @Path("type") String type);


    /**
     * 获取所有功能的操作项接口
     *
     * @return
     */
    @GET("rest/system/getAllOperationItems/{userId}")
    Observable<List<ActionConfigureModel>> getAllOperationItems(@Path("userId") String userId);


    @GET("rest/agdic/allDics")
    Observable<List<DictionaryItem>> getAllDic();

}

package com.augurit.agmobile.mapengine.project.dao;


import com.augurit.agmobile.mapengine.project.model.AgwebProjectLayer;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 获取所有专题图层的agweb接口
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.agmultiplan.layer.dao
 * @createTime 创建时间 ：2017-07-11
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-07-11
 * @modifyMemo 修改备注：
 */

public interface IAgwebProjectLayerApi {

    /**
     * 获取所有的专题图层
     *
     * @return
     */
    @GET("")
    Observable<AgwebProjectLayer> getAllProjectLayers(@Url String url);
}


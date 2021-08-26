package com.augurit.agmobile.mapengine.layerquery;

import android.content.Context;


import com.augurit.agmobile.mapengine.layerquery.service.ILayerQueryService;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.mapengine.layerquery
 * @createTime 创建时间 ：2017-04-26
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：2017-04-26
 * @modifyMemo 修改备注：
 */

public interface ILayerQueryServiceProvider {
    ILayerQueryService provideLayerService(Context context);
}

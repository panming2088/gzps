package com.augurit.agmobile.mapengine.bufferanalysis.dao;

import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;

/**
 * @author 创建人 ：liangshenghong
 * @package 包名 ：com.augurit.agmobile.mapengine.bufferanalysis.dao
 * @createTime 创建时间 ：2017-02-06
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-02-06
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public interface IBufferAnalysingDao {

    /**
     * 使用identify方法进行缓冲分析
     * @param url 图层MapServer的URL
     * @param parameters 参数
     * @return 结果数组
     */
    IdentifyResult[] getAnalysingResultViaIdentifyTask(String url, IdentifyParameters parameters);
 }

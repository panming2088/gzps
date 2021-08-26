package com.augurit.agmobile.mapengine.bufferanalysis.dao;

import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;

/**
 * @author 创建人 ：liangshenghong
 * @package 包名 ：com.augurit.agmobile.mapengine.bufferanalysis.dao
 * @createTime 创建时间 ：2016-12-06
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：201-12-06
 * @modifyMemo 修改备注：
 * @version 1.0
 */
public class RemoteBufferAnalysingRestDao implements IBufferAnalysingDao {

    /**
     * 使用identify方法进行缓冲分析
     * @param url 图层MapServer的URL
     * @param parameters 参数
     * @return 结果数组
     */
    @Override
    public IdentifyResult[] getAnalysingResultViaIdentifyTask(String url, IdentifyParameters parameters) {
        IdentifyTask identifyTask = new IdentifyTask(url);
        try {
            return identifyTask.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return new IdentifyResult[0];
        }

    }
}

package com.augurit.agmobile.patrolcore.common;

import java.util.Map;

/**
 * @author 创建人 ：liangshenghong
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.common
 * @createTime 创建时间 ：2017-07-13
 * @modifyBy 修改人 ：liangshenghong
 * @modifyTime 修改时间 ：2017-07-13
 * @modifyMemo 修改备注：
 */

public class TableDataChangedEvent {

    private Map<String, String> valueMap;

    public TableDataChangedEvent(Map<String, String> valueMap){
        this.valueMap = valueMap;
    }

    public Map<String, String> getValueMap(){
        return valueMap;
    }
}

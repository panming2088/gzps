package com.augurit.agmobile.mapengine.common.widget.callout.attribute;

import android.text.TextUtils;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 创建人 ：xuciluan
 * @package 包名 ：com.augurit.am.map.arcgis.widget
 * @createTime 创建时间 ：2016-11-16
 * @modifyBy 修改人 ：
 * @modifyTime 修改时间 ：2016-11-16
 */

public class DefaultFilterCondition implements IFilterCondition {
    @Override
    public Map<String, Object> filter(Map<String, Object> attributes) {
        Map<String, Object> map = new ListOrderedMap<>();
        if(attributes != null){
            Set<Map.Entry<String, Object>> entries = attributes.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                if (entry.getKey().toUpperCase().contains("OBJECTID")
                        || entry.getKey().toUpperCase().contains("SHAPE")
                        || entry.getKey().toUpperCase().contains("ID")
                        || TextUtils.isEmpty((entry.getValue() + "").trim())//过滤属性值为空格
                        || (entry.getValue() + "").toUpperCase().contains("NULL")) {//过滤属性值为NULL的
                    continue;
                }
                map.put(entry.getKey(), entry.getValue());
            }
            return map;
        }
        return attributes;
    }
}

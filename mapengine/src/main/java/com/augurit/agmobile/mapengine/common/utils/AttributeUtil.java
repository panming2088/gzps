package com.augurit.agmobile.mapengine.common.utils;

import android.content.Context;
import android.text.TextUtils;

import com.augurit.agmobile.mapengine.common.widget.callout.attribute.AMFindResult;
import com.augurit.agmobile.mapengine.common.widget.callout.attribute.IFilterCondition;
import com.augurit.am.fw.utils.BeanUtil;
import com.augurit.am.fw.utils.ListUtil;
import com.esri.core.tasks.identify.IdentifyResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 跟属性相关的工具类
 *
 * @author 创建人 ：xiejiexin
 * @version 1.0
 * @package 包名 ：com.augurit.am.map.arcgis.identify
 * @createTime 创建时间 ：2016-12-02 10:45
 * @modifyBy 修改人 ：xiejiexin
 * @modifyTime 修改时间 ：2016-12-02 10:45
 */
public final class AttributeUtil {
    private AttributeUtil() {
    }

    /**
     * 使用过滤器过滤属性
     *
     * @param attributes
     * @param iFilterCondition
     * @return
     */
    public static Map<String, Object> getAttributes(Map<String, Object> attributes, IFilterCondition iFilterCondition) {
        if (attributes == null) {
            return null;
        }
        //将它转成HashMap，方便之后的数据传递
        Map<String, Object> objectHashMap = new HashMap<String, Object>();
        Set<Map.Entry<String, Object>> entries = attributes.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            objectHashMap.put(entry.getKey(), entry.getValue());
        }
        if (iFilterCondition != null) {
            objectHashMap = iFilterCondition.filter(objectHashMap);//根据规则过滤属性字段
        }

        return objectHashMap;
    }

    /**
     * 根据displayFildName和attributes获取显示名称/标题
     *
     * @param context
     * @param displayFildName
     * @param attributes
     * @return
     */
    public static String getDisplayName(Context context, String displayFildName, Map<String, Object> attributes) {
        if (TextUtils.isEmpty(displayFildName)) {
            return displayFildName;
        }
        return !TextUtils.isEmpty(attributes.get(displayFildName) + "") ?
                attributes.get(displayFildName).toString() : "";
    }

    /**
     * 把IdentifyResult数组转换成AgFindResult
     * （主要因为IdentifyResult无法动态Set值，只能来自于点查返回结果。
     * 属性界面显示需要使用类似IdentifyResult结构的AgFindResult）
     *
     * @param identifyResultArray 传入的IdentifyResult数组
     * @return 返回AgFindResult 数组
     */
    public static AMFindResult[] covertIdentifyResultToAgFindResult(IdentifyResult[] identifyResultArray) {
        int lenght = ListUtil.isEmpty(identifyResultArray) ? 0 : identifyResultArray.length;
        AMFindResult[] agFindResultArray = new AMFindResult[lenght];
        for (int i = 0; i < identifyResultArray.length; i++) {
            AMFindResult agFindResult = new AMFindResult();
            BeanUtil.copyProperties(identifyResultArray[i], agFindResult);
            agFindResultArray[i] = agFindResult;
        }
        return agFindResultArray;
    }
}

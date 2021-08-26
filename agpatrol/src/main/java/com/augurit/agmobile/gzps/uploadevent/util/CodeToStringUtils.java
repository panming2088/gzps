package com.augurit.agmobile.gzps.uploadevent.util;

import com.augurit.agmobile.gzps.BaseApplication;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;

import java.util.List;

/**
 * 描述：
 *
 * @author 创建人 ：guokunhu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadevent.util
 * @createTime 创建时间 ：2017/12/19
 * @modifyBy 修改人 ：guokunhu
 * @modifyTime 修改时间 ：2017/12/19
 * @modifyMemo 修改备注：
 */

public class CodeToStringUtils {
    public static String getSpinnerValueByCode(String code) {
        List<DictionaryItem> dictionaryItems = new TableDBService(BaseApplication.application).getDictionaryByCode(code);
        if (ListUtil.isEmpty(dictionaryItems)) {
            return "";
        }
        DictionaryItem dictionaryItem = dictionaryItems.get(0);
        return dictionaryItem.getName();
    }

    /**
     * 获取多个数据字典值，编码以英文逗号分隔开
     * @param codes
     * @return
     */
    public static String getSpinnerValuesByMultiCode(String codes){
        if(StringUtil.isEmpty(codes)){
            return "";
        }
        String[] codeArray = codes.split(",");
        String value = "";
        for(String code : codeArray){
            value = value + "，" + getSpinnerValueByCode(code);
        }
        value = value.substring(1);
        return value;
    }
}

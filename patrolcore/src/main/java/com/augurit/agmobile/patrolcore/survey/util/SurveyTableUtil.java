package com.augurit.agmobile.patrolcore.survey.util;

import android.content.Context;

import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.common.table.model.TableItem;

import java.util.List;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.patrolcore.survey.util
 * @createTime 创建时间 ：17/9/13
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/9/13
 * @modifyMemo 修改备注：
 */

public class SurveyTableUtil {

    /**
     * 判断该项的值是否跟包含想要的结果
     *
     * @param tableItem
     * @return
     */
    public static boolean ifContainsExpectedValue(Context context, TableItem tableItem, String expectedValue) {
        String renkouleixing = tableItem.getValue();
        TableDBService tableDBService = new TableDBService(context);
        List<DictionaryItem> dictionaryByCode = tableDBService.getDictionaryByCode(renkouleixing);//如果类型是下拉框，要通过数据字段取到值
        if (dictionaryByCode.size() > 0) {
            if (dictionaryByCode.get(0).getName().contains(expectedValue)) {
                return true;
            }
        } else { //如果是文本
            if (renkouleixing.contains(expectedValue)) {
                return true;
            }
        }
        return false;
    }
}

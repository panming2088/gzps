package com.augurit.agmobile.gzps.common.util;

import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 包名：com.augurit.agmobile.gzws.common.utils
 * 类描述：
 * 创建人：luobiao
 * 创建时间：2020/4/25 20:09
 * 修改人：luobiao
 * 修改时间：2020/4/25 20:09
 * 修改备注：
 */
public class DictionarySortUtil {
    /**
     * 排序下拉字典数组
     *
     * @param list
     */
    public static void processOrder(List<DictionaryItem> list) {
        Collections.sort(list, new Comparator<DictionaryItem>() {
            @Override
            public int compare(DictionaryItem o1, DictionaryItem o2) {
                String code = o1.getCode();
                String target = code;
                if (code.length() > 1) {
                    target = code.replaceAll("[^(0-9)]", "");//去掉所有字母
                }

                String code2 = o2.getCode();
                String target2 = code;
                if (code2.length() > 1) {
                    target2 = code2.replaceAll("[^(0-9)]", "");//去掉所有字母
                }

                int num1 = Integer.valueOf(target);
                int num2 = Integer.valueOf(target2);
                int result = 0;
                if (num1 > num2) {
                    result = 1;
                }

                if (num1 < num2) {
                    result = -1;
                }
                return result;
            }
        });

    }
}

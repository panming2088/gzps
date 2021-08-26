package com.augurit.agmobile.gzps.uploadfacility.view.facilityprobrem;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.log.LogUtil;

import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.facilityprobrem
 * @createTime 创建时间 ：18/1/29
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：18/1/29
 * @modifyMemo 修改备注：
 */

public class FacilityProblemView {

    /**
     * 数据字典code
     */
    private static final String DICTIONARY_CODE = "A177";
    private static final String DICTIONARY_PSK_CODE = "A174008";
    private List<MultiSelectFlexLayout> mMultiSelectFlexLayoutList = null;

    public FacilityProblemView(Context context, ViewGroup container) {
        initView(context, container);
    }

    private void initView(Context context, ViewGroup container) {
        mMultiSelectFlexLayoutList = new ArrayList<>();
        TableDBService tableDBService = new TableDBService(context.getApplicationContext());
        List<DictionaryItem> a177 = tableDBService.getDictionaryByTypecodeInDB(DICTIONARY_CODE);
        processOrder(a177);
        for (DictionaryItem dictionaryItem : a177) {
            //获取子类
            List<DictionaryItem> childDictionaryByPCodeInDB = tableDBService.getChildDictionaryByPCodeInDB(dictionaryItem.getCode());
            processOrder(childDictionaryByPCodeInDB);
            Map<String, DictionaryItem> childItems = new LinkedHashMap<>(10);
            for (DictionaryItem dictionaryItem1 : childDictionaryByPCodeInDB) {
                childItems.put(dictionaryItem1.getName(), dictionaryItem1);
            }
            //构造view
            MultiSelectFlexLayout facilityProblemView = new MultiSelectFlexLayout(context);
            facilityProblemView.addItemsByDictionaryItem(dictionaryItem.getName(), dictionaryItem.getCode(), childItems, null);
            mMultiSelectFlexLayoutList.add(facilityProblemView);
            container.addView(facilityProblemView);
        }
    }

    /**
     * @param context
     * @param container
     * @param pCode     大类编码
     * @param childCode 小类编码
     */
    public FacilityProblemView(Context context, ViewGroup container, String pCode, String childCode) {

        if (TextUtils.isEmpty(childCode)) {
            initView(context, container);
            return;
        }

        mMultiSelectFlexLayoutList = new ArrayList<>();
        TableDBService tableDBService = new TableDBService(context.getApplicationContext());
        List<DictionaryItem> a177 = tableDBService.getDictionaryByTypecodeInDB(DICTIONARY_CODE);
        processOrder(a177);
        for (DictionaryItem dictionaryItem : a177) {
            List<DictionaryItem> childDictionaryByPCodeInDB = tableDBService.getChildDictionaryByPCodeInDB(dictionaryItem.getCode());
            processOrder(childDictionaryByPCodeInDB);
            Map<String, DictionaryItem> childItems = new LinkedHashMap<>(10);
            //默认选中的选项
            Map<String, DictionaryItem> defaultSelectItems = new LinkedHashMap<>(10);
            for (DictionaryItem dictionaryItem1 : childDictionaryByPCodeInDB) {
                childItems.put(dictionaryItem1.getName(), dictionaryItem1);

                if (childCode.contains(dictionaryItem1.getCode())) {
                    defaultSelectItems.put(dictionaryItem1.getName(), dictionaryItem1);
                }
            }


            MultiSelectFlexLayout facilityProblemView = new MultiSelectFlexLayout(context);
            facilityProblemView.addItemsByDictionaryItem(dictionaryItem.getName(), dictionaryItem.getCode(), childItems, defaultSelectItems);
            mMultiSelectFlexLayoutList.add(facilityProblemView);
            container.addView(facilityProblemView);
        }
    }


    /**
     * 排水口
     *
     * @param context
     * @param container
     */
    public FacilityProblemView(Context context, ViewGroup container, String childCode) {

//        if (TextUtils.isEmpty(childCode)) {
//            initView(context, container);
//            return;
//        }

        mMultiSelectFlexLayoutList = new ArrayList<>();
        TableDBService tableDBService = new TableDBService(context.getApplicationContext());
//        List<DictionaryItem> a177 = tableDBService.getDictionaryByTypecodeInDB(DICTIONARY_PSK_CODE);
//        processOrder(a177);
//        for (DictionaryItem dictionaryItem : a177) {
        List<DictionaryItem> childDictionaryByPCodeInDB = tableDBService.getChildDictionaryByPCodeInDB(DICTIONARY_PSK_CODE);
        processOrder(childDictionaryByPCodeInDB);
        Map<String, DictionaryItem> childItems = new LinkedHashMap<>(10);
        //默认选中的选项
        Map<String, DictionaryItem> defaultSelectItems = new LinkedHashMap<>(10);
        for (DictionaryItem dictionaryItem1 : childDictionaryByPCodeInDB) {
            childItems.put(dictionaryItem1.getName(), dictionaryItem1);

            if (!TextUtils.isEmpty(childCode) && childCode.contains(dictionaryItem1.getCode())) {
                if (dictionaryItem1.getName().contains("其他")) {
                    if (childCode.contains("其他")) {
                        String textOther = childCode.split("其他：")[1];
                        dictionaryItem1.setCode(dictionaryItem1.getCode()+",其他：" + textOther);
                    }
                }

                defaultSelectItems.put(dictionaryItem1.getName(), dictionaryItem1);

            }


        }

        MultiSelectFlexLayout facilityProblemView = new MultiSelectFlexLayout(context);
        facilityProblemView.setShouldShowPopup(false);

        facilityProblemView.addItemsByDictionaryItem("排水口问题", DICTIONARY_PSK_CODE, childItems, defaultSelectItems);
        mMultiSelectFlexLayoutList.add(facilityProblemView);
        container.addView(facilityProblemView);
//        }
    }


    public void reset() {

        if (ListUtil.isEmpty(mMultiSelectFlexLayoutList)) {
            return;
        }

        for (MultiSelectFlexLayout multiSelectFlexLayout : mMultiSelectFlexLayoutList) {
            multiSelectFlexLayout.reset();
        }

    }


    /**
     * 获取到选择的小类名称
     *
     * @return
     */
    public List<Map<String, Object>> getSelectedSmallItems() {
        if (ListUtil.isEmpty(mMultiSelectFlexLayoutList)) {
            return null;
        }
        List<Map<String, Object>> dictionaryItems = new ArrayList<>();
        for (MultiSelectFlexLayout multiSelectFlexLayout : mMultiSelectFlexLayoutList) {
            Map<String, Object> selectedItems = multiSelectFlexLayout.getSelectedItems();
            if (!MapUtils.isEmpty(selectedItems)) {
                dictionaryItems.add(selectedItems);
            }
        }
        return dictionaryItems;
    }

    /**
     * 获取到选择的大小类名称
     *
     * @return 集合，key是大类的code，value是选中的小类的code集合
     */
    public List<Map<String, List<String>>> getSelectedParentAndChildItem() {
        if (ListUtil.isEmpty(mMultiSelectFlexLayoutList)) {
            return null;
        }
        List<Map<String, List<String>>> result = new ArrayList<>();

        for (MultiSelectFlexLayout multiSelectFlexLayout : mMultiSelectFlexLayoutList) {

            if (!StringUtil.isEmpty(multiSelectFlexLayout.getTitleCode())) {

                /**
                 * 选中的子类集合，key 是子类的名称，value 是子类对象，这里是DictionaryItem
                 */
                Map<String, Object> selectedChildDictionaries = multiSelectFlexLayout.getSelectedItems();

                if (!MapUtils.isEmpty(selectedChildDictionaries)) {

                    /**
                     * key 是 选中的子类所属的父类code，value是选中的子类的code集合
                     */
                    Map<String, List<String>> pCodeAndChildCodeMap = new HashMap<>(1);
                    List<String> childCodes = new ArrayList<>();
                    completeChildCodes(selectedChildDictionaries, childCodes);
                    pCodeAndChildCodeMap.put(multiSelectFlexLayout.getTitleCode(), childCodes);
                    result.add(pCodeAndChildCodeMap);
                }
            } else {
                LogUtil.d("设施问题：titleCode 为空");
            }
        }
        return result;
    }

    /**
     * 返回父类编码和子类编码
     *
     * @return 父类编码和子类编码的集合 ， 第 0 个是父类编码，用","分割 ； 第 1 个 是子类编码，用","分割；
     */
    public List<String> getSelectedParentAndChildICodeList() {

        if (ListUtil.isEmpty(mMultiSelectFlexLayoutList)) {
            return null;
        }
        String pCode = "";
        String childCodes = "";

        for (MultiSelectFlexLayout multiSelectFlexLayout : mMultiSelectFlexLayoutList) {

            if (!StringUtil.isEmpty(multiSelectFlexLayout.getTitleCode())) {

                /**
                 * 选中的子类集合，key 是子类的名称，value 是子类对象，这里是DictionaryItem
                 */
                Map<String, Object> selectedChildDictionaries = multiSelectFlexLayout.getSelectedItems();

                if (!MapUtils.isEmpty(selectedChildDictionaries)) {

                    /**
                     * key 是 选中的子类所属的父类code，value是选中的子类的code集合
                     */
                    pCode += multiSelectFlexLayout.getTitleCode() + ",";
                    Set<Map.Entry<String, Object>> entries = selectedChildDictionaries.entrySet();
                    for (Map.Entry<String, Object> entry : entries) {
                        Object o = entry.getValue();
                        if (o instanceof DictionaryItem) {
                            DictionaryItem value = (DictionaryItem) entry.getValue();
                            childCodes += value.getCode() + ",";
                        }
                    }
                }
            } else {
                LogUtil.d("设施问题：titleCode 为空");
            }
        }

        List<String> result = new ArrayList<>(2);

        if (pCode.length() > 1) {
            //截掉最后一个","
            pCode = pCode.substring(0, pCode.length() - 1);
        }
        result.add(pCode);

        if (childCodes.length() > 1) {
            //截掉最后一个","
            childCodes = childCodes.substring(0, childCodes.length() - 1);
        }
        result.add(childCodes);

        return result;
    }

    private void completeChildCodes(Map<String, Object> selectedChildDictionaries, List<String> childCodes) {
        Set<Map.Entry<String, Object>> entries = selectedChildDictionaries.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object o = entry.getValue();
            if (o instanceof DictionaryItem) {
                DictionaryItem value = (DictionaryItem) entry.getValue();
                childCodes.add(value.getCode());
            }
        }
    }


    /**
     * 排序下拉字典数组
     *
     * @param list
     */
    private static void processOrder(List<DictionaryItem> list) {


        /**
         * 判断所有字典的value 字段是否可以转成整数，如果可以，那么用value字段进行排序，否则，那么采用编码进行排序
         */
        boolean ifAllCanTransToInteger = true;
        for (DictionaryItem dictionaryItem : list) {
            if (!ifAllCanTransToInteger) {
                break;
            }
            String value = dictionaryItem.getValue();
            try {
                Integer.valueOf(value);
            } catch (Exception e) {
                ifAllCanTransToInteger = false;
            }

        }

        if (ifAllCanTransToInteger) {
            /**
             * 采用value字段进行排序
             */
            orderByValue(list);

        } else {
            /**
             * 采用编码进行排序
             */
            orderByCode(list);
        }

    }

    private static void orderByValue(List<DictionaryItem> list) {
        Collections.sort(list, new Comparator<DictionaryItem>() {
            @Override
            public int compare(DictionaryItem o1, DictionaryItem o2) {

                int num1 = Integer.valueOf(o1.getValue());
                int num2 = Integer.valueOf(o2.getValue());
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

    private static void orderByCode(List<DictionaryItem> list) {
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

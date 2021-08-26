package com.augurit.agmobile.gzps.uploadfacility.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.common.facilityownership.FacilityOwnerShipUnitView;
import com.augurit.agmobile.gzps.common.widget.SpinnerTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.PaifangKouAttributes;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.ReadOnlyAttributeView;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;
import com.augurit.am.fw.db.AMDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.upload.util
 * @createTime 创建时间 ：17/12/18
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/18
 * @modifyMemo 修改备注：
 */

public class PaifangKouAttributeViewUtil {

    public static final String ATTRIBUTE_ONE = "天气情况";

    public static final String ATTRIBUTE_TWO = "排水状态";

    public static final String ATTRIBUTE_THREE = "有无挂牌";
    public static final String ATTRIBUTE_FOUR = "测流方法";
    public static final String ATTRIBUTE_FIVE = "测流结果";
    public static final String ATTRIBUTE_SIX = "氨氮浓度";
    public static final String ATTRIBUTE_SEVEN = "ph值";


    public static final String ATTRIBUTE_ONE_KEY = ComponentFieldKeyConstant.RIVER;
    public static final String ATTRIBUTE_TWO_KEY = ComponentFieldKeyConstant.SORT;
    public static final String ATTRIBUTE_THREE_KEY = ComponentFieldKeyConstant.OWNERDEPT;


    private PaifangKouAttributes paifangKouAttributes;

    private FacilityOwnerShipUnitView facilityOwnerShipUnitView;
    private TextItemTableItem attributeOneItem;

    private Map<String, String> originAttributeValue = new HashMap<>(5);


    /**
     * 排序下拉字典数组
     *
     * @param list
     */
    private static void processOrder(List<DictionaryItem> list) {
        for (DictionaryItem item : list) {
            String code = item.getCode();
            String target = code;
            if (code.length() > 1) {
                target = code.replaceAll("[^(0-9)]", "");//去掉所有字母
            }
            item.setCode(target);
        }
        //再排序
        Collections.sort(list, new Comparator<DictionaryItem>() {
            @Override
            public int compare(DictionaryItem t1, DictionaryItem t2) {
                int num1 = Integer.valueOf(t1.getCode());
                int num2 = Integer.valueOf(t2.getCode());
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


    public void addYushuikouAttributes(Context context, Map<String, Object> originValue, ViewGroup attributeContainer,
                                       Map<String, String> defaultSelectedValue) {

        this.paifangKouAttributes = new PaifangKouAttributes();

        if (defaultSelectedValue != null) {
            addAttributeOne(context, originValue, defaultSelectedValue.get(ATTRIBUTE_ONE), attributeContainer);
            addAttributeTwo(context, originValue, defaultSelectedValue.get(ATTRIBUTE_TWO), attributeContainer);
            addAttributeThree(context, originValue, defaultSelectedValue.get(ATTRIBUTE_THREE), attributeContainer);
        } else {
            addAttributeOne(context, originValue, null, attributeContainer);
            addAttributeTwo(context, originValue, null, attributeContainer);
            addAttributeThree(context, originValue, null, attributeContainer);
        }

    }


    /***
     * 属性3
     *
     * @param context
     * @param originValue 设施图层中的属性，当不是核准时，为null
     * @param
     */
    private void addAttributeThree(Context context,
                                   Map<String, Object> originValue, String defaultValue, ViewGroup ll_attributelist_container) {
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_THREE);
        String loginName = new LoginRouter(context, AMDatabase.getInstance()).getUser().getLoginName();
//        String text = "";
//        if (componentOriginValue == null) {
//            if (defaultValue != null) {
//                text = defaultValue;
//            }
//        } else {
//            text = componentOriginValue;
//        }
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_THREE, componentOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_THREE, defaultValue);
        }


        String text = "";
        if (defaultValue == null) {
            if (componentOriginValue != null) {
                text = componentOriginValue;
            }
            //facilityOwnerShipUnitView = new FacilityOwnerShipUnitView(context, loginName, defaultValue);
        } else {
            text = defaultValue;
        }
        facilityOwnerShipUnitView = new FacilityOwnerShipUnitView(context, loginName, text);
        facilityOwnerShipUnitView.addTo(ll_attributelist_container);
        //originAttributeValue.put(ATTRIBUTE_THREE, text);
        paifangKouAttributes.setAttributeThree(text);
    }


    /**
     * 属性二
     *
     * @param context
     * @param originValue 设施图层中的属性，当不是核准时，为null
     * @param container
     */
    private void addAttributeTwo(final Context context, final Map<String, Object> originValue, String defaultSelectedValue, ViewGroup container) {

        TableDBService dbService = new TableDBService(context);
        List<DictionaryItem> a163 = dbService.getDictionaryByTypecodeInDB("A163");
        processOrder(a163);

        List<String> allowValues = new ArrayList<>();
        final Map<String, Object> spinnerData = new LinkedHashMap<>();
        for (DictionaryItem dictionaryItem : a163) {
            allowValues.add(dictionaryItem.getName());
            spinnerData.put(dictionaryItem.getName(), dictionaryItem);
        }
        this.paifangKouAttributes.setAttributeTwoAllowValues(allowValues);

        if (defaultSelectedValue != null && " ".equals(defaultSelectedValue)) {
            DictionaryItem dictionaryItem = new DictionaryItem();
            dictionaryItem.setName(" ");
            spinnerData.put(" ", dictionaryItem);
        }

        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_TWO);
        if (componentOriginValue != null && " ".equals(componentOriginValue)) {
            DictionaryItem dictionaryItem = new DictionaryItem();
            dictionaryItem.setName(" ");
            spinnerData.put(" ", dictionaryItem);
        }

        final SpinnerTableItem spinnerTableItem = new SpinnerTableItem(context);
        spinnerTableItem.setSpinnerData(spinnerData);
        spinnerTableItem.setTextViewName(ATTRIBUTE_TWO);
        spinnerTableItem.setRequereTag();
        spinnerTableItem.setOnSpinnerChangeListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, final Object item) {
                /**
                 * 满足以下条件时弹出对话框提示：
                 * 1. 当处于核准
                 * 2. 前后选择的选项不一样时
                 */
                if (originValue != null && paifangKouAttributes != null
                        && paifangKouAttributes.getAttributeTwo() != null
                        && !paifangKouAttributes.getAttributeTwo().equals(((DictionaryItem) item).getName())) {
                    new AlertDialog.Builder(context)
                            .setMessage("是否确认要修改雨污属性?")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    paifangKouAttributes.setAttributeTwo(((DictionaryItem) item).getName());
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    spinnerTableItem.selectItem(paifangKouAttributes.getAttributeTwo());
                                }
                            })
                            .show();
                } else {
                    if(paifangKouAttributes != null) {
                        paifangKouAttributes.setAttributeTwo(((DictionaryItem) item).getName());
                    }
                }

            }
        });
        /**
         * 先判断OriginValue是否有值，如果有，那么将其放入originAttributeValue中，此时如果defaultValue没有值，那么默认的下拉框显示的就是originValue，
         * 如果defaultValue有值，显示的是defaultValue；
         */
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_TWO, componentOriginValue);
            if (defaultSelectedValue == null) {
                boolean b = spinnerTableItem.selectItem(componentOriginValue);
                if (!b) {
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
                    spinnerTableItem.put(componentOriginValue, item);
                    spinnerTableItem.selectItem(componentOriginValue);
                }
                paifangKouAttributes.setAttributeTwo(componentOriginValue);
            } else {
                boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
                if (!b) {
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
                    spinnerTableItem.put(componentOriginValue, item);
                    spinnerTableItem.selectItem(componentOriginValue);
                }
                paifangKouAttributes.setAttributeTwo(defaultSelectedValue);
            }
        } else if (defaultSelectedValue != null) {
            //如果originValue为空，但是defaultValue不为空
            boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
            if (!b) {
                DictionaryItem item = new DictionaryItem();
                item.setName(componentOriginValue);
                spinnerTableItem.put(componentOriginValue, item);
                spinnerTableItem.selectItem(componentOriginValue);

            }
            originAttributeValue.put(ATTRIBUTE_TWO, defaultSelectedValue);
            paifangKouAttributes.setAttributeTwo(defaultSelectedValue);
        } else {
            spinnerTableItem.selectItem(0);
        }


//        if (defaultSelectedValue != null) {
//            spinnerTableItem.selectItem(defaultSelectedValue);
//            originAttributeValue.put(ATTRIBUTE_TWO, defaultSelectedValue);
//            paifangKouAttributes.setAttributeTwo(defaultSelectedValue);
//        } else {
//
//            if (!TextUtils.isEmpty(componentOriginValue)) {
//                spinnerTableItem.selectItem(componentOriginValue);
//                originAttributeValue.put(ATTRIBUTE_TWO, componentOriginValue);
//                paifangKouAttributes.setAttributeTwo(componentOriginValue);
//            } else {
//                spinnerTableItem.selectItem(0);
//            }
//        }
        container.addView(spinnerTableItem);
    }


    /**
     * 属性1
     *
     * @param originMap 设施图层中的属性，当不是核准时，为null
     */
    private void addAttributeOne(Context context, Map<String, Object> originMap, String defaultValue, ViewGroup ll_attributelist_container) {
        //说明是文本框
        attributeOneItem = new TextItemTableItem(context);
        attributeOneItem.setTextViewName(ATTRIBUTE_ONE);
        attributeOneItem.setRequireTag();
        ll_attributelist_container.addView(attributeOneItem);
        String componentOriginValue = getComponentOriginValue(originMap, ATTRIBUTE_ONE);
//        String text = "";
//        if (componentOriginValue == null) {
//            if (defaultValue != null) {
//                text = defaultValue;
//            }
//        } else {
//            text = componentOriginValue;
//        }
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_ONE, componentOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_ONE, defaultValue);
        }


        String text = "";
        if (defaultValue == null) {
            if (componentOriginValue != null) {
                text = componentOriginValue;
            }
            //facilityOwnerShipUnitView = new FacilityOwnerShipUnitView(context, loginName, defaultValue);
        } else {
            text = defaultValue;
        }
        attributeOneItem.setText(text);
        //originAttributeValue.put(ATTRIBUTE_ONE, componentOriginValue);
        paifangKouAttributes.setAttributeOne(text);
    }


    public static String getComponentOriginValue(Map<String, Object> originValue, String attributeName) {

        if (originValue == null) {
            return null;
        }
        /**
         * 排水口
         */
        if (attributeName.contains(ATTRIBUTE_ONE)) {
            Object o = originValue.get(ATTRIBUTE_ONE_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_TWO)) {
            Object o = originValue.get(ATTRIBUTE_TWO_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_THREE)) {
            Object o = originValue.get(ATTRIBUTE_THREE_KEY);
            if (o != null) {
                return o.toString();
            }
        }
        return null;
    }


    public PaifangKouAttributes getPaifangKouAttributes() {

        if (facilityOwnerShipUnitView != null) {
            paifangKouAttributes.setAttributeThree(facilityOwnerShipUnitView.getText());
        }

        if (attributeOneItem != null) {
            paifangKouAttributes.setAttributeOne(attributeOneItem.getText());
        }

        //对比属性
        if (paifangKouAttributes.getAttributeOne() != null && !paifangKouAttributes.getAttributeOne().equals(originAttributeValue.get(ATTRIBUTE_ONE))) {
            paifangKouAttributes.setHasModified(true);
        } else if (paifangKouAttributes.getAttributeTwo() != null && !paifangKouAttributes.getAttributeTwo().equals(originAttributeValue.get(ATTRIBUTE_TWO))) {
            paifangKouAttributes.setHasModified(true);
        } else if (paifangKouAttributes.getAttributeThree() != null && !paifangKouAttributes.getAttributeThree().equals(originAttributeValue.get(ATTRIBUTE_THREE))) {
            paifangKouAttributes.setHasModified(true);
        }
        return paifangKouAttributes;
    }

    public PaifangKouAttributes getOriginalPaifangKouAttributes() {

        PaifangKouAttributes paifangKouAttributes = new PaifangKouAttributes();
        String attributeOne = originAttributeValue.get(ATTRIBUTE_ONE);
        if (attributeOne != null) {
            paifangKouAttributes.setAttributeOne(attributeOne);
        }

        String attributeTwo = originAttributeValue.get(ATTRIBUTE_TWO);
        if (attributeTwo != null) {
            paifangKouAttributes.setAttributeTwo(attributeTwo);
        }


        String attributeThree = originAttributeValue.get(ATTRIBUTE_THREE);
        if (attributeThree != null) {
            paifangKouAttributes.setAttributeThree(attributeThree);
        }

        return paifangKouAttributes;

    }


    public static void addReadOnlyAttributes(Context context,
                                             Map<String, Object> originValue,
                                             PaifangKouAttributes paifangKouAttributes,
                                             ViewGroup llContainer) {
        //属性一
        ReadOnlyAttributeView readOnlyAttributeView = new ReadOnlyAttributeView(context,
                ATTRIBUTE_ONE, paifangKouAttributes.getAttributeOne());
        readOnlyAttributeView.addTo(llContainer);

        //属性二
//        ReadOnlyAttributeView readOnlyAttributeView2 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_TWO, paifangKouAttributes.getAttributeTwo());
//        readOnlyAttributeView2.addTo(llContainer);
//        //属性3
//        ReadOnlyAttributeView readOnlyAttributeView3 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_THREE, paifangKouAttributes.getAttributeThree());
//        readOnlyAttributeView3.addTo(llContainer);

        if (!TextUtils.isEmpty(paifangKouAttributes.getAttributeFour())) {
            //属性4
            ReadOnlyAttributeView readOnlyAttributeView4 = new ReadOnlyAttributeView(context,
                    ATTRIBUTE_FOUR, paifangKouAttributes.getAttributeFour());
            readOnlyAttributeView4.addTo(llContainer);

            //属性5
            ReadOnlyAttributeView readOnlyAttributeView5 = new ReadOnlyAttributeView(context,
                    ATTRIBUTE_FIVE, paifangKouAttributes.getAttributeFive() + "L/min");
            readOnlyAttributeView5.addTo(llContainer);
        } else {
            //属性4
            ReadOnlyAttributeView readOnlyAttributeView4 = new ReadOnlyAttributeView(context,
                    ATTRIBUTE_FOUR, "未测");
            readOnlyAttributeView4.addTo(llContainer);
            //属性5
            ReadOnlyAttributeView readOnlyAttributeView5 = new ReadOnlyAttributeView(context,
                    ATTRIBUTE_FIVE, "未测");
            readOnlyAttributeView5.addTo(llContainer);
        }

        if (!TextUtils.isEmpty(paifangKouAttributes.getAttributeSix())) {
            //属性6
            ReadOnlyAttributeView readOnlyAttributeView6 = new ReadOnlyAttributeView(context,
                    ATTRIBUTE_SIX, paifangKouAttributes.getAttributeSix() + "mg/L");
            readOnlyAttributeView6.addTo(llContainer);
            //属性7
            ReadOnlyAttributeView readOnlyAttributeView7 = new ReadOnlyAttributeView(context,
                    ATTRIBUTE_SEVEN, paifangKouAttributes.getAttributeSeven());
            readOnlyAttributeView7.addTo(llContainer);
        } else {
            //属性6
            ReadOnlyAttributeView readOnlyAttributeView6 = new ReadOnlyAttributeView(context,
                    ATTRIBUTE_SIX, "未测");
            readOnlyAttributeView6.addTo(llContainer);
            //属性7
            ReadOnlyAttributeView readOnlyAttributeView7 = new ReadOnlyAttributeView(context,
                    ATTRIBUTE_SEVEN, "未测");
            readOnlyAttributeView7.addTo(llContainer);
        }

    }


    public static void addReadOnlyAttributes(Context context,
                                             PaifangKouAttributes paifangKouAttributes,
                                             ViewGroup llContainer) {
        //属性一

        ReadOnlyAttributeView readOnlyAttributeView = new ReadOnlyAttributeView(context,
                ATTRIBUTE_ONE, paifangKouAttributes.getAttributeOne());
        readOnlyAttributeView.addTo(llContainer);

        //属性二

        ReadOnlyAttributeView readOnlyAttributeView2 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_TWO, paifangKouAttributes.getAttributeTwo());
        readOnlyAttributeView2.addTo(llContainer);


        //属性3

        ReadOnlyAttributeView readOnlyAttributeView3 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_THREE, paifangKouAttributes.getAttributeThree());
        readOnlyAttributeView3.addTo(llContainer);

    }


    public static Map<String, String> getDefaultValue(ModifiedFacility modifiedFacility) {

        Map<String, String> defaultSelectValue = new HashMap<>(5);
        defaultSelectValue.put(ATTRIBUTE_ONE, modifiedFacility.getAttrOne());
        defaultSelectValue.put(ATTRIBUTE_TWO, modifiedFacility.getAttrTwo());
        defaultSelectValue.put(ATTRIBUTE_THREE, modifiedFacility.getAttrThree());
        return defaultSelectValue;
    }

    public static Map<String, Object> getOriginValue(ModifiedFacility modifiedFacility) {

        Map<String, Object> originValue = new HashMap<>(5);
        if (modifiedFacility.getOriginAttrOne() != null) {
            originValue.put(ATTRIBUTE_ONE_KEY, modifiedFacility.getOriginAttrOne());
        } else {
            originValue.put(ATTRIBUTE_ONE_KEY, "");
        }

        if (modifiedFacility.getOriginAttrTwo() != null) {
            originValue.put(ATTRIBUTE_TWO_KEY, modifiedFacility.getOriginAttrTwo());
        } else {
            originValue.put(ATTRIBUTE_TWO_KEY, "");
        }

        if (modifiedFacility.getOriginAttrThree() != null) {
            originValue.put(ATTRIBUTE_THREE_KEY, modifiedFacility.getOriginAttrThree());
        } else {
            originValue.put(ATTRIBUTE_THREE_KEY, "");
        }

        return originValue;
    }

    public void onDestroy() {
        if (facilityOwnerShipUnitView != null) {
            facilityOwnerShipUnitView.onDestroy();
            facilityOwnerShipUnitView = null;
        }
    }
}

package com.augurit.agmobile.gzps.uploadfacility.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.facilityownership.FacilityOwnerShipUnitView;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.YuShuiKouAttributes;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.ReadOnlySfHxView;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.fw.utils.StringUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

public class YushuikouAttributeViewUtil {

//    public static final String ATTRIBUTE_ONE = "特征";
//
//    public static final String ATTRIBUTE_THREE = "形式";
//
//    public static final String ATTRIBUTE_FOUR = "权属单位";


    public static final String ATTRIBUTE_ONE_KEY = ComponentFieldKeyConstant.FEATURE;

    public static final String ATTRIBUTE_THREE_KEY = ComponentFieldKeyConstant.STYLE;
    public static final String ATTRIBUTE_SFHXN_KEY = ComponentFieldKeyConstant.SFPSDYHXN;
    public static final String ATTRIBUTE_FOUR_KEY = ComponentFieldKeyConstant.OWNERDEPT;
    public static final String ATTRIBUTE_SFHX = "红线内设施";

    private YuShuiKouAttributes yuShuiKouAttributes;

    private FacilityOwnerShipUnitView facilityOwnerShipUnitView;

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

    /**
     * 是否属于排水单元红线内
     * @param context
     * @param originValue
     * @param defaultSelectedValue
     * @param attributeContainer
     * @param uploadedFacility
     */
    private void addAttributeSfHX(Context context, Map<String, Object> originValue, String defaultSelectedValue, ViewGroup attributeContainer, Object uploadedFacility) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_attribute_sfhx, null);
        final CheckBox cb_yes = (CheckBox) view.findViewById(R.id.cb_yes);
        ((TextView) view.findViewById(R.id.tv_name)).setText(ATTRIBUTE_SFHX);
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_SFHX);
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_SFHX, componentOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_SFHX, defaultSelectedValue);
        }
        String text = "";
        if (defaultSelectedValue == null) {
            if (componentOriginValue != null) {
                text = componentOriginValue;
            }
        } else {
            text = defaultSelectedValue;
        }
        if(uploadedFacility!=null){
            if(uploadedFacility instanceof UploadedFacility){
                UploadedFacility facility = (UploadedFacility) uploadedFacility;
                text = facility.getSfpsdyhxn();
            }else if(uploadedFacility instanceof ModifiedFacility){
                ModifiedFacility facility = (ModifiedFacility) uploadedFacility;
                text = facility.getSfpsdyhxn();
            }
        }

        if ("0".equals(text)
                || StringUtil.isEmpty(text)) {
            yuShuiKouAttributes.setSfpsdyhxn("0");
        } else {
            yuShuiKouAttributes.setSfpsdyhxn("1");
        }

        cb_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    yuShuiKouAttributes.setSfpsdyhxn("1");
                } else {
                    yuShuiKouAttributes.setSfpsdyhxn("0");
                }
            }
        });
        if ("0".equals(text)
                || StringUtil.isEmpty(text)) {
            cb_yes.setChecked(false);
        } else {
            cb_yes.setChecked(true);
        }
        attributeContainer.addView(view);
    }


    public void addYushuikouAttributes(Context context, Map<String, Object> originValue, ViewGroup attributeContainer,
                                       Map<String, String> defaultSelectedValue) {

        this.yuShuiKouAttributes = new YuShuiKouAttributes();

        if (defaultSelectedValue != null) {
            addAttributeSfHX(context, originValue, defaultSelectedValue.get(ATTRIBUTE_SFHX), attributeContainer, null);
//            addAttributeOne(context, originValue, defaultSelectedValue.get(ATTRIBUTE_ONE), attributeContainer);
//            addAttributeThree(context, originValue, defaultSelectedValue.get(ATTRIBUTE_THREE), attributeContainer);
//            addAttributeFour(context, originValue, defaultSelectedValue.get(ATTRIBUTE_FOUR), attributeContainer);

        } else {
            addAttributeSfHX(context, originValue, null, attributeContainer, null);
//            addAttributeOne(context, originValue, null, attributeContainer);
//            addAttributeThree(context, originValue, null, attributeContainer);
//            addAttributeFour(context, originValue, null, attributeContainer);
        }
    }


    /**
     * 雨水口属性一
     *
     * @param context
     * @param originValue
     * @param container
     */
  /*  private void addAttributeOne(Context context, Map<String, Object> originValue, String defaultSelectedValue, ViewGroup container) {

        TableDBService dbService = new TableDBService(context);
        List<DictionaryItem> a164 = dbService.getDictionaryByTypecodeInDB("A164");
        processOrder(a164);

        List<String> allowValues = new ArrayList<>();
        final Map<String, Object> spinnerData = new LinkedHashMap<>();
        for (DictionaryItem dictionaryItem : a164) {
            allowValues.add(dictionaryItem.getName());
            spinnerData.put(dictionaryItem.getName(), dictionaryItem);
        }
        this.yuShuiKouAttributes.setAttributeOneAllowValues(allowValues);

        if (defaultSelectedValue != null && " ".equals(defaultSelectedValue)) {
            DictionaryItem dictionaryItem = new DictionaryItem();
            dictionaryItem.setName(" ");
            spinnerData.put(" ", dictionaryItem);
        }

        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_ONE);
        if (componentOriginValue != null && " ".equals(componentOriginValue)) {
            DictionaryItem dictionaryItem = new DictionaryItem();
            dictionaryItem.setName(" ");
            spinnerData.put(" ", dictionaryItem);
        }

        SpinnerTableItem spinnerTableItem = new SpinnerTableItem(context);
        spinnerTableItem.setSpinnerData(spinnerData);
        spinnerTableItem.setTextViewName(ATTRIBUTE_ONE);
        spinnerTableItem.setRequereTag();
        spinnerTableItem.setOnSpinnerChangeListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                yuShuiKouAttributes.setAttributeOne(((DictionaryItem) item).getName());
            }
        });

        *//**
         * 先判断OriginValue是否有值，如果有，那么将其放入originAttributeValue中，此时如果defaultValue没有值，那么默认的下拉框显示的就是originValue，
         * 如果defaultValue有值，显示的是defaultValue；
         *//*
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_ONE, componentOriginValue);
            if (defaultSelectedValue == null) {
                boolean b = spinnerTableItem.selectItem(componentOriginValue);
                if (!b){
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
                    spinnerTableItem.put(componentOriginValue, item);
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yuShuiKouAttributes.setAttributeOne(componentOriginValue);
            } else {
                boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
                if (!b){
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
                    spinnerTableItem.put(componentOriginValue, item);
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yuShuiKouAttributes.setAttributeOne(defaultSelectedValue);
            }
        } else if (defaultSelectedValue != null) {
            //如果originValue为空，但是defaultValue不为空
            boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
            if (!b){
                DictionaryItem item = new DictionaryItem();
                item.setName(componentOriginValue);
                spinnerTableItem.put(componentOriginValue, item);
                spinnerTableItem.selectItem(componentOriginValue);

            }
            originAttributeValue.put(ATTRIBUTE_ONE, defaultSelectedValue);
            yuShuiKouAttributes.setAttributeOne(defaultSelectedValue);
        } else {
            spinnerTableItem.selectItem(0);
        }

//        if (defaultSelectedValue != null) {
//            spinnerTableItem.selectItem(defaultSelectedValue);
//            originAttributeValue.put(ATTRIBUTE_ONE, defaultSelectedValue);
//            yuShuiKouAttributes.setAttributeOne(defaultSelectedValue);
//        } else {
//            if (!TextUtils.isEmpty(componentOriginValue)) {
//                spinnerTableItem.selectItem(componentOriginValue);
//                originAttributeValue.put(ATTRIBUTE_ONE, componentOriginValue);
//                yuShuiKouAttributes.setAttributeOne(componentOriginValue);
//            } else {
//                spinnerTableItem.selectItem(0);
//            }
//        }
        container.addView(spinnerTableItem);
    }


    *//***
     * 属性三
     *
     * @param context
     * @param originValue
     *//*
    private void addAttributeThree(Context context, Map<String, Object> originValue, String defaultSelectedValue, ViewGroup container) {
        TableDBService dbService = new TableDBService(context);
        List<DictionaryItem> a165 = dbService.getDictionaryByTypecodeInDB("A165");
        processOrder(a165);

        List<String> allowValues = new ArrayList<>();
        final Map<String, Object> spinnerData = new LinkedHashMap<>();
        for (DictionaryItem dictionaryItem : a165) {
            allowValues.add(dictionaryItem.getName());
            spinnerData.put(dictionaryItem.getName(), dictionaryItem);
        }
        this.yuShuiKouAttributes.setAttributeThreeAllowValues(allowValues);

        if (defaultSelectedValue != null && " ".equals(defaultSelectedValue)) {
            DictionaryItem dictionaryItem = new DictionaryItem();
            dictionaryItem.setName(" ");
            spinnerData.put(" ", dictionaryItem);
        }

        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_THREE);
        if (componentOriginValue != null && " ".equals(componentOriginValue)) {
            DictionaryItem dictionaryItem = new DictionaryItem();
            dictionaryItem.setName(" ");
            spinnerData.put(" ", dictionaryItem);
        }


        SpinnerTableItem spinnerTableItem = new SpinnerTableItem(context);
        spinnerTableItem.setSpinnerData(spinnerData);
        spinnerTableItem.setTextViewName(ATTRIBUTE_THREE);
        spinnerTableItem.setRequereTag();
        spinnerTableItem.setOnSpinnerChangeListener(new AMSpinner.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                yuShuiKouAttributes.setAttributeThree(((DictionaryItem) item).getName());
            }
        });

        *//**
         * 先判断OriginValue是否有值，如果有，那么将其放入originAttributeValue中，此时如果defaultValue没有值，那么默认的下拉框显示的就是originValue，
         * 如果defaultValue有值，显示的是defaultValue；
         *//*
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_THREE, componentOriginValue);
            if (defaultSelectedValue == null) {
                boolean b = spinnerTableItem.selectItem(componentOriginValue);
                if (!b){
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
                    spinnerTableItem.put(componentOriginValue, item);
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yuShuiKouAttributes.setAttributeThree(componentOriginValue);
            } else {
                boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
                if (!b){
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
                    spinnerTableItem.put(componentOriginValue, item);
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yuShuiKouAttributes.setAttributeThree(defaultSelectedValue);
            }
        } else if (defaultSelectedValue != null) {
            //如果originValue为空，但是defaultValue不为空
            boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
            if (!b){
                DictionaryItem item = new DictionaryItem();
                item.setName(componentOriginValue);
                spinnerTableItem.put(componentOriginValue, item);
                spinnerTableItem.selectItem(componentOriginValue);

            }
            originAttributeValue.put(ATTRIBUTE_THREE, defaultSelectedValue);
            yuShuiKouAttributes.setAttributeThree(defaultSelectedValue);
        } else {
            spinnerTableItem.selectItem(0);
        }

//        if (defaultSelectedValue != null) {
//            spinnerTableItem.selectItem(defaultSelectedValue);
//            originAttributeValue.put(ATTRIBUTE_THREE, defaultSelectedValue);
//            yuShuiKouAttributes.setAttributeThree(defaultSelectedValue);
//        } else {
//
//            if (!TextUtils.isEmpty(componentOriginValue)) {
//                spinnerTableItem.selectItem(componentOriginValue);
//                originAttributeValue.put(ATTRIBUTE_THREE, componentOriginValue);
//                yuShuiKouAttributes.setAttributeThree(componentOriginValue);
//            } else {
//                spinnerTableItem.selectItem(0);
//            }
//        }
        container.addView(spinnerTableItem);
    }

    *//***
     * 属性四
     *
     * @param
     * @param
     * @param
     *//*
    private void addAttributeFour(Context context,
                                  Map<String, Object> originValue, String defaultValue, ViewGroup ll_attributelist_container) {
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_FOUR);
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
            originAttributeValue.put(ATTRIBUTE_FOUR, componentOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_FOUR, defaultValue);
        }


        String text = "";
        if (defaultValue == null) {
            if (componentOriginValue != null) {
                text = componentOriginValue;
            }
        } else {
            text = defaultValue;
        }

        facilityOwnerShipUnitView = new FacilityOwnerShipUnitView(context, loginName, text);
        facilityOwnerShipUnitView.addTo(ll_attributelist_container);
        //originAttributeValue.put(ATTRIBUTE_FOUR, text);
        yuShuiKouAttributes.setAttributeFour(text);
    }
*/




    public static String getComponentOriginValue(Map<String, Object> originValue, String attributeName) {

        if (originValue == null) {
            return null;
        }

        /**
         * 雨水口
         */
//        if (attributeName.contains(ATTRIBUTE_ONE)) {
//            Object o = originValue.get(ATTRIBUTE_ONE_KEY);
//            if (o != null) {
//                return o.toString();
//            }
//        } else if (attributeName.contains("雨水篦类型")) {
//            Object o = originValue.get(ComponentFieldKeyConstant.SUBTYPE);
//            if (o != null) {
//                return o.toString();
//            }
//        } else if (attributeName.contains(ATTRIBUTE_THREE)) {
//            Object o = originValue.get(ATTRIBUTE_THREE_KEY);
//            if (o != null) {
//                return o.toString();
//            }
//        } else if (attributeName.contains(ATTRIBUTE_FOUR)) {
//            Object o = originValue.get(ATTRIBUTE_FOUR_KEY);
//            if (o != null) {
//                return o.toString();
//            }
//        }
        return null;
    }


    public YuShuiKouAttributes getYuShuiKouAttributes() {

        if (facilityOwnerShipUnitView != null) {
            yuShuiKouAttributes.setAttributeFour(facilityOwnerShipUnitView.getText());
        }

        //对比属性
       /* if (yuShuiKouAttributes.getAttributeOne() != null && !yuShuiKouAttributes.getAttributeOne().equals(originAttributeValue.get(ATTRIBUTE_ONE))) {
            yuShuiKouAttributes.setHasModified(true);
        } else if (yuShuiKouAttributes.getAttributeThree() != null && !yuShuiKouAttributes.getAttributeThree().equals(originAttributeValue.get(ATTRIBUTE_THREE))) {
            yuShuiKouAttributes.setHasModified(true);
        } else if (yuShuiKouAttributes.getAttributeFour() != null && !yuShuiKouAttributes.getAttributeFour().equals(originAttributeValue.get(ATTRIBUTE_FOUR))) {
            yuShuiKouAttributes.setHasModified(true);
        }*/
        return yuShuiKouAttributes;

    }

    public YuShuiKouAttributes getOriginalYuShuiKouAttributes() {

        YuShuiKouAttributes yuShuiKouAttributes = new YuShuiKouAttributes();
      /*  String attributeOne = originAttributeValue.get(ATTRIBUTE_ONE);
        if (attributeOne != null) {
            yuShuiKouAttributes.setAttributeOne(attributeOne);
        }


        String attributeThree = originAttributeValue.get(ATTRIBUTE_THREE);
        if (attributeThree != null) {
            yuShuiKouAttributes.setAttributeThree(attributeThree);
        }

        String attributeFour = originAttributeValue.get(ATTRIBUTE_FOUR);
        if (attributeFour != null) {
            yuShuiKouAttributes.setAttributeFour(attributeFour);
        }*/


        return yuShuiKouAttributes;

    }


    public static void addReadOnlyAttributes(Context context,
                                             Map<String, Object> originValue,
                                             YuShuiKouAttributes yuShuiKouAttributes,
                                             ViewGroup llContainer) {
        //属性一
      /*  String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_ONE);
        ReadOnlyAttributeView readOnlyAttributeView = new ReadOnlyAttributeView(context,
                ATTRIBUTE_ONE, componentOriginValue, yuShuiKouAttributes.getAttributeOne());
        readOnlyAttributeView.addTo(llContainer);


        //属性3
        String componentOriginValue3 = getComponentOriginValue(originValue, ATTRIBUTE_THREE);
        ReadOnlyAttributeView readOnlyAttributeView3 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_THREE, componentOriginValue3, yuShuiKouAttributes.getAttributeThree());
        readOnlyAttributeView3.addTo(llContainer);

        //属性4
        String componentOriginValue4 = getComponentOriginValue(originValue, ATTRIBUTE_FOUR);
        ReadOnlyAttributeView readOnlyAttributeView4 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_FOUR, componentOriginValue4, yuShuiKouAttributes.getAttributeFour());
        readOnlyAttributeView4.addTo(llContainer);*/
        //是否属于排水单元红线内
        String attributesfHx= getComponentOriginValue(originValue, ATTRIBUTE_SFHX);
        ReadOnlySfHxView sfHxView = new ReadOnlySfHxView(context,ATTRIBUTE_SFHX,attributesfHx,yuShuiKouAttributes.getSfpsdyhxn());
        sfHxView.addTo(llContainer);

    }

    public static void addReadOnlyAttributes(Context context,
                                             YuShuiKouAttributes yuShuiKouAttributes,
                                             ViewGroup llContainer) {
        //属性一
//        ReadOnlyAttributeView readOnlyAttributeView = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_ONE, yuShuiKouAttributes.getAttributeOne());
//        readOnlyAttributeView.addTo(llContainer);
//
//
//        //属性3
//        ReadOnlyAttributeView readOnlyAttributeView3 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_THREE,  yuShuiKouAttributes.getAttributeThree());
//        readOnlyAttributeView3.addTo(llContainer);
//
//        //属性4
//
//        ReadOnlyAttributeView readOnlyAttributeView4 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_FOUR, yuShuiKouAttributes.getAttributeFour());
//        readOnlyAttributeView4.addTo(llContainer);
        //是否属于排水单元红线内
        ReadOnlySfHxView sfHxView = new ReadOnlySfHxView(context,ATTRIBUTE_SFHX,null,yuShuiKouAttributes.getSfpsdyhxn());
        sfHxView.addTo(llContainer);

    }


    public static Map<String,String> getDefaultValue(ModifiedFacility modifiedFacility){

        Map<String,String> defaultSelectValue = new HashMap<>(1);
        defaultSelectValue.put(ATTRIBUTE_SFHX, StringUtil.getNotNullString(modifiedFacility.getSfpsdyhxn(), ""));
        return defaultSelectValue;
    }

    public static Map<String,Object> getOriginValue(ModifiedFacility modifiedFacility){

        Map<String,Object> originValue = new HashMap<>(5);
        if (modifiedFacility.getOriginAttrOne()!= null){
            originValue.put(ATTRIBUTE_ONE_KEY, modifiedFacility.getOriginAttrOne());
        }else {
            originValue.put(ATTRIBUTE_ONE_KEY, "");
        }

        if (modifiedFacility.getOriginAttrThree()!= null){
            originValue.put(ATTRIBUTE_THREE_KEY, modifiedFacility.getOriginAttrThree());
        }else {
            originValue.put(ATTRIBUTE_THREE_KEY, "");
        }
        if (modifiedFacility.getOriginAttrFour()!= null){
            originValue.put(ATTRIBUTE_FOUR_KEY, modifiedFacility.getOriginAttrFour());
        }else {
            originValue.put(ATTRIBUTE_FOUR_KEY, "");
        }
        originValue.put(ATTRIBUTE_SFHXN_KEY, "");
        return originValue;
    }


    public void onDestroy(){
        if (facilityOwnerShipUnitView != null){
            facilityOwnerShipUnitView.onDestroy();
            facilityOwnerShipUnitView = null;
        }
    }

}

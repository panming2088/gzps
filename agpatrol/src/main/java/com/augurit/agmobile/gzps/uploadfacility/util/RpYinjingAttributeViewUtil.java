package com.augurit.agmobile.gzps.uploadfacility.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.OnSpinnerChangedEvent;
import com.augurit.agmobile.gzps.common.facilityownership.FacilityOwnerShipUnitView;
import com.augurit.agmobile.gzps.common.widget.SpinnerTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.YinjingAttributes;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.ReadOnlyAttributeView;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.spinner.AMSpinner;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

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

public class RpYinjingAttributeViewUtil {

    /**
     * 属性名称
     */
    public static final String ATTRIBUTE_ONE = "窨井类型";
    public static final String ATTRIBUTE_TWO = "雨污类别";
    public static final String ATTRIBUTE_THREE = "井盖材质";
    public static final String ATTRIBUTE_FOUR = "权属单位";
    public static final String ATTRIBUTE_FIVE = "已挂牌编号";

    /**
     * 属性在设施图层中的key
     */
    public static final String ATTRIBUTE_ONE_KEY = ComponentFieldKeyConstant.SUBTYPE;
    public static final String ATTRIBUTE_TWO_KEY = ComponentFieldKeyConstant.SORT;
    public static final String ATTRIBUTE_THREE_KEY = ComponentFieldKeyConstant.MATERIAL;
    public static final String ATTRIBUTE_FOUR_KEY = ComponentFieldKeyConstant.OWNERDEPT;
    public static final String ATTRIBUTE_FIVE_KEY = ComponentFieldKeyConstant.LICENSE;

    private YinjingAttributes yinjingAttributes;

    private FacilityOwnerShipUnitView facilityOwnerShipUnitView;

//    private TextItemTableItem attributeFiveItem;

    private EditText attributeFiveItem;
    private boolean fiveItemChecked = true;

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


    public void addYinjingAttributes(Context context, Map<String, Object> originValue, ViewGroup attributeContainer,
                                     Map<String, String> defaultSelectedValue) {

        this.yinjingAttributes = new YinjingAttributes();

//        if (defaultSelectedValue != null) {
//            addAttributeOne(context, originValue, defaultSelectedValue.get(ATTRIBUTE_ONE), attributeContainer);
//            addAttributeOne(context, originValue, defaultSelectedValue.get(ATTRIBUTE_ONE), attributeContainer);
//            addAttributeTwo(context, originValue, defaultSelectedValue.get(ATTRIBUTE_TWO), attributeContainer);
//            addAttributeThree(context, originValue, defaultSelectedValue.get(ATTRIBUTE_THREE), attributeContainer);
//            addAttributeFour(context, originValue, defaultSelectedValue.get(ATTRIBUTE_FOUR), attributeContainer);
//            addAttributeFive(context, originValue, defaultSelectedValue.get(ATTRIBUTE_FIVE), attributeContainer);
//
//        } else {
//            addAttributeOne(context, originValue, null, attributeContainer);
//            addAttributeTwo(context, originValue, null, attributeContainer);
//            addAttributeThree(context, originValue, null, attributeContainer);
//            addAttributeFour(context, originValue, null, attributeContainer);
//            addAttributeFive(context, originValue, null, attributeContainer);
//        }
        if (defaultSelectedValue != null) {
            addAttributeOne(context, originValue, defaultSelectedValue.get(ATTRIBUTE_ONE), attributeContainer, defaultSelectedValue);
//            addAttributeThree(context, originValue, defaultSelectedValue.get(ATTRIBUTE_THREE), attributeContainer);
//            addAttributeFour(context, originValue, defaultSelectedValue.get(ATTRIBUTE_FOUR), attributeContainer);
//            addAttributeSix(context, originValue, defaultSelectedValue.get(ATTRIBUTE_SIX), attributeContainer);

        } else {
            addAttributeOne(context, originValue, null, attributeContainer, defaultSelectedValue);
//            addAttributeThree(context, originValue, null, attributeContainer);
//            addAttributeFour(context, originValue, null, attributeContainer);
//            addAttributeSix(context, originValue, null, attributeContainer);
        }


    }


    /**
     * 窨井属性一
     *
     * @param context
     * @param originValue          设施图层中的属性，当不是核准时，为null
     * @param defaultSelectedValue 默认选中的值
     * @param container
     */
    private void addAttributeOne(Context context, Map<String, Object> originValue, String defaultSelectedValue, ViewGroup container) {

        TableDBService dbService = new TableDBService(context);
        List<DictionaryItem> a158 = dbService.getDictionaryByTypecodeInDB("A158");
        processOrder(a158);

        List<String> allowValues = new ArrayList<>();
        final Map<String, Object> spinnerData = new LinkedHashMap<>();
        for (DictionaryItem dictionaryItem : a158) {
            allowValues.add(dictionaryItem.getName());
            spinnerData.put(dictionaryItem.getName(), dictionaryItem);
        }

        this.yinjingAttributes.setAttributeOneAllowValues(allowValues);

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
                DictionaryItem dictionaryItem = ((DictionaryItem) item);
                yinjingAttributes.setAttributeOne(dictionaryItem.getName());
                EventBus.getDefault().post(new OnSpinnerChangedEvent(ATTRIBUTE_ONE, dictionaryItem.getName(), dictionaryItem));
            }
        });


        /**
         * 先判断OriginValue是否有值，如果有，那么将其放入originAttributeValue中，此时如果defaultValue没有值，那么默认的下拉框显示的就是originValue，
         * 如果defaultValue有值，显示的是defaultValue；
         */

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
                yinjingAttributes.setAttributeOne(componentOriginValue);
            } else {
                boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
                if (!b){
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
                    spinnerTableItem.put(componentOriginValue, item);
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yinjingAttributes.setAttributeOne(defaultSelectedValue);
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
            yinjingAttributes.setAttributeOne(defaultSelectedValue);
        } else {
            spinnerTableItem.selectItem(0);
        }
        container.addView(spinnerTableItem);
    }

    /***
     * 属性二
     *
     * @param context
     * @param originValue 设施图层中的属性，当不是核准时，为null
     */
    private void addAttributeTwo(final Context context, final Map<String, Object> originValue, String defaultSelectedValue, ViewGroup container) {

        TableDBService dbService = new TableDBService(context);
        List<DictionaryItem> a163 = dbService.getDictionaryByTypecodeInDB("A163");
        processOrder(a163);

        final Map<String, Object> spinnerData = new LinkedHashMap<>();
        List<String> allowValues = new ArrayList<>();
        for (DictionaryItem dictionaryItem : a163) {
            allowValues.add(dictionaryItem.getName());
            spinnerData.put(dictionaryItem.getName(), dictionaryItem);
        }
        this.yinjingAttributes.setAttributeTwoAllowValues(allowValues);

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
                 * 1. 当初处于核准
                 * 2. 前后选择的选项不一样时
                 */
                if (originValue != null && yinjingAttributes != null
                        && yinjingAttributes.getAttributeTwo() != null
                        && !yinjingAttributes.getAttributeTwo().equals(((DictionaryItem) item).getName())) {
                    new AlertDialog.Builder(context)
                            .setMessage("是否确认要修改雨污属性?")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    yinjingAttributes.setAttributeTwo(((DictionaryItem) item).getName());
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    spinnerTableItem.selectItem(yinjingAttributes.getAttributeTwo());
                                }
                            })
                            .show();
                } else {
                    if(yinjingAttributes != null) {
                        yinjingAttributes.setAttributeTwo(((DictionaryItem) item).getName());
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
                if (!b){
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
                    spinnerTableItem.put(componentOriginValue, item);
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yinjingAttributes.setAttributeTwo(componentOriginValue);
            } else {
                boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
                if (!b){
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
                    spinnerTableItem.put(componentOriginValue, item);
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yinjingAttributes.setAttributeTwo(defaultSelectedValue);
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
            originAttributeValue.put(ATTRIBUTE_TWO, defaultSelectedValue);
            yinjingAttributes.setAttributeTwo(defaultSelectedValue);
        } else {
            spinnerTableItem.selectItem(0);
        }
        container.addView(spinnerTableItem);
    }

    /***
     * 属性三
     *
     * @param context
     * @param originValue 设施图层中的属性，当不是核准时，为null
     */
    private void addAttributeThree(Context context, Map<String, Object> originValue, String defaultSelectedValue, ViewGroup container) {
        TableDBService dbService = new TableDBService(context);
        List<DictionaryItem> a159 = dbService.getDictionaryByTypecodeInDB("A159");
        processOrder(a159);

        final Map<String, Object> spinnerData = new LinkedHashMap<>();
        List<String> allowValues = new ArrayList<>();
        for (DictionaryItem dictionaryItem : a159) {
            allowValues.add(dictionaryItem.getName());
            spinnerData.put(dictionaryItem.getName(), dictionaryItem);
        }
        this.yinjingAttributes.setAttributeThreeAllowValues(allowValues);

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
                yinjingAttributes.setAttributeThree(((DictionaryItem) item).getName());
            }
        });

        /**
         * 先判断OriginValue是否有值，如果有，那么将其放入originAttributeValue中，此时如果defaultValue没有值，那么默认的下拉框显示的就是originValue，
         * 如果defaultValue有值，显示的是defaultValue；
         */

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
                yinjingAttributes.setAttributeThree(componentOriginValue);
            } else {
                boolean b = spinnerTableItem.selectItem(defaultSelectedValue);
                if (!b){
                    DictionaryItem item = new DictionaryItem();
                    item.setName(componentOriginValue);
                    spinnerTableItem.put(componentOriginValue, item);
                    spinnerTableItem.selectItem(componentOriginValue);

                }
                yinjingAttributes.setAttributeThree(defaultSelectedValue);
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
            yinjingAttributes.setAttributeThree(defaultSelectedValue);
        } else {
            spinnerTableItem.selectItem(0);
        }
        container.addView(spinnerTableItem);
    }

    /***
     * 属性四
     *
     * @param context
     * @param originValue 设施图层中的属性，当不是核准时，为null
     * @param
     */
    private void addAttributeFour(Context context,
                                  Map<String, Object> originValue, String defaultValue, ViewGroup attributelistContainer) {
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_FOUR);
        String loginName = new LoginRouter(context, AMDatabase.getInstance()).getUser().getLoginName();

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
        facilityOwnerShipUnitView.addTo(attributelistContainer);
        yinjingAttributes.setAttributeFour(text);
    }

    /**
     * 雨水口属性一
     *
     * @param context
     * @param originValue
     * @param defaultSelectedValue
     * @param container
     */
    private void addAttributeOne(final Context context, Map<String, Object> originValue,
                                 String defaultSelectedValue, ViewGroup container, Map<String, String> defaultSelectedValueMap) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_attribute_yinjing, null);
//        dateAndTime = Calendar.getInstance(Locale.CHINA);
//        sheShiDianAttributes = new NWSheShiDianAttributes();
//        textitem_facility_code = (TextItemTableItem)view.findViewById(R.id.textitem_facility_code);
////        textitem_facility_code.setRequireTag();
//        textitem_facility_name = (TextItemTableItem)view.findViewById(R.id.textitem_facility_name);
//        textitem_facility_name.setRequireTag();
//        facility_treatment_process= (NumberItemTableItem)view.findViewById(R.id.facility_treatment_process);
//        facility_treatment_process.setRequireTag();
//        facility_street_rp= (TextItemTableItem)view.findViewById(R.id.facility_street_rp);
//        facility_street_rp.setRequireTag();
//        facility_street_rp_phone= (PhoneItemTableItem)view.findViewById(R.id.facility_street_rp_phone);
//        facility_street_rp_phone.setRequireTag();
//        facility_village_rp= (TextItemTableItem)view.findViewById(R.id.facility_village_rp);
//        facility_village_rp.setRequireTag();
//        facility_village_rp_phone= (PhoneItemTableItem)view.findViewById(R.id.facility_village_rp_phone);
//        facility_village_rp_phone.setRequireTag();
//        facility_vascularunit= (TextItemTableItem)view.findViewById(R.id.facility_vascularunit);
//        facility_vascularunit.setRequireTag();
//        facility_vascularunit_rp= (TextItemTableItem)view.findViewById(R.id.facility_vascularunit_rp);
//        facility_vascularunit_rp.setRequireTag();
//        facility_vascularunit_rp_phone= (PhoneItemTableItem)view.findViewById(R.id.facility_vascularunit_rp_phone);
//        facility_vascularunit_rp_phone.setRequireTag();
//        facility_area= (NumberItemTableItem)view.findViewById(R.id.facility_area);
//        facility_area.setRequireTag();
//        if(defaultSelectedValueMap != null && defaultSelectedValueMap.size() != 0){
//            textitem_facility_code.setText(defaultSelectedValueMap.get(ATTRIBUTE_CODE));
//            textitem_facility_name.setText(defaultSelectedValueMap.get(ATTRIBUTE_NAME));
//            facility_treatment_process.setText(defaultSelectedValueMap.get(ATTRIBUTE_WATER));
//            facility_street_rp.setText(defaultSelectedValueMap.get(STREET_RESPONE));
//            facility_street_rp_phone.setText(defaultSelectedValueMap.get(STREET_RESPONE_PH));
//            facility_village_rp.setText(defaultSelectedValueMap.get(VILLAGE_RESPONE));
//            facility_village_rp_phone.setText(defaultSelectedValueMap.get(VILLAGE_RESPONE_PH));
//            facility_vascularunit.setText(defaultSelectedValueMap.get(VASCULAR_UNIT));
//            facility_vascularunit_rp.setText(defaultSelectedValueMap.get(VASCULAR_RESPONE));
//            facility_vascularunit_rp_phone.setText(defaultSelectedValueMap.get(VASCULAR_RESPONE_PH));
//            facility_area.setText(defaultSelectedValueMap.get(ATTRIBUTE_AREA));
//        }
//        if(originValue != null && originValue.size() != 0){
//            textitem_facility_code.setText(originValue.get(ATTRIBUTE_CODE).toString());
//            textitem_facility_name.setText(originValue.get(ATTRIBUTE_NAME).toString());
//            facility_treatment_process.setText(originValue.get(ATTRIBUTE_WATER).toString());
//            facility_street_rp.setText(originValue.get(STREET_RESPONE).toString());
//            facility_street_rp_phone.setText(originValue.get(STREET_RESPONE_PH).toString());
//            facility_village_rp.setText(originValue.get(VILLAGE_RESPONE).toString());
//            facility_village_rp_phone.setText(originValue.get(VILLAGE_RESPONE_PH).toString());
//            facility_vascularunit.setText(originValue.get(VASCULAR_UNIT).toString());
//            facility_vascularunit_rp.setText(originValue.get(VASCULAR_RESPONE).toString());
//            facility_vascularunit_rp_phone.setText(originValue.get(VASCULAR_RESPONE_PH).toString());
//            facility_area.setText(originValue.get(ATTRIBUTE_AREA).toString());
//        }
//        /**
//         * 处理工艺
//         */
//        final Map<String, Object> spinnerData = new LinkedHashMap<>();
//        List<String> gy = Arrays.asList("厌氧池+人工湿地","厌氧池","MBR","A/O工艺","A2/O工艺","生物接触氧化法","生物接触氧化法","生物转盘","厌氧池+生态塘，生态沟","其他");
//        for(String type:gy){
//            spinnerData.put(type,type);
//        }
//        edt_other = (EditText) view.findViewById(R.id.edt_other);
//        ll_other = (LinearLayout) view.findViewById(R.id.ll_other);
//        SpinnerTableItem spinnerTableItem = (SpinnerTableItem) view.findViewById(R.id.facility_type);
//        spinnerTableItem.setSpinnerData(spinnerData);
//        spinnerTableItem.setRequereTag();
//        spinnerTableItem.setOnSpinnerChangeListener(new AMSpinner.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position, Object item) {
//                if(item.toString().equals("其他")){
//                    ll_other.setVisibility(View.VISIBLE);
//                    isOther = true;
//                    sheShiDianAttributes.setHandicraft("");
//                }else{
//                    ll_other.setVisibility(View.GONE);
//                    isOther = false;
//                    sheShiDianAttributes.setHandicraft(item.toString());
//                }
//            }
//        });
//
//        spinnerTableItem.selectItem(0);
//        if(defaultSelectedValueMap != null && defaultSelectedValueMap.size() != 0){
//            String handicraft = defaultSelectedValueMap.get(ATTRIBUTE_HANDICRAFT).toString();
//            if(!TextUtils.isEmpty(handicraft)){
//                if(handicraft.contains("其他：")){
//                    spinnerTableItem.selectItem("其他");
//                    edt_other.setText(handicraft.substring(3));
//                }else {
//                    spinnerTableItem.selectItem(handicraft);
//                }
//            }
////            spinnerTableItem.selectItem(defaultSelectedValueMap.get(ATTRIBUTE_HANDICRAFT));
//        }
//        if(originValue != null && originValue.size() != 0){
//            String handicraft = defaultSelectedValueMap.get(ATTRIBUTE_HANDICRAFT).toString();
//            if(!TextUtils.isEmpty(handicraft)){
//                if(handicraft.contains("其他：")){
//                    spinnerTableItem.selectItem("其他");
//                    edt_other.setText(handicraft.substring(3));
//                }else {
//                    spinnerTableItem.selectItem(handicraft);
//                }
//            }
////            spinnerTableItem.selectItem(originValue.get(ATTRIBUTE_HANDICRAFT).toString());
//        }
//        btnDate = (Button) view.findViewById(R.id.btn_start_date);
//        if(defaultSelectedValueMap != null && defaultSelectedValueMap.size() != 0){
//            String yearAndMonth = defaultSelectedValueMap.get(ATTRIBUTE_TIME);
//            startDate = Long.valueOf(yearAndMonth);
//            Date date = new Date(startDate);
//
//            int year = date.getYear() + 1900;
//            String month = "" + (date.getMonth()+1);
//            if(Integer.valueOf(month)<=9){
//                month = "0"+ month;
//            }
//            btnDate.setText(year + "年" + month + "月");
//        } else if(originValue != null && originValue.size() != 0){
//            String yearAndMonth = originValue.get(ATTRIBUTE_TIME).toString();
//            btnDate.setText(yearAndMonth);
//            String year = yearAndMonth.substring(0,3);
//            String month = yearAndMonth.substring(3);
//            if(Integer.valueOf(month)<=9){
//                month = "0"+ month;
//            }
//            startDate = new Date(Integer.valueOf(year) - 1900, Integer.valueOf(month), 1).getTime();
//        }else {
//            String mm;
//            if (dateAndTime.get(Calendar.MONTH) <= 8) {
//                mMonth = dateAndTime.get(Calendar.MONTH) + 1;
//                mm = "0" + mMonth;
//            } else {
//                mMonth = dateAndTime.get(Calendar.MONTH) + 1;
//                mm = String.valueOf(mMonth);
//            }
//            startDate = new Date(dateAndTime.get(Calendar.YEAR) - 1900, dateAndTime.get(Calendar.MONTH), 1).getTime();
//            btnDate.setText(String.valueOf(dateAndTime.get(Calendar.YEAR)) + "年" + mm+"月");
//        }
//        btnDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(startDate != null){
//                    dateAndTime.setTimeInMillis(startDate);
//                }
//                mYear = dateAndTime.get(Calendar.YEAR);
//                mMonth = dateAndTime.get(Calendar.MONTH);
//                mDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
//                Context themed = new ContextThemeWrapper(context,
//                        android.R.style.Theme_Holo_Light_Dialog);
//                NWMonPickerDialog monPickerDialog = new NWMonPickerDialog(themed, mDateSetListener, mYear, mMonth,
//                        mDay);
//                monPickerDialog.setHasNoDay(true);
//                monPickerDialog.show();
//            }
//        });
//        /**
//         * 设计出水标准
//         */
//        final Map<String, Object> spinnerData1 = new LinkedHashMap<>();
//        List<String> gy1 = Arrays.asList("《农田灌溉水质标准》（GB-5084-2005）排放标准","《城镇污水处理厂污染物排放标准》（GB18918-2002）二级排放标准","城镇污水处理厂污染物排放标准》（GB18918-2002）一级B排放标准");
//        for(String type:gy1){
//            spinnerData1.put(type,type);
//        }
//        SpinnerTableItem spinnerTableItem1 = (SpinnerTableItem) view.findViewById(R.id.facility_water_standard);
//        spinnerTableItem1.setSpinnerData(spinnerData1);
//        spinnerTableItem1.setRequereTag();
//        spinnerTableItem1.setOnSpinnerChangeListener(new AMSpinner.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position, Object item) {
////                sheShiDianAttributes.setTreatmentWater(((DictionaryItem) item).getName());
//                sheShiDianAttributes.setEffluenttandards((String) item);
//            }
//        });
//        spinnerTableItem1.selectItem(0);
//        if(defaultSelectedValueMap != null && defaultSelectedValueMap.size() != 0){
//            spinnerTableItem1.selectItem(defaultSelectedValueMap.get(EFFLUENTTANDARDS).toString());
//        }
//        if(originValue != null && originValue.size() != 0){
//            spinnerTableItem1.selectItem(originValue.get(EFFLUENTTANDARDS).toString());
//        }
//        final CheckBox cb_yes = (CheckBox) view.findViewById(R.id.cb_yes);
//        final CheckBox cb_no = (CheckBox) view.findViewById(R.id.cb_no);
//        final CheckBox cb_mix = (CheckBox) view.findViewById(R.id.cb_mix);
//        String powertype = "";
//        if(defaultSelectedValueMap != null && defaultSelectedValueMap.size() != 0){
//            powertype = defaultSelectedValueMap.get(POWER_TYPE);
//        }
//        if(originValue != null && originValue.size() != 0){
//            powertype = originValue.get(POWER_TYPE).toString();
//        }
//        cb_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sheShiDianAttributes.setPowerType("0");
//                cb_yes.setChecked(true);
//                cb_no.setChecked(false);
//                cb_mix.setChecked(false);
//            }
//        });
//        cb_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sheShiDianAttributes.setPowerType("1");
//                cb_no.setChecked(true);
//                cb_yes.setChecked(false);
//                cb_mix.setChecked(false);
//            }
//        });
//        cb_mix.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sheShiDianAttributes.setPowerType("2");
//                cb_mix.setChecked(true);
//                cb_no.setChecked(false);
//                cb_yes.setChecked(false);
//            }
//        });
//
//        if (TextUtils.isEmpty(powertype) || "0".equals(powertype)) {
//            sheShiDianAttributes.setPowerType("0");
//            cb_yes.setChecked(true);
//            cb_no.setChecked(false);
//            cb_mix.setChecked(false);
//        } else if("1".equals(powertype)){
//            sheShiDianAttributes.setPowerType("1");
//            cb_no.setChecked(true);
//            cb_yes.setChecked(false);
//            cb_mix.setChecked(false);
//        }else{
//            sheShiDianAttributes.setPowerType("2");
//            cb_mix.setChecked(true);
//            cb_no.setChecked(false);
//            cb_yes.setChecked(false);
//        }
//
//        final CheckBox cb_type_yes = (CheckBox) view.findViewById(R.id.cb_type_yes);
//        final CheckBox cb_type_no = (CheckBox) view.findViewById(R.id.cb_type_no);
//        final CheckBox cb_type_wx = (CheckBox) view.findViewById(R.id.cb_type_wx);
//        String status = "";
//        if(defaultSelectedValueMap != null && defaultSelectedValueMap.size() != 0){
//            status = defaultSelectedValueMap.get(FACILITY_STATUS);
//        }
//        if(originValue != null && originValue.size() != 0){
//            status = originValue.get(FACILITY_STATUS).toString();
//        }
//        cb_type_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sheShiDianAttributes.setFacilityStatus("0");
//                cb_type_yes.setChecked(true);
//                cb_type_no.setChecked(false);
//                cb_type_wx.setChecked(false);
//            }
//        });
//        cb_type_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sheShiDianAttributes.setFacilityStatus("1");
//                cb_type_no.setChecked(true);
//                cb_type_yes.setChecked(false);
//                cb_type_wx.setChecked(false);
//            }
//        });
//        cb_type_wx.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sheShiDianAttributes.setFacilityStatus("2");
//                cb_type_wx.setChecked(true);
//                cb_type_no.setChecked(false);
//                cb_type_yes.setChecked(false);
//            }
//        });
//        if (TextUtils.isEmpty(status) || "0".equals(status)) {
//            sheShiDianAttributes.setFacilityStatus("0");
//            cb_type_yes.setChecked(true);
//            cb_type_no.setChecked(false);
//            cb_type_wx.setChecked(false);
//        } else if("1".equals(status)){
//            sheShiDianAttributes.setFacilityStatus("1");
//            cb_type_no.setChecked(true);
//            cb_type_yes.setChecked(false);
//            cb_type_wx.setChecked(false);
//        }else{
//            sheShiDianAttributes.setFacilityStatus("2");
//            cb_type_wx.setChecked(true);
//            cb_type_no.setChecked(false);
//            cb_type_yes.setChecked(false);
//        }
        container.addView(view);
    }



    /**
     * 属性五
     * @param originMap 设施图层中的属性，当不是核准时，为null
     */
    private void addAttributeFive(Context context, Map<String, Object> originMap, String defaultValue, ViewGroup attributelistContainer) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_attribute_five, null);
        final EditText et = (EditText) view.findViewById(R.id.et_1);
        attributeFiveItem = et;
        final CheckBox cb_yes = (CheckBox) view.findViewById(R.id.cb_yes);
        final CheckBox cb_no = (CheckBox) view.findViewById(R.id.cb_no);
        cb_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fiveItemChecked = true;
                    et.setEnabled(true);
                } else {
                    fiveItemChecked = false;
                    et.setEnabled(false);
                }
                cb_no.setChecked(!isChecked);
            }
        });
        cb_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fiveItemChecked = false;
                    et.setEnabled(false);
                } else {
                    fiveItemChecked = true;
                    et.setEnabled(true);
                }
                cb_yes.setChecked(!isChecked);
            }
        });

        if ("无".equals(defaultValue)
                || "".equals(defaultValue)) {
            cb_no.setChecked(true);
        } else {
            et.setText(defaultValue);
            cb_yes.setChecked(true);
        }

        attributelistContainer.addView(view);
    }


    public static String getComponentOriginValue(Map<String, Object> originValue, String attributeName) {

        if (originValue == null) {
            return null;
        }
        /**
         * 窨井
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
        } else if (attributeName.contains(ATTRIBUTE_FOUR)) {
            Object o = originValue.get(ATTRIBUTE_FOUR_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_FIVE)) {
            Object o = originValue.get(ATTRIBUTE_FIVE_KEY);
            if (o != null) {
                return o.toString();
            }
        }
        return null;
    }


    public YinjingAttributes getYinjingAttributes() {

        if (facilityOwnerShipUnitView != null) {
            yinjingAttributes.setAttributeFour(facilityOwnerShipUnitView.getText());
        }

        if (fiveItemChecked && StringUtil.isEmpty(attributeFiveItem.getText().toString().replace(" ", ""))) {
            yinjingAttributes.setAttributeFive(null);
        } else if (attributeFiveItem != null && fiveItemChecked) {
            yinjingAttributes.setAttributeFive(attributeFiveItem.getText().toString());
        } else {
            yinjingAttributes.setAttributeFive("无");
        }

        //对比属性
        if (yinjingAttributes.getAttributeOne() != null && !yinjingAttributes.getAttributeOne().equals(originAttributeValue.get(ATTRIBUTE_ONE))) {
            yinjingAttributes.setHasModified(true);
        } else if (yinjingAttributes.getAttributeTwo() != null && !yinjingAttributes.getAttributeTwo().equals(originAttributeValue.get(ATTRIBUTE_TWO))) {
            yinjingAttributes.setHasModified(true);
        } else if (yinjingAttributes.getAttributeThree() != null && !yinjingAttributes.getAttributeThree().equals(originAttributeValue.get(ATTRIBUTE_THREE))) {
            yinjingAttributes.setHasModified(true);
        } else if (yinjingAttributes.getAttributeFour() != null && !yinjingAttributes.getAttributeFour().equals(originAttributeValue.get(ATTRIBUTE_FOUR))) {
            yinjingAttributes.setHasModified(true);
        } else if (yinjingAttributes.getAttributeFive() != null && !yinjingAttributes.getAttributeFive().equals(originAttributeValue.get(ATTRIBUTE_FIVE))) {
            yinjingAttributes.setHasModified(true);
        }

        return yinjingAttributes;

    }

    public YinjingAttributes getOriginalYinjingAttributes() {


        YinjingAttributes yinjingAttributes = new YinjingAttributes();

        String attributeOne = originAttributeValue.get(ATTRIBUTE_ONE);
        if (attributeOne != null) {
            yinjingAttributes.setAttributeOne(attributeOne);
        }

        String attributeTwo = originAttributeValue.get(ATTRIBUTE_TWO);
        if (attributeTwo != null) {
            yinjingAttributes.setAttributeTwo(attributeTwo);
        }


        String attributeThree = originAttributeValue.get(ATTRIBUTE_THREE);
        if (attributeThree != null) {
            yinjingAttributes.setAttributeThree(attributeThree);
        }

        String attributeFour = originAttributeValue.get(ATTRIBUTE_FOUR);
        if (attributeFour != null) {
            yinjingAttributes.setAttributeFour(attributeFour);
        }

        String attributeFive = originAttributeValue.get(ATTRIBUTE_FIVE);
        if (attributeFive != null) {
            yinjingAttributes.setAttributeFive(attributeFive);
        }

        return yinjingAttributes;

    }


    public static void addReadOnlyAttributes(Context context,
                                             Map<String, Object> originValue,
                                             YinjingAttributes yinjingAttributes,
                                             ViewGroup llContainer) {
        //属性一
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_ONE);
        ReadOnlyAttributeView readOnlyAttributeView = new ReadOnlyAttributeView(context,
                ATTRIBUTE_ONE, componentOriginValue, yinjingAttributes.getAttributeOne());
        readOnlyAttributeView.addTo(llContainer);

        //属性二
        String attributeTwo = getComponentOriginValue(originValue, ATTRIBUTE_TWO);
        ReadOnlyAttributeView readOnlyAttributeView2 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_TWO, attributeTwo, yinjingAttributes.getAttributeTwo());
        readOnlyAttributeView2.addTo(llContainer);


        //属性3
        String componentOriginValue3 = getComponentOriginValue(originValue, ATTRIBUTE_THREE);
        ReadOnlyAttributeView readOnlyAttributeView3 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_THREE, componentOriginValue3, yinjingAttributes.getAttributeThree());
        readOnlyAttributeView3.addTo(llContainer);

        //属性4
        String componentOriginValue4 = getComponentOriginValue(originValue, ATTRIBUTE_FOUR);
        ReadOnlyAttributeView readOnlyAttributeView4 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_FOUR, componentOriginValue4, yinjingAttributes.getAttributeFour());
        readOnlyAttributeView4.addTo(llContainer);

        //属性5
        String componentOriginValue5 = getComponentOriginValue(originValue, ATTRIBUTE_FIVE);
        ReadOnlyAttributeView readOnlyAttributeView5 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_FIVE, componentOriginValue5, yinjingAttributes.getAttributeFive());
        readOnlyAttributeView5.addTo(llContainer);
    }


    public static void addReadOnlyAttributes(Context context,
                                             YinjingAttributes yinjingAttributes,
                                             ViewGroup llContainer) {
        //属性一
        ReadOnlyAttributeView readOnlyAttributeView = new ReadOnlyAttributeView(context,
                ATTRIBUTE_ONE, yinjingAttributes.getAttributeOne());
        readOnlyAttributeView.addTo(llContainer);

        //属性二
        ReadOnlyAttributeView readOnlyAttributeView2 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_TWO, yinjingAttributes.getAttributeTwo());
        readOnlyAttributeView2.addTo(llContainer);


        //属性3
        ReadOnlyAttributeView readOnlyAttributeView3 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_THREE, yinjingAttributes.getAttributeThree());
        readOnlyAttributeView3.addTo(llContainer);

        //属性4

        ReadOnlyAttributeView readOnlyAttributeView4 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_FOUR, yinjingAttributes.getAttributeFour());
        readOnlyAttributeView4.addTo(llContainer);

        //属性5

        ReadOnlyAttributeView readOnlyAttributeView5 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_FIVE, yinjingAttributes.getAttributeFive());
        readOnlyAttributeView5.addTo(llContainer);
    }


    public static Map<String, String> getDefaultValue(ModifiedFacility modifiedFacility) {

        Map<String, String> defaultSelectValue = new HashMap<>(5);
        defaultSelectValue.put(ATTRIBUTE_ONE, modifiedFacility.getAttrOne());
        defaultSelectValue.put(ATTRIBUTE_TWO, modifiedFacility.getAttrTwo());
        defaultSelectValue.put(ATTRIBUTE_THREE, modifiedFacility.getAttrThree());
        defaultSelectValue.put(ATTRIBUTE_FOUR, modifiedFacility.getAttrFour());
        defaultSelectValue.put(ATTRIBUTE_FIVE, modifiedFacility.getAttrFive());
        if (modifiedFacility.getAttrFive() == null) {
            defaultSelectValue.put(ATTRIBUTE_FIVE, "");
        } else {
            defaultSelectValue.put(ATTRIBUTE_FIVE, modifiedFacility.getAttrFive());
        }
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
        if (modifiedFacility.getOriginAttrFour() != null) {
            originValue.put(ATTRIBUTE_FOUR_KEY, modifiedFacility.getOriginAttrFour());
        } else {
            originValue.put(ATTRIBUTE_FOUR_KEY, "");
        }
        if (modifiedFacility.getOriginAttrFive() != null) {
            originValue.put(ATTRIBUTE_FIVE_KEY, modifiedFacility.getOriginAttrFive());
        } else {
            originValue.put(ATTRIBUTE_FIVE_KEY, "");
        }

        return originValue;
    }


    public void onDestroy(){
        if (facilityOwnerShipUnitView != null){
            facilityOwnerShipUnitView.onDestroy();
            facilityOwnerShipUnitView = null;
        }
    }

}

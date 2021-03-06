package com.augurit.agmobile.gzps.uploadfacility.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.facilityownership.FacilityOwnerShipUnitView;
import com.augurit.agmobile.gzps.common.selectcomponent.SelectComponentFinishEvent2;
import com.augurit.agmobile.gzps.common.selectcomponent.SelectRiverFlowActivity;
import com.augurit.agmobile.gzps.common.widget.MyGridView;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadevent.adapter.SimpleTypeAdapter;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.PaifangKouAttributesNew;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.ReadOnlyAttributeView;
import com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility.ReadOnlyGuaPaiNoPfKouView;
import com.augurit.agmobile.mapengine.common.base.OnRecyclerItemClickListener;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.agmobile.patrolcore.selectlocation.model.DetailAddress;
import com.augurit.am.cmpt.login.router.LoginRouter;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.db.AMDatabase;
import com.augurit.am.fw.utils.StringUtil;
import com.esri.core.geometry.Point;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ????????? ???xuciluan
 * @version 1.0
 * @package ?????? ???com.augurit.agmobile.gzps.upload.util
 * @createTime ???????????? ???17/12/18
 * @modifyBy ????????? ???xuciluan
 * @modifyTime ???????????? ???17/12/18
 * @modifyMemo ???????????????
 */

public class PaifangKouAttributeViewUtilNew {
    private boolean fiveItemChecked = true;
    private EditText attributeFiveItem;
    //    public static final String ATTRIBUTE_ONE = "????????????";
    public static final String ATTRIBUTE_ONE = "????????????";
    public static final String ATTRIBUTE_NAME = "????????????";
    public static final String ATTRIBUTE_TWO = "????????????";
    public static final String ATTRIBUTE_THREE = "????????????";
    public static final String ATTRIBUTE_FOUR = "???????????????";
    public static final String ATTRIBUTE_FIVE = "????????????";
    public static final String ATTRIBUTE_SIX = "?????????????????????";
    public static final String ATTRIBUTE_SEVEN = "???????????????";
    public static final String ATTRIBUTE_EIGHT = "??????????????????";
    public static final String ATTRIBUTE_NINE = "????????????";
    public static final String ATTRIBUTE_TEN = "pH???";
    public static final String ATTRIBUTE_ELEVEN = "??????????????????";
    public static final String ATTRIBUTE_TWELVE = "??????????????????";
    public static final String ATTRIBUTE_THIRTEEN = "???????????????";
    public static final String ATTRIBUTE_X = "??????X??????";
    public static final String ATTRIBUTE_Y = "??????Y??????";
    public static final String ATTRIBUTE_GPBH = "????????????";
    public static final String ATTRIBUTE_LAST = "??????";

//    public static final String ATTRIBUTE_ONE_KEY = ComponentFieldKeyConstant.RIVER;
//    public static final String ATTRIBUTE_TWO_KEY = ComponentFieldKeyConstant.SORT;
//    public static final String ATTRIBUTE_THREE_KEY = ComponentFieldKeyConstant.OWNERDEPT;

    public static final String ATTRIBUTE_GPBH_KEY = "gpbh";
    public static final String ATTRIBUTE_ONE_KEY = ComponentFieldKeyConstant.RIVER;
    public static final String ATTRIBUTE_TWO_KEY = ComponentFieldKeyConstant.SORT;
    public static final String ATTRIBUTE_THREE_KEY = ComponentFieldKeyConstant.OWNERDEPT;
    public static final String ATTRIBUTE_FOUR_KEY = "PSKAB";
    public static final String ATTRIBUTE_FIVE_KEY = "RHFS";
    public static final String ATTRIBUTE_SIX_KEY = "PSKSSLX";
    public static final String ATTRIBUTE_SEVEN_KEY = "PWKGJ";
    public static final String ATTRIBUTE_X_KEY = "X";
    public static final String ATTRIBUTE_Y_KEY = "Y";
    public static final String ATTRIBUTE_EIGHT_KEY = ComponentFieldKeyConstant.OWNERDEPT;
    public static final String ATTRIBUTE_NINE_KEY = ComponentFieldKeyConstant.OWNERDEPT;
    public static final String ATTRIBUTE_TEN_KEY = ComponentFieldKeyConstant.OWNERDEPT;
    public static final String ATTRIBUTE_ELEVEN_KEY = ComponentFieldKeyConstant.OWNERDEPT;
    public static final String ATTRIBUTE_TWELVE_KEY = ComponentFieldKeyConstant.OWNERDEPT;
    public static final String ATTRIBUTE_THIRTEEN_KEY = ComponentFieldKeyConstant.OWNERDEPT;
    public static final String ATTRIBUTE_LAST_KEY = ComponentFieldKeyConstant.OWNERDEPT;


    private Context mContext;

    private PaifangKouAttributesNew paifangKouAttributes;

    private FacilityOwnerShipUnitView facilityOwnerShipUnitView;
    private TextItemTableItem attributeOneItem;
    private TextItemTableItem attributeNameItem;

    private Map<String, String> originAttributeValue = new HashMap<>(5);

    private Component mSelComponent;
    private DetailAddress mSelDetailAddress;
    private TextItemTableItem mName;
    private TextView tv_select_or_check_location;
    private EditText mRHOther;
    private EditText mLXOther;
    private int index = 0;
    private EditText mEt_pipe3;
    private EditText mEt_pipe22;
    private EditText mEt_pipe21;
    private EditText mEt_pipe1;

    private String riverX;
    private String riverY;
    private boolean isCheck = false;

    public PaifangKouAttributeViewUtilNew() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * ????????????????????????
     *
     * @param list
     */
    private static void processOrder(List<DictionaryItem> list) {
        for (DictionaryItem item : list) {
            String code = item.getCode();
            String target = code;
            if (code.length() > 1) {
                target = code.replaceAll("[^(0-9)]", "");//??????????????????
            }
            item.setCode(target);
        }
        //?????????
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
                                       Map<String, String> defaultSelectedValue,Object uploadedFacility) {

        this.paifangKouAttributes = new PaifangKouAttributesNew();
        this.mContext = context;
        if (defaultSelectedValue != null) {
            riverX = defaultSelectedValue.get(ATTRIBUTE_X);
            riverY = defaultSelectedValue.get(ATTRIBUTE_Y);
            addAttributeOne(context, originValue, defaultSelectedValue.get(ATTRIBUTE_ONE), attributeContainer);
//            addAttributeName(context, originValue, defaultSelectedValue.get(ATTRIBUTE_ONE), attributeContainer);
//            addAttributeTwo(context, originValue, defaultSelectedValue.get(ATTRIBUTE_TWO), attributeContainer);
            addAttributeFour(context, originValue, defaultSelectedValue.get(ATTRIBUTE_FOUR), attributeContainer);
            addAttributeFive(context, originValue, defaultSelectedValue.get(ATTRIBUTE_GPBH), attributeContainer, uploadedFacility);
            addAttributeFiveRuHe(context, originValue, defaultSelectedValue.get(ATTRIBUTE_FIVE), attributeContainer);
//            addAttributeSix(context, originValue, defaultSelectedValue.get(ATTRIBUTE_SIX), attributeContainer);
            addAttributeSix(context, originValue, defaultSelectedValue.get(ATTRIBUTE_SIX), attributeContainer);
            addAttributePipe(context, originValue, defaultSelectedValue, attributeContainer);
            addAttributeThree(context, originValue, defaultSelectedValue.get(ATTRIBUTE_THREE), attributeContainer);
//            addAttributeoOthers(context, originValue, defaultSelectedValue, attributeContainer);
        } else {


            //???????????????
            addAttributeOne(context, originValue, null, attributeContainer);
            //????????????
//            addAttributeName(context, originValue, null, attributeContainer);
            //????????????
//            addAttributeTwo(context, originValue, null, attributeContainer);
            //???????????????
            addAttributeFour(context, originValue, null, attributeContainer);
            addAttributeFive(context, originValue, null, attributeContainer, uploadedFacility);
            //????????????
            addAttributeFiveRuHe(context, originValue, null, attributeContainer);
            //???????????????
//            addAttributeSix(context, originValue, null, attributeContainer);
            //?????????????????????
            addAttributeSix(context, originValue, null, attributeContainer);
//            addAttributeoOthers(context, originValue, null, attributeContainer);
            addAttributePipe(context, originValue, null, attributeContainer);
            //????????????
            addAttributeThree(context, originValue, null, attributeContainer);
        }

    }


    private void addAttributeoOthers(Context context, Map<String, Object> originValue, Map<String, String> defaultValue, ViewGroup attributeContainer) {
        View view = View.inflate(context, R.layout.others_type, null);
        String pipeDefaultValue = null;
        String sampleDefaultValue = null;
        String densityDefaultValue = null;
        String flowDefaultValue = null;
        if (defaultValue == null) {

        } else {
            pipeDefaultValue = defaultValue.get(ATTRIBUTE_THIRTEEN);
            sampleDefaultValue = defaultValue.get(ATTRIBUTE_EIGHT);
            densityDefaultValue = defaultValue.get(ATTRIBUTE_NINE);
            flowDefaultValue = defaultValue.get(ATTRIBUTE_ELEVEN);
        }
        CheckBox cb_pipe1 = (CheckBox) view.findViewById(R.id.cb_pipe1);
        CheckBox cb_pipe2 = (CheckBox) view.findViewById(R.id.cb_pipe2);
        CheckBox cb_pipe3 = (CheckBox) view.findViewById(R.id.cb_pipe3);
        String pipeOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_THIRTEEN);
        if (pipeOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_THIRTEEN, pipeOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_THIRTEEN, pipeDefaultValue);
        }
        String textPipe = "";
        if (pipeDefaultValue == null) {
            if (pipeOriginValue != null) {
                textPipe = pipeOriginValue;
            }
        } else {
            textPipe = pipeDefaultValue;
        }
        paifangKouAttributes.setAttributeThirteen(textPipe);


        //??????
        RadioButton rb_sample1 = (RadioButton) view.findViewById(R.id.rb_sample1);
        RadioButton rb_sample2 = (RadioButton) view.findViewById(R.id.rb_sample2);
        String sampleOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_EIGHT);

        if (sampleOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_EIGHT, pipeOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_EIGHT, pipeDefaultValue);
        }
        String textSample = "";
        if (sampleDefaultValue == null) {
            if (sampleOriginValue != null) {
                textSample = sampleOriginValue;
            }
        } else {
            textSample = sampleDefaultValue;
        }
        paifangKouAttributes.setAttributeEight(textSample);

        //????????????
        EditText et_density = (EditText) view.findViewById(R.id.et_density);
        String densityOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_NINE);
        if (densityOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_NINE, densityOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_NINE, densityDefaultValue);
        }
        String textdensity = "";
        if (densityDefaultValue == null) {
            if (pipeOriginValue != null) {
                textdensity = densityOriginValue;
            }
        } else {
            textdensity = densityDefaultValue;
        }
        paifangKouAttributes.setAttributeNine(textdensity);

        //??????????????????

        RadioButton rb_flow1 = (RadioButton) view.findViewById(R.id.rb_flow1);
        RadioButton rb_flow2 = (RadioButton) view.findViewById(R.id.rb_flow2);
        RadioButton rb_flow3 = (RadioButton) view.findViewById(R.id.rb_flow3);
        String flowOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_ELEVEN);

        if (flowOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_ELEVEN, densityOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_ELEVEN, densityDefaultValue);
        }
        String textflow = "";
        if (flowDefaultValue == null) {
            if (pipeOriginValue != null) {
                textflow = flowOriginValue;
            }
        } else {
            textflow = flowDefaultValue;
        }
        paifangKouAttributes.setAttributeEleven(textflow);

        attributeContainer.addView(view);

    }

    private void addAttributePipe(Context context, Map<String, Object> originValue, Map<String, String> defaultValue, ViewGroup attributeContainer) {
        View view = View.inflate(context, R.layout.paiwukou_pipe, null);
        String pipeDefaultValue = null;

        if (defaultValue == null) {

        } else {
            pipeDefaultValue = defaultValue.get(ATTRIBUTE_THIRTEEN);
        }
        final CheckBox cb_pipe1 = (CheckBox) view.findViewById(R.id.cb_pipe1);
        final CheckBox cb_pipe2 = (CheckBox) view.findViewById(R.id.cb_pipe2);
        final CheckBox cb_pipe3 = (CheckBox) view.findViewById(R.id.cb_pipe3);
        mEt_pipe1 = (EditText) view.findViewById(R.id.et_pipe1);
        mEt_pipe21 = (EditText) view.findViewById(R.id.et_pipe21);
        mEt_pipe22 = (EditText) view.findViewById(R.id.et_pipe22);
        mEt_pipe3 = (EditText) view.findViewById(R.id.et_pipe3);
        String pipeOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_THIRTEEN);
        cb_pipe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_pipe1.isChecked()){
                    index = 1;
                    cb_pipe2.setChecked(false);
                    cb_pipe3.setChecked(false);
                    mEt_pipe1.setEnabled(true);
                    mEt_pipe21.setEnabled(false);
                    mEt_pipe22.setEnabled(false);
                    mEt_pipe3.setEnabled(false);
                }else{
                    mEt_pipe1.setEnabled(false);
                    index = -1;
                }
            }
        });

        cb_pipe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_pipe2.isChecked()) {
                    index = 2;
                    cb_pipe1.setChecked(false);
                    cb_pipe3.setChecked(false);
                    mEt_pipe1.setEnabled(false);
                    mEt_pipe21.setEnabled(true);
                    mEt_pipe22.setEnabled(true);
                    mEt_pipe3.setEnabled(false);
                } else {
                    mEt_pipe21.setEnabled(false);
                    mEt_pipe22.setEnabled(false);
                    index = -1;
                }
            }
        });

        cb_pipe3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_pipe3.isChecked()) {
                    index = 3;
                    cb_pipe2.setChecked(false);
                    cb_pipe1.setChecked(false);
                    mEt_pipe1.setEnabled(false);
                    mEt_pipe21.setEnabled(false);
                    mEt_pipe22.setEnabled(false);
                    mEt_pipe3.setEnabled(true);
                } else {
                    mEt_pipe3.setEnabled(false);
                    index = -1;
                }
            }
        });

        if (pipeOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_THIRTEEN, pipeOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_THIRTEEN, pipeDefaultValue);
        }
        String textPipe = "";
        if (pipeDefaultValue == null) {
            if (pipeOriginValue != null) {
                textPipe = pipeOriginValue;
            }
        } else {
            textPipe = pipeDefaultValue;
        }

        if (!StringUtil.isEmpty(textPipe)) {
            if (textPipe.startsWith("DN")) {
                cb_pipe1.setChecked(true);
                index = 1;
                mEt_pipe1.setEnabled(true);
                int lastIndex = textPipe.lastIndexOf("mm");
                mEt_pipe1.setText(textPipe.substring(2, lastIndex));
            } else if (textPipe.startsWith("?????????")) {
                index = 3;
                cb_pipe3.setChecked(true);
                mEt_pipe3.setText(textPipe.substring(3));
            } else {
                cb_pipe2.setChecked(true);
                mEt_pipe21.setEnabled(true);
                mEt_pipe22.setEnabled(true);
                index = 2;
                String arg[] = textPipe.split("mm??");
                String one = arg[0];
                String temp = arg[1];
                String two = temp.substring(0, temp.length() - 2);
                mEt_pipe21.setText(one);
                mEt_pipe22.setText(two);
            }
        }
        paifangKouAttributes.setAttributeSeven(textPipe);

        attributeContainer.addView(view);

    }

    private void addAttributeSix(Context context, Map<String, Object> originValue, String defaultValue, ViewGroup attributeContainer) {
//        String[] types = new String[]{"?????????", "?????????", "??????", "??????", "?????????", "??????", "??????", "??????????????????","?????????", "??????"};
        TableDBService dbService = new TableDBService(context);
        List<DictionaryItem> a195 = dbService.getDictionaryByTypecodeInDB("A195");
        processOrder(a195);

        List<String> types = new ArrayList<>();
        for (DictionaryItem dictionaryItem : a195) {
            types.add(dictionaryItem.getName());
        }
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_SIX);
        View view = View.inflate(context, R.layout.paishuikou_type, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(ATTRIBUTE_SIX);
        MyGridView gv = (MyGridView) view.findViewById(R.id.gv_type);
        mLXOther = ((EditText) view.findViewById(R.id.other));
        mLXOther.setHint("???????????????????????????");
        SimpleTypeAdapter adapter = new SimpleTypeAdapter(mContext, false);
        gv.setAdapter(adapter);
        adapter.notifyDataSetChanged(types);
        adapter.setSelectedType(defaultValue);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object selectedData) {
                String data = (String) selectedData;
                if (!StringUtil.isEmpty(data)) {
                    if ("??????".equals(data)) {
                        mLXOther.setVisibility(View.VISIBLE);
                    } else {
                        mLXOther.setVisibility(View.GONE);
                    }
                    paifangKouAttributes.setAttributeSix(data);
                } else {
                    mLXOther.setVisibility(View.GONE);
                    paifangKouAttributes.setAttributeSix(null);
                }
            }

            @Override
            public void onItemLongClick(View view, int position, Object selectedData) {

            }
        });
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_SIX, componentOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_SIX, defaultValue);
        }
        String text = "";
        if (defaultValue == null) {
            if (componentOriginValue != null) {
                text = componentOriginValue;
            }
        } else {
            text = defaultValue;
        }
        if (!TextUtils.isEmpty(text)) {
            if (text.contains("?????????")) {
                adapter.setSelectedType("??????");
                mLXOther.setText(text.substring(3));
                mLXOther.setVisibility(View.VISIBLE);
            } else {
                adapter.setSelectedType(text);
            }
        }
        paifangKouAttributes.setAttributeSix(text);
        attributeContainer.addView(view);
    }
    /**
     * ?????????
     *
     * @param originMap ???????????????????????????????????????????????????null
     * @param uploadedFacility
     */
    private void addAttributeFive(Context context, Map<String, Object> originMap, String defaultValue, ViewGroup attributelistContainer, Object uploadedFacility) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_attribute_guapai_no, null);
        final EditText et = (EditText) view.findViewById(R.id.et_1);
        final MultiTakePhotoTableItem takePhotoTableItem = (MultiTakePhotoTableItem) view.findViewById(R.id.take_photo_pfkou);
        List<Photo> photos = null;
//        takePhotoTableItem.setPhotoNumShow(false,9);
        if(uploadedFacility!=null){
            if(uploadedFacility instanceof UploadedFacility){
                UploadedFacility facility = (UploadedFacility) uploadedFacility;
                photos = facility.getWellPhotos();
            }else if(uploadedFacility instanceof ModifiedFacility){
                ModifiedFacility facility = (ModifiedFacility) uploadedFacility;
                photos = facility.getWellPhotos();
            }
            takePhotoTableItem.setSelectedPhotos(photos);
            takePhotoTableItem.setVisibility(View.VISIBLE);
        }
        attributeFiveItem = et;
        final CheckBox cb_yes = (CheckBox) view.findViewById(R.id.cb_yes);
        final CheckBox cb_no = (CheckBox) view.findViewById(R.id.cb_no);
        cb_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fiveItemChecked = true;
                    et.setEnabled(true);
                    takePhotoTableItem.setVisibility(View.VISIBLE);
                    takePhotoTableItem.setPhotoNumShow(true,9);
                } else {
                    fiveItemChecked = false;
                    et.setEnabled(false);
                    takePhotoTableItem.setVisibility(View.GONE);
                    takePhotoTableItem.setSelectedPhotos(null);
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
                    takePhotoTableItem.setVisibility(View.GONE);
                    takePhotoTableItem.setSelectedPhotos(null);
                } else {
                    fiveItemChecked = true;
                    et.setEnabled(true);
                    takePhotoTableItem.setVisibility(View.VISIBLE);
                    takePhotoTableItem.setPhotoNumShow(true,9);
                }
                cb_yes.setChecked(!isChecked);
            }
        });

        if ("???".equals(defaultValue)
                || StringUtil.isEmpty(defaultValue)) {
            cb_no.setChecked(true);
        } else {
            et.setText(defaultValue);
            cb_yes.setChecked(true);
        }

        attributelistContainer.addView(view);
    }
   private void addAttributeFiveRuHe(Context context, Map<String, Object> originValue, String defaultValue, ViewGroup attributeContainer) {
//        String[] types = new String[]{"??????", "??????", "??????", "??????", "??????"};
        TableDBService dbService = new TableDBService(context);
        List<DictionaryItem> a194 = dbService.getDictionaryByTypecodeInDB("A194");
        processOrder(a194);

        List<String> types = new ArrayList<>();
        for (DictionaryItem dictionaryItem : a194) {
            types.add(dictionaryItem.getName());
        }
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_FIVE);
        View view = View.inflate(context, R.layout.paishuikou_type, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(ATTRIBUTE_FIVE);
        mRHOther = ((EditText) view.findViewById(R.id.other));
        mRHOther.setHint("???????????????????????????");
        MyGridView gv = (MyGridView) view.findViewById(R.id.gv_type);
        SimpleTypeAdapter adapter = new SimpleTypeAdapter(mContext, true);
        gv.setAdapter(adapter);
        adapter.notifyDataSetChanged(types);
        adapter.setSelectedType(defaultValue);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object selectedData) {
                String data = (String) selectedData;
                if (!StringUtil.isEmpty(data)) {
                    if ("??????".equals(data)) {
                        mRHOther.setVisibility(View.VISIBLE);
                    } else {
                        mRHOther.setVisibility(View.GONE);
                    }
                    paifangKouAttributes.setAttributeFive(data);
                } else {
                    mRHOther.setVisibility(View.GONE);
                    paifangKouAttributes.setAttributeFive(null);
                }
            }

            @Override
            public void onItemLongClick(View view, int position, Object selectedData) {

            }
        });
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_FIVE, componentOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_FIVE, defaultValue);
        }
        String text = "";
        if (defaultValue == null) {
            if (componentOriginValue != null) {
                text = componentOriginValue;
            }
        } else {
            text = defaultValue;
        }
        if (!TextUtils.isEmpty(text)) {
            if (text.contains("?????????")) {
                adapter.setSelectedType("??????");
                mRHOther.setVisibility(View.VISIBLE);
                mRHOther.setText(text.substring(3));
            } else {
                adapter.setSelectedType(text);
            }
        }
        paifangKouAttributes.setAttributeFive(text);
        attributeContainer.addView(view);
    }


    private void addAttributeSeven(Context context, Map<String, Object> originValue, String defaultValue, ViewGroup attributeContainer) {
        String[] types = new String[]{"?????????", "?????????", "?????????", "?????????", "???????????????"};
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_FIVE);
        View view = View.inflate(context, R.layout.paishuikou_type, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(ATTRIBUTE_FIVE);
        MyGridView gv = (MyGridView) view.findViewById(R.id.gv_type);
        SimpleTypeAdapter adapter = new SimpleTypeAdapter(mContext, false);
        gv.setAdapter(adapter);
        adapter.notifyDataSetChanged(Arrays.asList(types));
        adapter.setSelectedType(defaultValue);
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_FIVE, componentOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_FIVE, defaultValue);
        }
        String text = "";
        if (defaultValue == null) {
            if (componentOriginValue != null) {
                text = componentOriginValue;
            }
        } else {
            text = defaultValue;
        }
        if (!TextUtils.isEmpty(text)) {
            adapter.setSelectedType(text);
        }
        paifangKouAttributes.setAttributeSix(text);
        attributeContainer.addView(view);
    }

    /***
     * ?????????
     *
     * @param context
     * @param originValue ???????????????????????????????????????????????????null
     * @param
     */
    private void addAttributeThree(Context context,
                                   Map<String, Object> originValue, String defaultValue, ViewGroup attributelistContainer) {
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_THREE);
        String loginName = new LoginRouter(context, AMDatabase.getInstance()).getUser().getLoginName();

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
        } else {
            text = defaultValue;
        }
        facilityOwnerShipUnitView = new FacilityOwnerShipUnitView(context, text);
        facilityOwnerShipUnitView.addTo(attributelistContainer);
        paifangKouAttributes.setAttributeThree(text);
    }


    /***
     * ??????3
     *
     * @param context
     * @param originValue ???????????????
     * @param
     */
    private void addAttributeFour(Context context,
                                  Map<String, Object> originValue, String defaultValue, ViewGroup ll_attributelist_container) {
//        String[] types = new String[]{"??????", "??????", "??????"};
        TableDBService dbService = new TableDBService(context);
        List<DictionaryItem> a193 = dbService.getDictionaryByTypecodeInDB("A193");
        processOrder(a193);

        List<String> types = new ArrayList<>();
        final Map<String, Object> spinnerData = new LinkedHashMap<>();
        for (DictionaryItem dictionaryItem : a193) {
            types.add(dictionaryItem.getName());
        }
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_FOUR);
        View view = View.inflate(context, R.layout.paishuikou_type, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(ATTRIBUTE_FOUR);
        MyGridView gv = (MyGridView) view.findViewById(R.id.gv_type);
        SimpleTypeAdapter adapter = new SimpleTypeAdapter(mContext, false);
        gv.setAdapter(adapter);
        adapter.notifyDataSetChanged(types);
        adapter.setSelectedType(defaultValue);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object selectedData) {
                String data = (String) selectedData;
                if (!StringUtil.isEmpty(data)) {
                    paifangKouAttributes.setAttributeFour(data);
                } else {
                    paifangKouAttributes.setAttributeFour(null);
                }
            }

            @Override
            public void onItemLongClick(View view, int position, Object selectedData) {

            }
        });
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
        if (!TextUtils.isEmpty(text)) {
            adapter.setSelectedType(text);
        }
        paifangKouAttributes.setAttributeFour(text);
        ll_attributelist_container.addView(view);
    }


    /**
     * ?????????
     *
     * @param context
     * @param originValue ???????????????????????????????????????????????????null
     * @param container
     */
    private void addAttributeTwo(final Context context, final Map<String, Object> originValue, String defaultValue, ViewGroup container) {
        final String[] types = new String[]{"???", "??????", "??????"};
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_TWO);
        View view = View.inflate(context, R.layout.paishuikou_type, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(ATTRIBUTE_TWO);
        MyGridView gv = (MyGridView) view.findViewById(R.id.gv_type);
        SimpleTypeAdapter adapter = new SimpleTypeAdapter(mContext, false);
        gv.setAdapter(adapter);
        adapter.notifyDataSetChanged(Arrays.asList(types));
        adapter.setSelectedType(defaultValue);
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_TWO, componentOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_TWO, defaultValue);
        }
        String text = "";
        if (defaultValue == null) {
            if (componentOriginValue != null) {
                text = componentOriginValue;
            }
        } else {
            text = defaultValue;
        }
        if (!TextUtils.isEmpty(text)) {
            adapter.setSelectedType(text);
        }
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                paifangKouAttributes.setAttributeTwo(types[position]);
            }
        });
        paifangKouAttributes.setAttributeTwo(text);
        container.addView(view);
    }


    /**
     * ??????1
     *
     * @param originMap ??????????????????????????????187???????????????????????????
     */
    private void addAttributeOne(Context context, Map<String, Object> originMap, String defaultValue, ViewGroup ll_attributelist_container) {
        String componentOriginValue = getComponentOriginValue(originMap, ATTRIBUTE_ONE);
        View view = View.inflate(context, R.layout.paifangkou_select_name, null);
        mName = (TextItemTableItem) view.findViewById(R.id.addr_tab_item);
        mName.setReadOnly();
        tv_select_or_check_location = (TextView) view.findViewById(R.id.tv_select_or_check_location);
        /**
         * ???????????????????????????
         */
        view.findViewById(R.id.ll_select_component).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,
                        SelectRiverFlowActivity.class);
                if (mSelDetailAddress != null) {
                    intent.putExtra("geometry", new Point(mSelDetailAddress.getX(), mSelDetailAddress.getY()));
                }
                if (!StringUtil.isEmpty(riverX) && !"0.0".equals(riverX) && !StringUtil.isEmpty(riverY) && !"0.0".equals(riverY)) {
                    intent.putExtra("geometry", new Point(Double.valueOf(riverX), Double.valueOf(riverY)));
                }
                mContext.startActivity(intent);
            }
        });
        if (!StringUtil.isEmpty(riverX) && !StringUtil.isEmpty(riverY)) {
            paifangKouAttributes.setX(Double.valueOf(riverX));
            paifangKouAttributes.setY(Double.valueOf(riverY));
        }
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
        } else {
            text = defaultValue;
        }

        if (!StringUtil.isEmpty(text)) {
            mName.setText(text);
            tv_select_or_check_location.setText(text);
        }

        paifangKouAttributes.setAttributeOne(text);
        ll_attributelist_container.addView(view);
    }

    /**
     * ??????1
     *
     * @param originMap ??????????????????????????????187???????????????????????????
     */
    private void addAttributeName(Context context, Map<String, Object> originMap, String defaultValue, ViewGroup ll_attributelist_container) {
        //??????????????????
        attributeNameItem = new TextItemTableItem(context);
        attributeNameItem.setTextViewName(ATTRIBUTE_NAME);
        attributeNameItem.setRequireTag();
        ll_attributelist_container.addView(attributeNameItem);
        String componentOriginValue = getComponentOriginValue(originMap, ATTRIBUTE_NAME);
//        String text = "";
//        if (componentOriginValue == null) {
//            if (defaultValue != null) {
//                text = defaultValue;
//            }
//        } else {
//            text = componentOriginValue;
//        }
        if (componentOriginValue != null) {
            originAttributeValue.put(ATTRIBUTE_NAME, componentOriginValue);
        } else {
            originAttributeValue.put(ATTRIBUTE_NAME, defaultValue);
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
        attributeNameItem.setText(text);
        //originAttributeValue.put(ATTRIBUTE_ONE, componentOriginValue);
        paifangKouAttributes.setAttributeOne(text);
    }


    public static String getComponentOriginValue(Map<String, Object> originValue, String attributeName) {

        if (originValue == null) {
            return null;
        }
        /**
         * ?????????
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
        } else if (attributeName.contains(ATTRIBUTE_SIX)) {
            Object o = originValue.get(ATTRIBUTE_THREE_KEY);
            if (o != null) {
                return o.toString();
            }
        }else if (attributeName.contains(ATTRIBUTE_SEVEN)) {
            Object o = originValue.get(ATTRIBUTE_SEVEN_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_EIGHT)) {
            Object o = originValue.get(ATTRIBUTE_EIGHT_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_NINE)) {
            Object o = originValue.get(ATTRIBUTE_NINE_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_TEN)) {
            Object o = originValue.get(ATTRIBUTE_TEN_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_ELEVEN)) {
            Object o = originValue.get(ATTRIBUTE_ELEVEN_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_TWELVE)) {
            Object o = originValue.get(ATTRIBUTE_TWELVE_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_THIRTEEN)) {
            Object o = originValue.get(ATTRIBUTE_THIRTEEN_KEY);
            if (o != null) {
                return o.toString();
            }
        } else if (attributeName.contains(ATTRIBUTE_LAST)) {
            Object o = originValue.get(ATTRIBUTE_LAST_KEY);
            if (o != null) {
                return o.toString();
            }
        }else if (attributeName.contains(ATTRIBUTE_GPBH)) {
            Object o = originValue.get(ATTRIBUTE_GPBH_KEY);
            if (o != null) {
                return o.toString();
            }
        }


        return null;
    }


    public PaifangKouAttributesNew getPaifangKouAttributes() {

        if (facilityOwnerShipUnitView != null) {
            paifangKouAttributes.setAttributeThree(facilityOwnerShipUnitView.getText());
        }

        if (attributeOneItem != null) {
            paifangKouAttributes.setAttributeOne(attributeOneItem.getText());
        }
        if (fiveItemChecked && StringUtil.isEmpty(attributeFiveItem.getText().toString().replace(" ", ""))) {
            paifangKouAttributes.setAttributeGpbh(null);
        } else if (attributeFiveItem != null && fiveItemChecked) {
            paifangKouAttributes.setAttributeGpbh(attributeFiveItem.getText().toString());
        } else {
            paifangKouAttributes.setAttributeGpbh("???");
        }
        if ("??????".equals(paifangKouAttributes.getAttributeFive()) && !StringUtil.isEmpty(mRHOther.getText().toString())) {
            paifangKouAttributes.setAttributeFive("?????????" + mRHOther.getText().toString());
        }

        if ("??????".equals(paifangKouAttributes.getAttributeSix()) && !StringUtil.isEmpty(mLXOther.getText().toString())) {
            paifangKouAttributes.setAttributeSix("?????????" + mLXOther.getText().toString());
        }

        if (index == 1) {
            if (!StringUtil.isEmpty(mEt_pipe1.getText().toString())) {
                paifangKouAttributes.setAttributeSeven("DN" + mEt_pipe1.getText().toString() + "mm");
            }else{
                paifangKouAttributes.setAttributeSeven(null);
            }
        } else if (index == 2) {
            if (!StringUtil.isEmpty(mEt_pipe21.getText().toString()) && !StringUtil.isEmpty(mEt_pipe22.getText().toString())) {
                paifangKouAttributes.setAttributeSeven(mEt_pipe21.getText().toString() + "mm??" + mEt_pipe22.getText().toString() + "mm");
            }else{
                paifangKouAttributes.setAttributeSeven(null);
            }
        } else if (index == 3) {
            if (!StringUtil.isEmpty(mEt_pipe3.getText().toString())) {
                paifangKouAttributes.setAttributeSeven("?????????" + mEt_pipe3.getText().toString());
            }else{
                paifangKouAttributes.setAttributeSeven(null);
            }
        } else {
            paifangKouAttributes.setAttributeSeven(null);
        }

        //????????????
        if (paifangKouAttributes.getAttributeOne() != null && !paifangKouAttributes.getAttributeOne().equals(originAttributeValue.get(ATTRIBUTE_ONE))) {
            paifangKouAttributes.setHasModified(true);
        } else if (paifangKouAttributes.getAttributeTwo() != null && !paifangKouAttributes.getAttributeTwo().equals(originAttributeValue.get(ATTRIBUTE_TWO))) {
            paifangKouAttributes.setHasModified(true);
        } else if (paifangKouAttributes.getAttributeThree() != null && !paifangKouAttributes.getAttributeThree().equals(originAttributeValue.get(ATTRIBUTE_THREE))) {
            paifangKouAttributes.setHasModified(true);
        }
        return paifangKouAttributes;
    }

    public PaifangKouAttributesNew getOriginalPaifangKouAttributes() {

        PaifangKouAttributesNew paifangKouAttributes = new PaifangKouAttributesNew();
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
        String attributeGpbh = originAttributeValue.get(ATTRIBUTE_GPBH);
        if (attributeGpbh != null) {
            paifangKouAttributes.setAttributeGpbh(attributeGpbh);
        }
        return paifangKouAttributes;

    }

        //??????
    public static void addReadOnlyAttributes(Context context,
                                             Map<String, Object> originValue,
                                             PaifangKouAttributesNew paifangKouAttributes,
                                             ViewGroup llContainer,ModifiedFacility modifiedFacility) {
        //?????????
        String componentOriginValue = getComponentOriginValue(originValue, ATTRIBUTE_ONE);
        ReadOnlyAttributeView readOnlyAttributeView = new ReadOnlyAttributeView(context,
                ATTRIBUTE_ONE, componentOriginValue, paifangKouAttributes.getAttributeOne());
        readOnlyAttributeView.addTo(llContainer);

//        //?????????
//        String attributeTwo = getComponentOriginValue(originValue, ATTRIBUTE_TWO);
//        ReadOnlyAttributeView readOnlyAttributeView2 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_TWO, attributeTwo, paifangKouAttributes.getAttributeTwo());
//        readOnlyAttributeView2.addTo(llContainer);


        String componentOriginValue4 = getComponentOriginValue(originValue, ATTRIBUTE_FOUR);
        ReadOnlyAttributeView readOnlyAttributeView4 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_FOUR, componentOriginValue4, paifangKouAttributes.getAttributeFour());
        readOnlyAttributeView4.addTo(llContainer);

        //????????????
        ReadOnlyGuaPaiNoPfKouView readOnlyGuaPaiNoView5 = new ReadOnlyGuaPaiNoPfKouView(context,
                ATTRIBUTE_GPBH_KEY, paifangKouAttributes.getAttributeGpbh(),modifiedFacility.getWellPhotos());
        readOnlyGuaPaiNoView5.addTo(llContainer);

            //????????????
        String componentOriginValue5 = getComponentOriginValue(originValue, ATTRIBUTE_FIVE);
        ReadOnlyAttributeView readOnlyAttributeView5 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_FIVE, componentOriginValue5, paifangKouAttributes.getAttributeFive());
        readOnlyAttributeView5.addTo(llContainer);
        //?????????????????????
        String componentOriginValue6 = getComponentOriginValue(originValue, ATTRIBUTE_SIX);
        ReadOnlyAttributeView readOnlyAttributeView6 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_SIX, componentOriginValue6, paifangKouAttributes.getAttributeSix());
        readOnlyAttributeView6.addTo(llContainer);
        //???????????????
        String componentOriginValue7 = getComponentOriginValue(originValue, ATTRIBUTE_SEVEN);
        ReadOnlyAttributeView readOnlyAttributeView7 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_SEVEN, componentOriginValue7, paifangKouAttributes.getAttributeSeven());
        readOnlyAttributeView7.addTo(llContainer);

        //??????3 ????????????
        String componentOriginValue3 = getComponentOriginValue(originValue, ATTRIBUTE_THREE);
        ReadOnlyAttributeView readOnlyAttributeView3 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_THREE, componentOriginValue3, paifangKouAttributes.getAttributeThree());
        readOnlyAttributeView3.addTo(llContainer);
    }

    //??????
    public static void addReadOnlyAttributes(Context context,
                                             PaifangKouAttributesNew paifangKouAttributes,
                                             ViewGroup llContainer,UploadedFacility uploadedFacility) {
        //?????????
        ReadOnlyAttributeView readOnlyAttributeView = new ReadOnlyAttributeView(context,
                ATTRIBUTE_ONE, paifangKouAttributes.getAttributeOne());
        readOnlyAttributeView.addTo(llContainer);

//        //?????????
//        String attributeTwo = getComponentOriginValue(originValue, ATTRIBUTE_TWO);
//        ReadOnlyAttributeView readOnlyAttributeView2 = new ReadOnlyAttributeView(context,
//                ATTRIBUTE_TWO, attributeTwo, paifangKouAttributes.getAttributeTwo());
//        readOnlyAttributeView2.addTo(llContainer);


        ReadOnlyAttributeView readOnlyAttributeView4 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_FOUR, paifangKouAttributes.getAttributeFour());
        readOnlyAttributeView4.addTo(llContainer);

        ReadOnlyGuaPaiNoPfKouView readOnlyGuaPaiNoView5 = new ReadOnlyGuaPaiNoPfKouView(context,
                ATTRIBUTE_GPBH_KEY, paifangKouAttributes.getAttributeGpbh(),uploadedFacility.getWellPhotos());
        readOnlyGuaPaiNoView5.addTo(llContainer);
        ReadOnlyAttributeView readOnlyAttributeView5 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_FIVE, paifangKouAttributes.getAttributeFive());
        readOnlyAttributeView5.addTo(llContainer);

        ReadOnlyAttributeView readOnlyAttributeView6 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_SIX, paifangKouAttributes.getAttributeSix());
        readOnlyAttributeView6.addTo(llContainer);

        ReadOnlyAttributeView readOnlyAttributeView7 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_SEVEN, paifangKouAttributes.getAttributeSeven());
        readOnlyAttributeView7.addTo(llContainer);

        //??????3
        ReadOnlyAttributeView readOnlyAttributeView3 = new ReadOnlyAttributeView(context,
                ATTRIBUTE_THREE, paifangKouAttributes.getAttributeThree());
        readOnlyAttributeView3.addTo(llContainer);

    }


    public static Map<String, String> getDefaultValue(ModifiedFacility modifiedFacility) {

        Map<String, String> defaultSelectValue = new HashMap<>(5);
        defaultSelectValue.put(ATTRIBUTE_ONE, modifiedFacility.getAttrOne());
        defaultSelectValue.put(ATTRIBUTE_TWO, modifiedFacility.getAttrTwo());
        defaultSelectValue.put(ATTRIBUTE_THREE, modifiedFacility.getAttrThree());
        defaultSelectValue.put(ATTRIBUTE_FOUR, modifiedFacility.getAttrFour());
        defaultSelectValue.put(ATTRIBUTE_FIVE, modifiedFacility.getAttrFive());
        defaultSelectValue.put(ATTRIBUTE_SIX, modifiedFacility.getAttrSix());
        defaultSelectValue.put(ATTRIBUTE_SEVEN, modifiedFacility.getAttrSeven());
        defaultSelectValue.put(ATTRIBUTE_GPBH, modifiedFacility.getOriginAttrGpbh());
        defaultSelectValue.put(ATTRIBUTE_X, String.valueOf(modifiedFacility.getRiverx()));
        defaultSelectValue.put(ATTRIBUTE_Y, String.valueOf(modifiedFacility.getRivery()));
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
        if (modifiedFacility.getGpbh()!= null) {
            originValue.put(ATTRIBUTE_GPBH_KEY, modifiedFacility.getOriginAttrGpbh());
        } else {
            originValue.put(ATTRIBUTE_GPBH_KEY, "");
        }
        return originValue;
    }

    public void onDestroy() {
        if (facilityOwnerShipUnitView != null) {
            facilityOwnerShipUnitView.onDestroy();
            facilityOwnerShipUnitView = null;
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param selectComponentFinishEvent
     */
    @Subscribe
    public void onReceivedFinishedSelectEvent2(SelectComponentFinishEvent2 selectComponentFinishEvent) {
        Component component = selectComponentFinishEvent.getFindResult();
        mSelComponent = component;
        mSelDetailAddress = selectComponentFinishEvent.getDetailAddress();
        if (mSelComponent != null) {
            //??????????????????
            String subType = String.valueOf(component.getGraphic().getAttributes().get(ComponentFieldKeyConstant.NAME));
            String title = StringUtil.getNotNullString(subType, "");
            tv_select_or_check_location.setText(title);
            mName.setText(title);
            paifangKouAttributes.setAttributeOne(title);
        }
        if (mSelDetailAddress != null) {
            paifangKouAttributes.setX(mSelDetailAddress.getX());
            paifangKouAttributes.setY(mSelDetailAddress.getY());
        }

    }

}

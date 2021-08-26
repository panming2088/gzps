package com.augurit.agmobile.gzps.professionreparation.reparationnewfacility;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.facilityownership.FacilityOwnerShipUnitView;
import com.augurit.agmobile.gzps.common.widget.FlexRadioGroup;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAttributeModel;
import com.augurit.agmobile.gzps.uploadfacility.model.PaifangKouAttributes;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.YinjingAttributes;
import com.augurit.agmobile.gzps.uploadfacility.model.YuShuiKouAttributes;
import com.augurit.agmobile.gzps.uploadfacility.util.PaifangKouAttributeViewUtil;
import com.augurit.agmobile.gzps.uploadfacility.util.RpYinjingAttributeViewUtil;
import com.augurit.agmobile.gzps.uploadfacility.util.YinjingAttributeViewUtil;
import com.augurit.agmobile.gzps.uploadfacility.util.YushuikouAttributeViewUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 添加设施属性
 * <p>
 * Created by xcl on 2017/11/12.
 */

public class RpFacilityAttributeView {


    private Context mContext;
    private List<TextItemTableItem> addedTextItems = new ArrayList<>();
    private LinearLayout ll_attributelist_container;
    private View root;
    private Map<String, String> selectedAttributes = new HashMap<>(5);
    private String layerName = null;
    /**
     * 权属单位
     */
    private FacilityOwnerShipUnitView ownerShipUnit = null;
    private RpYinjingAttributeViewUtil yinjingAttributeViewUtil;
    private YushuikouAttributeViewUtil yuShuiKouAttributes;
    private PaifangKouAttributeViewUtil paifangKouAttributeViewUtil;

    public RpFacilityAttributeView(Context context) {
        this.mContext = context;
        initView();
    }


    public RpFacilityAttributeView(Context context, UploadedFacility uploadedFacility) {
        this.mContext = context;
        initView(uploadedFacility);
    }


    public void addTo(ViewGroup container) {
        container.addView(root);
    }

    private void initView() {
        root = View.inflate(mContext, R.layout.view_reparation_add_new_facility, null);
        ll_attributelist_container = (LinearLayout) root.findViewById(R.id.ll_attributelist_container);
        FlexRadioGroup flexRadioGroup = (FlexRadioGroup) root.findViewById(R.id.rg_1);
        flexRadioGroup.setOnCheckedChangeListener(new FlexRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yinjing:
                        layerName = "窨井";
                        ll_attributelist_container.removeAllViews();
                        yinjingAttributeViewUtil = new RpYinjingAttributeViewUtil();
                        yinjingAttributeViewUtil.addYinjingAttributes(mContext, null, ll_attributelist_container, null);
                        break;
                    case R.id.rb_yushuikou:
                        layerName = "雨水口";
                        ll_attributelist_container.removeAllViews();
                        yuShuiKouAttributes = new YushuikouAttributeViewUtil();
                        yuShuiKouAttributes.addYushuikouAttributes(mContext, null, ll_attributelist_container, null);
                        break;
                    case R.id.rb_paifangkou:
                        layerName = "排水口";
                        ll_attributelist_container.removeAllViews();
                        paifangKouAttributeViewUtil = new PaifangKouAttributeViewUtil();
                        paifangKouAttributeViewUtil.addYushuikouAttributes(mContext, null, ll_attributelist_container, null);
                        break;
                    default:
                        break;
                }
            }
        });
        flexRadioGroup.check(R.id.rb_yinjing);
    }


    private void initView(final UploadedFacility uploadedFacility) {
        root = View.inflate(mContext, R.layout.view_reparation_add_new_facility, null);
        ll_attributelist_container = (LinearLayout) root.findViewById(R.id.ll_attributelist_container);
        FlexRadioGroup flexRadioGroup = (FlexRadioGroup) root.findViewById(R.id.rg_1);
        flexRadioGroup.setOnCheckedChangeListener(new FlexRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@IdRes int checkedId) {
                Map<String, String> defaultSelectValue = null;
                switch (checkedId) {
                    case R.id.rb_yinjing:
                        layerName = "窨井";
                        ll_attributelist_container.removeAllViews();
                        if (uploadedFacility != null && layerName.equals(uploadedFacility.getLayerName())) {
                            defaultSelectValue = new HashMap<>(5);
                            defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_ONE, uploadedFacility.getAttrOne());
                            defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_TWO, uploadedFacility.getAttrTwo());
                            defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_THREE, uploadedFacility.getAttrThree());
                            defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_FOUR, uploadedFacility.getAttrFour());
                            if(uploadedFacility.getAttrFive() == null){
                                defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_FIVE, "");
                            } else {
                                defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_FIVE, uploadedFacility.getAttrFive());
                            }
                        }
                        yinjingAttributeViewUtil = new RpYinjingAttributeViewUtil();
                        yinjingAttributeViewUtil.addYinjingAttributes(mContext, null, ll_attributelist_container, defaultSelectValue);
                        break;
                    case R.id.rb_yushuikou:
                        layerName = "雨水口";
                        ll_attributelist_container.removeAllViews();
                        if (uploadedFacility != null && layerName.equals(uploadedFacility.getLayerName())) {
                            defaultSelectValue = new HashMap<>(5);
//                            defaultSelectValue.put(YushuikouAttributeViewUtil.ATTRIBUTE_ONE, uploadedFacility.getAttrOne());
//                            defaultSelectValue.put(YushuikouAttributeViewUtil.ATTRIBUTE_THREE, uploadedFacility.getAttrThree());
//                            defaultSelectValue.put(YushuikouAttributeViewUtil.ATTRIBUTE_FOUR, uploadedFacility.getAttrFour());
                        }
                        yuShuiKouAttributes = new YushuikouAttributeViewUtil();
                        yuShuiKouAttributes.addYushuikouAttributes(mContext, null, ll_attributelist_container, defaultSelectValue);
                        break;
                    case R.id.rb_paifangkou:
                        layerName = "排水口";
                        ll_attributelist_container.removeAllViews();
                        if (uploadedFacility != null && layerName.equals(uploadedFacility.getLayerName())) {
                            defaultSelectValue = new HashMap<>(5);
                            defaultSelectValue.put(PaifangKouAttributeViewUtil.ATTRIBUTE_ONE, uploadedFacility.getAttrOne());
                            defaultSelectValue.put(PaifangKouAttributeViewUtil.ATTRIBUTE_TWO, uploadedFacility.getAttrTwo());
                            defaultSelectValue.put(PaifangKouAttributeViewUtil.ATTRIBUTE_THREE, uploadedFacility.getAttrThree());
                        }
                        paifangKouAttributeViewUtil = new PaifangKouAttributeViewUtil();
                        paifangKouAttributeViewUtil.addYushuikouAttributes(mContext, null, ll_attributelist_container, defaultSelectValue);
                        break;
                    default:
                        break;
                }
            }
        });

        if ("窨井".equals(uploadedFacility.getLayerName())) {
            flexRadioGroup.check(R.id.rb_yinjing);
        } else if ("雨水口".equals(uploadedFacility.getLayerName())) {
            flexRadioGroup.check(R.id.rb_yushuikou);
        } else if ("排水口".equals(uploadedFacility.getLayerName())) {
            flexRadioGroup.check(R.id.rb_paifangkou);
        }
    }


    public FacilityAttributeModel getFacilityAttributeModel() {

        FacilityAttributeModel facilityAttributeModel = new FacilityAttributeModel();
        facilityAttributeModel.setLayerName(layerName);
        if (layerName.equals("窨井") && yinjingAttributeViewUtil != null) {
            YinjingAttributes yinjingAttributes = yinjingAttributeViewUtil.getYinjingAttributes();
            facilityAttributeModel.setAttrOne(yinjingAttributes.getAttributeOne());
            facilityAttributeModel.setAttrTwo(yinjingAttributes.getAttributeTwo());
            facilityAttributeModel.setAttrThree(yinjingAttributes.getAttributeThree());
            facilityAttributeModel.setAttrFour(yinjingAttributes.getAttributeFour());
            facilityAttributeModel.setAttrFive(yinjingAttributes.getAttributeFive());
            facilityAttributeModel.setIfAllowUpload(yinjingAttributes.ifAllowUpload());
            facilityAttributeModel.setNotAllowUploadReason(yinjingAttributes.getNotAllowUploadReason());
        } else if (layerName.equals("雨水口") && yuShuiKouAttributes != null) {
            YuShuiKouAttributes yuShuiKouAttributes = this.yuShuiKouAttributes.getYuShuiKouAttributes();
            facilityAttributeModel.setAttrOne(yuShuiKouAttributes.getAttributeOne());
            facilityAttributeModel.setAttrThree(yuShuiKouAttributes.getAttributeThree());
            facilityAttributeModel.setAttrFour(yuShuiKouAttributes.getAttributeFour());
            facilityAttributeModel.setIfAllowUpload(yuShuiKouAttributes.ifAllowUpload());
            facilityAttributeModel.setNotAllowUploadReason(yuShuiKouAttributes.getNotAllowUploadReason());
        } else if (layerName.equals("排水口") && paifangKouAttributeViewUtil != null) {
            PaifangKouAttributes paifangKouAttributes = paifangKouAttributeViewUtil.getPaifangKouAttributes();
            facilityAttributeModel.setAttrOne(paifangKouAttributes.getAttributeOne());
            facilityAttributeModel.setAttrTwo(paifangKouAttributes.getAttributeTwo());
            facilityAttributeModel.setAttrThree(paifangKouAttributes.getAttributeThree());
            facilityAttributeModel.setIfAllowUpload(paifangKouAttributes.ifAllowUpload());
            facilityAttributeModel.setNotAllowUploadReason(paifangKouAttributes.getNotAllowUploadReason());
        }
        return facilityAttributeModel;
    }


    public void destroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        if (yinjingAttributeViewUtil != null) {
            yinjingAttributeViewUtil.onDestroy();
            yinjingAttributeViewUtil = null;
        }

        if (yuShuiKouAttributes != null) {
            yuShuiKouAttributes.onDestroy();
            yuShuiKouAttributes = null;
        }

        if (paifangKouAttributeViewUtil != null) {
            paifangKouAttributeViewUtil.onDestroy();
            paifangKouAttributeViewUtil = null;
        }

    }

}

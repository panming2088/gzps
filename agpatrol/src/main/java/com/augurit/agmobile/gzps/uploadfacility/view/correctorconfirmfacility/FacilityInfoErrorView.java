package com.augurit.agmobile.gzps.uploadfacility.view.correctorconfirmfacility;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.selectcomponent.SelectLimitedComponentActivity;
import com.augurit.agmobile.gzps.common.widget.FlexRadioGroup;
import com.augurit.agmobile.gzps.uploadfacility.model.FacilityInfoErrorModel;
import com.augurit.agmobile.gzps.uploadfacility.model.ModifiedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.PaifangKouAttributesNew;
import com.augurit.agmobile.gzps.uploadfacility.model.RefreshTypeEvent;
import com.augurit.agmobile.gzps.uploadfacility.model.YinjingAttributes;
import com.augurit.agmobile.gzps.uploadfacility.model.YuShuiKouAttributes;
import com.augurit.agmobile.gzps.uploadfacility.util.PaifangKouAttributeViewUtilNew;
import com.augurit.agmobile.gzps.uploadfacility.util.YinjingAttributeViewUtil;
import com.augurit.agmobile.gzps.uploadfacility.util.YushuikouAttributeViewUtil;
import com.augurit.agmobile.patrolcore.common.model.Component;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * 信息错误视图
 * Created by xcl on 2017/11/12.
 */

public class FacilityInfoErrorView {

    private Context mContext;
//    private TextItemTableItem textItemComponentType;
    private View root;
    private LinearLayout attributelistContainer;
    private TextView tvSelectOrCheckLocation;
    private Component mCurrentComponent;
    private Geometry mLastSelectLocation;
    private FacilityInfoErrorModel facilityInfoErrorModel;

    private YinjingAttributeViewUtil yinjingAttributeViewUtil;
    private YushuikouAttributeViewUtil yuShuiKouAttributes;
    private PaifangKouAttributeViewUtilNew paifangKouAttributeViewUtil;
    private boolean isMenPai = true;
    private boolean isAllowEditWellType = false;
    public FacilityInfoErrorView(Context context,
                                 Component component) {
        this.mContext = context;
        isAllowEditWellType = component.isAllowEditWellType();
        this.facilityInfoErrorModel = new FacilityInfoErrorModel();
        initView(component);
    }

    public FacilityInfoErrorView(Context context,
                                 ModifiedFacility modifiedFacility) {
        this.mContext = context;
        isAllowEditWellType = modifiedFacility.isAllowEditWellType();
        this.facilityInfoErrorModel = new FacilityInfoErrorModel();
        initView(modifiedFacility);
    }

    public FacilityInfoErrorView(Context context,
                                 ModifiedFacility modifiedFacility,boolean isMenPai) {
        this.mContext = context;
        this.isMenPai = isMenPai;
        this.facilityInfoErrorModel = new FacilityInfoErrorModel();
        initView(modifiedFacility);
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

    public void addTo(ViewGroup container) {
        container.addView(root);
    }


    private void initView(final ModifiedFacility modifiedFacility) {

        root = View.inflate(mContext, R.layout.view_component_info_error, null);
//        textItemComponentType = (TextItemTableItem) root.findViewById(R.id.textitem_component_type);
        attributelistContainer = (LinearLayout) root.findViewById(R.id.ll_attributelist_container);
        tvSelectOrCheckLocation = (TextView) root.findViewById(R.id.tv_select_or_check_location);
        /**
         * 选择部件
         */
        root.findViewById(R.id.ll_select_component).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectLimitedComponentActivity.class);
                if (mCurrentComponent != null) {
                    intent.putExtra("component", mCurrentComponent);
                }
                mContext.startActivity(intent);
            }
        });
        root.findViewById(R.id.rl_select_location).setVisibility(View.GONE);

        /**
         * 根据部件类型进行显示属性
         */
        if (modifiedFacility == null) {
            return;
        }
//        textItemComponentType.setText(modifiedFacility.getLayerName());
//        textItemComponentType.setVisibility(View.VISIBLE);
//        textItemComponentType.setReadOnly();
        attributelistContainer.setVisibility(View.VISIBLE);
        FlexRadioGroup flexRadioGroup = (FlexRadioGroup) root.findViewById(R.id.rg_1);
        if(!modifiedFacility.isAllowEditWellType()){
            flexRadioGroup.setEnabled(false);
        }

//        switch (modifiedFacility.getLayerName()) {
//            case "窨井":
//                attributelistContainer.removeAllViews();
//
//                yinjingAttributeViewUtil = new YinjingAttributeViewUtil();
//                Point point = new Point(modifiedFacility.getX(),modifiedFacility.getY());
//                if(isMenPai) {
//                    yinjingAttributeViewUtil.addYinjingAttributes(mContext,
//                            YinjingAttributeViewUtil.getOriginValue(modifiedFacility),
//                            attributelistContainer,
//                            YinjingAttributeViewUtil.getDefaultValue(modifiedFacility), modifiedFacility.getMpBeen(), point,modifiedFacility);
//                }else{
//                    yinjingAttributeViewUtil.addYinjingAttributes(mContext,
//                            YinjingAttributeViewUtil.getOriginValue(modifiedFacility),
//                            attributelistContainer,
//                            YinjingAttributeViewUtil.getDefaultValue(modifiedFacility),modifiedFacility);
//                }
//                break;
//            case "雨水口":
//                attributelistContainer.removeAllViews();
//                yuShuiKouAttributes = new YushuikouAttributeViewUtil();
//
//                yuShuiKouAttributes.addYushuikouAttributes(mContext,
//                        YushuikouAttributeViewUtil.getOriginValue(modifiedFacility),
//                        attributelistContainer,
//                        YushuikouAttributeViewUtil.getDefaultValue(modifiedFacility));
//                break;
//            case "排水口":
//                attributelistContainer.removeAllViews();
//                paifangKouAttributeViewUtil = new PaifangKouAttributeViewUtilNew();
//
//                paifangKouAttributeViewUtil.addYushuikouAttributes(mContext,
//                        PaifangKouAttributeViewUtilNew.getOriginValue(modifiedFacility),
//                        attributelistContainer,
//                        PaifangKouAttributeViewUtilNew.getDefaultValue(modifiedFacility),modifiedFacility);
//                break;
//            default:
//                break;
//        }
        flexRadioGroup.setOnCheckedChangeListener(new FlexRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@IdRes int checkedId) {
                Map<String, String> defaultSelectValue = null;
                switch (checkedId) {
                    case R.id.rb_yinjing:
                        layerName = "窨井";
                        EventBus.getDefault().post(new RefreshTypeEvent(1));
                        attributelistContainer.removeAllViews();
                        yinjingAttributeViewUtil = new YinjingAttributeViewUtil();
                Point point = new Point(modifiedFacility.getX(),modifiedFacility.getY());
                if(isMenPai) {
                    yinjingAttributeViewUtil.addYinjingAttributes(mContext,
                            YinjingAttributeViewUtil.getOriginValue(modifiedFacility),
                            attributelistContainer,
                            YinjingAttributeViewUtil.getDefaultValue(modifiedFacility), modifiedFacility.getMpBeen(), point,modifiedFacility);
                }else{
                    yinjingAttributeViewUtil.addYinjingAttributes(mContext,
                            YinjingAttributeViewUtil.getOriginValue(modifiedFacility),
                            attributelistContainer,
                            YinjingAttributeViewUtil.getDefaultValue(modifiedFacility),modifiedFacility);
                }
                        break;
                    case R.id.rb_yushuikou:
                        layerName = "雨水口";
                        EventBus.getDefault().post(new RefreshTypeEvent(2));
                        attributelistContainer.removeAllViews();

                        yuShuiKouAttributes = new YushuikouAttributeViewUtil();
                         yuShuiKouAttributes.addYushuikouAttributes(mContext,
                        YushuikouAttributeViewUtil.getOriginValue(modifiedFacility),
                        attributelistContainer,
                        YushuikouAttributeViewUtil.getDefaultValue(modifiedFacility));

//                        if (uploadedFacility != null && layerName.equals(uploadedFacility.getLayerName())) {
//                            defaultSelectValue = new HashMap<>(5);
//                            defaultSelectValue.put(YushuikouAttributeViewUtil.ATTRIBUTE_ONE, uploadedFacility.getAttrOne());
//                            defaultSelectValue.put(YushuikouAttributeViewUtil.ATTRIBUTE_THREE, uploadedFacility.getAttrThree());
//                            defaultSelectValue.put(YushuikouAttributeViewUtil.ATTRIBUTE_FOUR, uploadedFacility.getAttrFour());
//                        }
//                        yuShuiKouAttributes = new YushuikouAttributeViewUtil();
//                        yuShuiKouAttributes.addYushuikouAttributes(mContext, null, ll_attributelist_container, defaultSelectValue);
                        break;
                    case R.id.rb_paifangkou:
                        layerName = "排水口";
                        EventBus.getDefault().post(new RefreshTypeEvent(3));
                        attributelistContainer.removeAllViews();
                        paifangKouAttributeViewUtil = new PaifangKouAttributeViewUtilNew();
                        paifangKouAttributeViewUtil.addYushuikouAttributes(mContext,
                        PaifangKouAttributeViewUtilNew.getOriginValue(modifiedFacility),
                        attributelistContainer,
                        PaifangKouAttributeViewUtilNew.getDefaultValue(modifiedFacility),modifiedFacility);
                        break;
                    default:
                        break;
                }
            }
        });

        if ("窨井".equals(modifiedFacility.getLayerName())) {
            flexRadioGroup.check(R.id.rb_yinjing);
        } else if ("雨水口".equals(modifiedFacility.getLayerName())) {
            flexRadioGroup.check(R.id.rb_yushuikou);
        } else if ("排水口".equals(modifiedFacility.getLayerName())) {
            flexRadioGroup.check(R.id.rb_paifangkou);
        }
    }

    private String layerName = null;
    private void initView(final Component component) {
        root = View.inflate(mContext, R.layout.view_component_info_error, null);
//        textItemComponentType = (TextItemTableItem) root.findViewById(R.id.textitem_component_type);
        attributelistContainer = (LinearLayout) root.findViewById(R.id.ll_attributelist_container);
        FlexRadioGroup flexRadioGroup = (FlexRadioGroup) root.findViewById(R.id.rg_1);
        tvSelectOrCheckLocation = (TextView) root.findViewById(R.id.tv_select_or_check_location);
        /**
         * 选择部件
         */
        root.findViewById(R.id.ll_select_component).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectLimitedComponentActivity.class);
                if (mCurrentComponent != null) {
                    intent.putExtra("component", mCurrentComponent);
                }
                mContext.startActivity(intent);
            }
        });
        root.findViewById(R.id.rl_select_location).setVisibility(View.GONE);

        /**
         * 根据部件类型进行显示属性
         */
        if (component == null) {
            return;
        }
//        textItemComponentType.setText(component.getLayerName());
//        textItemComponentType.setVisibility(View.VISIBLE);
        if(!component.isAllowEditWellType()){
            flexRadioGroup.setEnabled(false);
        }
        attributelistContainer.setVisibility(View.VISIBLE);

//        switch (component.getLayerName()) {
//            case "窨井":
//                attributelistContainer.removeAllViews();
//                yinjingAttributeViewUtil = new YinjingAttributeViewUtil();
//                yinjingAttributeViewUtil.addYinjingAttributes(mContext, YinjingAttributeViewUtil.getOriginValue(component), attributelistContainer,YinjingAttributeViewUtil.getDefaultValue(component), (Point) component.getGraphic().getGeometry(),null);
//                break;
//            case "雨水口":
//                attributelistContainer.removeAllViews();
//                yuShuiKouAttributes = new YushuikouAttributeViewUtil();
//                yuShuiKouAttributes.addYushuikouAttributes(mContext, component.getGraphic().getAttributes(), attributelistContainer, null);
//                break;
//                //改不动 从新写界面PfkJournalActivity
//            case "排水口":
//                attributelistContainer.removeAllViews();
//                paifangKouAttributeViewUtil = new PaifangKouAttributeViewUtilNew();
//                paifangKouAttributeViewUtil.addYushuikouAttributes(mContext, component.getGraphic().getAttributes(), attributelistContainer, null,null);
//                break;
//            default:
//                break;
//        }
        flexRadioGroup.setOnCheckedChangeListener(new FlexRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@IdRes int checkedId) {
                Map<String, String> defaultSelectValue = null;
                switch (checkedId) {
                    case R.id.rb_yinjing:
                        layerName = "窨井";
                        EventBus.getDefault().post(new RefreshTypeEvent(1));
                        attributelistContainer.removeAllViews();
                        yuShuiKouAttributes = null;
                        paifangKouAttributeViewUtil = null;
                        yinjingAttributeViewUtil = new YinjingAttributeViewUtil();
                         yinjingAttributeViewUtil.addYinjingAttributes(mContext, YinjingAttributeViewUtil.getOriginValue(component), attributelistContainer,YinjingAttributeViewUtil.getDefaultValue(component), (Point) component.getGraphic().getGeometry(),null);
                break;
                    case R.id.rb_yushuikou:
                        layerName = "雨水口";
                        EventBus.getDefault().post(new RefreshTypeEvent(2));
                        attributelistContainer.removeAllViews();
                        yinjingAttributeViewUtil = null;
                        paifangKouAttributeViewUtil = null;
                        yuShuiKouAttributes = new YushuikouAttributeViewUtil();
                        yuShuiKouAttributes.addYushuikouAttributes(mContext, component.getGraphic().getAttributes(), attributelistContainer, YinjingAttributeViewUtil.getDefaultValue(component));
                        break;
                    case R.id.rb_paifangkou:
                        layerName = "排水口";
                        EventBus.getDefault().post(new RefreshTypeEvent(3));
                        attributelistContainer.removeAllViews();
                        yinjingAttributeViewUtil = null;
                        yuShuiKouAttributes = null;
                        paifangKouAttributeViewUtil = new PaifangKouAttributeViewUtilNew();
                        paifangKouAttributeViewUtil.addYushuikouAttributes(mContext, component.getGraphic().getAttributes(), attributelistContainer, null,null);
                        break;
                    default:
                        break;
                }
            }
        });

        if ("窨井".equals(component.getLayerName())) {
            flexRadioGroup.check(R.id.rb_yinjing);
        } else if ("雨水口".equals(component.getLayerName())) {
            flexRadioGroup.check(R.id.rb_yushuikou);
        } else if ("排水口".equals(component.getLayerName())) {
            flexRadioGroup.check(R.id.rb_paifangkou);
        }
    }

    public FacilityInfoErrorModel getFacilityInfoErrorModel() {

        if (yinjingAttributeViewUtil != null) {
            YinjingAttributes yinjingAttributes = yinjingAttributeViewUtil.getYinjingAttributes();
            facilityInfoErrorModel.setAttrOne(yinjingAttributes.getAttributeOne());
            facilityInfoErrorModel.setMpBeen(yinjingAttributes.getMpBeen());
            facilityInfoErrorModel.setAttrTwo(yinjingAttributes.getAttributeTwo());
            facilityInfoErrorModel.setAttrSfgjjd(yinjingAttributes.getAttributesfGjjd());
            facilityInfoErrorModel.setAttrJDBH(yinjingAttributes.getGjjdBh());
            facilityInfoErrorModel.setAttrYJBH(yinjingAttributes.getAttributeYjBh());
            facilityInfoErrorModel.setAttrJDZRR(yinjingAttributes.getGjjdZrr());
            facilityInfoErrorModel.setAttrLXDH(yinjingAttributes.getLxdh());
            facilityInfoErrorModel.setAttrThree(yinjingAttributes.getAttributeThree());
            facilityInfoErrorModel.setAttrFour(yinjingAttributes.getAttributeFour());
            facilityInfoErrorModel.setAttrFive(yinjingAttributes.getAttributeFive());
            facilityInfoErrorModel.setHasModified(yinjingAttributes.isHasModified());
            facilityInfoErrorModel.setIfAllowUpload(yinjingAttributes.ifAllowUpload());
            facilityInfoErrorModel.setAttrsfCzwscl(yinjingAttributes.getAttributesfCzwscl());
            facilityInfoErrorModel.setSfpsdyhxn(yinjingAttributes.getSfpsdyhxn());
            facilityInfoErrorModel.setNotAllowUploadReason(yinjingAttributes.getNotAllowUploadReason());
        }

        if (yuShuiKouAttributes != null) {
            YuShuiKouAttributes yuShuiKouAttributes = this.yuShuiKouAttributes.getYuShuiKouAttributes();
            facilityInfoErrorModel.setAttrOne(yuShuiKouAttributes.getAttributeOne());
            facilityInfoErrorModel.setAttrThree(yuShuiKouAttributes.getAttributeThree());
            facilityInfoErrorModel.setAttrFour(yuShuiKouAttributes.getAttributeFour());
            facilityInfoErrorModel.setSfpsdyhxn(yuShuiKouAttributes.getSfpsdyhxn());
            facilityInfoErrorModel.setHasModified(yuShuiKouAttributes.isHasModified());
            facilityInfoErrorModel.setIfAllowUpload(yuShuiKouAttributes.ifAllowUpload());
            facilityInfoErrorModel.setNotAllowUploadReason(yuShuiKouAttributes.getNotAllowUploadReason());
        }

        if (paifangKouAttributeViewUtil != null) {
            PaifangKouAttributesNew paifangKouAttributes = paifangKouAttributeViewUtil.getPaifangKouAttributes();
            facilityInfoErrorModel.setAttrOne(paifangKouAttributes.getAttributeOne());
            facilityInfoErrorModel.setAttrTwo(paifangKouAttributes.getAttributeTwo());
            facilityInfoErrorModel.setAttrThree(paifangKouAttributes.getAttributeThree());
            facilityInfoErrorModel.setHasModified(paifangKouAttributes.isHasModified());
            facilityInfoErrorModel.setIfAllowUpload(paifangKouAttributes.ifAllowUpload());
            facilityInfoErrorModel.setNotAllowUploadReason(paifangKouAttributes.getNotAllowUploadReason());
            facilityInfoErrorModel.setAttrFour(paifangKouAttributes.getAttributeFour());
            facilityInfoErrorModel.setAttrFive(paifangKouAttributes.getAttributeFive());
            facilityInfoErrorModel.setAttrSix(paifangKouAttributes.getAttributeSix());
            facilityInfoErrorModel.setAttrSeven(paifangKouAttributes.getAttributeSeven());
            facilityInfoErrorModel.setX(paifangKouAttributes.getX());
            facilityInfoErrorModel.setY(paifangKouAttributes.getY());
        }

        return facilityInfoErrorModel;
    }

    public FacilityInfoErrorModel getOriginalModel() {
        if (yinjingAttributeViewUtil != null) {
            YinjingAttributes yinjingAttributes = yinjingAttributeViewUtil.getOriginalYinjingAttributes();
            facilityInfoErrorModel.setAttrOne(yinjingAttributes.getAttributeOne());
            facilityInfoErrorModel.setAttrsfCzwscl(yinjingAttributes.getAttributesfCzwscl());
            facilityInfoErrorModel.setAttrTwo(yinjingAttributes.getAttributeTwo());
            facilityInfoErrorModel.setAttrThree(yinjingAttributes.getAttributeThree());
            facilityInfoErrorModel.setAttrFour(yinjingAttributes.getAttributeFour());
            facilityInfoErrorModel.setAttrFive(yinjingAttributes.getAttributeFive());
            facilityInfoErrorModel.setHasModified(yinjingAttributes.isHasModified());
            facilityInfoErrorModel.setIfAllowUpload(yinjingAttributes.ifAllowUpload());
            facilityInfoErrorModel.setMpBeen(yinjingAttributes.getMpBeen());
            facilityInfoErrorModel.setNotAllowUploadReason(yinjingAttributes.getNotAllowUploadReason());
        }

        if (yuShuiKouAttributes != null) {
            YuShuiKouAttributes yuShuiKouAttributes = this.yuShuiKouAttributes.getOriginalYuShuiKouAttributes();
            facilityInfoErrorModel.setAttrOne(yuShuiKouAttributes.getAttributeOne());
            facilityInfoErrorModel.setAttrThree(yuShuiKouAttributes.getAttributeThree());
            facilityInfoErrorModel.setAttrFour(yuShuiKouAttributes.getAttributeFour());
            facilityInfoErrorModel.setHasModified(yuShuiKouAttributes.isHasModified());
            facilityInfoErrorModel.setIfAllowUpload(yuShuiKouAttributes.ifAllowUpload());
            facilityInfoErrorModel.setNotAllowUploadReason(yuShuiKouAttributes.getNotAllowUploadReason());
        }

        if (paifangKouAttributeViewUtil != null) {
            PaifangKouAttributesNew paifangKouAttributes = paifangKouAttributeViewUtil.getOriginalPaifangKouAttributes();
            facilityInfoErrorModel.setAttrOne(paifangKouAttributes.getAttributeOne());
            facilityInfoErrorModel.setAttrTwo(paifangKouAttributes.getAttributeTwo());
            facilityInfoErrorModel.setAttrThree(paifangKouAttributes.getAttributeThree());
            facilityInfoErrorModel.setHasModified(paifangKouAttributes.isHasModified());
            facilityInfoErrorModel.setIfAllowUpload(paifangKouAttributes.ifAllowUpload());
            facilityInfoErrorModel.setNotAllowUploadReason(paifangKouAttributes.getNotAllowUploadReason());
            facilityInfoErrorModel.setAttrFive(paifangKouAttributes.getAttributeGpbh());
            facilityInfoErrorModel.setAttrSix(paifangKouAttributes.getAttributeSix());
            facilityInfoErrorModel.setAttrSeven(paifangKouAttributes.getAttributeSeven());
            facilityInfoErrorModel.setX(paifangKouAttributes.getX());
            facilityInfoErrorModel.setY(paifangKouAttributes.getY());
        }
        return facilityInfoErrorModel;
    }

}

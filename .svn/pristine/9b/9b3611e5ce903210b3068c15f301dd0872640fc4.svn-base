package com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.FlexRadioGroup;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.common.facilityownership.FacilityOwnerShipUnitView;

import com.augurit.agmobile.gzps.uploadfacility.model.FacilityAttributeModel;
import com.augurit.agmobile.gzps.uploadfacility.model.PaifangKouAttributesNew;
import com.augurit.agmobile.gzps.uploadfacility.model.RefreshTypeEvent;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.YinjingAttributes;
import com.augurit.agmobile.gzps.uploadfacility.model.YuShuiKouAttributes;
import com.augurit.agmobile.gzps.uploadfacility.util.PaifangKouAttributeViewUtilNew;
import com.augurit.agmobile.gzps.uploadfacility.util.YinjingAttributeViewUtil;
import com.augurit.agmobile.gzps.uploadfacility.util.YushuikouAttributeViewUtil;
import com.esri.core.geometry.Point;

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

public class FacilityAttributeView {


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
    private YinjingAttributeViewUtil yinjingAttributeViewUtil;
    private YushuikouAttributeViewUtil yuShuiKouAttributes;
    private PaifangKouAttributeViewUtilNew paifangKouAttributeViewUtil;
    private boolean isDraft = false;
    private Point mPoint;

    public FacilityAttributeView(Context context) {
        this.mContext = context;
        initView();
    }

    public FacilityAttributeView(Context context,Point point) {
        this.mContext = context;
        this.mPoint = point;
        initView();
    }


    public FacilityAttributeView(Context context, UploadedFacility uploadedFacility) {
        this.mContext = context;
        initView(uploadedFacility);
    }
    public FacilityAttributeView(Context context, UploadedFacility uploadedFacility,boolean isDraft) {
        this.mContext = context;
        this.isDraft = isDraft;
        initView(uploadedFacility);
    }


    public void addTo(ViewGroup container) {
        container.addView(root);
    }

    private void initView() {
        root = View.inflate(mContext, R.layout.view_add_new_facility, null);
        ll_attributelist_container = (LinearLayout) root.findViewById(R.id.ll_attributelist_container);
        FlexRadioGroup flexRadioGroup = (FlexRadioGroup) root.findViewById(R.id.rg_1);
        flexRadioGroup.setOnCheckedChangeListener(new FlexRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yinjing:
                        layerName = "窨井";
                        EventBus.getDefault().post(new RefreshTypeEvent(1));
                        ll_attributelist_container.removeAllViews();
                        yinjingAttributeViewUtil = new YinjingAttributeViewUtil();
//                        yinjingAttributeViewUtil.addYinjingAttributes(mContext, null, ll_attributelist_container, null,null);
                        yinjingAttributeViewUtil.addYinjingAttributes(mContext, null, ll_attributelist_container, null,null,mPoint,null);
                        break;
                    case R.id.rb_yushuikou:
                        EventBus.getDefault().post(new RefreshTypeEvent(2));
                        layerName = "雨水口";
                        ll_attributelist_container.removeAllViews();
                        yuShuiKouAttributes = new YushuikouAttributeViewUtil();
                        yuShuiKouAttributes.addYushuikouAttributes(mContext, null, ll_attributelist_container, null);
                        break;
                    case R.id.rb_paifangkou:
                        EventBus.getDefault().post(new RefreshTypeEvent(3));
                        layerName = "排水口";
                        ll_attributelist_container.removeAllViews();
                        paifangKouAttributeViewUtil = new PaifangKouAttributeViewUtilNew();
                        paifangKouAttributeViewUtil.addYushuikouAttributes(mContext, null, ll_attributelist_container, null,null);
                        break;
                    default:
                        break;
                }
            }
        });
        flexRadioGroup.check(R.id.rb_yinjing);
    }


    private void initView(final UploadedFacility uploadedFacility) {
        root = View.inflate(mContext, R.layout.view_add_new_facility, null);
        ll_attributelist_container = (LinearLayout) root.findViewById(R.id.ll_attributelist_container);
        FlexRadioGroup flexRadioGroup = (FlexRadioGroup) root.findViewById(R.id.rg_1);
        flexRadioGroup.setOnCheckedChangeListener(new FlexRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@IdRes int checkedId) {
                Map<String, String> defaultSelectValue = null;
                switch (checkedId) {
                    case R.id.rb_yinjing:
                        layerName = "窨井";
                        EventBus.getDefault().post(new RefreshTypeEvent(1));
                        ll_attributelist_container.removeAllViews();
                        if (uploadedFacility != null && layerName.equals(uploadedFacility.getLayerName())) {
                            defaultSelectValue = new HashMap<>(5);
                            defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_ONE, uploadedFacility.getAttrOne());
                            defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_TWO, uploadedFacility.getAttrTwo());
                            defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_THREE, uploadedFacility.getAttrThree());
                            defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_FOUR, uploadedFacility.getAttrFour());
                            defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_SFCZWSCL, uploadedFacility.getSfCzwscl());
                            defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_SFHX, uploadedFacility.getSfpsdyhxn());
                            defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_SFGJJD, uploadedFacility.getSfgjjd());
                            defaultSelectValue.put(YinjingAttributeViewUtil.YJ_BH, uploadedFacility.getYjbh());
                            if(uploadedFacility.getAttrFive() == null){
                                defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_FIVE, "");
                            } else {
                                defaultSelectValue.put(YinjingAttributeViewUtil.ATTRIBUTE_FIVE, uploadedFacility.getAttrFive());
                            }
                        }
                        Point point = new Point(uploadedFacility.getX(),uploadedFacility.getY());
                        yinjingAttributeViewUtil = new YinjingAttributeViewUtil();
                        yinjingAttributeViewUtil.addYinjingAttributes(mContext, null, ll_attributelist_container, defaultSelectValue,uploadedFacility.getMpBeen(),point,uploadedFacility);
                        break;
                    case R.id.rb_yushuikou:
                        layerName = "雨水口";
                        EventBus.getDefault().post(new RefreshTypeEvent(2));
                        ll_attributelist_container.removeAllViews();
                        if (uploadedFacility != null && layerName.equals(uploadedFacility.getLayerName())) {
                            defaultSelectValue = new HashMap<>(1);
                            defaultSelectValue.put(YushuikouAttributeViewUtil.ATTRIBUTE_SFHX, uploadedFacility.getSfpsdyhxn());
//                            defaultSelectValue.put(YushuikouAttributeViewUtil.ATTRIBUTE_THREE, uploadedFacility.getAttrThree());
//                            defaultSelectValue.put(YushuikouAttributeViewUtil.ATTRIBUTE_FOUR, uploadedFacility.getAttrFour());
                        }
                        yuShuiKouAttributes = new YushuikouAttributeViewUtil();
                        yuShuiKouAttributes.addYushuikouAttributes(mContext, null, ll_attributelist_container, defaultSelectValue);
                        break;
                    case R.id.rb_paifangkou:
                        layerName = "排水口";
                        EventBus.getDefault().post(new RefreshTypeEvent(3));
                        ll_attributelist_container.removeAllViews();
                        if (uploadedFacility != null && layerName.equals(uploadedFacility.getLayerName())) {
                            defaultSelectValue = new HashMap<>(5);
                            defaultSelectValue.put(PaifangKouAttributeViewUtilNew.ATTRIBUTE_ONE, uploadedFacility.getAttrOne());
                            defaultSelectValue.put(PaifangKouAttributeViewUtilNew.ATTRIBUTE_TWO, uploadedFacility.getAttrTwo());
                            defaultSelectValue.put(PaifangKouAttributeViewUtilNew.ATTRIBUTE_THREE, uploadedFacility.getAttrThree());
                            defaultSelectValue.put(PaifangKouAttributeViewUtilNew.ATTRIBUTE_FOUR, uploadedFacility.getAttrFour());
                            defaultSelectValue.put(PaifangKouAttributeViewUtilNew.ATTRIBUTE_FIVE, uploadedFacility.getAttrFive());
                            defaultSelectValue.put(PaifangKouAttributeViewUtilNew.ATTRIBUTE_SIX, uploadedFacility.getAttrSix());
                            defaultSelectValue.put(PaifangKouAttributeViewUtilNew.ATTRIBUTE_GPBH, uploadedFacility.getGpbh());
                            defaultSelectValue.put(PaifangKouAttributeViewUtilNew.ATTRIBUTE_SEVEN, uploadedFacility.getAttrSeven());
                            defaultSelectValue.put(PaifangKouAttributeViewUtilNew.ATTRIBUTE_X, String.valueOf(uploadedFacility.getRiverx()));
                            defaultSelectValue.put(PaifangKouAttributeViewUtilNew.ATTRIBUTE_Y, String.valueOf(uploadedFacility.getRivery()));
                        }
                        paifangKouAttributeViewUtil = new PaifangKouAttributeViewUtilNew();
                        paifangKouAttributeViewUtil.addYushuikouAttributes(mContext, null, ll_attributelist_container, defaultSelectValue,uploadedFacility);
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
        if(!isDraft) {
            root.findViewById(R.id.rb_yinjing).setEnabled(false);
            root.findViewById(R.id.rb_yushuikou).setEnabled(false);
            root.findViewById(R.id.rb_paifangkou).setEnabled(false);
        }
    }


    public FacilityAttributeModel getFacilityAttributeModel() {

        FacilityAttributeModel facilityAttributeModel = new FacilityAttributeModel();
        facilityAttributeModel.setLayerName(layerName);
        if (layerName.equals("窨井") && yinjingAttributeViewUtil != null) {
            YinjingAttributes yinjingAttributes = yinjingAttributeViewUtil.getYinjingAttributes();
            facilityAttributeModel.setAttrOne(yinjingAttributes.getAttributeOne());
            facilityAttributeModel.setMpBeen(yinjingAttributes.getMpBeen());
            facilityAttributeModel.setAttrTwo(yinjingAttributes.getAttributeTwo());
            facilityAttributeModel.setAttrSfgjjd(yinjingAttributes.getAttributesfGjjd());
            facilityAttributeModel.setAttrJDBH(yinjingAttributes.getGjjdBh());
            facilityAttributeModel.setAttrYJBH(yinjingAttributes.getAttributeYjBh());
            facilityAttributeModel.setAttrJDZRR(yinjingAttributes.getGjjdZrr());
            facilityAttributeModel.setAttrLXDH(yinjingAttributes.getLxdh());
            facilityAttributeModel.setAttrThree(yinjingAttributes.getAttributeThree());
            facilityAttributeModel.setAttrFour(yinjingAttributes.getAttributeFour());
            facilityAttributeModel.setAttrFive(yinjingAttributes.getAttributeFive());
            facilityAttributeModel.setAttrsfCzwscl(yinjingAttributes.getAttributesfCzwscl());
            facilityAttributeModel.setSfpsdyhxn(yinjingAttributes.getSfpsdyhxn());
            facilityAttributeModel.setIfAllowUpload(yinjingAttributes.ifAllowUpload());
            facilityAttributeModel.setNotAllowUploadReason(yinjingAttributes.getNotAllowUploadReason());
        } else if (layerName.equals("雨水口")) {
            YuShuiKouAttributes yuShuiKouAttributes = this.yuShuiKouAttributes.getYuShuiKouAttributes();
//            facilityAttributeModel.setAttrOne(yuShuiKouAttributes.getAttributeOne());
//            facilityAttributeModel.setAttrThree(yuShuiKouAttributes.getAttributeThree());
//            facilityAttributeModel.setAttrFour(yuShuiKouAttributes.getAttributeFour());
//            facilityAttributeModel.setIfAllowUpload(yuShuiKouAttributes.ifAllowUpload());
            facilityAttributeModel.setSfpsdyhxn(yuShuiKouAttributes.getSfpsdyhxn());
            facilityAttributeModel.setIfAllowUpload(true);
//            facilityAttributeModel.setNotAllowUploadReason(yuShuiKouAttributes.getNotAllowUploadReason());
        } else if (layerName.equals("排水口") && paifangKouAttributeViewUtil != null) {
            PaifangKouAttributesNew paifangKouAttributes = paifangKouAttributeViewUtil.getPaifangKouAttributes();
            facilityAttributeModel.setAttrOne(paifangKouAttributes.getAttributeOne());
            facilityAttributeModel.setAttrTwo(paifangKouAttributes.getAttributeTwo());
            facilityAttributeModel.setAttrThree(paifangKouAttributes.getAttributeThree());
            facilityAttributeModel.setAttrFour(paifangKouAttributes.getAttributeFour());
            facilityAttributeModel.setAttrFive(paifangKouAttributes.getAttributeFive());
            facilityAttributeModel.setAttrSix(paifangKouAttributes.getAttributeSix());
            facilityAttributeModel.setAttrSeven(paifangKouAttributes.getAttributeSeven());
            facilityAttributeModel.setX(paifangKouAttributes.getX());
            facilityAttributeModel.setY(paifangKouAttributes.getY());
            facilityAttributeModel.setGpbh(paifangKouAttributes.getAttributeGpbh());
//            facilityAttributeModel.setAttrEight(paifangKouAttributes.getAttributeEight());
//            facilityAttributeModel.setAttrNine(paifangKouAttributes.getAttributeNine());
//            facilityAttributeModel.setAttrTen(paifangKouAttributes.getAttributeTen());
//            facilityAttributeModel.setAttrEleven(paifangKouAttributes.getAttributeEleven());
//            facilityAttributeModel.setAttrTwelve(paifangKouAttributes.getAttributeTwelve());
//            facilityAttributeModel.setAttrThirteen(paifangKouAttributes.getAttributeThirteen());
//            facilityAttributeModel.setAttrThirteen(paifangKouAttributes.getAttributeThirteen());
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

package com.augurit.agmobile.gzps.uploadfacility.view.uploadnewfacility;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.util.SetCheckStateUtil;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.componentmaintenance.util.ComponentFieldKeyConstant;
import com.augurit.agmobile.gzps.uploadfacility.model.PaifangKouAttributesNew;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadedFacility;
import com.augurit.agmobile.gzps.uploadfacility.model.YinjingAttributes;
import com.augurit.agmobile.gzps.uploadfacility.model.YuShuiKouAttributes;
import com.augurit.agmobile.gzps.uploadfacility.util.PaifangKouAttributeViewUtilNew;
import com.augurit.agmobile.gzps.uploadfacility.util.YinjingAttributeViewUtil;
import com.augurit.agmobile.gzps.uploadfacility.util.YushuikouAttributeViewUtil;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.TimeUtil;
import com.esri.core.geometry.Point;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 在详情界面中用于生成“上报信息”表的类
 *
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.identification
 * @createTime 创建时间 ：17/11/10
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/11/10
 * @modifyMemo 修改备注：
 */

public class UploadFacilityTableViewManager {

    private Context mContext;
    private View root;

    private MultiTakePhotoTableItem take_photo_item;
    private TextItemTableItem tableitem_current_time;
    private TextItemTableItem tableitem_current_user;
    private TextFieldTableItem textitem_description;
    private TextItemTableItem tableitem_component_type;
    private TextItemTableItem tableitem_binding;
    private TextItemTableItem tableitem_checkstate;
    private UploadedFacility uploadedFacility;
    private TextItemTableItem textitem_address;
    private TextItemTableItem textitem_road;
    private LinearLayout ll_attributelist_container;
    private TextItemTableItem tableitem_parent_org;
    private Map<String, Object> attrs;
    private TextItemTableItem tableitem_last_modified_time;
    private TextItemTableItem tableitem_direct_org;
    private View ll__facility_problem;
    private TextFieldTableItem textitem_facility_problem;
    private TextItemTableItem textitem_upload_place;

    public UploadFacilityTableViewManager(
            Context context,
            UploadedFacility modifiedIdentification) {

        this.mContext = context;
        this.uploadedFacility = modifiedIdentification;
        initView();
        initData();
    }

    public UploadFacilityTableViewManager(
            Context context,
            UploadedFacility modifiedIdentification,
            Map<String, Object> attrs) {

        this.attrs = attrs;
        this.mContext = context;
        this.uploadedFacility = modifiedIdentification;
        initView();
        initData();
    }

    private void initData() {
        if (uploadedFacility != null) {
            setPhotos();
            tableitem_component_type.setText(uploadedFacility.getComponentType());
            tableitem_component_type.setReadOnly();
            tableitem_binding.setVisibility(View.VISIBLE);
            tableitem_binding.setText(uploadedFacility.getIsBinding() == 1 ? "数据确认" : "数据新增");
            tableitem_binding.setReadOnly();

            Long date = uploadedFacility.getMarkTime();
            if (date == null) {
                tableitem_current_time.setText("");
            }
            if (date != null) {
                tableitem_current_time.setText(TimeUtil.getStringTimeYMDMChines(new Date(date)));
            }

            /**
             * 更新时间
             */
            Long updateTime = uploadedFacility.getUpdateTime();
            if (updateTime != null && updateTime > 0 && !updateTime.equals(date)) {
                tableitem_last_modified_time.setText(TimeUtil.getStringTimeYMDMChines(new Date(updateTime)));
                tableitem_last_modified_time.setVisibility(View.VISIBLE);
                tableitem_last_modified_time.setReadOnly();
            }

            /**
             *  上报单位,判断规则：
             *  判断directOrg是否不为空，如果不为空显示directOrg，
             *  否则判断superviseOrg是否为空，如果superviseOrg为空，那么隐藏上报单位item
             */
            String directOrg = null;

            if (!TextUtils.isEmpty(uploadedFacility.getDirectOrgName())) {
                directOrg = uploadedFacility.getDirectOrgName();
            } else if (!TextUtils.isEmpty(uploadedFacility.getSuperviseOrgName())) {
                directOrg = uploadedFacility.getSuperviseOrgName();
            }

            if (directOrg == null) {
                tableitem_direct_org.setVisibility(View.GONE);
            } else {
                tableitem_direct_org.setText(directOrg);
                tableitem_direct_org.setVisibility(View.VISIBLE);
            }

            tableitem_current_time.setVisibility(View.VISIBLE);
            tableitem_current_user.setText(uploadedFacility.getMarkPerson());
            tableitem_current_time.setReadOnly();
            tableitem_current_user.setReadOnly();

            if (TextUtils.isEmpty(uploadedFacility.getDescription())) {
                textitem_description.setText("");
            } else {
                textitem_description.setText(uploadedFacility.getDescription());
            }
            textitem_description.setReadOnly();

            if (attrs != null && attrs.get(ComponentFieldKeyConstant.ADDR) != null) {
                String oldAddress = attrs.get(ComponentFieldKeyConstant.ADDR).toString();
                if (!oldAddress.equals(uploadedFacility.getAddr())) {
                    textitem_address.setEditTextColor(Color.RED);
                }
            }
            textitem_address.setText(uploadedFacility.getAddr());
            textitem_address.setReadOnly();
            if (TextUtils.isEmpty(uploadedFacility.getRoad())) {
                textitem_road.setText("");
            } else {
                textitem_road.setText(uploadedFacility.getRoad());
            }
            textitem_road.setReadOnly();

            tableitem_parent_org.setText(uploadedFacility.getParentOrgName());
            tableitem_parent_org.setReadOnly();
            //审核状态
            tableitem_checkstate.setReadOnly();
//            if (uploadedFacility.getCheckState() != null) {
//                if (uploadedFacility.getCheckState().equals("1")) {
//                    tableitem_checkstate.setText("未审核");
//                    tableitem_checkstate.setEditTextColor(Color.RED);
//                } else if (uploadedFacility.getCheckState().equals("2")) {
//                    tableitem_checkstate.setText("审核通过");
//                    tableitem_checkstate.setEditTextColor(Color.parseColor("#3EA500"));
//                } else if (uploadedFacility.getCheckState().equals("3")) {
//                    tableitem_checkstate.setText("存在疑问");
//                    tableitem_checkstate.setEditTextColor(Color.parseColor("#FFFFC248"));
//                }
//            }
            SetCheckStateUtil.setCheckState(uploadedFacility.getCheckState(),tableitem_checkstate);
            addAttributeView();

            //设施问题
            TableDBService dbService = new TableDBService(mContext);
            String pCodes = uploadedFacility.getpCode();
            String childCodes = uploadedFacility.getChildCode();
            if (!TextUtils.isEmpty(pCodes) && !TextUtils.isEmpty(childCodes)) {
                StringBuilder stringBuilder = new StringBuilder();
                String[] split = pCodes.split(",");
                for (String pCode : split) {

                    List<DictionaryItem> childProblems = dbService.getChildDictionaryByPCodeInDB(pCode);
                    List<DictionaryItem> parentProblems = dbService.getDictionaryByCode(pCode);
                    if (ListUtil.isEmpty(childProblems) || ListUtil.isEmpty(parentProblems)) {
                        continue;
                    }
                    stringBuilder.append(parentProblems.get(0).getName());
                    stringBuilder.append(" : ");
                    for (DictionaryItem childProblem : childProblems) {
                        if (childCodes.contains(childProblem.getCode())) {
                            stringBuilder.append(childProblem.getName());
                            stringBuilder.append("；");
                        }
                    }
                    //删除掉末尾的"；"
                    int i = stringBuilder.lastIndexOf("；");
                    if (i == stringBuilder.length() - 1) {
                        stringBuilder.deleteCharAt(i);
                    }
                    stringBuilder.append("\n");
                }

                if (!TextUtils.isEmpty(stringBuilder.toString()) && !"排水口".equals(uploadedFacility.getComponentType())) {
                    ll__facility_problem.setVisibility(View.GONE);
                    //删除掉末尾的"\n"
                    int i = stringBuilder.lastIndexOf("\n");
                    String facilityDes = stringBuilder.toString().substring(0, i);
                    textitem_facility_problem.setText(facilityDes);
                }else{
                    ll__facility_problem.setVisibility(View.GONE);
                }
            }

            /**
             * 管理状态
             */
            if (!TextUtils.isEmpty(uploadedFacility.getCityVillage()) && !"排水口".equals(uploadedFacility.getComponentType())) {
                textitem_upload_place.setText(uploadedFacility.getCityVillage());
            }else {
                textitem_upload_place.setVisibility(View.GONE);
            }
        }
        take_photo_item.setAddPhotoEnable(false);
    }

    public void addTo(ViewGroup container) {
        container.removeAllViews();
        container.addView(root);
    }

    private void addAttributeView() {


        if ("窨井".equals(uploadedFacility.getLayerName())) {

            YinjingAttributes yinjingAttributes = new YinjingAttributes();
            yinjingAttributes.setAttributeOne(uploadedFacility.getAttrOne());
            yinjingAttributes.setAttributeTwo(uploadedFacility.getAttrTwo());
            yinjingAttributes.setAttributesfCzwscl(uploadedFacility.getSfCzwscl());
            yinjingAttributes.setSfpsdyhxn(uploadedFacility.getSfpsdyhxn());
            yinjingAttributes.setAttributeThree(uploadedFacility.getAttrThree());
            yinjingAttributes.setAttributeFour(uploadedFacility.getAttrFour());
            yinjingAttributes.setAttributeFive(uploadedFacility.getAttrFive());
            yinjingAttributes.setAttributesfGjjd(uploadedFacility.getSfgjjd());
            yinjingAttributes.setGjjdBh(uploadedFacility.getGjjdBh());
            yinjingAttributes.setGjjdZrr(uploadedFacility.getGjjdZrr());
            yinjingAttributes.setLxdh(uploadedFacility.getGjjdLxdh());
            yinjingAttributes.setAttributeYjBh(uploadedFacility.getYjbh());
            Point point = new Point(uploadedFacility.getX(),uploadedFacility.getY());
            YinjingAttributeViewUtil.addReadOnlyAttributes(mContext, yinjingAttributes, ll_attributelist_container,uploadedFacility.getMpBeen(),point,uploadedFacility);

        } else if ("雨水口".equals(uploadedFacility.getLayerName())) {

            YuShuiKouAttributes yinjingAttributes = new YuShuiKouAttributes();
            yinjingAttributes.setAttributeOne(uploadedFacility.getAttrOne());
            yinjingAttributes.setAttributeThree(uploadedFacility.getAttrThree());
            yinjingAttributes.setSfpsdyhxn(uploadedFacility.getSfpsdyhxn());
            yinjingAttributes.setAttributeFour(uploadedFacility.getAttrFour());
            YushuikouAttributeViewUtil.addReadOnlyAttributes(mContext, yinjingAttributes, ll_attributelist_container);
        }
        if ("排水口".equals(uploadedFacility.getLayerName())) {

            PaifangKouAttributesNew yinjingAttributes = new PaifangKouAttributesNew();
            yinjingAttributes.setAttributeOne(uploadedFacility.getAttrOne());
            yinjingAttributes.setAttributeTwo(uploadedFacility.getAttrTwo());
            yinjingAttributes.setAttributeThree(uploadedFacility.getAttrThree());
            yinjingAttributes.setAttributeFour(uploadedFacility.getAttrFour());
            yinjingAttributes.setAttributeFive(uploadedFacility.getAttrFive());
            yinjingAttributes.setAttributeSix(uploadedFacility.getAttrSix());
            yinjingAttributes.setAttributeSeven(uploadedFacility.getAttrSeven());
            yinjingAttributes.setAttributeGpbh(uploadedFacility.getGpbh());
            PaifangKouAttributeViewUtilNew.addReadOnlyAttributes(mContext, yinjingAttributes, ll_attributelist_container,uploadedFacility);

        }
    }


    private void setPhotos() {

        List<Photo> photos = uploadedFacility.getPhotos();
        if (!ListUtil.isEmpty(photos)) {

            take_photo_item.setSelectedPhotos(photos);
        } else {

            take_photo_item.setVisibility(View.GONE);
        }
        take_photo_item.setReadOnly();
    }

    private void initView() {

        root = LayoutInflater.from(mContext).inflate(R.layout.view_upload_facility_table_view, null);
        tableitem_checkstate = (TextItemTableItem) root.findViewById(R.id.tableitem_checkstate); //审核状态
        tableitem_binding = (TextItemTableItem) root.findViewById(R.id.textitem_binding);
        tableitem_component_type = (TextItemTableItem) root.findViewById(R.id.tableitem_component_type);
        take_photo_item = (MultiTakePhotoTableItem) root.findViewById(R.id.take_photo_item);
        tableitem_current_time = (TextItemTableItem) root.findViewById(R.id.tableitem_current_time);
        /**
         * 最后修改时间
         */
        tableitem_last_modified_time = (TextItemTableItem) root.findViewById(R.id.tableitem_last_modified_time);
        /**
         * 填表人
         */
        tableitem_current_user = (TextItemTableItem) root.findViewById(R.id.tableitem_current_user);
        /**
         * 描述
         */
        textitem_description = (TextFieldTableItem) root.findViewById(R.id.textitem_description);

        /**
         * 地址
         */
        textitem_address = (TextItemTableItem) root.findViewById(R.id.textitem_address);

        textitem_road = (TextItemTableItem) root.findViewById(R.id.textitem_road);

        /**
         * 属性容器
         */
        ll_attributelist_container = (LinearLayout) root.findViewById(R.id.ll_attributelist_container);
        /**
         * 业主单位
         */
        tableitem_parent_org = (TextItemTableItem) root.findViewById(R.id.tableitem_parent_Org);
        /**
         * 上报单位
         */
        tableitem_direct_org = (TextItemTableItem) root.findViewById(R.id.tableitem_direct_org);
        tableitem_direct_org.setReadOnly();
        /**
         * 设施问题
         */
        ll__facility_problem = root.findViewById(R.id.ll__facility_problem);
        textitem_facility_problem = (TextFieldTableItem) root.findViewById(R.id.textitem_facility_problem);
        textitem_facility_problem.setReadOnly();

        /**
         * 管理状态
         */
        textitem_upload_place = (TextItemTableItem) root.findViewById(R.id.textitem_upload_place);
        textitem_upload_place.setReadOnly();
    }

}

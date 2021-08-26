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
import com.augurit.agmobile.gzps.common.widget.CheckBoxItem;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.uploadfacility.model.PipeBean;
import com.augurit.agmobile.patrolcore.common.table.dao.local.TableDBService;
import com.augurit.agmobile.patrolcore.common.table.model.DictionaryItem;
import com.augurit.am.fw.utils.StringUtil;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
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

public class PipeTableViewManager {

    private Context mContext;
    private View root;

    private MultiTakePhotoTableItem take_photo_item;
    private TextItemTableItem tableitem_current_time;
    private TextItemTableItem tableitem_current_user;
    private TextFieldTableItem textitem_description;
    private TextItemTableItem tableitem_component_type;
    private TextItemTableItem tableitem_checkstate;
    private PipeBean uploadedFacility;
    private TextItemTableItem textitem_address;
    //    private TextItemTableItem textitem_road;
    private LinearLayout ll_attributelist_container;
    private TextItemTableItem tableitem_parent_org;
    private TextItemTableItem tableitem_last_modified_time;
    private TextItemTableItem tableitem_direct_org;
    private View ll__facility_problem;
    private TextFieldTableItem textitem_facility_problem;
    private TextItemTableItem textitem_upload_place;
    private TextItemTableItem textitem_gjlx;
    private TextItemTableItem textitem_gxcd;
    private CheckBoxItem cbitem_isFacilityOfRedLine;
    private boolean mIsShiPaiOrKexuecheng = false;

    public PipeTableViewManager(
            Context context,
            PipeBean modifiedIdentification) {

        this.mContext = context;
        this.uploadedFacility = modifiedIdentification;
        initView();
        initData();
    }

    public PipeTableViewManager(
            Context context,
            PipeBean modifiedIdentification, boolean isShiPaiOrKexuecheng) {

        this.mContext = context;
        this.uploadedFacility = modifiedIdentification;
        mIsShiPaiOrKexuecheng = isShiPaiOrKexuecheng;
        initView();
        initData();

    }

    private void initData() {
        if (uploadedFacility != null) {
            tableitem_component_type.setText(uploadedFacility.getPipeType());
            textitem_address.setText(uploadedFacility.getDirection());
            TableDBService dbService = new TableDBService(mContext);
            String gjName = "";
            if (!mIsShiPaiOrKexuecheng) {
                List<DictionaryItem> a205 = dbService.getDictionaryByTypecodeInDB("A205");
                final Map<String, Object> gjData = new LinkedHashMap<>();
                String pipeGJ = uploadedFacility.getPipeGj();
                List<String> gjValues = new ArrayList<>();
                for (DictionaryItem dictionaryItem : a205) {
                    if (dictionaryItem.getCode().equals(pipeGJ)) {
                        gjName = dictionaryItem.getName();
                    }
                    gjValues.add(dictionaryItem.getName());
                    gjData.put(dictionaryItem.getName(), dictionaryItem);
                }
                textitem_gjlx.setTextViewName("管径类型");
            } else {
                textitem_gjlx.setTextViewName("管径");
                gjName = uploadedFacility.getPipeGj();
            }
            textitem_gjlx.setText(gjName);
            textitem_gjlx.setReadOnly();
            textitem_gxcd.setReadOnly();
            if (uploadedFacility.getLineLength() != 0 && uploadedFacility.getLineLength() != -1) {
                textitem_gxcd.setText(uploadedFacility.getLineLength() + "");
            } else {
                textitem_gxcd.setText("");
            }
            tableitem_component_type.setReadOnly();
            textitem_address.setReadOnly();
            String oldPipeType = uploadedFacility.getOldPipeType();
            if (!StringUtil.isEmpty(oldPipeType) && !oldPipeType.equals(uploadedFacility.getPipeType())) {
                tableitem_component_type.setEditTextColor(Color.RED);
            }

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

            if (!TextUtils.isEmpty(uploadedFacility.getParentOrgName())) {
                directOrg = uploadedFacility.getParentOrgName();
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

            if (!TextUtils.isEmpty(uploadedFacility.getOldDirection())) {
                String oldAddress = uploadedFacility.getDirection();
                if (!uploadedFacility.getOldDirection().equals(oldAddress)) {
                    textitem_address.setEditTextColor(Color.RED);
                }
            }
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
            cbitem_isFacilityOfRedLine.setCheck("1".equals(uploadedFacility.isSfpsdyhxn()));
            SetCheckStateUtil.setCheckState(uploadedFacility.getCheckState(), tableitem_checkstate);
        }
    }

    public void addTo(ViewGroup container) {
        container.removeAllViews();
        container.addView(root);
    }

    /**
     *
     */
    private void initView() {

        root = LayoutInflater.from(mContext).inflate(R.layout.view_upload_pipe_table_view, null);
        tableitem_checkstate = (TextItemTableItem) root.findViewById(R.id.tableitem_checkstate); //审核状态
        tableitem_component_type = (TextItemTableItem) root.findViewById(R.id.tableitem_component_type);
//        take_photo_item = (MultiTakePhotoTableItem) root.findViewById(R.id.take_photo_item);
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
        /**
         * 管径类型
         */
        textitem_gjlx = (TextItemTableItem) root.findViewById(R.id.textitem_gjlx);
        /**
         * 管线长度
         */
        textitem_gxcd = (TextItemTableItem) root.findViewById(R.id.textitem_gxcd);

//        textitem_road = (TextItemTableItem) root.findViewById(R.id.textitem_road);

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
//        ll__facility_problem = root.findViewById(R.id.ll__facility_problem);
//        textitem_facility_problem = (TextFieldTableItem) root.findViewById(R.id.textitem_facility_problem);
//        textitem_facility_problem.setReadOnly();

        /**
         * 管理状态
         */
//        textitem_upload_place = (TextItemTableItem) root.findViewById(R.id.textitem_upload_place);
//        textitem_upload_place.setReadOnly();

        /**
         * 是否属于排水单元的红线内
         */
        cbitem_isFacilityOfRedLine = root.findViewById(R.id.cbitem_isFacilityOfRedLine);
        cbitem_isFacilityOfRedLine.setEnabled(false);
    }

}

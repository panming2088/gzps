package com.augurit.agmobile.gzps.uploadfacility.view.facilitydetail;

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
import com.augurit.agmobile.gzps.uploadfacility.model.HookBean;
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

public class HookTableViewManager {

    private Context mContext;
    private View root;

    private TextItemTableItem tableitem_current_time;
    private TextItemTableItem tableitem_current_user;
    private HookBean uploadedFacility;
    private TextItemTableItem tableitem_last_modified_time;
    private TextItemTableItem tableitem_direct_org;
    private View ll__facility_problem;

    private boolean mIsShiPaiOrKexuecheng = false;
    private TextItemTableItem tableitem_jb_bh;
    private TextItemTableItem tableitem_jb_type;
    private TextItemTableItem tableitem_jb_addr;
    private TextItemTableItem textitem_dy_name;
    private TextItemTableItem textitem_dy_type;

    public HookTableViewManager(
            Context context,
            HookBean modifiedIdentification) {

        this.mContext = context;
        this.uploadedFacility = modifiedIdentification;
        initView();
        initData();
    }
    public HookTableViewManager(
            Context context,
            HookBean modifiedIdentification, boolean isShiPaiOrKexuecheng) {

        this.mContext = context;
        this.uploadedFacility = modifiedIdentification;
        mIsShiPaiOrKexuecheng = isShiPaiOrKexuecheng;
        initView();
        initData();

    }

    private void initData() {
        if (uploadedFacility != null) {

            Long date = uploadedFacility.getMarkTime();
            if (date == null) {
                tableitem_current_time.setText("");
            }
            if (date != null) {
                tableitem_current_time.setText(TimeUtil.getStringTimeYMDMChines(new Date(date)));
            }
            tableitem_jb_bh.setText(uploadedFacility.getJbjObjectId());
            if(!StringUtil.isEmpty(uploadedFacility.getSort())) {
                tableitem_jb_type.setText(uploadedFacility.getSort());
            }else{
                tableitem_jb_type.setText("");
            }
            if(!StringUtil.isEmpty(uploadedFacility.getJbjAddr())) {
                tableitem_jb_addr.setText(uploadedFacility.getJbjAddr());
            }else{
                tableitem_jb_addr.setText("");
            }
            if(!StringUtil.isEmpty(uploadedFacility.getPsdyName())) {
                textitem_dy_name.setText(uploadedFacility.getPsdyName());
            }else{
                textitem_dy_name.setText("");
            }
            if(!StringUtil.isEmpty(uploadedFacility.getPsdyLx())) {
                textitem_dy_type.setText(uploadedFacility.getPsdyLx());
            }else{
                textitem_dy_type.setText("");
            }

            /**
             * 更新时间
             */
            Long updateTime = null;
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

        root = LayoutInflater.from(mContext).inflate(R.layout.view_upload_hook_table_view, null);
        tableitem_jb_bh = (TextItemTableItem) root.findViewById(R.id.tableitem_jb_bh);
        tableitem_jb_bh.setReadOnly();
        tableitem_jb_type = (TextItemTableItem) root.findViewById(R.id.tableitem_jb_type);
        tableitem_jb_type.setReadOnly();
        tableitem_jb_addr = (TextItemTableItem) root.findViewById(R.id.tableitem_jb_addr);
        tableitem_jb_addr.setReadOnly();
        textitem_dy_name = (TextItemTableItem) root.findViewById(R.id.textitem_dy_name);
        textitem_dy_name.setReadOnly();
        textitem_dy_type = (TextItemTableItem) root.findViewById(R.id.textitem_dy_type);
        textitem_dy_type.setReadOnly();

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
         * 上报单位
         */
        tableitem_direct_org = (TextItemTableItem) root.findViewById(R.id.tableitem_direct_org);
        tableitem_direct_org.setReadOnly();
        /**
         * 设施问题
         */
    }

}

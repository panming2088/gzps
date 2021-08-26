package com.augurit.agmobile.gzps.uploadfacility.view.facilitydetail;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.MultiTakePhotoTableItem;
import com.augurit.agmobile.gzps.common.util.SetCheckStateUtil;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.monitor.model.InspectionWellMonitorInfo;
import com.augurit.agmobile.gzps.monitor.view.TextItemTableItem1;
import com.augurit.agmobile.gzps.uploadfacility.util.InspectionSetCheckStateUtil;
import com.augurit.am.cmpt.widget.HorizontalScrollPhotoView.Photo;
import com.augurit.am.fw.utils.ListUtil;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;
import java.util.List;

/**
 * 在详情界面中用于生成“上报信息”表的类
 *
 * @author 创建人 ：huangchongwu
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps
 * @createTime 创建时间 ：2020/09/16
 */

public class InspectionWellMonitorTableViewManager {

    private Context mContext;
    private View root;

    private TextItemTableItem1 tableitem_current_time;
    private TextItemTableItem1 tableitem_current_user;
    private InspectionWellMonitorInfo uploadedFacility;
    private TextItemTableItem1 tableitem_last_modified_time;
    private TextItemTableItem1 tableitem_direct_org;
    /**
     * 备注
     */
    private TextFieldTableItem tableitem_remarks;
    private View ll__facility_problem;
    private MultiTakePhotoTableItem take_photo_item;
    private boolean mIsShiPaiOrKexuecheng = false;
    private TextItemTableItem1 tableitem_jb_lx;
    private TextItemTableItem1 tableitem_jb_ad;
    private TextItemTableItem1 tableitem_jb_nd;
    private TextItemTableItem tableitem_checkstate;

    public InspectionWellMonitorTableViewManager(
            Context context,
            InspectionWellMonitorInfo modifiedIdentification) {

        this.mContext = context;
        this.uploadedFacility = modifiedIdentification;
        initView();
        initData();
    }

    public InspectionWellMonitorTableViewManager(
            Context context,
            InspectionWellMonitorInfo modifiedIdentification, boolean isShiPaiOrKexuecheng) {

        this.mContext = context;
        this.uploadedFacility = modifiedIdentification;
        mIsShiPaiOrKexuecheng = isShiPaiOrKexuecheng;
        initView();
        initData();

    }

    private void initData() {
        if (uploadedFacility != null) {
            setPhotos();
            take_photo_item.setAddPhotoEnable(false);
            Long date = uploadedFacility.getMarkTime();
            if (date == null) {
                tableitem_current_time.setText("");
            }
            if (date != null) {
                tableitem_current_time.setText(TimeUtil.getStringTimeYMDMChines(new Date(date)));
            }
            tableitem_jb_lx.setText(uploadedFacility.getJbjType());
            tableitem_jb_ad.setText(uploadedFacility.getAd());
            tableitem_jb_nd.setText(uploadedFacility.getCod());
            /**
             * 更新时间
             */
            Long updateTime = uploadedFacility.getUpdateTime();;
            if (updateTime != null && updateTime > 0 && !updateTime.equals(date)) {
                tableitem_last_modified_time.setText(TimeUtil.getStringTimeYMDMChines(new Date(updateTime)));
                tableitem_last_modified_time.setVisibility(View.VISIBLE);
                tableitem_last_modified_time.setReadOnly();
            }
            InspectionSetCheckStateUtil.setCheckState(uploadedFacility.getCheckState(), tableitem_checkstate);
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
            String description = uploadedFacility.getDescription();
            if (!TextUtils.isEmpty(description)) {
                // 解决\n无法正常换行的问题
                description = description.replaceAll("(\\n)|(＼n)", "\n");
            }
            tableitem_remarks.setText(description);
            tableitem_current_time.setReadOnly();
            tableitem_current_user.setReadOnly();
            tableitem_remarks.setReadOnly();

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

        root = LayoutInflater.from(mContext).inflate(R.layout.view_upload_inspection_monitor_table_view, null);
        take_photo_item = (MultiTakePhotoTableItem) root.findViewById(R.id.take_photo_item);
        tableitem_jb_lx = (TextItemTableItem1) root.findViewById(R.id.tableitem_jb_lx);
        tableitem_jb_lx.setReadOnly();
        tableitem_jb_ad = (TextItemTableItem1) root.findViewById(R.id.tableitem_jb_ad);
        tableitem_jb_ad.setReadOnly();
        tableitem_jb_nd = (TextItemTableItem1) root.findViewById(R.id.tableitem_jb_nd);
        tableitem_jb_nd.setReadOnly();


        tableitem_current_time = (TextItemTableItem1) root.findViewById(R.id.tableitem_current_time);
        /**
         * 最后修改时间
         */
        tableitem_last_modified_time = (TextItemTableItem1) root.findViewById(R.id.tableitem_last_modified_time);
        /**
         * 填表人
         */
        tableitem_current_user = (TextItemTableItem1) root.findViewById(R.id.tableitem_current_user);
        /**
         * 上报单位
         */
        tableitem_direct_org = (TextItemTableItem1) root.findViewById(R.id.tableitem_direct_org);
        tableitem_direct_org.setReadOnly();
        /**
         * 备注
         */
        tableitem_remarks = (TextFieldTableItem) root.findViewById(R.id.textitem_remarks);
        tableitem_remarks.setReadOnly();
        /**
         * 备注
         */
        tableitem_checkstate = (TextItemTableItem) root.findViewById(R.id.tableitem_checkstate);
        tableitem_checkstate.setReadOnly();
        /**
         * 设施问题
         */
        setRightTextColor();
    }

    private void setRightTextColor() {
        tableitem_jb_lx.getEt_right().setTextColor(Color.BLACK);
        tableitem_jb_ad.getEt_right().setTextColor(Color.BLACK);
        tableitem_jb_nd.getEt_right().setTextColor(Color.BLACK);
        tableitem_current_time.getEt_right().setTextColor(Color.BLACK);
        tableitem_last_modified_time.getEt_right().setTextColor(Color.BLACK);
        tableitem_current_user.getEt_right().setTextColor(Color.BLACK);
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

}

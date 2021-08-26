package com.augurit.agmobile.gzps.uploadfacility.view.doorno;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.TextFieldTableItem;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadDoorNoDetailBean;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;
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

public class UploadDoorNoTableViewManager {

    private Context mContext;
    private View root;
    private TextItemTableItem tableitemCheckstate;


    private TextItemTableItem tableitemOne;
    private TextItemTableItem tableitemTwo;
    private TextItemTableItem tableitemThree;
    private TextItemTableItem tableitemFour;
    private TextItemTableItem tableitemFive;

    private TextItemTableItem tableitemUploadUser;
    private TextItemTableItem tableitemUploadUnit;
    private TextItemTableItem tableitemUploadTime;
    private TextItemTableItem tableitemLastModifyTime;

    private TextFieldTableItem tableitemUploadNote;
    private UploadDoorNoDetailBean uploadedDoorNo;
    private LinearLayout ll_attributelist_container;
    private Map<String, Object> attrs;

    public UploadDoorNoTableViewManager(
            Context context,
            UploadDoorNoDetailBean modifiedIdentification) {

        this.mContext = context;
        this.uploadedDoorNo = modifiedIdentification;
        initView();
        initData();
    }

    public UploadDoorNoTableViewManager(
            Context context,
            UploadDoorNoDetailBean modifiedIdentification,
            Map<String, Object> attrs) {

        this.attrs = attrs;
        this.mContext = context;
        this.uploadedDoorNo = modifiedIdentification;
        initView();
        initData();
    }

    private void initData() {
        if (uploadedDoorNo != null) {
            tableitemOne.setText(uploadedDoorNo.getSsxzqh());
            tableitemOne.setVisibility(View.VISIBLE);

            tableitemTwo.setText(uploadedDoorNo.getSsxzjd());
            tableitemTwo.setVisibility(View.VISIBLE);

            tableitemThree.setText(uploadedDoorNo.getSssqcjwh());
            tableitemThree.setVisibility(View.VISIBLE);

            tableitemFour.setText(uploadedDoorNo.getSsjlx());
            tableitemFour.setVisibility(View.VISIBLE);

            tableitemFive.setText(uploadedDoorNo.getMpdzmc());
            tableitemFive.setVisibility(View.VISIBLE);

            tableitemUploadNote.setText(uploadedDoorNo.getDescription());

            tableitemUploadUser.setText(uploadedDoorNo.getMarkPerson());
            tableitemUploadUser.setVisibility(View.VISIBLE);

            Long date = Long.valueOf(uploadedDoorNo.getMarkTime());
            if (date == null) {
                tableitemUploadTime.setText("");
            }
            if (date != null) {
                tableitemUploadTime.setText(TimeUtil.getStringTimeYMDMChines(new Date(date)));
            }

            /**
             * 更新时间
             */
            Long updateTime = Long.valueOf(uploadedDoorNo.getUpdateTime());
            if (updateTime != null && updateTime > 0 && !updateTime.equals(date)) {
                tableitemLastModifyTime.setText(TimeUtil.getStringTimeYMDMChines(new Date(updateTime)));
                tableitemLastModifyTime.setVisibility(View.VISIBLE);
                tableitemLastModifyTime.setReadOnly();
            }

            tableitemCheckstate.setReadOnly();
            if (uploadedDoorNo.getState()==1) {
                tableitemCheckstate.setText("未审核");
                tableitemCheckstate.setEditTextColor(Color.RED);
            } else if (uploadedDoorNo.getState()==2) {
                tableitemCheckstate.setText("审核通过");
                tableitemCheckstate.setEditTextColor(Color.parseColor("#3EA500"));
            } else if (uploadedDoorNo.getState()==3) {
                tableitemCheckstate.setText("存在疑问");
                tableitemCheckstate.setEditTextColor(Color.parseColor("#FFFFC248"));
            }
        }
    }

    public void addTo(ViewGroup container) {
        container.removeAllViews();
        container.addView(root);
    }


    private void initView() {
        /**
         * 属性容器
         */
        root = LayoutInflater.from(mContext).inflate(R.layout.view_upload_doorno_table_view, null);
//        ll_attributelist_container = (LinearLayout) root.findViewById(R.id.ll_attributelist_container);
        tableitemOne = (TextItemTableItem) root.findViewById(R.id.tableitem_one);
        tableitemTwo = (TextItemTableItem) root.findViewById(R.id.textitem_two);
        tableitemThree = (TextItemTableItem) root.findViewById(R.id.textitem_three);
        tableitemFour = (TextItemTableItem) root.findViewById(R.id.textitem_four);
        tableitemFive = (TextItemTableItem) root.findViewById(R.id.textitem_five);

        tableitemCheckstate = (TextItemTableItem) root.findViewById(R.id.tableitem_checkstate);


        tableitemUploadUser = (TextItemTableItem) root.findViewById(R.id.tableitem_upload_user);
        ;
        tableitemUploadUnit = (TextItemTableItem) root.findViewById(R.id.tableitem_upload_unit);
        ;
        tableitemUploadTime = (TextItemTableItem) root.findViewById(R.id.tableitem_upload_time);
        ;
        tableitemLastModifyTime = (TextItemTableItem) root.findViewById(R.id.tableitem_last_modified_time);
        ;
        tableitemUploadNote = (TextFieldTableItem) root.findViewById(R.id.textfield_description);
        ;

        tableitemOne.setReadOnly();
        tableitemTwo.setReadOnly();
        tableitemThree.setReadOnly();
        tableitemFour.setReadOnly();
        tableitemFive.setReadOnly();

        tableitemUploadUser.setReadOnly();
        tableitemUploadUnit.setReadOnly();
        tableitemUploadTime.setReadOnly();
        tableitemLastModifyTime.setReadOnly();

        tableitemUploadNote.setReadOnly();

    }

}

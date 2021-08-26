package com.augurit.agmobile.gzps.uploadfacility.view.doorno;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.uploadfacility.model.UploadDoorNoDetailBean;
import com.augurit.am.fw.utils.TimeUtil;

import java.util.Date;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.approvalopinion
 * @createTime 创建时间 ：17/12/26
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/26
 * @modifyMemo 修改备注：
 */

public class UploadDoorNoApprovalViewManager {
    private TextItemTableItem date;
    private TextItemTableItem state;
    private TextItemTableItem option;
    private EditText person;
    private TextView empty;
    private View personLayout;
    private View root;
    private ImageView iv_phone;
    private TextView tv_phone;
    private Context mContext;

    public UploadDoorNoApprovalViewManager(Context context, UploadDoorNoDetailBean uploadedDoorNo) {
        mContext = context;
        initViews(context);
        if (null == uploadedDoorNo || uploadedDoorNo.getCheckPerson() == null || uploadedDoorNo.getCheckDescription() == null) {
            hiddleViews();
        } else {
            initData(uploadedDoorNo.getMarkTime(), uploadedDoorNo.getCheckState(), uploadedDoorNo.getCheckDescription(), uploadedDoorNo.getMarkPerson(), "");
        }
    }

    private void initViews(Context context) {
        root = LayoutInflater.from(context).inflate(R.layout.view_doorno_opinions, null);
        date = (TextItemTableItem) root.findViewById(R.id.tv_approve_date);
        state = (TextItemTableItem) root.findViewById(R.id.tv_approve_state);
        option = (TextItemTableItem) root.findViewById(R.id.tv_option);
        personLayout = root.findViewById(R.id.person_layout);
        person = (EditText) root.findViewById(R.id.tv_approver);
        empty = (TextView) root.findViewById(R.id.tv_empty);
        iv_phone = (ImageView) root.findViewById(R.id.iv_phone);
        tv_phone = (TextView) root.findViewById(R.id.tv_phone);
        person.setEnabled(false);
        date.setReadOnly();
        option.setReadOnly();
        state.setReadOnly();
    }

    private void hiddleViews() {
        empty.setVisibility(View.VISIBLE);
        person.setVisibility(View.INVISIBLE);
        personLayout.setVisibility(View.INVISIBLE);
        date.setVisibility(View.INVISIBLE);
        option.setVisibility(View.INVISIBLE);
        state.setVisibility(View.INVISIBLE);

    }


    private void initData(String checkTime, String CheckState, String CheckDescription, String MarkPerson, final String phone) {
        empty.setVisibility(View.GONE);
        date.setText(TimeUtil.getStringTimeMdHmChines(new Date(Long.valueOf(checkTime))));
        state.setText(CheckState.equals("1") ? "未审核" : CheckState.equals("2") ? "审核通过" : "存在疑问");
        option.setText(CheckDescription);
        person.setText(MarkPerson);
        /**
         * 手机为空，不显示
         */
        if (TextUtils.isEmpty(phone)) {
            iv_phone.setVisibility(View.GONE);
            tv_phone.setVisibility(View.GONE);
        } else {
            tv_phone.setText(phone);
            root.findViewById(R.id.ll_phone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + phone));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public View getView() {
        return root;
    }

    public void addTo(ViewGroup container) {
        if (container == null) {
            return;
        }
        container.addView(root);
    }
}

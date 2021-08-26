package com.augurit.agmobile.gzps.uploadfacility.view.approvalopinion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.augurit.agmobile.gzps.R;
import com.augurit.agmobile.gzps.common.util.SetCheckStateUtil;
import com.augurit.agmobile.gzps.common.widget.TextItemTableItem;
import com.augurit.agmobile.gzps.uploadfacility.model.ApprovalOpinion;
import com.augurit.agmobile.gzps.uploadfacility.util.InspectionSetCheckStateUtil;

/**
 * @author 创建人 ：xuciluan
 * @version 1.0
 * @package 包名 ：com.augurit.agmobile.gzps.uploadfacility.view.approvalopinion
 * @createTime 创建时间 ：17/12/27
 * @modifyBy 修改人 ：xuciluan
 * @modifyTime 修改时间 ：17/12/27
 * @modifyMemo 修改备注：
 */

public class InspectionOpinionView {


    private final View root;

    public InspectionOpinionView(final Context context, final ApprovalOpinion approvalOpinion) {
        root = LayoutInflater.from(context).inflate(R.layout.item_approval_opinion, null);
        TextItemTableItem tv_option = (TextItemTableItem) root.findViewById(R.id.tv_option);
        TextItemTableItem tv_approve_date = (TextItemTableItem) root.findViewById(R.id.tv_approve_date);
        EditText tv_approver = (EditText) root.findViewById(R.id.tv_approver);
        TextItemTableItem tv_approve_state = (TextItemTableItem) root.findViewById(R.id.tv_approve_state);
        TextView tv_phone = (TextView) root.findViewById(R.id.tv_phone);
        View iv_phone = root.findViewById(R.id.iv_phone);

        tv_approve_date.setText(approvalOpinion.getCheckTimeStr());
        tv_approve_date.setReadOnly();
        tv_approver.setText(approvalOpinion.getCheckPerson());
        tv_option.setText(approvalOpinion.getCheckDesription());
        tv_option.setReadOnly();
        tv_approve_state.setReadOnly();

        /**
         * 手机为空，不显示
         */
        if (TextUtils.isEmpty(approvalOpinion.getPhone())){
            iv_phone.setVisibility(View.GONE);
            tv_phone.setVisibility(View.GONE);
        }else {
            tv_phone.setText(String.valueOf(approvalOpinion.getPhone()));
            root.findViewById(R.id.ll_phone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + approvalOpinion.getPhone()));
                    context.startActivity(intent);
                }
            });
        }

//        if (approvalOpinion.getCheckState().equals(CheckState.UNCHECK)) {
//            tv_approve_state.setText("未审核");
//            tv_approve_state.setEditTextColor(Color.RED);
//        } else if (approvalOpinion.getCheckState().equals(CheckState.SUCCESS)) {
//            tv_approve_state.setText("审核通过");
//            tv_approve_state.setEditTextColor(Color.parseColor("#3EA500"));
//        } else if (approvalOpinion.getCheckState().equals(CheckState.IN_DOUBT)) {
//            tv_approve_state.setText("存在疑问");
//            tv_approve_state.setEditTextColor(Color.parseColor("#FFFFC248"));
//        }
        InspectionSetCheckStateUtil.setCheckState(approvalOpinion.getCheckState(),tv_approve_state);
    }


    public void addTo(ViewGroup container){
        if (container == null) {
            return;
        }
        container.addView(root);
    }
}
